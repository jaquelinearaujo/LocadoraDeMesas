package com.dawii.trabfinal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/vendor/**","/fonts/**","/css/**", "/images/**", "/js/**","/","/api/locacao/abririnserir").permitAll()
                .antMatchers("/api/usuario/**", "/api/produto/pesquisar", "/api/produto/abrirpesquisa", "/api/produto/remover", "/api/produto/alterar").hasRole("ADMIN")
                .antMatchers("/api/locacao/abririnserir", "/api/locacao/cadastrar").hasAnyRole("ADMIN", "USUARIO")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select usuario, senha, ativo "
                        + "from usuario "
                        + "where usuario = ?")
                .authoritiesByUsernameQuery("SELECT tab.usuario, papel.nome from"
                        + "(SELECT usuario.usuario, usuario.codigo from usuario where usuario = ?) as tab "
                        + " inner join usuario_papel on codigo_usuario = tab.codigo \n"
                        + " inner join papel on codigo_papel = papel.codigo;")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idEncoder = "argon2";
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("argon2", new Argon2PasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idEncoder, encoders);

        return passwordEncoder;
    }
}
