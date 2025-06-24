package com.sodimacapp.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Servicio que maneja la lógica de almacenamiento y recuperación de archivos (CVs).
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    /**
     * Constructor que inicializa y crea el directorio donde se guardarán los archivos.
     */
    public FileStorageService() {
        // Define el directorio donde se almacenarán los CVs, dentro de la carpeta "uploads/cvs".
        this.fileStorageLocation = Paths.get("uploads/cvs").toAbsolutePath().normalize();

        try {
            // Crea el directorio si no existe
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para guardar los archivos.", ex);
        }
    }

    /**
     * Guarda un archivo en el sistema de archivos.
     * 
     * @param file El archivo recibido (MultipartFile)
     * @return El nombre único generado del archivo guardado
     */
    public String storeFile(MultipartFile file) {
        // Limpia el nombre del archivo original para evitar caracteres problemáticos
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Seguridad básica: evita intentos de acceso al sistema de archivos
            if (originalFileName.contains("..")) {
                throw new RuntimeException("El nombre del archivo contiene caracteres inválidos: " + originalFileName);
            }

            // Extrae la extensión del archivo (por ejemplo, .pdf)
            String fileExtension = "";
            int lastDot = originalFileName.lastIndexOf('.');
            if (lastDot >= 0) {
                fileExtension = originalFileName.substring(lastDot);
            }

            // Genera un nuevo nombre de archivo único usando UUID
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Define la ruta completa donde se guardará el archivo
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);

            // Copia el archivo a la ruta destino, reemplazando si ya existe
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + originalFileName, ex);
        }
    }

    /**
     * Carga un archivo previamente guardado y lo expone como un recurso descargable.
     * 
     * @param fileName El nombre del archivo a cargar
     * @return Recurso (Resource) listo para ser descargado o mostrado
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            // Construye la ruta absoluta del archivo
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            // Crea un recurso URL para acceder al archivo
            Resource resource = new UrlResource(filePath.toUri());

            // Verifica si el archivo existe físicamente
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("Archivo no encontrado: " + fileName);
            }

        } catch (MalformedURLException ex) {
            throw new RuntimeException("Error al cargar el archivo: " + fileName, ex);
        }
    }
}
