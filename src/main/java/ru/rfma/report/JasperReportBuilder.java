package ru.rfma.report;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JasperReportBuilder {

    private final DataSource dataSource;

    private final JasperReportCompiler jasperReportsCompiler;

    public JasperReportBuilder(final DataSource dataSource, final JasperReportCompiler compiler) {
        this.dataSource = dataSource;
        this.jasperReportsCompiler = compiler;
    }

    public String buildReport(final BuildReportDTO buildReportDTO, final Integer userId) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", buildReportDTO.from());
        parameters.put("to", buildReportDTO.to());
        parameters.put("userId", userId);

        final JasperReport selectedReport = jasperReportsCompiler.getJasperReports().stream()
                .filter(report -> report.getName().equals(buildReportDTO.type().name()))
                .findFirst().orElse(null);

        if (selectedReport != null) {
            final JasperPrint jasperPrint = fillReport(selectedReport, parameters);
            if (jasperPrint != null) {
                return exportReport(jasperPrint, buildReportDTO.type().name());
            }
        } else {
            System.out.println("Отчет не найден: " + buildReportDTO.type().name());
        }
        return null;
    }

    private JasperPrint fillReport(final JasperReport jasperReport, final Map<String, Object> parameters) {
        try {
            return JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        } catch (Exception e) {
            // Обработка исключений
            e.printStackTrace();
            return null;
        }
    }

    private String exportReport(final JasperPrint jasperPrint, final String reportName) {
        try {
            final JRXlsxExporter exporter = new JRXlsxExporter();
            final SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
            reportConfig.setSheetNames(new String[] { reportName });

            final String exportingFileName = reportName + new Date() + ".xlsx";
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exportingFileName));
            exporter.setConfiguration(reportConfig);

            exporter.exportReport(); // Экспорт отчета в файл
            System.out.println("Отчет успешно экспортирован в файл: " + reportName + ".xlsx");
            return exportingFileName;
        } catch (Exception e) {
            e.printStackTrace(); // Обработка исключений
        }

        return null;
    }
}
