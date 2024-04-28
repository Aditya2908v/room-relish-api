package org.example.roomrelish.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.TestOnly;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TestOnly
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerProfile {
    private String id;
    private String username;
    private String profilePicture;
}
