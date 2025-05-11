package ru.rfma.report;

import java.util.Date;

public record BuildReportDTO(String type, Date from, Date to, String interval) {
}
