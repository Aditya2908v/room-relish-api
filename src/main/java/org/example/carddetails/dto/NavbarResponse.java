package org.example.carddetails.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.carddetails.models.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavbarResponse {
    private boolean success;
    private CustomerProfile info;
}
