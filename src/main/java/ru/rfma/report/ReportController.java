package ru.rfma.report;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.rfma.auth.service.AuthService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static ru.rfma.core.util.Util.uncheck;

@RestController
@RequestMapping("report")
public class ReportController {

    private final JasperReportBuilder builder;

    private final AuthService authService;

    public ReportController(final JasperReportBuilder builder, final AuthService authService) {
        this.builder = builder;
        this.authService = authService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<?> buildReport(@RequestBody final BuildReportDTO buildReportDTO, final HttpServletRequest request) {
        final Integer userId = authService.tryGetClientIdFromRequest(request);
        final String fileName = builder.buildReport(buildReportDTO, userId);

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // Получаем байты из ByteArrayOutputStream
            byte[] bytes = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(bytes);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            uncheck(() -> FileUtils.forceDelete(new File(fileName)));
        }
    }


}
