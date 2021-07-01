package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public  class Pessoa implements Serializable {
    private static final long serialVersionUID = -6873580871502135134L;

    @Id
    @SequenceGenerator(name="gerador", sequenceName="usuario_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador", strategy= GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "Nome é requerido")
    @Size(min = 1, max = 255, message = "O nome deve ter entre 1 e 255 caracteres")
    private String nome;
    @NotBlank(message = "Email é requerido")
    private String email;
    @NotBlank(message = "Senha é requerida")
    private String senha;
    private Boolean ativo;
    @ManyToMany
    @JoinTable(name = "usuario_papel",
            joinColumns = @JoinColumn(name = "codigo_usuario"),
            inverseJoinColumns = @JoinColumn(name = "codigo_papel"))
    private List<Papel> papeis = new ArrayList<>();
    @NotBlank(message = "Usuario é requerido")
    private String usuario;

}
