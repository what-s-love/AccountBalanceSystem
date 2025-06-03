package ge.accbalsystem.service;

import ge.accbalsystem.model.Balance;
import ge.accbalsystem.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Mono<Balance> getBalance(String name) {
        return balanceRepository.findByName(name).switchIfEmpty(Mono.error(new IllegalArgumentException("Balance with name '" + name + "' not found")));
    }

    public Mono<Balance> createBalance(String name) {
        Balance newBalance = Balance.builder()
                .name(name)
                .currentValueUsd(0.0d)
                .createdAt(LocalDateTime.now())
                .build();

        return balanceRepository.existsByName(name)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Balance with name '" + name + "' already exists"));
                    } else {
                        return balanceRepository.save(newBalance);
                    }
                });
    }
}
