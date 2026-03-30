package com.pet.jiraliketracker.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Email не може бути пустим")
    @Email(message = "Будь-ласка, введіть коректну адресу електронної пошти")
    private String email;

    @NotBlank(message = "Пароль не може бути пустим")
    @Size(min = 8, max = 32, message = "Пароль має містити від 8 до 32 символів")
    private String password;

    @NotBlank(message = "Юзернейм не може бути пустим")
    @Size(min = 8, max = 50, message = "Юзернейм має містити від 8 до 50 символів")
    private String username;
}
