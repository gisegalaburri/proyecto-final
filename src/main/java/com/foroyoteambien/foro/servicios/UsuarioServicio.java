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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    FotoServicio fotoServicio;

    public void altaUsuario(String nombre, String apellido, String nickname,
            String email, String clave1, String clave2, String descripcion,
            Pais pais, Date fechaNacimiento, Diagnostico diagnostico,
            MultipartFile archivo) throws ErrorServicio {

        validar(nombre, apellido, nickname, email, clave1, clave2, pais, fechaNacimiento, diagnostico);

        Usuario usuario = usuarioRepositorio.buscarPorNick(nickname);

        if (usuario == null) {

            usuario = usuarioRepositorio.buscarPorMail(email);

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
                usuario.setRol(Rol.USUARIO);
                usuario.setFechaAlta(new Date());
                usuario.setActivo(true);

                Foto foto = fotoServicio.guardarFoto(archivo);
                usuario.setFoto(foto);

                usuarioRepositorio.save(usuario);

            } else {
                throw new ErrorServicio("El email ingresado ya está en uso. Inicia sesión.");
            }

        } else {
            throw new ErrorServicio("El nickname elegido ya está en uso.");
        }
    }

    @Transactional
    public Usuario modificarUsuario(String id, String nombre, String apellido,
            String nickname, String email, String clave1, String clave2,
            String descripcion, Pais pais, Date fechaNacimiento,
            Diagnostico diagnositco, MultipartFile archivo) throws ErrorServicio {

        validar(nombre, apellido, nickname, email, clave1, clave2, pais, fechaNacimiento, diagnositco);

        if (id.isEmpty() || id == null) {
            throw new ErrorServicio("El id no puede ser nulo.");
        }

        Optional<Usuario> optional = usuarioRepositorio.findById(id);

        if (optional.isPresent()) {

            if (!optional.get().getEmail().equals(email)) {
                Usuario usuarioEncontrado = usuarioRepositorio.buscarPorMail(email);

                if (usuarioEncontrado != null) {
                    throw new ErrorServicio("Ese e-mail ya está en uso. Ingrese otro.");
                }
            }

            Usuario usuario = optional.get();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setEmail(email);

            String encypted1 = new BCryptPasswordEncoder().encode(clave1);
            usuario.setClave1(encypted1);

            String encypted2 = new BCryptPasswordEncoder().encode(clave2);
            usuario.setClave2(encypted2);

            usuario.setDescripcion(descripcion);
            usuario.setPais(pais);
            usuario.setFechaNacimiento(fechaNacimiento);
            usuario.setDiagnostico(diagnositco);

            String idFoto = "";
            if (archivo.getContentType().equals("image/png") || archivo.getContentType().equals("image/jpeg")) {
                idFoto = usuario.getFoto().getId();
                
                if (idFoto.equals("default")) {
                    Foto foto = fotoServicio.guardarFoto(archivo);
                    usuario.setFoto(foto);
                } else {

                    Foto foto = fotoServicio.actualizar(idFoto, archivo);
                    usuario.setFoto(foto);
                }

            }

            usuario.setFechaModificacion(new Date());

            return usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("Id no encontrado");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);

        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            usuario.setFechaModificacion(new Date());
            usuario.setActivo(false);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");

        }
    }

    @Transactional
    public void volverAHabilitar(String id) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);

        if (opt.isPresent()) {
            Usuario usuario = opt.get();

            usuario.setFechaModificacion(new Date());
            usuario.setActivo(true);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");

        }
    }

    public Boolean validarActivo(String id) throws ErrorServicio {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);

        if (opt.isPresent()) {
            Usuario usuario = opt.get();

            return usuario.isActivo();
        } else {
            throw new ErrorServicio("No se encontró el usuario solicitado.");
        }
    }

    public List<Usuario> listarActivos() {
        return usuarioRepositorio.buscarActivos();
    }

    public List<Usuario> listarNoActivos() {
        return usuarioRepositorio.listarNoActivos();
    }

    
    
    
    public Usuario buscarUno(String id) {
        Optional<Usuario> opt = usuarioRepositorio.findById(id);

        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            return usuario;
        } else {
            return null;
        }
    }

    public Usuario buscarPorNickname(String nickname) {

        Usuario usuario = usuarioRepositorio.buscarPorNick(nickname);
       
        return usuario;
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

        if (nickname.trim().isEmpty() || nickname == null) {
            throw new ErrorServicio("El campo nickname no puede estar vacío.");
        }

        if (email.trim().isEmpty() || !email.contains("@") || email == null) {
            throw new ErrorServicio("El campo E-mail no puede estar vacío.");
        }

        if (clave1.isEmpty() || clave1 == null) {
            throw new ErrorServicio("Debe ingresar una contraseña");
        }

        if (clave1.length() < 6) {
            throw new ErrorServicio("La contraseña debe tener al menos 6 caracteres.");
        }

        if (clave2.isEmpty() || clave2 == null) {
            throw new ErrorServicio("Debe repetir su contraseña.");
        }

        if (!clave1.equals(clave2)) {
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

    public Date convertirDate(String fecha) {

        try {
            DateFormat fechaHora = new SimpleDateFormat("yyyy-MM-dd");
            Date convertido = fechaHora.parse(fecha);

            return convertido;

        } catch (ParseException ex) {
            Logger.getLogger(UsuarioServicio.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) {

        Usuario usuario = usuarioRepositorio.buscarPorNick(nickname);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<GrantedAuthority>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getNickname(), usuario.getClave1(),usuario.isActivo(),true, true, true, permisos);
            //return new User(usuario.getNickname(), usuario.getClave1(), permisos);
        } else {
            return null;
        }
    }
}
