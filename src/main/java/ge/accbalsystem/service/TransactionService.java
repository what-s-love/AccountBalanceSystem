package ge.accbalsystem.service;

import ge.accbalsystem.dto.TransactionDTO;
import ge.accbalsystem.enums.TransactionType;
import ge.accbalsystem.model.Balance;
import ge.accbalsystem.model.Transaction;
import ge.accbalsystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.List;

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

    public Mono<Transaction> createTransactionReactive(String name, TransactionDTO dto) {
        return Mono.defer(() ->
                balanceService.getBalance(name)
                        .flatMap(balance -> dtoToModel(dto, balance))
                        .flatMap(transactionRepository::save)
                        .subscribeOn(Schedulers.boundedElastic())
        );
    }

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
}
