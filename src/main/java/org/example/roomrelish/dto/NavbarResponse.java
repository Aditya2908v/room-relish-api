package org.example.roomrelish.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.roomrelish.models.Customer;
import org.jetbrains.annotations.TestOnly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
public class NavbarResponse {
    private boolean success;
    private CustomerProfile info;
}
