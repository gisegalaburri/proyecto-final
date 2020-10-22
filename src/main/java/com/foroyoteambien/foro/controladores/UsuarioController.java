/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.enumeraciones.Diagnostico;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;
import com.foroyoteambien.foro.servicios.UsuarioServicio;
import java.util.Date;
import java.util.Optional;
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

        Optional<Usuario> opt = usuarioRepositorio.findById(id);

        if (opt.isPresent()) {
            Usuario usuario = opt.get();
            modelMap.put("usuario", usuario);
        }

        return "perfil.html";
    }

    @PostMapping("/modificar-perfil")
    public String editarPerfil(ModelMap modelMap,
            @RequestParam String id,
            @RequestParam String apellido,
            @RequestParam String nickname,
            @RequestParam String email,
            @RequestParam String clave1,
            @RequestParam String clave2,
            @RequestParam(required = false) String descripcion,
            @RequestParam Diagnostico diagnostico,
            @RequestParam Pais pais,
            @RequestParam Date fechaNacimiento,
            MultipartFile archivo,
            HttpSession session) {

        try {

            usuarioServicio.modificarUsuario(email, email, apellido, nickname, email, clave1, clave2, descripcion, pais, fechaNacimiento, diagnostico, archivo);

        } catch (ErrorServicio e) {
            modelMap.put("error", e.getMessage());
            Optional<Usuario> opt = usuarioRepositorio.findById(id);

            if (opt.isPresent()) {
                Usuario usuario = opt.get();
                modelMap.put("usuario", usuario);
            }
        }

        modelMap.put("exito", "Se actualizaron correctamente los datos.");
        return "perfil.html";
    }

}