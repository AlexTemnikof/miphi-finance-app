package ru.rfma.report;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.rfma.core.util.Util.uncheck;

@Component
public class JasperReportCompiler {

    private final String reportsPath = "/path/to/reports"; // Замените на ваш путь
    private final List<JasperReport> jasperReports = new ArrayList<>();

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        uncheck(() -> Files.walk(Paths.get(getClass().getResource(reportsPath).toURI()))
                .filter(path -> path.toString().endsWith(".jrxml"))
                .forEach(path -> {
                    try (InputStream reportStream = Files.newInputStream(path)) {
                        JasperReport jasperReport = uncheck(() -> JasperCompileManager.compileReport(reportStream));
                        String jasperFilePath = path.toString().replace(".jrxml", ".jasper");
                        uncheck(() -> JRSaver.saveObject(jasperReport, jasperFilePath));
                        jasperReports.add(jasperReport); // Добавление скомпилированного отчета в список
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
    }

    public List<JasperReport> getJasperReports() {
        return new ArrayList<>(jasperReports); // Возврат копии списка отчетов
    }
}
