/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.foroyoteambien.foro.repositorios;

import com.foroyoteambien.foro.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Gisele Galaburri <gisele.galaburri89 at gmail.com>
 */
@Repository
public interface FotoRepositorio extends JpaRepository<Foto, String>{

}
