package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ComentarioRepositorio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;
import com.foroyoteambien.foro.servicios.ComentarioServicio;
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
public class ComentarioController {

    @Autowired
    HiloRepositorio hiloRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    ComentarioServicio comentarioServicio;

    @Autowired
    ComentarioRepositorio comentarioRepositorio;

//  lista los comentarios al elegir el hilo
    @GetMapping("/listarcomentarios/{idhilo}")
    public String listarcomentarios(@PathVariable String idhilo,
            ModelMap modelo,
            HttpSession session) {
        Hilo hilo = hiloRepositorio.getOne(idhilo);
        modelo.put("titulohilo", hilo.getTitulo());
        modelo.put("descripcionhilo", hilo.getDescripcion());
        modelo.put("idhilo", hilo.getId());
        modelo.put("mostrar", "notnull");

        List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
        if (listacomentarios.isEmpty()) {
            modelo.put("tablavacia", "notnull");
        } else {
            modelo.put("tablallena", "notnull");
            modelo.put("listacomentarios", listacomentarios);
        }

        return "hilo.html";
    }

//  guarda comentario nuevo
    @PostMapping("/guardarcomentario")
    public String guardarcomentario(ModelMap modelo,
            HttpSession session,
            @RequestParam String idhilo,
            @RequestParam String iduser,
            @RequestParam String comentario) {

        Hilo hilo = hiloRepositorio.getOne(idhilo);
        try {
            comentarioServicio.crearComentario(comentario, iduser, idhilo);
            modelo.put("tablallena", "notnull");
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("comentario", comentario);
        }
        List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
        if (listacomentarios.isEmpty()) {
            modelo.put("tablavacia", "notnull");
        } else {
            modelo.put("tablallena", "notnull");
            modelo.put("listacomentarios", listacomentarios);
        }
        modelo.put("titulohilo", hilo.getTitulo());
        modelo.put("descripcionhilo", hilo.getDescripcion());
        modelo.put("idhilo", hilo.getId());
        modelo.put("mostrar", "notnull");
        return "hilo.html";
    }

    @GetMapping("/desactivarcomentario")
    public String desactivarcomentario(ModelMap modelo,
            HttpSession session,
            @RequestParam String idhilo,
            @RequestParam String iduser,
            @RequestParam String idcomentario) {

        try {
            comentarioServicio.desactivarComentario(iduser, idcomentario);

        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
        }
        Hilo hilo = hiloRepositorio.getOne(idhilo);
        List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
        if (listacomentarios.isEmpty()) {
            modelo.put("tablavacia", "notnull");
        } else {
            modelo.put("tablallena", "notnull");
            modelo.put("listacomentarios", listacomentarios);
        }
        modelo.put("titulohilo", hilo.getTitulo());
        modelo.put("descripcionhilo", hilo.getDescripcion());
        modelo.put("idhilo", hilo.getId());
        modelo.put("mostrar", "notnull");
        return "hilo.html";
        }

  
    @PostMapping("/editarcomentario")
    public String editarcomentario(ModelMap modelo,
            HttpSession session,
            @RequestParam String iduser,
            @RequestParam String idcomentario,
            @RequestParam String idhilo,
            @RequestParam String descripcion) {

        try {
            comentarioServicio.modificarComentario(descripcion, iduser, idcomentario);

            modelo.put("exitom", "Se actualizó correctamente tu comentario");
            

        } catch (ErrorServicio ex) {
            modelo.put("editar", "notnull");
            modelo.put("errorm", ex.getMessage());
            modelo.put("iduser", iduser);
            modelo.put("idcomentario", idcomentario);
            modelo.put("descripcion", descripcion);
            
        }
        Hilo hilo = hiloRepositorio.getOne(idhilo);
        List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
        if (listacomentarios.isEmpty()) {
            modelo.put("tablavacia", "notnull");
        } else {
            modelo.put("tablallena", "notnull");
            modelo.put("listacomentarios", listacomentarios);
        }
        modelo.put("titulohilo", hilo.getTitulo());
        modelo.put("descripcionhilo", hilo.getDescripcion());
        modelo.put("idhilo", hilo.getId());
        modelo.put("mostrar", "notnull");
        return "hilo.html";
    }

    @GetMapping("/editarcomentario")
    public String editarcomentarioget(ModelMap modelo,
            HttpSession session,
            String idhilo,
            String idcomentario) {

        Comentario comentario = comentarioRepositorio.getOne(idcomentario);
        modelo.put("idhilo", idhilo);
        modelo.put("descripcion", comentario.getDescripcion());
        modelo.put("idcomentario", comentario.getId());
        modelo.put("editar", "notnull");
        
        Hilo hilo = hiloRepositorio.getOne(idhilo);
        List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
        modelo.put("tablallena", "notnull");
        modelo.put("listacomentarios", listacomentarios);
        modelo.put("titulohilo", hilo.getTitulo());
        modelo.put("descripcionhilo", hilo.getDescripcion());
        modelo.put("idhilo", hilo.getId());
        modelo.put("mostrar", "notnull");
            
        return "hilo.html";

    }

}
