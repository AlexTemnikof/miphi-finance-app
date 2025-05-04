package ru.rfma.core.enums;

import java.io.Serializable;

public enum OperationType implements Serializable {
    INCOME("Доход"),
    EXPENSE("Расход");

    private String label;

    OperationType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
