package com.dawii.trabfinal.models.request;

import com.dawii.trabfinal.models.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaRequest {
    private Pessoa pessoa;
}
