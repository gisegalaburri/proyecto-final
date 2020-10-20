package com.foroyoteambien.foro.servicios;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.SalaRepositorio;


@Service
public class SalaServicio {
	@Autowired
	SalaRepositorio salaRepositorio;
	
	
	//Listar Hilos buscar forma de relacionar los hilos con su sala correspondiente
	
	@Transactional
	public void crearSala(String titulo, String descripcion) throws ErrorServicio {
	
	validar(titulo, descripcion);
	
	Sala sala = new Sala(); 
	sala.setActiva(true);
	sala.setDescripcion(descripcion);
	sala.setFechaAlta(new Date());
	sala.setTitulo(titulo);
	//sala.setListaHilos(listaHilos); 
	salaRepositorio.save(sala); 
	}
	
	@Transactional
	public void desactivarSala(String titulo, String descripcion, String idSala) throws ErrorServicio {
		validar(titulo, descripcion);
		Optional<Sala> respuesta = salaRepositorio.findById(idSala);
		if(respuesta.isPresent()){
			Sala sala= respuesta.get(); 
			sala.setActiva(false);
			sala.setFechaModificacion(new Date());
		} else {
			throw new ErrorServicio("La sala no fue encontrada, no se puede realizar operación.");
		}
			
	}
	
	
	@Transactional
	private void validar(String titulo, String descripcion)throws ErrorServicio{
		
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
