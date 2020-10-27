
package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.servicios.HiloServicio;
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
    @GetMapping("/vermensajes")
    public String crear (ModelMap modelo, HttpSession session) throws ErrorServicio{
        modelo.put("vermensajes", "notnull");
        List<Mensaje> mensajes = mensajeServicio.listaNoResueltos();
        modelo.put("mensajes", mensajes);
       return "menuadministrador.html";
    }
    
      
    @PostMapping("/solucionarmensaje")
    public String solucionarmensaje(ModelMap modelo,
            @RequestParam String id,
            HttpSession session) throws ErrorServicio {

        try {
            mensajeServicio.solucionarMensaje(id);

            List<Sala> salas = salaServicio.listarSalas();
            modelo.put("salas", salas);
            List<Mensaje> listamensajes = mensajeServicio.listaNoResueltos();
            modelo.put("listamensajes", listamensajes);
            return "menuadministrador.html";

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "menuadministrador.html";
    }
    
    
    
//    FALTA metodo guardar mensaje nuevo
//    form th:action=" @{/guardarmensaje/} mensaje.html Linea 82
    
}
