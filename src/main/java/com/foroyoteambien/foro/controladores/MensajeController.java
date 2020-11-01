package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.enumeraciones.Asunto;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.servicios.HiloServicio;
import com.foroyoteambien.foro.servicios.MensajeServicio;
import com.foroyoteambien.foro.servicios.ProfesionalServicio;
import com.foroyoteambien.foro.servicios.SalaServicio;
import java.util.List;
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

@Controller
@PreAuthorize("hasRole('ROLE_MODERADOR') || hasRole('ROLE_USUARIO')")
@RequestMapping("/")
public class MensajeController {

    @Autowired
    MensajeServicio mensajeServicio;

    @Autowired
    HiloServicio hiloServicio;

    @Autowired
    SalaServicio salaServicio;

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    //  muestra los mensajes al administrador
    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/vermensajes")
    public String vermensajes(ModelMap modelo, HttpSession session) throws ErrorServicio {
        modelo.put("vermensajes", "notnull");
        List<Mensaje> mensajes = mensajeServicio.listaNoResueltos();
        modelo.put("mensajes", mensajes);
        return "menuadministrador.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/solucionarmensaje/{id}")
    public String solucionarmensaje(ModelMap modelo,
            @PathVariable String id,
            HttpSession session) {
        try {
            mensajeServicio.solucionarMensaje(id);
            modelo.put("exito", "Mensaje marcado como solucionado");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "redirect:/vermensajes";
    }
    
     // pagina para mensaje nuevo de usuario
    @GetMapping("/mensajes")
    public String crearmensaje(HttpSession session) {
        return "mensaje.html";
    }

    @PostMapping("/guardarmensaje")
    public String guardarmensaje(ModelMap modelo,
            @RequestParam Asunto asunto,
            @RequestParam String descripcion,
            @RequestParam String idUsuario,
            HttpSession session) {
        try {
            mensajeServicio.crearMensaje(asunto, descripcion, idUsuario);
            modelo.put("exito", "Mensaje enviado correctamente");

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }

        return "mensaje.html";
    }

}
