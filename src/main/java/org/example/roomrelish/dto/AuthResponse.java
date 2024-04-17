package org.example.roomrelish.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TestOnly
public class AuthResponse {
    @JsonProperty("access_token")
    private String token;
}
