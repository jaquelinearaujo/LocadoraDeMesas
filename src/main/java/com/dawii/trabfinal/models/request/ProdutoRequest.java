package com.dawii.trabfinal.models.request;

import com.dawii.trabfinal.models.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequest {
    private Produto produto;
}
