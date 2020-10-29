/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.foroyoteambien.foro.repositorios;

import com.foroyoteambien.foro.entidades.Profesional;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Profesion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, String>{

    @Query("SELECT p FROM Profesional p WHERE p.email LIKE :email")
    public Profesional buscarPorMail(@Param("email") String email);
    
    @Query("SELECT p FROM Profesional p WHERE p.activo IS TRUE")
    public List<Profesional> listarActivos();
    
    @Query("SELECT p FROM Profesional p WHERE p.pais LIKE :pais AND p.profesion LIKE :profesion")
    public List<Profesional> listarPorPais(@Param("pais") Pais pais, @Param("profesion") Profesion profesion);
    
    @Query("SELECT p FROM Profesional p WHERE p.activo IS FALSE")
    public List<Profesional> listarNoActivos();

}