package com.pet.jiraliketracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AppErrorDTO {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;
}
