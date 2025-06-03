package ge.accbalsystem.model;

import ge.accbalsystem.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTIONS")
public class Transaction {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotEmpty
    @Column(name = "TYPE", nullable = false)
    private TransactionType type;
    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Double amount;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;
    @ManyToOne
    @JoinColumn(name = "BALANCE_ID")
    private Balance balance;
}
