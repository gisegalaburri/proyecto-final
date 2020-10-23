package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.servicios.HiloServicio;
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
@RequestMapping("/sala")

public class SalaController {
    @Autowired
    SalaServicio salaServicio; 
    
    @GetMapping("/listarsalas")
    private String listarsalas(ModelMap modelo, HttpSession session) throws ErrorServicio{
        List <Sala> salas= salaServicio.listarSalas(); 
        modelo.put("salas", salas);
        return "loginsuccess.html";
    }
    
    
    @PostMapping("/crearsala")
    private String crearsala(ModelMap modelo,
            @RequestParam String titulosala,
            @RequestParam String descripcionsala,
            HttpSession session) throws ErrorServicio {

        try {
            salaServicio.crearSala(titulosala, descripcionsala); 
            
       
       } 
        catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
    return "menuadministrador.html"; 
    }
    
    
    
    
}

    

            
    
