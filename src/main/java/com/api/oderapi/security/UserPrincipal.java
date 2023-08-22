package com.api.oderapi.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.oderapi.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ESTA CLASE REPRESENTA EL USUARIO AUTENTICADO EN SPRING SECURITY
// LA CLASE USER NO ES UNA CLASE QUE SPRING SECURITY RECONOCE
// SE HACE UN WRAPPER CON LA IMPLEMENTACION DE USERDETAIL
// PARA QUE SPRING SECURITY PUEDE RECONOCER
@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private User user;
    private Collection<? extends GrantedAuthority> authorities;// COLECCION DE AUTORIZACIONES

    public static UserPrincipal create(User user) {
        // CUANDO LOS TOKENS ESTAN BASADOS EN TOKEN
        // PODEMOS DEFINIR CUANTOS ROLES TIENE LA APP
        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        // RETORNA EL USUARIO PRINCIPAL
        return new UserPrincipal(user, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;

    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    // LA FORMA DE EXTRAER EL USUARIO ES METODO STATIC
    public static User getCureentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getUser();
    }
}
