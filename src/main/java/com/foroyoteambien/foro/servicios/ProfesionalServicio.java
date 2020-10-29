/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foroyoteambien.foro.servicios;

import com.foroyoteambien.foro.entidades.Foto;
import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Profesion;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import java.util.Date;
import java.util.List;
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
public class ProfesionalServicio {

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    FotoServicio fotoServicio;

    @Transactional
    public void altaProfesional(String nombre, String apellido, String email,
            Pais pais, Profesion profesion, String descripcion, Integer telefono,
            MultipartFile archivo) throws ErrorServicio {

        validar(nombre, apellido, email, pais, profesion, telefono);

        Profesional profesional = profesionalRepositorio.buscarPorMail(email);

        if (profesional == null) {
            profesional = new Profesional();

            profesional.setNombre(nombre);
            profesional.setApellido(apellido);
            profesional.setEmail(email);
            profesional.setPais(pais);
            profesional.setProfesion(profesion);
            profesional.setTelefono(telefono);
            profesional.setDescripcion(descripcion);
            profesional.setFechaAlta(new Date());
            profesional.setActivo(true);

            Foto foto = fotoServicio.guardarFoto(archivo);
            profesional.setFoto(foto);

            profesionalRepositorio.save(profesional);
        } else {
            throw new ErrorServicio("Ya existe en nuestra lista un profesional con ese e-mail.");
        }
    }

    @Transactional
    public void modificarProfesional(String id, String nombre, String apellido, String email,
            Pais pais, Profesion profesion, String descripcion, Integer telefono,
            MultipartFile archivo) throws ErrorServicio {

        validar(nombre, apellido, email, pais, profesion, telefono);

        if (id.isEmpty() || id == null) {
            throw new ErrorServicio("El id no puede ser nulo.");
        }

        Optional<Profesional> optional = profesionalRepositorio.findById(id);

        if (optional.isPresent()) {

            Profesional profesional = optional.get();

            if (!profesional.getEmail().equals(email)) {
                Profesional profesionalEncontrado = profesionalRepositorio.buscarPorMail(email);

                if (profesionalEncontrado != null) {
                    throw new ErrorServicio("E-mail en uso por otro profesional. Ingrese otro "
                            + "e-mail o póngase en contacto con el administrador");
                }
            }

            profesional.setNombre(nombre);
            profesional.setApellido(apellido);
            profesional.setEmail(email);
            profesional.setPais(pais);
            profesional.setProfesion(profesion);
            profesional.setDescripcion(descripcion);
            profesional.setTelefono(telefono);
            profesional.setFechaModificacion(new Date());

            String idFoto = "";
            if (archivo.getContentType().equals("image/png") || archivo.getContentType().equals("image/jpeg")) {
                idFoto = profesional.getFoto().getId();
                
                if (idFoto.equals("default")) {
                    Foto foto = fotoServicio.guardarFoto(archivo);
                    profesional.setFoto(foto);
                } else {

                    Foto foto = fotoServicio.actualizar(idFoto, archivo);
                    profesional.setFoto(foto);
                }
            }

            profesionalRepositorio.save(profesional);

        } else {
            throw new ErrorServicio("Profesional no encontrado");
        }
    }

    @Transactional
    public void dehabilitar(String id) throws ErrorServicio {
        Optional<Profesional> opt = profesionalRepositorio.findById(id);

        if (opt.isPresent()) {
            Profesional profesional = opt.get();

            profesional.setFechaModificacion(new Date());
            profesional.setActivo(false);

            profesionalRepositorio.save(profesional);

        } else {
            throw new ErrorServicio("No se encontró el profesional solicitado");
        }
    }

    @Transactional
    public void volverAHabilitar(String id) throws ErrorServicio {
        Optional<Profesional> opt = profesionalRepositorio.findById(id);

        if (opt.isPresent()) {
            Profesional profesional = opt.get();

            profesional.setFechaModificacion(new Date());
            profesional.setActivo(true);

            profesionalRepositorio.save(profesional);

        } else {
            throw new ErrorServicio("No se encontró el profesional solicitado");
        }
    }

    public List<Profesional> listarActivos() {

        List<Profesional> profesionales = profesionalRepositorio.listarActivos();
        return profesionales;

    }


    public List<Profesional> listarPorPais(Pais pais, Profesion profesion) {
        return profesionalRepositorio.listarPorPais(pais, profesion);
    }
    
    
    public List<Profesional> listarNoActivos() {
        return profesionalRepositorio.listarNoActivos();
    }

    private void validar(String nombre, String apellido, String email,
            Pais pais, Profesion profesion, Integer telefono) throws ErrorServicio {

        if (nombre.trim().isEmpty() || nombre == null) {
            throw new ErrorServicio("El campo nombre no puede estar vacío.");
        }

        if (apellido.trim().isEmpty() || apellido == null) {
            throw new ErrorServicio("El campo apellido no puede estar vacío.");
        }

        if (email.trim().isEmpty() || !email.contains("@") || email == null) {
            throw new ErrorServicio("El campo E-mail no puede estar vacío.");
        }

        if (pais == null) {
            throw new ErrorServicio("Debe elegir un país de la lista.");
        }

        if (profesion == null) {
            throw new ErrorServicio("Debe elegir una profesión de la lista.");
        }

        if (telefono == null) {
            throw new ErrorServicio("El campo teléfono no puede estar vacío.");
        }

    }

}
