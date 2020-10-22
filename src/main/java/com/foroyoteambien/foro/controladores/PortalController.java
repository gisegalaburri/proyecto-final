/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.enumeraciones.Diagnostico;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.servicios.UsuarioServicio;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */

@Controller
@RequestMapping("/")
public class PortalController {
    
    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    private String index() {
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            ModelMap modelMap) {
        
        if (error != null) {
            modelMap.put("error", "Usuario o clave incorrectos.");
            return "login.html";
        } 

        if (logout != null) {
            modelMap.put("logout", "Has cerrado sesión.");
            return "index.html";
        }

        return "login.html";
    }
    
    @GetMapping("/registro-usuario")
    public String registroUsuario() {
        return "registro.html";
    }
    
    @PostMapping("/registro-usuario") 
    public String registrarUsuario(@RequestParam String nombre, 
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
            ModelMap modelMap) {
        
        try {
            
            usuarioServicio.altaUsuario(nombre, apellido, nickname, email, clave1, clave2, descripcion, pais, fechaNacimiento, diagnostico, archivo);
        
        } catch (ErrorServicio e) {
            modelMap.put("error", e.getMessage());
            modelMap.put("nombre", nombre);
            modelMap.put("apellido", apellido);
            modelMap.put("nickname", nickname);
            modelMap.put("email", email);
            modelMap.put("descripcion", descripcion);
            modelMap.put("diagnostico", diagnostico);
            modelMap.put("pais", pais);
            modelMap.put("fechaNacimiento", fechaNacimiento);
            return "registro.html";
        }
        
        modelMap.put("login", "Te registraste exitosamente. Ahora inicia sesión.");
        return "login.html";
    }
}
