package com.foroyoteambien.foro.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ComentarioRepositorio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;

@Service
public class ComentarioServicio {

 
	
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	ComentarioRepositorio comentarioRepositorio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	


	@Transactional
	public void crearComentario(String descripcion, String idUsuario) throws ErrorServicio {

		validar(descripcion, idUsuario);
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);

		if (respuesta.isPresent()) {
			Comentario comentario = new Comentario();
			comentario.setUsuario(respuesta.get());
			comentario.setActivo(true);
			comentario.setDescripcion(descripcion);
			comentario.setFechaAlta(new Date());
			comentarioRepositorio.save(comentario);
		}

		else {
			throw new ErrorServicio("Usuario no registrado, no puede comentar");

		}

	}

	@Transactional
	public void modificarComentario(String descripcion, String idUsuario, String idComentario) throws ErrorServicio {
		validar(descripcion, idUsuario);
		Optional<Comentario> respuesta = comentarioRepositorio.findById(idComentario);

		if (respuesta.isPresent()) {
			Comentario comentario = respuesta.get();
			if (comentario.getUsuario().getId().equals(idUsuario)) {
				comentario.setDescripcion(descripcion);
				comentario.setFechaModificacion(new Date());
				comentarioRepositorio.save(comentario);
			} else {
				throw new ErrorServicio("Solo puede editar sus propios comentarios");
			}
		} else {
			throw new ErrorServicio("Comentario no encontrado");
		}

	}
	private List<Comentario> listarActivos(String titulo, String descripcion) throws ErrorServicio{
		List<Comentario> listaActivos = comentarioRepositorio.buscarActivos();
		return listaActivos;
		
	}

	@Transactional
	public void desactivarComentario(String descripcion, String idUsuario, String idComentario) throws ErrorServicio{
		validar(descripcion, idUsuario);
		Optional<Comentario> respuesta = comentarioRepositorio.findById(idComentario);

		if (respuesta.isPresent()) {
			Comentario comentario = respuesta.get();
			if (comentario.getUsuario().getId().equals(idUsuario)) {
				comentario.setFechaModificacion(new Date());
				comentario.setActivo(false);
			} else {
				throw new ErrorServicio("Id comentario no coincide con usuario"); 
			}
			} else {
				throw new ErrorServicio("Comentario no encontrado"); 
			}
	}
	@Transactional
	public void validar(String descripcion, String idUsuario) throws ErrorServicio {
		if (descripcion.trim().isEmpty() || descripcion == null) {
			throw new ErrorServicio("Debe agregar cuerpo al comentario");
		}
		if (descripcion.length() < 10) {
			throw new ErrorServicio("El comentario no cumple con el minimo de caracteres");

		}
		if (descripcion.length() > 250) {
			throw new ErrorServicio("El comentario excede el máximo permitido de caracteres");
		}
		if (descripcion.trim().isEmpty() || descripcion == null) {
			throw new ErrorServicio("El comentario no puede ir vacio");
		}
		if (idUsuario == null || idUsuario.isEmpty()) {
			throw new ErrorServicio("El id no puede ser nulo");
		}
	}
}
