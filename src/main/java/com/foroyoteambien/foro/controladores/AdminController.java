package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.MensajeRepositorio;
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
public class AdminController {

    @Autowired
    SalaServicio salaServicio;

    @Autowired
    MensajeServicio mensajeServicio;
    
    @Autowired
    MensajeRepositorio mensajeRepositorio;

    @Autowired
    HiloServicio hiloServicio;

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @GetMapping("/menuAdmin")
    public String menuAdmin(ModelMap modelo, HttpSession session) throws ErrorServicio {

        List<Sala> salas = salaServicio.listarSalas();
        modelo.put("salas", salas);

        List<Mensaje> listamensajes = mensajeServicio.listaNoResueltos();
        modelo.put("listamensajes", listamensajes);

        List<Profesional> listaprofesionales = profesionalRepositorio.findAll();
        modelo.put("listaprofesionales", listaprofesionales);

        return "menuadministrador.html";

    }

    @PostMapping("/crearhilo")
    private String crearhilo(ModelMap modelo,
            @RequestParam String idsala,
            @RequestParam String idusuario,
            @RequestParam String nuevohilo,
            @RequestParam String nuevadescripcion,
            HttpSession session) throws ErrorServicio {

        try {
            Hilo hilo = hiloServicio.crearHilo(idsala, nuevohilo, nuevadescripcion, idusuario);

            List<Sala> salas = salaServicio.listarSalas();
            modelo.put("salas", salas);
            List<Mensaje> listamensajes = mensajeServicio.listaNoResueltos(); 
            modelo.put("listamensajes", listamensajes);
            List<Profesional> listaprofesionales = profesionalRepositorio.findAll();
            modelo.put("listaprofesionales", listaprofesionales);
            return "menuadministrador.html";

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "menuadministrador.html";
    }


@PostMapping("/solucionarmensaje")
    private String solucionarmensaje(ModelMap modelo,
            @RequestParam String id, 
            HttpSession session) throws ErrorServicio {

        try {
            mensajeServicio.solucionarMensaje(id); 
             
            List<Sala> salas = salaServicio.listarSalas();
            modelo.put("salas", salas);
            List<Mensaje> listamensajes = mensajeServicio.listaNoResueltos(); 
            modelo.put("listamensajes", listamensajes);
            List<Profesional> listaprofesionales = profesionalRepositorio.findAll();
            modelo.put("listaprofesionales", listaprofesionales);
            return "menuadministrador.html";

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
    return "menuadministrador.html";
    }
    
}

