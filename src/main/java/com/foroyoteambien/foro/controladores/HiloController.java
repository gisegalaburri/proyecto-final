package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Mensaje;
import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.repositorios.SalaRepositorio;
import com.foroyoteambien.foro.servicios.ComentarioServicio;
import com.foroyoteambien.foro.servicios.HiloServicio;
import com.foroyoteambien.foro.servicios.MensajeServicio;
import com.foroyoteambien.foro.servicios.SalaServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HiloController {

    @Autowired
    HiloServicio hiloServicio;

    @Autowired
    SalaServicio salaServicio;

    @Autowired
    ComentarioServicio comentarioServicio;

    @Autowired
    SalaRepositorio salaRepositorio;

    @Autowired
    HiloRepositorio hiloRepositorio;
    
    @Autowired
    MensajeServicio mensajeServicio;
    
    @Autowired
    ProfesionalRepositorio profesionalRepositorio;
     
// carga el formulario para crear hilo
    @GetMapping("/crearhilo")
    public String crear (ModelMap modelo, HttpSession session) throws ErrorServicio{
        modelo.put("crearhilo", "notnull");
        return "menuadministrador.html";
    }
    
    
//  guarda datos de nuevo hilo
    @PostMapping("/crearhiloAdmin")
    public String crearhiloAdmin (ModelMap modelo,
            @RequestParam String idsala,
            @RequestParam String idusuario,
            @RequestParam String nuevohilo,
            @RequestParam String nuevadescripcion,
            HttpSession session) throws ErrorServicio {
        try {
            Hilo hilo = hiloServicio.crearHilo(idsala, nuevohilo, nuevadescripcion, idusuario);
            List<Sala> salas = salaServicio.listarSalas();
            modelo.put("salas", salas);
            return "menuadministrador.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        return "menuadministrador.html";
    }
    
    
//  pasa de loginsucces a hilo.Html
    @GetMapping("/listarhilos/{id}")
    public String listarhilos(@PathVariable String id, ModelMap modelo, HttpSession session) throws ErrorServicio {
        List<Hilo> listaHilos = hiloServicio.listarHiloXSala(id);
        Sala sala = salaRepositorio.getOne(id);
        modelo.put("sala", sala);
        modelo.put("listaHilos", listaHilos);
        modelo.put("mostrar", "null");
        return "hilo.html";
    }

      
// crea hilo el usuario-comun en la pagina hilo.html
    @PostMapping("/crearhiloUser")
    public String crearhiloUser (ModelMap modelo,
            @RequestParam String idsala,
            @RequestParam String idusuario,
            @RequestParam String nuevohilo,
            @RequestParam String nuevadescripcion,
            HttpSession session) throws ErrorServicio {
        try {
            Hilo hilo = hiloServicio.crearHilo(idsala, nuevohilo, nuevadescripcion, idusuario);
            modelo.put("hilo", hilo);
            modelo.put("mostrar", "notnull");
            String idhilo=hilo.getId(); 
            List<Comentario> listaComentarios = comentarioServicio.listarActivos(idhilo);
            return "hilo.html";
       } 
        catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
    return "hilo.html"; 
    }
    
}

    
    


