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
@Table(name = "mesa")
public class Mesa implements Serializable {
    private static final long serialVersionUID = -5873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="mesa_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    private String cor;
    @Column(name = "valor_aluguel")
    private Double valorAluguel;
    @Column(name = "status_locacao")
    private String statusLocacao;
}
