package br.com.emersondias.ebd.services.interfaces;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.Map;

public interface IReportService {

    byte[] generatePdf(String fileName, Map<String, Object> params) throws IOException, JRException;

    byte[] generatePdf(String fileName, Map<String, Object> params, JRDataSource dataSource) throws IOException, JRException;

}
