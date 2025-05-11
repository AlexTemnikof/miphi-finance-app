package ru.rfma.report;

public enum ReportType {

    TRANSACTIONS_COUNT("transactions_count", "Количество транзакций"),
    TRANSACTIONS_TYPE("transactions_type", "Тип транзакций"),
    PROFIT("profit", "Профит"),
    TRANSACTIONS_STATUS("transactions_status", "Статус транзакций"),
    BANKS("banks", "Банки"),
    CATEGORIES("categories", "Количество транзакций");

    final String fileName;

    final String title;

    ReportType(String fileName, String title) {
        this.fileName = fileName;
        this.title = title;
    }

}
