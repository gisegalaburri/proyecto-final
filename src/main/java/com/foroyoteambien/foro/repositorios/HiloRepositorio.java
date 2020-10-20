package com.foroyoteambien.foro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foroyoteambien.foro.entidades.Hilo;
import com.foroyoteambien.foro.entidades.Mensaje;

@Repository 
public interface HiloRepositorio extends JpaRepository<Hilo, String>{

	@Query("SELECT h FROM Hilo h WHERE h.activo IS TRUE")
	public List<Hilo> buscarActivos(); 
}
