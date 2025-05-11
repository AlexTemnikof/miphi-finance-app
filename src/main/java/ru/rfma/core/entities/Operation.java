package ru.rfma.core.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
import ru.rfma.auth.entity.ClientType;
import ru.rfma.core.enums.OperationStatus;
import ru.rfma.core.enums.OperationType;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @Column(name = "amount")
    private float amount;

    @NonNull
    @Column(name = "date")
    private Date date;

    private ClientType receiverType;

    private OperationStatus status;

    /**
     * Банк отправителя
     */
    private String senderBank;

    /**
     * Банк получателя
     */
    private String receiverBank;

    /**
     * Счет отправителя
     */
    private String senderAccountId;

    /**
     * Счет получателя
     */
    private String receiverAccountId;
    private String receiverPhoneNumber;
    /**
     * Доп. информация
     */
    @Nullable
    @Column(name = "description")
    private String description;

    @NonNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    @Nullable
    @Column(name="category_id")
    private int categoryId;

    @Column(name = "user_id")
    private Integer userId;
}
