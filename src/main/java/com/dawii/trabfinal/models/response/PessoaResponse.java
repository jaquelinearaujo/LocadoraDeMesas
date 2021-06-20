package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaResponse extends BaseResponse{
    private List<Pessoa> pessoas;
}
