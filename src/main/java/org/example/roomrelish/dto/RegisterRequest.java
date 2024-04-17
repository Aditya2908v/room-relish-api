package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Date dateOfBirth;
    private String phoneNumber;
    private String address;
}
