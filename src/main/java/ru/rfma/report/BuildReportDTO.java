package ru.rfma.report;

import java.util.Date;

public record BuildReportDTO(ReportType type, Date from, Date to) {
}
