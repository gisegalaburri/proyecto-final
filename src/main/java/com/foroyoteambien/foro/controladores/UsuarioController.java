package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.enumeraciones.Diagnostico;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;
import com.foroyoteambien.foro.servicios.UsuarioServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Controller
@PreAuthorize("hasRole('ROLE_MODERADOR') || hasRole('ROLE_USUARIO')")
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/modificar-perfil/{id}")
    public String editar(@PathVariable String id,
            ModelMap modelMap,
            HttpSession session) {

        Usuario usuario = null;
        usuario = usuarioServicio.buscarUno(id);
        modelMap.put("usuario", usuario);

        return "perfil.html";
    }

    @PostMapping("/modificar-perfil")
    public String editarPerfil(ModelMap modelMap,
            @RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam(required = false) String descripcion,
            @RequestParam Diagnostico diagnostico,
            @RequestParam Pais pais,
            @RequestParam String fechaNacimiento,
            MultipartFile archivo,
            HttpSession session) {

        Usuario usuario = usuarioServicio.buscarUno(id);
        try {
            Date fechaNac = usuarioServicio.convertirDate(fechaNacimiento);
            usuario = usuarioServicio.modificarUsuario(id, nombre, apellido, nickname, email, clave1, clave2, descripcion, pais, fechaNac, diagnostico, archivo);

            modelMap.put("exito", "Se actualizaron correctamente los datos.");
        } catch (ErrorServicio e) {
            modelMap.put("error", e.getMessage());
        }

        modelMap.put("usuario", usuario);

        return "perfil.html";
    }

    @GetMapping("/comunidad")
    public String comunidad(ModelMap modelo, HttpSession session) {
        List<String> ranking = usuarioRepositorio.rankingPaises();
        modelo.put("paises", ranking);
        return "comunidad.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/bloquearUsuario")
    public String bloquearUsuario(ModelMap modelo, HttpSession session) {
        modelo.put("bloquearUser", "notnull");
        return "menuadministrador.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @PostMapping("/buscarUnUsuario")
    public String buscarUnUsuario(ModelMap modelo,
            @RequestParam String nickname,
            HttpSession session) {

        try {
            Usuario usuario = usuarioServicio.buscarPorNickname(nickname);
            modelo.put("usuario", usuario);

        } catch (ErrorServicio ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
        }
        modelo.put("mostraruser", "notnull");
        return "menuadministrador.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/bloquear/{id}")
    public String bloquear(@PathVariable String id,
            ModelMap modelo,
            HttpSession session) {
        try {
            usuarioServicio.deshabilitar(id);
            modelo.put("exito", "Usuario bloqueado correctamente");
        } catch (ErrorServicio e) {
            modelo.put("error", "No andaaaaaaaaaaaaaaaaaaaa");
        }
        modelo.put("bloquearUser", "notnull");
        return "menuadministrador.html";

    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/habilitar-usuario")
    public String buscarUsuarioNoActivo(ModelMap modelo,
            HttpSession session) {
        List<Usuario> usuarios = usuarioServicio.listarNoActivos();
        modelo.put("usuarios", usuarios);
        modelo.put("mostrarUsuariosDeshabilitados", "notnull");
        return "menuadministrador.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/habilitar-usuario/{id}")
    public String habilitarUsuario(@PathVariable String id, ModelMap modelo,
            HttpSession session) {
        try {
            usuarioServicio.volverAHabilitar(id);
            modelo.put("exito", "Se activo correctamente el Usuario seleccionado");
        } catch (ErrorServicio e) {
            modelo.put("error", e.getMessage());

        }
        List<Usuario> usuarios = usuarioServicio.listarNoActivos();
        modelo.put("usuarios", usuarios);
        modelo.put("mostrarUsuariosDeshabilitados", "notnull");
        return "menuadministrador.html";
    }
}
