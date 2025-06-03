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
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final CurrencyService currencyService;

    public Flux<Transaction> getTransactions(String balanceName) {
        return balanceService.getBalance(balanceName)
                .flatMapMany(transactionRepository::findAllByBalance);
    }

/*
    public Mono<Transaction> createTransaction(String name, TransactionDTO dto) {
        return Mono.defer(() ->
                balanceService.getBalance(name)
                        .flatMap(balance -> dtoToModel(dto, balance))
                        .flatMap(transactionRepository::save)
                        .subscribeOn(Schedulers.boundedElastic())
        );
    }
*/

    public Mono<Transaction> createTransaction(String name, TransactionDTO dto) {
        return balanceService.getBalance(name)
                .flatMap(balance ->
                        calculateBalanceValue(balance)
                                .zipWith(currencyService.getCurrencyByCode(dto.getCurrencyCode()))
                                .flatMap(tuple -> {
                                    double currentValueUsd = tuple.getT1();
                                    Currency currency = tuple.getT2();
                                    double transactionAmountUsd = currency.getRateToUsd() * dto.getAmount();

                                    TransactionType type = TransactionType.getStatusEnum(dto.getType());
                                    if (type == TransactionType.WITHDRAW && transactionAmountUsd > currentValueUsd) {
                                        return Mono.error(new IllegalArgumentException("Баланс недостаточен для списания"));
                                    }

                                    Transaction transaction = Transaction.builder()
                                            .balance(balance)
                                            .type(type)
                                            .amount(dto.getAmount())
                                            .currency(currency)
                                            .createdAt(LocalDateTime.now())
                                            .build();

                                    return transactionRepository.save(transaction);
                                })
                );
    }

/*
    private Mono<Transaction> dtoToModel(TransactionDTO dto, Balance balance) {
        return currencyService.getCurrencyByCode(dto.getCurrencyCode())
                .map(currency -> Transaction.builder()
                        .balance(balance)
                        .type(TransactionType.getStatusEnum(dto.getType()))
                        .amount(dto.getAmount())
                        .currency(currency)
                        .createdAt(LocalDateTime.now())
                        .build());
    }
*/

    private Mono<Double> calculateBalanceValue(Balance balance) {
        return transactionRepository.findAllByBalance(balance)
                .flatMap(t -> {
                    double usdAmount = t.getAmount() * t.getCurrency().getRateToUsd();
                    if (t.getType() == TransactionType.DEPOSIT) {
                        return Mono.just(usdAmount);
                    } else {
                        return Mono.just(-usdAmount);
                    }
                })
                .reduce(0.0, Double::sum);
    }
}
