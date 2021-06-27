package com.dawii.trabfinal.models.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoFilter {
    private Long codigo;
    private String nome;
}
