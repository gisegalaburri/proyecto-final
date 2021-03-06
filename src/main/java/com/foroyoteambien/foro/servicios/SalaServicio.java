package com.foroyoteambien.foro.servicios;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.SalaRepositorio;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaServicio {

    @Autowired
    SalaRepositorio salaRepositorio;

    @Autowired
    HiloRepositorio hiloRepositorio;

    //Listar Hilos buscar forma de relacionar los hilos con su sala correspondiente
    @Transactional
    public void crearSala(String titulo, String descripcion) throws ErrorServicio {

        validar(titulo, descripcion);

        Sala sala = new Sala();
        sala.setActiva(true);
        sala.setDescripcion(descripcion);
        sala.setFechaAlta(new Date());
        sala.setTitulo(titulo);
        List<Hilo> listaHilos = new ArrayList();
        sala.setListaHilos(listaHilos);
        salaRepositorio.save(sala);
    }

    @Transactional
    public void desactivarSala(String titulo, String descripcion, String idSala) throws ErrorServicio {
        validar(titulo, descripcion);
        Optional<Sala> respuesta = salaRepositorio.findById(idSala);
        if (respuesta.isPresent()) {
            Sala sala = respuesta.get();
            sala.setActiva(false);
            sala.setFechaModificacion(new Date());

            salaRepositorio.save(sala);
        } else {
            throw new ErrorServicio("La sala no fue encontrada, no se puede realizar operación.");
        }

    }

    private List<Hilo> listarHilos(String idSala) throws ErrorServicio {
        Optional<Sala> respuesta = salaRepositorio.findById(idSala);

        if (respuesta.isPresent()) {

            Sala sala = respuesta.get();

            List<Hilo> listaHilos = hiloRepositorio.hilosActivosPorSala(idSala);
            return listaHilos;
        } else {
            throw new ErrorServicio("No se encontró la sala solicitada.");
        }
    }

    public List<Sala> listarSalas() {
        List<Sala> listaSalas = salaRepositorio.findAll();
        return listaSalas;

    }

    private void validar(String titulo, String descripcion) throws ErrorServicio {

        if (titulo.trim().isEmpty() || titulo == null) {
            throw new ErrorServicio("La sala debe contar con un titulo");
        }
        if (titulo.length() < 5) {
            throw new ErrorServicio("El titulo ingresado no cumple con el minimo de caracteres");

        }
        if (titulo.length() > 30) {
            throw new ErrorServicio("El titulo ingresado excede el maximo  de caracteres permitidos. ");
        }
        if (descripcion.trim().isEmpty() || descripcion == null) {
            throw new ErrorServicio("Debe agregar una descripción a la sala");
        }
        if (descripcion.length() < 10) {
            throw new ErrorServicio("La descripción de la sala debe superar los 10 caracteres");
        }
        if (descripcion.length() > 250) {
            throw new ErrorServicio("La descripción de la sala debe tener un máximo de 250 caracteres");
        }

    }

}
