/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foroyoteambien.foro.servicios;

import com.foroyoteambien.foro.entidades.Foto;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.enumeraciones.Diagnostico;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Rol;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Service
public class UsuarioServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositrio;
    
    @Autowired
    FotoServicio fotoServicio;

  
    public void altaUsuario(String nombre, String apellido, String nickname,
            String email, String clave1, String clave2, String descripcion,
            Pais pais, Date fechaNacimiento, Diagnostico diagnostico,
            MultipartFile archivo) throws ErrorServicio {

        validar(nombre, apellido, nickname, email, clave1, clave2, pais, fechaNacimiento, diagnostico);

        Usuario usuario = usuarioRepositrio.buscarPorNick(nickname);
        
        if (usuario == null) {
            
            usuario = usuarioRepositrio.buscarPorMail(email);

            if (usuario == null) {

                usuario = new Usuario();

                usuario.setNombre(nombre);
                usuario.setApellido(apellido);
                usuario.setNickname(nickname);
                usuario.setEmail(email);

                String encypted1 = new BCryptPasswordEncoder().encode(clave1);
                usuario.setClave1(encypted1);

                String encypted2 = new BCryptPasswordEncoder().encode(clave2);
                usuario.setClave2(encypted2);

                usuario.setDescripcion(descripcion);
                usuario.setPais(pais);
                usuario.setFechaNacimiento(fechaNacimiento);
                usuario.setDiagnostico(diagnostico);
                usuario.setRol(Rol.MODERADOR);
                usuario.setFechaAlta(new Date());
                usuario.setActivo(true);
                
                Foto foto = fotoServicio.guardarFoto(archivo);
                usuario.setFoto(foto);

                usuarioRepositrio.save(usuario);

            } else {
                throw new ErrorServicio("El email ingresado ya está en uso. Inicia sesión.");
            }

        } else {
            throw new ErrorServicio("El nickname elegido ya está en uso.");
        }
    }
    
    

    private void validar(String nombre, String apellido, String nickname,
            String email, String clave1, String clave2,
            Pais pais, Date fechaNacimiento, Diagnostico diagnostico) throws ErrorServicio {

        if (nombre.trim().isEmpty() || nombre == null) {
            throw new ErrorServicio("El campo nombre no puede estar vacío.");
        }

        if (apellido.trim().isEmpty() || apellido == null) {
            throw new ErrorServicio("El campo apellido no puede estar vacío.");
        }

        if (email.trim().isEmpty() || !email.contains("@") || email == null) {
            throw new ErrorServicio("El campo E-mail no puede estar vacío.");
        }

        if (clave1.isEmpty() || clave1 == null) {
            throw new ErrorServicio("Debe ingresar una contraseña");
        }

        if (clave1.length() < 8) {
            throw new ErrorServicio("La contraseña debe tener al menos 8 caracteres");
        }

        if (clave2.isEmpty() || clave2 == null) {
            throw new ErrorServicio("Debe repetir su contraseña.");
        }

        if (clave1 != clave2) {
            throw new ErrorServicio("Las contraseñas no coinciden.");
        }

        if (pais == null) {
            throw new ErrorServicio("Debe elegir un país de la lista.");
        }

        if (fechaNacimiento == null || fechaNacimiento.toString().isEmpty()) {
            throw new ErrorServicio("Debe indicar su edad.");
        }

        if (diagnostico.toString().equals("NO")) {
            throw new ErrorServicio("Lo sentimos, no podemos admitir tu registro ya que es "
                    + "un sitio exclusivo para mujeres diagnosticadas con TEA.");
        }

        if (diagnostico == null) {
            throw new ErrorServicio("Debes responder la pregunta sobre el diagnóstico para continuar.");
        }

    }

}
