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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class ProfesionalController {

    @Autowired
    private ProfesionalServicio profesionalservicio;
    @Autowired
    private ProfesionalRepositorio profesionalrepositorio;
    
    @GetMapping("/registro-profesional")
    private String RegistrarPro() {
        return "registroprofesional.html";
    }
    
    @GetMapping("/profesional")
    private String Profesional() {
        return "profesional.html";
    }
    
    @PostMapping("/registro-profesionall")
    public String altaProfesional(@RequestParam String nombre,@RequestParam String apellido,@RequestParam String email
            ,@RequestParam Pais pais,@RequestParam Profesion profesion,@RequestParam (required = false) String descripcion,
            @RequestParam Integer telefono, @RequestParam (required = false) MultipartFile archivo) throws ErrorServicio{
        try {
          profesionalservicio.altaProfesional(nombre,apellido,email,pais,profesion,descripcion,telefono,archivo);
        } catch (Exception e) {
          return "registroprofesional.html";
        }
        return "index.html";
    }
    
    @PostMapping("/modificar-profesionall")
    public String modificarProfesional(@RequestParam String id, @RequestParam String nombre,@RequestParam String apellido,@RequestParam String email
            ,@RequestParam Pais pais,@RequestParam Profesion profesion,@RequestParam (required = false) String descripcion,
            @RequestParam Integer telefono, @RequestParam (required = false) MultipartFile archivo) throws ErrorServicio{
        try {
          profesionalservicio.modificarProfesional(id,nombre,apellido,email,pais,profesion,descripcion,telefono,archivo);
        } catch (Exception e) {
          return "registroprofesional.html";
        }
        return "index.html";
    }
    
    
    @PostMapping("/deshabilitar-profesionall")
    public String eliminarPro(@RequestParam String id){
        try {
          profesionalservicio.dehabilitar(id);
        } catch (Exception e) {
          return "menuadministrador.html";
        }
        return "menuadministrador.html";
    }
    
    
    @GetMapping("/habilitar-profesional")
    public String buscarProfesionalNoActivo(ModelMap modelo,
            HttpSession session) {
        
        List<Profesional> profesionales= profesionalservicio.listarNoActivos();
        modelo.put("profesionales", profesionales); 
        modelo.put("mostrarprofe", "notnull");
        return "menuadministrador.html"; 
    }
    
    
    @GetMapping("/habilitar-profesional/{id}")
    public String habilitarPro(@PathVariable String id, ModelMap modelo,
            HttpSession session){
        try {
          profesionalservicio.volverAHabilitar(id);
          modelo.put("exito", "Se activo correctamente el Profesional seleccionado"); 
        } catch (ErrorServicio e ) {
          modelo.put("error", e.getMessage());
        }
        List<Profesional> profesionales= profesionalservicio.listarNoActivos();
        modelo.put("profesionales", profesionales); 
        modelo.put("mostrarprofe", "notnull");
                return "menuadministrador.html";
    }
}
