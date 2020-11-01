/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Foto;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.servicios.FotoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Controller
@RequestMapping("/")
public class FotoController {

    @Autowired
    private FotoServicio fotoServicio;

    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> photo(@PathVariable String id, ModelMap modelMap) {
        Foto foto = null;
        try {
            foto = fotoServicio.buscarPorId(id);
            modelMap.put("nombre", foto.getNombre());
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(foto.getContenido(), headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/subir-foto")
    public String subirFoto() {
        return "foto-default.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @PostMapping("/subir-foto")
    public String subirFotoPorDefault(MultipartFile archivo, ModelMap modelMap) {
        Foto foto = null;
        try {
            foto = fotoServicio.guardarPorDefecto(archivo);
        } catch (ErrorServicio e) {
        }

        modelMap.put("titulo", "Foto cargada con exito");
        return "index.html";
    }

}
