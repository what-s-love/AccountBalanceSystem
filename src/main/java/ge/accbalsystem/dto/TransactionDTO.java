package ge.accbalsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @NotEmpty
    @JsonProperty("type")
    private String type;
    @NotNull
    @Positive
    @JsonProperty("amount")
    private Double amount;
    @NotEmpty
    @JsonProperty("currency_code")
    private String currencyCode;
}
