package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TestOnly
public class LoginRequest {
    private String email;
    private String password;
}
