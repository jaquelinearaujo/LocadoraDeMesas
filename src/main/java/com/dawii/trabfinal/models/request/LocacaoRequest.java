package com.dawii.trabfinal.models.request;

import com.dawii.trabfinal.models.Locacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocacaoRequest {
    private Locacao locacao;
    private List<Integer> quantidades;
}
