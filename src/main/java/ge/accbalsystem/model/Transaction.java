package ge.accbalsystem.model;

import ge.accbalsystem.enums.TransactionType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTIONS")
public class Transaction {
    @Id
    private UUID id;
    @NotEmpty
    private TransactionType type;
    @NotNull
    private Double amount;
    private LocalDateTime createdAt;
    private Long currencyId;
    private Long balanceId;
}
