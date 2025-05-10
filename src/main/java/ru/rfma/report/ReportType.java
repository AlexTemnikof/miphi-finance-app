package ru.rfma.report;

public enum ReportType {

    TRANSACTIONS_COUNT("transactions_count"),
    TRANSACTIONS_TYPE("transactions_type"),
    PROFIT("profit"),
    TRANSACTIONS_STATUS("transactions_status"),
    BANKS("banks"),
    CATEGORIES("categories");

    final String fileName;

    ReportType(String fileName) {
        this.fileName = fileName;
    }
}
