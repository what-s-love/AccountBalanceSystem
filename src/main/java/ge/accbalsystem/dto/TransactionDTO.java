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
    @NotEmpty(message = "Field 'type' is required!")
    @JsonProperty("type")
    private String type;
    @NotNull(message = "Field 'amount' is required!")
    @Positive
    @JsonProperty("amount")
    private Double amount;
    @NotEmpty(message = "Field 'currency_code' is required!")
    @JsonProperty("currency_code")
    private String currencyCode;
}
