package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locacao")
public class Locacao implements Serializable {
    private static final long serialVersionUID = -5873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="locacao_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;

    @Column(name = "cod_pessoa")
    private Long codPessoa;
    @Column(name = "cod_mesa")
    private Long codMesa;
    @Column(name = "dt_inicio_locacao")
    private String dtInicioLocacao;
    @Column(name = "dt_fim_locacao")
    private String dtFimLocacao;
    @Column(name = "val_total")
    private Double valTotal;
}


