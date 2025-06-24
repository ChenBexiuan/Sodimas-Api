package com.sodimacapp.demo.controller;

import com.sodimacapp.demo.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController // Controlador REST para manejar operaciones con archivos
@RequestMapping("/api/files") // Ruta base para la gestión de archivos
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    // Descargar archivo CV desde la carpeta de archivos guardados
    @GetMapping("/cvs/{fileName:.+}")
    public ResponseEntity<Resource> downloadCv(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = "application/octet-stream"; // Tipo de contenido por defecto
        try {
            // Intentar determinar el tipo MIME del archivo
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Si falla, se mantiene el tipo por defecto
        }

        // Retorna el archivo como recurso descargable (o visualizable en navegador)
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
