package pucmm.edu.eitc.practica2mocky.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by vacax on 27/09/16.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ConfiguracionSeguridad {

    //Configuación para la validación del acceso modo JDBC
    private final UserDetailsService userDetailsService;

    public ConfiguracionSeguridad(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    /**
     * La autentificación de los usuarios.
     * @param auth
     * @throws Exception
     */

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    protected void configure(HttpSecurity http) throws Exception {
        //Marcando las reglas para permitir unicamente los usuarios
        http
                .authorizeRequests()
                .antMatchers("/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/usuarios","/Proyectos","/addUser","/eliminarUser","/modUser").hasAnyRole("ADMIN")
                .anyRequest().authenticated() //cualquier llamada debe ser validada
                .and()
                .formLogin()
                .loginPage("/login") //indicando la ruta que estaremos utilizando.
                .failureUrl("/login?error") //en caso de fallar puedo indicar otra pagina.
                .permitAll()
                .and()
                .logout()
                .permitAll();


        http.csrf().disable();
        http.headers().frameOptions().disable();

    }


    @Bean
    @Primary
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
