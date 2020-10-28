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
            
        return "profesional.html";
    }

    @GetMapping("/buscar-profesional")
    public String buscarProfesional(@RequestParam(required = false) Pais pais,
            @RequestParam(required = false) Profesion profesion,
            ModelMap modelMap) {

        List<Profesional> profesionales = profesionalServicio.listarPorPais(pais, profesion);
        modelMap.put("profesionales", profesionales);
        
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
    @PostMapping("/profesional/modificar/{id}")
    public String modificarProfesional(@RequestParam String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam Pais pais,
            @RequestParam Profesion profesion,
            @RequestParam(required = false) String descripcion,
            @RequestParam Integer telefono,
            MultipartFile archivo,
            ModelMap modelMap) {
        try {
            profesionalServicio.modificarProfesional(id, nombre, apellido, email, pais, profesion, descripcion, telefono, archivo);
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex.getMessage());

            return "registroprofesional.html";
        }
        return "index.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @PostMapping("/profesional/eliminar/{id}")
    public String eliminarProfesional(@PathVariable String id,
            ModelMap modelMap) {
        try {
            profesionalServicio.dehabilitar(id);
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex.getMessage());

            return "profesional.html";
        }
        
        List<Profesional> profesionales = profesionalServicio.listarActivos();
        modelMap.put("profesionales", profesionales);
        modelMap.put("exito", "Se ha borrado exitosamente el profesional seleccionado.");
        return "profesional.html";
    }

    @PreAuthorize("hasRole('ROLE_MODERADOR')")
    @PostMapping("/habilitar-profesional")
    public String habilitarPro(@RequestParam String id,
            ModelMap modelMap) {
        try {
            profesionalServicio.volverAHabilitar(id);
        } catch (ErrorServicio ex) {
            modelMap.put("error", ex.getMessage());

            return "";
        }
        return "";
    }
}
