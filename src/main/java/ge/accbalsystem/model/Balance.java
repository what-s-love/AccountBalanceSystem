package ge.accbalsystem.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BALANCES")
public class Balance {
    @Id
    private Long id;
    @NotEmpty
    private String name;
    private Double valueUsd;
    private LocalDateTime createdAt;
}
