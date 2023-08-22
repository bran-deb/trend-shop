package com.api.oderapi.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SignupRequestDTO {
    private String username;
    private String password;
    // SOLO RECIBE UN PASSWORD PARA QUE SE VERIFIQUE EN EL LADO DEL CLIENTE
}
