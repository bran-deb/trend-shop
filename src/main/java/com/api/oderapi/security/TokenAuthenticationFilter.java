package com.api.oderapi.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.api.oderapi.domain.model.User;
import com.api.oderapi.exceptions.NoDataFoundException;
import com.api.oderapi.repository.UserRepository;
import com.api.oderapi.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// INTERCEPTA EL TOKEN
// SI EL TOKEN ES CORRECTO LO DECODIFICA Y INJECTA A LA SESSION
// SI EL TOKEN ES INCORRECTO LO RECHAZA PETICION
// OncePerRequestFilter SE EJECUTA PARA CADA PETICION
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // LOS FILTRO USAN EL PATRON CHAINOFRESPONSABILITY(cadena de responsabilidades)
        // CUANDO UN FILTRO TERMINA DEBE LLAMAR AL QUE SIGUE
        try {
            String jwt = getJwtFromRequest(request);
            // VALIDAR TOKEN
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String username = userService.getUsernameGetToken(jwt);

                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new NoDataFoundException("No existe el usuario"));
                // LE DICE A PRING SECURITY QUE ESTE ES EL USER AUTENTICADO
                UserPrincipal principal = UserPrincipal.create(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
                        null, principal.getAuthorities());
                // LA FUENTE DE AUTENTICACION VA A SER WEB
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // UsernamePasswordAuthenticationToken HACE WRAPER A principal
                // principal HACE WRAPER A UserPrincipal

                // ESTABLECE EL USUARIO AUTENTICADO Y SE PUEDE ACCEDER A EL DE CUALQUIER PARTE
                // DE LA APP
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Error al autenticar al usuario", e);
        }
        // PARA QUE PASE AL SIGUIENTE FILTRO
        filterChain.doFilter(request, response);
    }

    // EXTRAE EL TOKEN DEL REQUEST
    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        // CUANDO SE MANDA EL TOKEN A TRAVEZ DEL HEADER EN POSTMAN SE LO MANDA CON EL
        // PREFIJO("Bearer ") PARA DECIRLE QUE ES UN TOKEN YA QUE EN HEADERS HAY VARIOS
        // TIPOS DE AUTENTICACION Y SI SE LO MANDA COMO AUTHORIZATION EN POSTMAN
        // NO ES NECESARIO
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // LE QUITA 7 CARACTERES Y TRAE LO DEMAS ("BEARER ")
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
