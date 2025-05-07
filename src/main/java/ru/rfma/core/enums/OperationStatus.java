package ru.rfma.core.enums;

import lombok.Getter;

@Getter
public enum OperationStatus {
    NEW(true, true),
    CONFIRMED(false, false),
    IN_PROGRESS(false, false),
    CANCELLED(false, false),
    PAYED(false, false),
    PAYMENT_DELETED(false, true),
    RETURNED(false, false);

    private final boolean canModify;

    private final boolean canDelete;

    OperationStatus(final boolean canModify, final boolean canDelete) {
        this.canModify = canModify;
        this.canDelete = canDelete;
    }

}
