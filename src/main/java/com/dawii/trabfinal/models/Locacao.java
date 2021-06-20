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
    @JoinColumn(name = "cod_pessoa", referencedColumnName = "codigo")
    @ManyToOne(targetEntity = Pessoa.class)
    private Long codPessoa;
    @JoinColumn(name = "cod_mesa", referencedColumnName = "codigo")
    @ManyToOne(targetEntity = Mesa.class)
    private Long codMesa;
    @Column(name = "dt_inicio_locacao")
    private String dtInicioLocacao;
    @Column(name = "dt_fim_locacao")
    private String dtFimLocacao;
    @Column(name = "val_total")
    private Double valTotal;
}
