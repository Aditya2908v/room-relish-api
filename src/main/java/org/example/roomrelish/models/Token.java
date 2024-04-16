package org.example.roomrelish.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;
    private String token;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    private String customerId;
}
