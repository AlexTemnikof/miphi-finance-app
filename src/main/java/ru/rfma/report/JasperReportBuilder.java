package ru.rfma.report;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.antlr.v4.runtime.misc.Interval;
import org.springframework.stereotype.Service;
import ru.rfma.core.util.Intervals;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
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

        final ReportType type = ReportType.valueOf(buildReportDTO.type());
        final Intervals interval = Intervals.valueOf(buildReportDTO.interval());
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("from", buildReportDTO.from());
        parameters.put("to", buildReportDTO.to());
        parameters.put("userId", userId);
        parameters.put("title", type.title);
        parameters.put("INTERVAL", interval);

        final JasperReport selectedReport = jasperReportsCompiler.getJasperReports().stream()
                .filter(report -> report.getName().equals(type.name()))
                .findFirst().orElse(null);

        if (selectedReport != null) {
            final JasperPrint jasperPrint = fillReport(selectedReport, parameters);
            if (jasperPrint != null) {
                return exportReport(jasperPrint, type.name());
            }
        } else {
            System.out.println("Отчет не найден: " + type.name());
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

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            final String dateStr = formatter.format(new Date());

            final String exportingFileName = reportName.toLowerCase() + dateStr + ".xlsx";
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exportingFileName));
            exporter.setConfiguration(reportConfig);

            exporter.exportReport(); // Экспорт отчета в файл
            System.out.println("Отчет успешно экспортирован в файл: " + exportingFileName);
            return exportingFileName;
        } catch (Exception e) {
            e.printStackTrace(); // Обработка исключений
        }

        return null;
    }
}
