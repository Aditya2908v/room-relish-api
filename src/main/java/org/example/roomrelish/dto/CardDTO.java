package org.example.roomrelish.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class CardDTO {
    @NotEmpty(message = "Card number must not be empty")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 digits")
    private String cardNumber;
    @NotEmpty(message = "Card holder must not be empty")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Card holder must contain only letters and spaces")
    private String cardHolder;
    @NotEmpty(message = "Expiration date must not be empty")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Expiration date must be in the format MM/YY")
    private String expirationDate;
    @NotEmpty(message = "CVV must not be empty")
    @Pattern(regexp = "^[0-9]{3}$", message = "CVV must be 3 digits")
    private String cvv;
    private String cardName;
}
