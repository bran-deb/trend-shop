package com.api.oderapi.config;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.api.oderapi.converter.OrderConverter;
import com.api.oderapi.converter.ProductConverter;
import com.api.oderapi.converter.UserConverter;

@Configuration // NUEVOS BINS PARA SER INSTANCIADOS
public class ConverterConfig {
    // INJECT PROPIEDAD EN LA VARIABLE DATEFORMAT DESDE CONVERTERCONFIG
    @Value("${config.datetimeformat}")
    private String dateFormat;

    // @BEAN LOS PONE EN EL CONTEXTO DE SPRINGBOOT
    // Y PUEDEN SER INYECTADOS CON @AUTOWIRED
    @Bean
    ProductConverter getProductConverter() {
        return new ProductConverter();
    }

    @Bean
    UserConverter getUserConverter() {
        return new UserConverter();
    }

    @Bean
    OrderConverter getOrderConverter() {
        // DA FORMATO DE LA FECHA QUE SE QUIERE
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateFormat);
        return new OrderConverter(format, getProductConverter(), getUserConverter());
    }
    // GETPRODUCTCONVERTER()
    // DATEFORMAT
    // GETUSERCONVERTER()
    // AL INYECTAR LOS CONVERTER DE ESTA FORMA YA NO SE INSTANCIA EN TODOS LOS
    // SERVICIOS QUE LO REQUIEREN Y AL AGREGAR NUEVOS CONVERTER NO NOS MARCA ERROR
    // EN TODOS LOS SERVICIOS O ARCHIVOS
    // SI SE REQUIERE UN NUEVO CONVERTER SE LO AGREGA UNICAMENTE AQUI Y SE INYECTA
    // AUTOMATICAMENTE EN TODOS LOS SERVICIOS O ARCHIVOS
    // OrderConverter(format, getProductConverter(), getUserConverter());
}
