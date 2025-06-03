package ge.accbalsystem.controller;

import ge.accbalsystem.dto.TransactionDTO;
import ge.accbalsystem.model.Balance;
import ge.accbalsystem.model.Transaction;
import ge.accbalsystem.service.BalanceService;
import ge.accbalsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/balances")
public class BalanceController {
    private final BalanceService balanceService;
    private final TransactionService transactionService;
    @PostMapping
    public Mono<ResponseEntity<Balance>> createBalance(@RequestParam String name) {
        return balanceService.createBalance(name).map(ResponseEntity::ok);
    }

    @GetMapping("/{name}")
    public Mono<ResponseEntity<Balance>> getBalance(@PathVariable String name) {
        return balanceService.getBalance(name).map(ResponseEntity::ok);
    }

    @PostMapping("/{name}/transactions")
    public Mono<ResponseEntity<Transaction>> addTransaction(@PathVariable String name, @RequestBody @Valid TransactionDTO dto) {
        return transactionService.createTransactionReactive(name, dto).map(ResponseEntity::ok);
    }

    @GetMapping("/{name}/transactions")
    public Flux<ResponseEntity<Transaction>> getTransactions(@PathVariable String name) {
        return transactionService.getTransactions(name).map(ResponseEntity::ok);
    }
}
