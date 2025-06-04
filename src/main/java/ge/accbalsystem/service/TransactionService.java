package ge.accbalsystem.service;

import ge.accbalsystem.dto.TransactionDTO;
import ge.accbalsystem.enums.TransactionType;
import ge.accbalsystem.model.Balance;
import ge.accbalsystem.model.Currency;
import ge.accbalsystem.model.Transaction;
import ge.accbalsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;

    public Flux<Transaction> getTransactions(String balanceName) {
        return balanceService.getBalance(balanceName)
                .flatMapMany(balance -> transactionRepository.findAllByBalanceId(balance.getId()));
    }

    public Mono<Transaction> createTransaction(String name, TransactionDTO dto) {
        return balanceService.getBalance(name)
                .flatMap(balance ->
                        calculateCurrentValue(balance)
                                .zipWith(calculateTransactionAmountUsd(dto))
                                .flatMap(tuple -> {
                                    double currentValueUsd = tuple.getT1();
                                    double transactionAmountUsd = tuple.getT2();

                                    TransactionType type = TransactionType.getStatusEnum(dto.getType());
                                    if (type == TransactionType.WITHDRAW && transactionAmountUsd > currentValueUsd) {
                                        return Mono.error(new IllegalArgumentException("Баланс недостаточен для списания"));
                                    }

                                    return dtoToModel(dto, balance)
                                            .flatMap(transactionRepository::save)
                                            .flatMap(txn -> {
                                                double newBalanceValue = txn.getType() == TransactionType.WITHDRAW
                                                        ? currentValueUsd - transactionAmountUsd
                                                        : currentValueUsd + transactionAmountUsd;

                                                balance.setValueUsd(newBalanceValue);
                                                return balanceService.updateBalance(balance)
                                                        .thenReturn(txn);
                                            });
                                })
                );
    }

    private Mono<Transaction> dtoToModel(TransactionDTO dto, Balance balance) {
        return currencyService.getCurrencyByCode(dto.getCurrencyCode())
                .map(currency -> Transaction.builder()
//                        .id(UUID.randomUUID())
                        .balanceId(balance.getId())
                        .type(TransactionType.getStatusEnum(dto.getType()))
                        .amount(dto.getAmount())
                        .currencyId(currency.getId())
                        .createdAt(LocalDateTime.now())
                        .build());
    }

    private Mono<Double> calculateCurrentValue(Balance balance) {
        return transactionRepository.findAllByBalanceId(balance.getId())
                .flatMap(t ->
                        currencyService.getCurrencyById(t.getCurrencyId())
                                .map(currency -> {
                                    double usdAmount = t.getAmount() * currency.getRate();
                                    return t.getType() == TransactionType.DEPOSIT ? usdAmount : -usdAmount;
                                })
                )
                .reduce(0.0, Double::sum);
    }

    public Mono<Double> calculateTransactionAmountUsd(TransactionDTO dto) {
        return currencyService.getCurrencyByCode(dto.getCurrencyCode())
                .handle((currency, sink) -> {
                    if (currency.getRate() <= 0) {
                        sink.error(new IllegalStateException("Неверный курс валюты: " + currency.getCode()));
                        return;
                    }
                    sink.next(dto.getAmount() * currency.getRate());
                });
    }
}
