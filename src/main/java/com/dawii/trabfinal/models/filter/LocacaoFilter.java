package com.dawii.trabfinal.models.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocacaoFilter {
    private Long codigo;
    private Long user;
    private String dataInicio;
}
