package org.example.roomrelish.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    private String id;
    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvv;
    private String cardName;
}