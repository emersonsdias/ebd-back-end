package br.com.emersondias.ebd.services.impl;

import br.com.emersondias.ebd.services.interfaces.IReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ReportServiceImpl implements IReportService {

    private static final String REPORTS_PATH = "reports";
    private static final String JASPER_EXTENSION = ".jasper";

    @Override
    public byte[] generatePdf(String fileName, Map<String, Object> params) throws IOException, JRException {
        return this.generatePdf(fileName, params, new JREmptyDataSource());
    }

    @Override
    public byte[] generatePdf(String fileName, Map<String, Object> params, JRDataSource dataSource) throws IOException, JRException {
        var reportStream = new ClassPathResource(
                REPORTS_PATH.concat("/").concat(fileName).concat(JASPER_EXTENSION)
        ).getInputStream();
        var jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
        var jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
