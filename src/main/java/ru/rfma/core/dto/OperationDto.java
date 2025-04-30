package ru.rfma.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rfma.core.enums.OperationType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {
    private float amount;

    private Date date;

    private String description;

    private OperationType operationType;
    private int categoryId;
    private int user_id;
}
