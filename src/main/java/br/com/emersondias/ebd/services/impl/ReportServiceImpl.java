package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.exceptions.ReportGenerationException;
import br.com.emersondias.ebd.services.interfaces.IReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements IReportService {

    private static final String REPORTS_PATH = "reports";
    private static final String JASPER_EXTENSION = ".jasper";

    private final DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public byte[] generatePdf(String fileName, Map<String, Object> params) throws ReportGenerationException {
        try {
            var reportStream = new ClassPathResource(
                    REPORTS_PATH.concat("/").concat(fileName).concat(JASPER_EXTENSION)
            ).getInputStream();
            var jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            var jasperPrint = JasperFillManager.fillReport(jasperReport, params, getConnection());

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException | SQLException | IOException e) {
            throw new ReportGenerationException("Failed to generate PDF report, params: [" + params + "]", e, fileName);
        }
    }
}
