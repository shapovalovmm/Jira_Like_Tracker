package com.pet.jiraliketracker.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Email не може бути пустим")
    @Email(message = "Будь-ласка, введіть коректну адресу електронної пошти")
    public String email;

    @NotBlank(message = "Пароль не може бути пустим")
    @Size(min = 8, max = 32, message = "Пароль має містити від 8 до 32 символів")
    public String password;
}
