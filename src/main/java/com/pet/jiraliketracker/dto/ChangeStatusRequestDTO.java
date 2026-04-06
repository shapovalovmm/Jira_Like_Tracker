package com.pet.jiraliketracker.dto;

import com.pet.jiraliketracker.model.Status;
import lombok.Data;

@Data
public class ChangeStatusRequestDTO {
    private Long id;
    private Status status;
}
