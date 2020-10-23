package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.servicios.MensajeServicio;
import com.foroyoteambien.foro.servicios.ProfesionalServicio;
import com.foroyoteambien.foro.servicios.SalaServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/menu")
public class AdminController {

    @Autowired
    SalaServicio salaServicio;

    @Autowired
    MensajeServicio mensajeServicio;
    
    @Autowired
    ProfesionalServicio profesionalServicio;
    
    @Autowired
    ProfesionalRepositorio profesionalRepositorio;
    
    
    @GetMapping("/menuAdmin")
    private String menuAdmin(ModelMap modelo, HttpSession session) throws ErrorServicio {

        List<Sala> salas = salaServicio.listarSalas();
        modelo.put("salas", salas);

        List<Mensaje> listamensajes = mensajeServicio.listaMensajes(); 
        modelo.put("listamensajes", listamensajes);

        List<Profesional> listaprofesionales = profesionalRepositorio.findAll(); 
        modelo.put("listaprofesionales", listaprofesionales);
        
        return "menuadministrador.html"; 
        
    }
}

