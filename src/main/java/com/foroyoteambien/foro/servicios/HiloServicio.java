package com.foroyoteambien.foro.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ComentarioRepositorio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.SalaRepositorio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;

@Service
public class HiloServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    HiloRepositorio hiloRepositorio;
    @Autowired
    ComentarioRepositorio comentarioRepositorio;
    
    @Autowired
    SalaRepositorio salaRepositorio;

    //Hilo : Listar comentario , que reciba el id del hilo , busque el hilo, que devuelva la lista de comentario
    @Transactional
    private void crearHilo(String titulo, String descripcion, String idUsuario, String idSala) throws ErrorServicio {
        validar(titulo, descripcion);
        if (idUsuario == null) {
            throw new ErrorServicio("El id de usuario no puede ser nulo");
        }
        Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idUsuario);

        Optional<Sala> respuestaSala = salaRepositorio.findById(idSala);
        
        if (respuestaUsuario.isPresent()) {
            Hilo hilo = new Hilo();
            hilo.setUsuario(respuestaUsuario.get());
            hilo.setActivo(true);
            hilo.setDescripcion(descripcion);
            hilo.setFechaAlta(new Date());
            hilo.setTitulo(titulo);
            hiloRepositorio.save(hilo);
            
            if(respuestaSala.isPresent()) {
                Sala sala = respuestaSala.get();
                sala.getListaHilos().add(hilo);
                salaRepositorio.save(sala);
            } else {
                throw new ErrorServicio("No se encontró el id de la Sala.");
            }
            
        } else {
            throw new ErrorServicio("Usuario no registrado, no puede crear Hilo");
        }
    }

    private List<Hilo> listarActivos(String titulo, String descripcion) throws ErrorServicio {
        List<Hilo> listaActivos = hiloRepositorio.buscarActivos();
        return listaActivos;

    }

    private List<Comentario> listarComentarios(String idHilo) throws ErrorServicio {
        Optional<Hilo> respuesta = hiloRepositorio.findById(idHilo);

        if (respuesta.isPresent()) {

            Hilo hilo = respuesta.get();

            List<Comentario> listaComentarios = comentarioRepositorio.comentariosActivosPorHilo(idHilo);
            return listaComentarios;
        } else {
            throw new ErrorServicio("No se encontró el hilo solicitado.");
        }
    }

    @Transactional
    private void desactivarHilo(String idHilo) throws ErrorServicio {

        if (idHilo == null) {
            throw new ErrorServicio("El id de Hilo no puede ser nulo");
        }
        Optional<Hilo> respuesta = hiloRepositorio.findById(idHilo);
        if (respuesta.isPresent()) {
            Hilo hilo = respuesta.get();
            hilo.setFechaModificacion(new Date());
            hilo.setActivo(false);
            hiloRepositorio.save(hilo);
        } else {
            throw new ErrorServicio("Hilo no encontrado, no es posible desactivarlo.");
        }
    }

    @Transactional
    private void validar(String titulo, String descripcion) throws ErrorServicio {

        if (titulo.trim().isEmpty() || titulo == null) {
            throw new ErrorServicio("Por favor ingrese un titulo");
        }
        if (titulo.length() < 5) {
            throw new ErrorServicio("El titulo ingresado no cumple con el minimo de caracteres");

        }
        if (titulo.length() >= 50) {
            throw new ErrorServicio("El titulo ingresado excede el maximo  de caracteres permitidos. "
                    + "Por favor ingrese un titulo con máximo 50 caracteres");
        }
        if (descripcion.trim().isEmpty() || descripcion == null) {
            throw new ErrorServicio("Para crear un Hilo debe agregar una descripción");
        }
//Agregar validaciones de largo de descripcion (minimo de 20 caracteres ) max 250 
    }

}
