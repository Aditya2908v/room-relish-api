package org.example.roomrelish.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfile {
    private String id;
    private String username;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dob;
    private String password;
}
