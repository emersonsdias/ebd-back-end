package br.com.emersondias.ebd.services.interfaces;

import br.com.emersondias.ebd.exceptions.ReportGenerationException;

import java.util.Map;

public interface IReportService {

    byte[] generatePdf(String fileName, Map<String, Object> params) throws ReportGenerationException;

}
