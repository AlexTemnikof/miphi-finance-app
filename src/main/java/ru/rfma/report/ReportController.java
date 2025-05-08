package ru.rfma.report;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rfma.auth.service.AuthService;

import java.io.File;
import java.io.FileInputStream;

import static ru.rfma.core.util.Util.uncheck;

@RestController
public class ReportController {

    private final JasperReportBuilder builder;

    private final AuthService authService;

    public ReportController(final JasperReportBuilder builder, final AuthService authService) {
        this.builder = builder;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<?> buildReport(@RequestParam final BuildReportDTO buildReportDTO, final HttpServletRequest request) {
        final Integer userId = authService.tryGetClientIdFromRequest(request);
        final String fileName = builder.buildReport(buildReportDTO, userId);
        try {
            final FileInputStream fileInputStream = uncheck(() -> new FileInputStream(fileName));

            final InputStreamResource resource = new InputStreamResource(fileInputStream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=file.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } finally {
            uncheck(() -> FileUtils.forceDelete(new File(fileName)));
        }
    }
}
