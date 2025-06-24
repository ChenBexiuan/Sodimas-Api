package com.sodimacapp.demo.config; // Paquete donde se encuentra la configuración de CORS

import org.springframework.context.annotation.Bean; // Importamos la anotación Bean
import org.springframework.context.annotation.Configuration; // Importamos la anotación Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry; // Importamos CorsRegistry para configurar CORS
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // Importamos WebMvcConfigurer para personalizar la configuración de MVC

// Clase de configuración para CORS
@Configuration
public class CorsConfig {

    // Método que define un bean de configuración de CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            // Método que se utiliza para agregar mapeos de CORS
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permitimos CORS para todas las rutas
                        .allowedOrigins("https://green-water-0ea21d31e.6.azurestaticapps.net") // Permitimos el origen de tu frontend React
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP que permitimos
                        .allowedHeaders("*") // Permitimos todas las cabeceras en las solicitudes
                        .allowCredentials(true) // Importante para que se envíen credenciales como JWT en el encabezado Authorization
                        .maxAge(3600); 
            }
        };
    }
}
