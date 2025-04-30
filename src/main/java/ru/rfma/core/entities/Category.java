package ru.rfma.core.entities;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "spendLimit")
    private Float spendLimit;

    @NonNull
    @Column(name = "user_id")
    private int userId;

    public Category(final String name, final Float spendLimit, final int userId) {
        this.name = name;
        this.spendLimit = spendLimit;
        this.userId = userId;
    }

}
