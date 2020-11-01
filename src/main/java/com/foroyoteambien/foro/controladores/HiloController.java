package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Sala;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.repositorios.SalaRepositorio;
import com.foroyoteambien.foro.servicios.ComentarioServicio;
import com.foroyoteambien.foro.servicios.HiloServicio;
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
    ProfesionalRepositorio profesionalRepositorio;

// carga el formulario para crear hilo
    @GetMapping("/crearhilo")
    public String crear(ModelMap modelo, HttpSession session) {
        modelo.put("crearhilo", "notnull");
        List<Sala> salas = salaServicio.listarSalas();
        modelo.put("salas", salas);
        return "hilo.html";
    }

//  guarda datos de nuevo hilo
    @PostMapping("/crearhilo")
    public String crearhiloAdmin(ModelMap modelo,
            @RequestParam String idsala,
            @RequestParam String iduser,
            @RequestParam String nuevohilo,
            @RequestParam String nuevadescripcion,
            HttpSession session) {
        try {
            Hilo hilo = hiloServicio.crearHilo(idsala, nuevohilo, nuevadescripcion, iduser);
            modelo.put("exito", "Hilo creado correctamente");
               
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nuevohilo", nuevohilo);
            modelo.put("nuevadescripcion", nuevadescripcion); 

        }
        List<Sala> salas = salaServicio.listarSalas();
        modelo.put("salas", salas);
        modelo.put("crearhilo", "notnull");
        return "hilo.html";
    }

//  pasa de loginsucces a hilo.Html
    @GetMapping("/listarhilos/{id}")
    public String listarhilos(@PathVariable String id,
            ModelMap modelo,
            HttpSession session) {
        List<Hilo> hilos = hiloServicio.listarHiloXSala(id);
        Sala sala = salaRepositorio.getOne(id);
        modelo.put("sala", sala);
        modelo.put("hilos", hilos);
        modelo.put("mostrarSala", "notNull");
        return "hilo.html";
    }

   }
