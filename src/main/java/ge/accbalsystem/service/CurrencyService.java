package ge.accbalsystem.service;

import ge.accbalsystem.model.Currency;
import ge.accbalsystem.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Mono<Currency> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code).switchIfEmpty(Mono.error(new IllegalArgumentException("Currency with code '" + code + "' not found")));
    }
}
