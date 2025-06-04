package ge.accbalsystem.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCIES")
public class Currency {
    @Id
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String code;
    @NotNull
    private Double rate;
}
