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
@Table(name = "historico")
public class Historico implements Serializable {
    private static final long serialVersionUID = -5873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="historico_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @ManyToOne(targetEntity = Locacao.class)
    @JoinColumn(name = "cod_locacao")
    private Long codLocacao;
    private String data;
}
