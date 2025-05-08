package ru.rfma.report;

public enum ReportType {

    OPERATIONS("");

    final String fileName;

    ReportType(String name) {
        this.fileName = name;
    }
}
