package com.api.oderapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.api.oderapi.security.RestAuthenticationEntryPoint;
import com.api.oderapi.security.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity // para que spring detecte como archivo de config
public class SecurityConfig {

        @Autowired
        RestAuthenticationEntryPoint entryPoint;

        // HttpSecurity ES EL BEAN CON EL QUE CONFIGURAMOS LA SEGURIDAD EN SPRING
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.disable())
                                .csrf((csrf) -> csrf.disable())
                                .formLogin(form -> form
                                                // .successHandler(successHandler()) //redirecciona a una url al pasar
                                                // el form
                                                .disable())
                                .httpBasic(basic -> basic.disable())
                                // .sessionManagement(session ->session.sessionRegistry(sessionRegistry()))
                                // //injecta un objeto que administra todas las sesiones
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                // .invalidSessionUrl("/login") // si la sesion es erronea redirecciona al login
                                // .maximumSessions(1) // numero de sessiones que puede tener un usuario
                                // .expiredUrl("/login") // si expira el login se lo redirecciona al login
                                )
                                // .exceptionHandling(exception -> exception
                                // .authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                                .exceptionHandling((exceptionHandling) -> exceptionHandling
                                                .authenticationEntryPoint(entryPoint))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/login").permitAll()
                                                .requestMatchers("/signup").permitAll()
                                                .anyRequest().authenticated());

                http.addFilterBefore(
                                createTokenAuthenticationFilter(),
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public TokenAuthenticationFilter createTokenAuthenticationFilter() {
                return new TokenAuthenticationFilter();
        }

        // ENCRIPTA LOS DATOS
        @Bean
        public PasswordEncoder createPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
// CORS PERMITE CONSUMIR UNA API QUE NO ESTA EN EL MISMO DOMINIO
// csrf INTERCEPTA LOS DATOS DEL FORMULARIO Y OBTIENE DATOS SENSIBLES
// sessionManagement CADA PETICION QUE SE HACE AL API SE MANEJA SIN ESTADO
// exceptionHandling MANEJO DE LAS EXCEPCIONES
// authorizeHttpRequests TIENE permitAll() .authenticated()
// permitAll() PERMITE ACCEDER SIN AUTH
// .authenticated() NECESITA AUTH PARA ACCEDER

// CONFIG SPRING SECURITY VER 5...
// @Bean
// public UserDetailsService userDetailsService() {
// UserDetails user = User.withDefaultPasswordEncoder()
// .username("user")
// .password("password")
// .roles("USER")
// .build();
// UserDetails admin = User.withDefaultPasswordEncoder()1
// .username("admin")
// .password("password")
// .roles("ADMIN", "USER")
// .build();
// return new InMemoryUserDetailsManager(user, admin);
// }

// BEAN QUE ADMINISTRA TODAS LAS SESSIONS
// @Bean
// public SessionRegistry sessionRegistry() {
// return new SessionRegistryImpl();
// }

// URL A LA QUE SE REDIRIGE
// public AuthenticationSuccessHandler successHandler() {
// return ((request, response, authentication) -> {
// response.sendRedirect("/v1/index");
// });
// }