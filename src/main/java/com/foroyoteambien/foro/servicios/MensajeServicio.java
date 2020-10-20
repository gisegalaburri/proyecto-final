package com.foroyoteambien.foro.servicios;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.enumeraciones.Asunto;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.MensajeRepositorio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;

@Service
public class MensajeServicio {
	@Autowired
	MensajeRepositorio mensajeRepositorio;
	@Autowired
	UsuarioServicio usuarioServicio;
	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	public void crearMensaje(Asunto asunto, String descripcion, String idUsuario, Usuario remitente)
			throws ErrorServicio {
		validar(asunto, descripcion, idUsuario);
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
		if (respuesta.isPresent()) {

			Mensaje mensaje = new Mensaje();

			remitente = respuesta.get();
			mensaje.setAsunto(asunto);
			mensaje.setDescripcion(descripcion);
			mensaje.setFechaAlta(new Date());
			mensaje.setSolucionado(false);

			mensajeRepositorio.save(mensaje);
		} else {
			throw new ErrorServicio("Usuario no encontrado, no puede enviar mensaje");
		}

	}

	public void modificarMensaje(Asunto asunto, String descripcion, String idUsuario, String idMensaje)
			throws ErrorServicio {
		validar(asunto, descripcion, idUsuario);

		Optional<Mensaje> respuesta = mensajeRepositorio.findById(idMensaje);

		if (respuesta.isPresent()) {
			Mensaje mensaje = respuesta.get();
			if (mensaje.getRemitente().getId().equals(idUsuario)) {
				mensaje.setAsunto(asunto);
				mensaje.setDescripcion(descripcion);
			} else {
				throw new ErrorServicio("Solo puede editar sus propios mensajes");
			}

		} else {
			throw new ErrorServicio("El mensaje no ha sido encontrado, no se puede modificar");
		}
	}

	// Dejar por defecto el solucionado como false y crear metodo para solucionarlo
	public void cambiarEstado(Asunto asunto, String descripcion, String idUsuario, String idMensaje,
			boolean solucionado) throws ErrorServicio {
		validar(asunto, descripcion, idUsuario);

		Optional<Mensaje> respuesta = mensajeRepositorio.findById(idMensaje);

		if (respuesta.isPresent()) {
			Mensaje mensaje = respuesta.get();
			mensaje.setSolucionado(true);
		} else {
			throw new ErrorServicio("Mensaje no se encuentra en la base de datos, no se puede cambiar estado");
		}
	}

	// Lista y ordena todos los Mensajes de DB

	public List<Mensaje> listaMensajes() {
		List<Mensaje> mensajes = mensajeRepositorio.findAll();
		Collections.sort(mensajes, (Mensaje a1, Mensaje a2) -> a1.getId().compareTo(a2.getId()));

		return mensajes;
	}

	// Lista todos los mensajes no resueltos
	public List<Mensaje> listaNoResueltos(String id) {
		List<Mensaje> listaNoResueltos = mensajeRepositorio.buscarNoResuelto();

		return listaNoResueltos;
	}
	 

	public void validar(Asunto asunto, String descripcion, String idUsuario) throws ErrorServicio {

		if (asunto == null) {
			throw new ErrorServicio("Debe elegir uno de los asuntos de la lista");
		}
		if (descripcion.trim().isEmpty() || descripcion == null) {
			throw new ErrorServicio("El mensaje esta vacio, este debe tener un cuerpo");
		}
		if (descripcion.length() < 10) {
			throw new ErrorServicio("El cuerpo del mensaje debe superar los 10 caracteres");
		}
		if (idUsuario == null || idUsuario.isEmpty()) {
			throw new ErrorServicio("El id no puede ser nulo");
		}
	}

}
