package ru.rfma.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rfma.core.enums.OperationStatus;
import ru.rfma.core.enums.OperationType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperationDto {
    private Integer id;
    private float amount;
    private Date date;
    private OperationStatus status;
    private String senderBank;
    private String receiverBank;
    private String senderAccountId;
    private String receiverAccountId;
    private String receiverPhoneNumber;
    private String description;
    private OperationType operationType;
    private int categoryId;
    private Integer userId;
}
