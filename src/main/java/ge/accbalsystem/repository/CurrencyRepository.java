package ge.accbalsystem.repository;

import ge.accbalsystem.model.Currency;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CurrencyRepository extends ReactiveCrudRepository<Currency, Long> {
    Mono<Currency> findByCode(String code);
}
