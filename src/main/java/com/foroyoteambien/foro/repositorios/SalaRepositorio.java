package com.foroyoteambien.foro.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foroyoteambien.foro.entidades.Sala;
/*Natalie Gajardo*/

@Repository

public interface SalaRepositorio extends JpaRepository<Sala, String>{

}
