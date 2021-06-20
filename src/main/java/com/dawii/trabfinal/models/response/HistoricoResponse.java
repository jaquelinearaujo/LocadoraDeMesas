package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Historico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoResponse extends BaseResponse{
    private List<Historico> historicos;
}
