package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerDTO {
    private String email;
    private String password;
    private String phoneNumber;
}
