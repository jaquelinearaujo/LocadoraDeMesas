package com.dawii.trabfinal.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class JasperSoftUtil {
	@Autowired
	private DataSource dataSource;
	
	public byte[] gerarRelatorioSQL(String nomeRelatorio) {
		return gerarRelatorioSQLParametros(nomeRelatorio, null);
	}
	
	public byte[] gerarRelatorioSQLParametros(String nomeRelatorio, Map<String, Object> parametros) {

		try (Connection conexao = dataSource.getConnection()) {
			try {
				InputStream arquivoJasper = getClass().getResourceAsStream("/reports/" + nomeRelatorio);
				JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, parametros, conexao);
				return JasperExportManager.exportReportToPdf(jasperPrint);
			} catch (JRException e) {
			}
		} catch (SQLException e) {
		}
		return null;
	}
	
	public byte[] gerarRelatorio(String nomeRelatorio, List<?> objetos) {
		return gerarRelatorio(nomeRelatorio, objetos, null);
	}
	
	public byte[] gerarRelatorio(String nomeRelatorio, List<?> objetos, Map<String, Object> parametros) {
		try {
			ClassPathResource cpr = new ClassPathResource("relatorios/" + nomeRelatorio);
			InputStream arquivoJasper = cpr.getInputStream();
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(objetos);
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, parametros, ds);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
		} catch (IOException e) {
		}
		return null;
	}
			
	
}
