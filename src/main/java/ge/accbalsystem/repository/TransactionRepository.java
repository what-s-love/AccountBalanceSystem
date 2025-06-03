package ge.accbalsystem.repository;

import ge.accbalsystem.model.Balance;
import ge.accbalsystem.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, UUID> {
    Flux<Transaction> findAllByBalance(Balance balance);
}
