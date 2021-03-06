package com.dawii.trabfinal.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Nome é requerido")
    @Size(min = 1, max = 255, message = "O nome deve ter entre 1 e 255 caracteres")
    private String nome;
    @Email(message = "Email é requerido")
    private String email;
    @NotEmpty(message = "Senha é requerida")
    private String senha;
    private Boolean ativo;
    @ManyToMany
    @JoinTable(name = "usuario_papel",
            joinColumns = @JoinColumn(name = "codigo_usuario"),
            inverseJoinColumns = @JoinColumn(name = "codigo_papel"))
    private List<Papel> papeis = new ArrayList<>();
    @NotEmpty(message = "Usuario é requerido")
    private String usuario;
    @ManyToMany
    @JoinTable(name = "usuario_locacao",
            joinColumns = @JoinColumn(name = "codigo_usuario"),
            inverseJoinColumns = @JoinColumn(name = "codigo_locacao"))
    private List<Locacao> locacoes = new ArrayList<>();
}
