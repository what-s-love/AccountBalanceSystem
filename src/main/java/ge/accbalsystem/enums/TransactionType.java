package ge.accbalsystem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW");

    private final String type;

    public static TransactionType getStatusEnum(String type) {
        return switch (type.toUpperCase()) {
            case "DEPOSIT" -> DEPOSIT;
            case "WITHDRAW" -> WITHDRAW;
            default -> throw new NoSuchElementException(String.format("No such transaction type: '%s'", type));
        };
    }

    @Override
    public String toString() {
        return type;
    }
}
