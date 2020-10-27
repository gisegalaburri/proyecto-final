package com.foroyoteambien.foro.controladores;

import com.foroyoteambien.foro.errores.ErrorServicio;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AdminController {
  
    @GetMapping("/menuAdmin")
    public String menuAdmin(ModelMap modelo, HttpSession session) {
    modelo.put("ingreso", "notnull");
    return "menuadministrador.html";

    }
}

