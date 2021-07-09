package com.dawii.trabfinal.services.impl;

import com.dawii.trabfinal.report.JasperSoftUtil;
import com.dawii.trabfinal.services.IReportService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
public class ReportService implements IReportService {
    @Autowired
    private JasperSoftUtil jasperSoftUtil;

    @Override
    public byte[] gerarRelatorioComplexoTodasLocacoes() {
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("TITULO", "Locações");
        return jasperSoftUtil.gerarRelatorioSQLParametros("LocacaoReport.jasper", parametros);
    }
}
