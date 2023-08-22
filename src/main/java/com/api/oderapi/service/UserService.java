package com.api.oderapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.oderapi.converter.UserConverter;
import com.api.oderapi.domain.model.User;
import com.api.oderapi.dto.LoginRequestDTO;
import com.api.oderapi.dto.LoginResponseDTO;
import com.api.oderapi.dto.UserDTO;
import com.api.oderapi.exceptions.GeneralServiceException;
import com.api.oderapi.exceptions.NoDataFoundException;
import com.api.oderapi.exceptions.ValidateServiceException;
import com.api.oderapi.repository.UserRepository;
import com.api.oderapi.security.JwtUtils;
import com.api.oderapi.validators.UserValidator;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter converter;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ENCRIPTAMOS EL PASSWORD CUANDO SE CREA EL USUARIO
    public User createUser(User user) {
        try {
            UserValidator.signup(user);
            User existuser = userRepository.findByUsername(user.getUsername())
                    .orElse(null);

            if (existuser != null) {
                throw new ValidateServiceException("El nombre de usuario ya existe");
            }
            // CODIFICAMOS EL PASSWORD PARA NO MANDARLO POR PAYLOAD
            String passEncoded = passwordEncoder.encode(user.getPassword());
            // ACTUALIZA EL PASSWORD AL CODIFICADO Y LO GUARDA
            user.setPassword(passEncoded);
            return userRepository.save(user);

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    // LOGINRESPONSEDTO Y EL LOGINREQUESTDTO NO PERTENECEN A UN MODELO DE DOMINIO
    // LOGIN NO ES UN ENTITY POR LO QUE RECIVE Y DEVUELVE DTOs
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ValidateServiceException("Usuario o Password incorrecto"));
            // COMPARA EL PASSWORD QUE SE LO MANDA POR REQUEST CON EL
            // PASSWORD ENCRIPTADO QUE TENEMOS EN LA DB
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new ValidateServiceException("Usuario o Password incorrecto");
            }
            // COMPARA EL PASSWORD QUE SE MANDA POR REQUEST CON EL PASSWORD DE DB
            // if (!user.getPassword().equals(request.getPassword())) {
            // throw new ValidateServiceException("Usuario o Password incorrecto");
            // }

            // GENERA UN TOKEN PARA EL USUARIO
            String token = jwtUtils.generateAccesstoken(user.getUsername());

            UserDTO userDTO = converter.fromEntity(user);
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .user(userDTO)
                    .token(token)
                    .build();
            return response;

        } catch (ValidateServiceException | NoDataFoundException e) {
            log.info(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GeneralServiceException(e.getMessage(), e);
        }
    }

    public String getUsernameGetToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtUtils.getSignatureKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // NO DA DETALLES DE QUE EXPIRO POR SEGURIDAD
            throw new ValidateServiceException("Invalid Token");
        }
    }
}
