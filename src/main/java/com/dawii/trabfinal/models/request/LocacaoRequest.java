package com.dawii.trabfinal.models.request;

import com.dawii.trabfinal.models.Locacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocacaoRequest {
    private Locacao locacao;
}
