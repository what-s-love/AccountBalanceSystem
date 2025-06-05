package ge.accbalsystem.service;

import ge.accbalsystem.dto.TransactionDTO;
import ge.accbalsystem.enums.TransactionType;
import ge.accbalsystem.model.Balance;
import ge.accbalsystem.model.Currency;
import ge.accbalsystem.model.Transaction;
import ge.accbalsystem.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    BalanceService balanceService;

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void calculateTransactionAmountUsd_shouldConvertCorrectly() {
        TransactionDTO dto = new TransactionDTO("deposit", 100.0, "EUR");
        Currency eur = new Currency(1L, "Euro", "EUR", 1.1);

        when(currencyService.getCurrencyByCode("EUR")).thenReturn(Mono.just(eur));

        Mono<Double> result = transactionService.calculateTransactionAmountUsd(dto);

        StepVerifier.create(result)
                .expectNextMatches(actual -> Math.abs(actual - 110.0) < 0.0001)
                .verifyComplete();
    }

    @Test
    void createTransaction_shouldErrorIfWithdrawExceedsBalance() {
        TransactionDTO dto = new TransactionDTO("withdraw", 200.0, "USD");
        Balance balance = Balance.builder()
                .id(1L)
                .name("main")
                .valueUsd(100.0)
                .createdAt(LocalDateTime.now())
                .build();

        when(balanceService.getBalance("main")).thenReturn(Mono.just(balance));
        when(transactionRepository.findAllByBalanceId(1L)).thenReturn(Flux.empty());
        when(currencyService.getCurrencyByCode("USD")).thenReturn(Mono.just(new Currency(1L, "Dollar", "USD", 1.0)));

        Mono<Transaction> result = transactionService.createTransaction("main", dto);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().contains("Баланс недостаточен"))
                .verify();
    }

    @Test
    void calculateCurrentValue_shouldSumTransactionsCorrectly() {
        Balance balance = Balance.builder().id(1L).build();

        Transaction t1 = Transaction.builder()
                .type(TransactionType.DEPOSIT).amount(100.0).currencyId(1L).build();

        Transaction t2 = Transaction.builder()
                .type(TransactionType.WITHDRAW).amount(30.0).currencyId(1L).build();

        when(transactionRepository.findAllByBalanceId(1L)).thenReturn(Flux.fromIterable(List.of(t1, t2)));
        when(currencyService.getCurrencyById(1L)).thenReturn(Mono.just(new Currency(1L, "USD", "USD", 1.0)));

        Mono<Double> result = transactionService.calculateCurrentValue(balance);

        StepVerifier.create(result)
                .expectNext(70.0)
                .verifyComplete();
    }
}
