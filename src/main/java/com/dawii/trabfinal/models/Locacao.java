package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locacao")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Locacao implements Serializable {
    private static final long serialVersionUID = -7873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="locacao_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @Column(name = "cod_pessoa")
    private Long codPessoa;
    @Transient
    private List<Produto> produtos = new ArrayList<>();
    @Column(name = "val_total")
    private Float valTotal;
    @Column(name = "data_inicio")
    private String dataInicio;
    @Column(name = "data_fim")
    private String dataFim;
}


