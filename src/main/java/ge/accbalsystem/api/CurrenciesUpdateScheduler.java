package ge.accbalsystem.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import ge.accbalsystem.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrenciesUpdateScheduler {
    private final CurrencyRepository currencyRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${currencies.api.url}")
    private String url;
    @Value("${currencies.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    @Scheduled(cron = "0 */10 * * * *")
    public void updateCurrencies() {
        log.info("Currency update started");
        fetchRatesFromApi()
                .flatMapMany(apiRates -> currencyRepository.findAll()
                        .flatMap(currency -> {
                            Double newRate = apiRates.get(currency.getCode());

                            if (newRate == null) {
                                log.warn("Currency '{}' not found in API response!", currency.getCode());
                                return Mono.error(new IllegalArgumentException("Missing rate for currency: " + currency.getCode()));
                            }

                            currency.setRate(newRate);
                            return currencyRepository.save(currency);
                        })
                )
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    private Mono<Map<String, Double>> fetchRatesFromApi() {
        return webClient.get()
                .uri(url + "?apikey=" + apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> {
                    try {
                        JsonNode root = objectMapper.readTree(json);
                        JsonNode dataNode = root.path("data");

                        if (dataNode.isMissingNode()) {
                            throw new IllegalStateException("Missing 'data' field in API response");
                        }

                        return objectMapper.convertValue(dataNode, new TypeReference<Map<String, Double>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse currency API response", e);
                    }
                });
    }
}