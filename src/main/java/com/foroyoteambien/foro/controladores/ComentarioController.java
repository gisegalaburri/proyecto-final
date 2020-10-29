package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Usuario;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ComentarioRepositorio;
import com.foroyoteambien.foro.repositorios.HiloRepositorio;
import com.foroyoteambien.foro.repositorios.UsuarioRepositorio;
import com.foroyoteambien.foro.servicios.ComentarioServicio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
        modelo.put("hilo", hilo);
        modelo.put("mostrar", "notnull");
       
        try {
            List<Comentario> listacomentarios = comentarioServicio.listarActivos(idhilo);
            if(listacomentarios.isEmpty()){
                modelo.put("tablavacia", "notnull");
            }else {
              modelo.put("tablallena", "notnull");  
              modelo.put("listacomentarios", listacomentarios);   
            }
        } catch (ErrorServicio ex) {
           modelo.put("error", ex.getMessage());
        }
        
        return "hilo.html";
    }

    
//  guarda comentario nuevo
    @PostMapping("/guardarcomentario")
    public String guardarcomentario(ModelMap modelo,
            HttpSession session,
            String idsala,
            String idhilo,
            String iduser,
            String comentario) {

        Hilo hilo = hiloRepositorio.getOne(idhilo);
        Comentario comentarioUno = new Comentario();
        comentarioUno.setDescripcion(comentario);
        comentarioUno.setFechaAlta(new Date());
        comentarioUno.setActivo(true);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(iduser);
        if (respuesta.isPresent()) {
            Usuario usuarioUno = respuesta.get();
            comentarioUno.setUsuario(usuarioUno);
        }
        comentarioRepositorio.save(comentarioUno);
        List<Comentario> listaComentario = hilo.getListaComentarios();
        listaComentario.add(comentarioUno);
        hilo.setListaComentarios(listaComentario);
        hiloRepositorio.save(hilo);

        modelo.put("hilo", hilo);
        modelo.put("mostrar", "notnull");
        modelo.put("listacomentarios", "listaComentario");
        return "redirect:/listarcmentarios/{"+idhilo+"}";
    }

    
    @PostMapping("/desactivarcomentario")
    public String desactivarcomentario(ModelMap modelo,
            HttpSession session,
            String idhilo,
            String iduser,
            String idcomentario) {

        try {
            comentarioServicio.desactivarComentario(iduser, idcomentario);
            
            Hilo hilo = hiloRepositorio.getOne(idhilo);
            List<Comentario> listaComentario = hilo.getListaComentarios();
            modelo.put("hilo", hilo);
            modelo.put("mostrar", "notnull");
            modelo.put("listacomentarios", listaComentario);
            
        } catch (ErrorServicio ex) {
           modelo.put("error", ex.getMessage());
        }
        return "hilo.html";
    }
    
   
    
//  FALTA terminar editar y guardar comentario modificado
//  hilo.HTML ver desde linea 198
    @PostMapping("/editarcomentario")
    public String editarcomentario(ModelMap modelo,
            HttpSession session,
            String iduser,
            String idcomentario,
            String idhilo,
            String descripcion) {
        
        try {
            comentarioServicio.modificarComentario(descripcion, iduser, idcomentario);
            
            modelo.put("mostrar", "notnull");

        } catch (ErrorServicio ex) {
            modelo.put("editar", "notnull");
            modelo.put("error", ex.getMessage());
            modelo.put("iduser", iduser);
            modelo.put("idcomentario", idcomentario);
            modelo.put("idhilo", idhilo);
            modelo.put("descripcion", descripcion);
            return "hilo.html";
        }
      
        return "redirect:/listarcomentario/{"+idhilo+"}";
    }
    
    @GetMapping("/editarcomentario")
    public String editarcomentarioget(ModelMap modelo,
            HttpSession session,
            String idhilo,
            String idcomentario) {
      
        Comentario comentario= comentarioRepositorio.getOne(idcomentario);
        modelo.put("idhilo", idhilo);
        modelo.put("descripcion", comentario.getDescripcion());
        modelo.put("idcomentario", comentario.getId());
        
        modelo.put("editar", "notnull");
        return "hilo.html";

    }
    
}
