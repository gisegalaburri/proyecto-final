package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Profesion;
import com.foroyoteambien.foro.errores.ErrorServicio;
import com.foroyoteambien.foro.repositorios.ProfesionalRepositorio;
import com.foroyoteambien.foro.servicios.ProfesionalServicio;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
@PreAuthorize("hasRole('ROLE_MODERADOR') || hasRole('ROLE_USUARIO')")
@RequestMapping("/")
public class ProfesionalController {

    @Autowired
    ProfesionalServicio profesionalServicio;

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @GetMapping("/profesionales")
    public String profesional(ModelMap modelMap, HttpSession session) {

        List<Profesional> profesionales = profesionalServicio.listarActivos();
        modelMap.put("profesionales", profesionales);
        modelMap.put("mostrar", "notNull");

        return "profesional.html";
    }

    @GetMapping("/buscar-profesional")
    public String buscarProfesional(Pais pais,
            Profesion profesion,
            ModelMap modelMap,
            HttpSession session) {

        List<Profesional> profesionales = profesionalServicio.listarPorPais(pais, profesion);
        modelMap.put("profesionales", profesionales);
        modelMap.put("mostrar", "notNull");

        return "profesional.html";
    }

    @GetMapping("/registro-profesional")
    public String registrarProfesional() {
        return "registroprofesional.html";
    }

    @PostMapping("/registro-profesional")
    public String altaProfesional(@RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam Pais pais,
            @RequestParam Profesion profesion,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer telefono,
            MultipartFile archivo,
            ModelMap modelMap) {
        try {

            profesionalServicio.altaProfesional(nombre, apellido, email, pais, profesion, descripcion, telefono, archivo);

        } catch (ErrorServicio ex) {
            modelMap.put("nombre", nombre);
            modelMap.put("apellido", apellido);
            modelMap.put("email", email);
            modelMap.put("pais", pais);
            modelMap.put("profesion", profesion);
            modelMap.put("descripcion", descripcion);
            modelMap.put("telefono", telefono);

            modelMap.put("error", ex.getMessage());

            return "registroprofesional.html";
        }
        modelMap.put("exito", "Gracias por registrarte en nuestra base de datos de "
                + "Profesionales.");
        return "registroprofesional.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/profesional/modificar/{id}")
    public String modificarProfesional(@PathVariable String id,
            ModelMap modelMap,
            HttpSession session) {

        modelMap.put("modificarProfesional", "notNull");
        Profesional profesional = profesionalRepositorio.getOne(id);
        modelMap.put("profesional", profesional);

        return "profesional.html";

    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @PostMapping("/profesional/modificar")
    public String actualizrProfesional(@RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam Pais pais,
            @RequestParam Profesion profesion,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer telefono,
            MultipartFile archivo,
            ModelMap modelMap,
            HttpSession session) {
        try {
            profesionalServicio.modificarProfesional(id, nombre, apellido, email, pais, profesion, descripcion, telefono, archivo);
            List<Profesional> profesionales = profesionalServicio.listarActivos();
            modelMap.put("profesionales", profesionales);
            modelMap.put("mostrar", "notNull");
            modelMap.put("exito", "Se actualizaron correctamente los datos del profesional.");
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex.getMessage());
            Profesional profesional = profesionalRepositorio.getOne(id);
            modelMap.put("profesional", profesional);
            modelMap.put("modificarProfesional", "notNull");

        }

        return "profesional.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @GetMapping("/profesional/eliminar/{id}")
    public String eliminarProfesional(@PathVariable String id,
            ModelMap modelMap,
            HttpSession session) {
        try {

            profesionalServicio.dehabilitar(id);
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex.getMessage());

        }

        List<Profesional> profesionales = profesionalServicio.listarActivos();
        modelMap.put("profesionales", profesionales);
        modelMap.put("exito", "Se ha borrado exitosamente el profesional seleccionado.");
        modelMap.put("mostrar", "notNull");
        return "profesional.html";
    }


    @GetMapping("/habilitar-profesional")
    public String buscarProfesionalNoActivo(ModelMap modelo,
            HttpSession session) {
        
        List<Profesional> profesionales= profesionalServicio.listarNoActivos();
        modelo.put("profesionales", profesionales); 
        modelo.put("mostrarprofe", "notnull");
        return "menuadministrador.html"; 
    }
    
    
    @GetMapping("/habilitar-profesional/{id}")
    public String habilitarPro(@PathVariable String id, ModelMap modelo,
            HttpSession session){
        try {
          profesionalServicio.volverAHabilitar(id);
          modelo.put("exito", "Se activo correctamente el Profesional seleccionado"); 
        } catch (ErrorServicio e ) {
          modelo.put("error", e.getMessage());

        }
        List<Profesional> profesionales= profesionalServicio.listarNoActivos();
        modelo.put("profesionales", profesionales); 
        modelo.put("mostrarprofe", "notnull");
                return "menuadministrador.html";
    }
}
