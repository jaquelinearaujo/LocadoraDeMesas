package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produto")
public class Produto implements Serializable {
    private static final long serialVersionUID = -5873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="produtos_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    private Float preco;
    private Integer estoque;
    @Column(name = "estoque_atual")
    private Integer estoqueAtual;
}
