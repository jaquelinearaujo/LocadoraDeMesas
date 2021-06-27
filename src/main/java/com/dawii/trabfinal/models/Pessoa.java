package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pessoa")
public  class Pessoa implements Serializable {
    private static final long serialVersionUID = -6873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="pessoa_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "Nome é requerido")
    @Size(min = 1, max = 255, message = "O nome deve ter entre 1 e 255 caracteres")
    private String nome;
    @NotBlank(message = "Email é requerido")
    private String email;
    @NotBlank(message = "Senha é requerida")
    private String senha;
    @NotBlank(message = "Role é requerido")
    private String role;
    @NotBlank(message = "Telefone de contato é requerido")
    @Pattern(regexp = "(^\\(?[1-9]{2}\\)?\\s?(?:[2-8]|9[1-9])\\d{3}\\-?\\d{4}$)", message = "O telefone deve incluir o DDD e o número")
    @Column(name = "tel_contato")
    private String telContato;
    @NotBlank(message = "Endereço é requerido")
    @Size(min = 1, max = 255, message = "O endereço deve ter entre 1 e 255 caracteres")
    private String endereco;
}
