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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")

public class SalaController {
    @Autowired
    SalaServicio salaServicio; 
    
    @Autowired
    MensajeServicio mensajeServicio;
    
    @Autowired
    ProfesionalServicio profesionalServicio;
    @Autowired
    ProfesionalRepositorio profesionalRepositorio;


//  carga formulario para crear sala
    @GetMapping("/crearsala")
    public String crearsala (ModelMap modelo, HttpSession session) throws ErrorServicio{
        modelo.put("crearsala", "notnull");
        return null;

   
 
    
    }

//  guarda datos de nueva sala
    @PostMapping("/crearsalaAdmin")
    public String crearsalaAdmin(ModelMap modelo,
            @RequestParam String titulosala,
            @RequestParam String descripcionsala,
            HttpSession session) throws ErrorServicio {
        try {
            salaServicio.crearSala(titulosala, descripcionsala); 
       } 
        catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        List<Sala> salas = salaServicio.listarSalas();
        modelo.put("salas", salas);
        return "menuadministrador.html"; 
    }
         
}

    

            
    
