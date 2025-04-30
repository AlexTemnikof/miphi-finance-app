package ru.rfma.core.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.Nullable;
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
    /**
     * Доп. информация
     */
    @Nullable
    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "operation_type")
    private OperationType operationType;

    @Nullable
    @Column(name="category_id")
    private int categoryId;

    @NonNull
    @Column(name = "user_id")
    private int userId;

    public Operation(final Float amount, final Date date,
                     final String description, final OperationType operationType,
                     final int categoryId, final int userId) {
        this.amount = amount;
        this.date = date;
        this.description= description;
        this.operationType=operationType;
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
