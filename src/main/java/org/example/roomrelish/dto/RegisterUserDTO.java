package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String username;
    private String email;
    private String password;
    private String dateOfBirth;
    private String phoneNumber;
    private String street;
    private String city;
    private String state;

}
