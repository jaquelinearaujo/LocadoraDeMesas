package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Locacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocacaoResponse extends BaseResponse{
    private List<Locacao> locacoes;
}
