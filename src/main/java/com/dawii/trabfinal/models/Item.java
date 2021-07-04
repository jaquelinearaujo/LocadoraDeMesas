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
@Table(name = "item")
public class Item implements Serializable {
    private static final long serialVersionUID = -3873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="item_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @Column(name = "cod_locacao")
    private Long codigoLocacao;
    @Column(name = "cod_produto")
    private Long codigoProduto;
    private Integer quantidade;

    public Item(Long codigoLocacao, Long codigoProduto) {
        this.codigoLocacao = codigoLocacao;
        this.codigoProduto = codigoProduto;
    }
}
