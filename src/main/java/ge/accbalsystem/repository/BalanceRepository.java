package ge.accbalsystem.repository;

import ge.accbalsystem.model.Balance;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BalanceRepository extends ReactiveCrudRepository<Balance, Long> {
    Mono<Balance> findByName(String name);

    Mono<Boolean> existsByName(String name);

}
