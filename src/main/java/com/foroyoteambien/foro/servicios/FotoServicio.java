/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foroyoteambien.foro.servicios;

import com.foroyoteambien.foro.entidades.Foto;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.FotoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Service
public class FotoServicio {

    @Autowired
    FotoRepositorio fotoRepositorio;

    @Transactional
    public Foto guardarFoto(MultipartFile archivo) throws ErrorServicio {

        Foto foto = null;
        if (archivo.getContentType().equals("image/png") || archivo.getContentType().equals("image/jpeg")) {
            try {
                foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());
                foto.setContenido(archivo.getBytes());
                fotoRepositorio.save(foto);
                return foto;

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else {
            foto = fotoRepositorio.getOne("default");
        }
        return foto;
    }

    @Transactional
    public Foto guardarPorDefecto(MultipartFile archivo) throws ErrorServicio {

        if (archivo != null) {
            try {
                Foto foto = new Foto();

                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getOriginalFilename());
                foto.setContenido(archivo.getBytes());

                return fotoRepositorio.save(foto);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio {
        Foto foto = null;
        
        if (archivo.getContentType().equals("image/png") || archivo.getContentType().equals("image/jpeg")) {
            try {

                Optional<Foto> opt = fotoRepositorio.findById(idFoto);
                if (opt.isPresent()) {
                    foto = opt.get();
                    foto.setMime(archivo.getContentType());
                    foto.setNombre(archivo.getOriginalFilename());
                    foto.setContenido(archivo.getBytes());
                }

                return fotoRepositorio.save(foto);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        return fotoRepositorio.getOne(idFoto);
    }

    public Foto buscarPorId(String id) throws ErrorServicio {
        return fotoRepositorio.getOne(id);
    }
}
