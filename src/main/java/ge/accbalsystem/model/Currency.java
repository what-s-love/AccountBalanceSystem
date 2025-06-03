package ge.accbalsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CURRENCIES")
public class Currency {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(name = "NAME", nullable = false)
    private String name;
    @NotEmpty
    @Column(name = "CODE", nullable = false)
    private String code;
    @NotNull
    @Column(name = "RATE", nullable = false)
    private Double rateToUsd;
}
