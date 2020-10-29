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
    
    public List<Hilo> listarHiloXSala(String idSala) throws ErrorServicio {
        List<Hilo> listaActivos = hiloRepositorio.hilosActivosPorSala(idSala);
        return listaActivos;}
    
    

        @Transactional
        public Hilo crearHilo (String idsala, String titulo, String descripcion, String idUsuario)
                throws ErrorServicio {
            validar(titulo, descripcion);
             Hilo hilo = new Hilo(); 
            
            if (idUsuario == null) {
                throw new ErrorServicio("El id de usuario no puede ser nulo");
            }
            Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

            if (respuesta.isPresent()) {
                hilo.setUsuario(respuesta.get());
                hilo.setActivo(true);
                hilo.setDescripcion(descripcion);
                hilo.setFechaAlta(new Date());
                hilo.setTitulo(titulo);
                hiloRepositorio.save(hilo); 
                
                Optional<Sala> respuestaSala = salaRepositorio.findById(idsala);
                
                Sala sala = respuestaSala.get(); 
                if(respuestaSala.isPresent()){
                    List<Hilo> listaHilos= sala.getListaHilos();
                    listaHilos.add(hilo); 
                    sala.setListaHilos(listaHilos);
                    salaRepositorio.save(sala); 
                   return hilo; 
                   
            } else {
                throw new ErrorServicio("Usuario no registrado, no puede crear Hilo");
            }
        
           } return hilo; 
        }
                     
	
 
    @Transactional
    public void desactivarHilo(String idHilo) throws ErrorServicio {

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

   
    public void validar(String titulo, String descripcion) throws ErrorServicio {


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
