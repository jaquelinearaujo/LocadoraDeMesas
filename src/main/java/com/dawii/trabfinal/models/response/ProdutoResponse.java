package com.dawii.trabfinal.models.response;

import com.dawii.trabfinal.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse extends BaseResponse{
    private List<Produto> produtos;
}
