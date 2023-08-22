package com.api.oderapi.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    // NO SE TRANSMITE EL PASSWORD
}
