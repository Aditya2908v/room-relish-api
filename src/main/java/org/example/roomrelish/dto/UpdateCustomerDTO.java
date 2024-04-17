package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class UpdateCustomerDTO {
    private String email;
    private String password;
    private String phoneNumber;
}
