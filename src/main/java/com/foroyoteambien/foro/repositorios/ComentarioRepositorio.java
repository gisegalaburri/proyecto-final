package com.foroyoteambien.foro.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.foroyoteambien.foro.entidades.Comentario;
import com.foroyoteambien.foro.entidades.Hilo;
import org.springframework.data.repository.query.Param;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, String> {

    @Query("SELECT c FROM Comentario c WHERE c.activo IS TRUE")
    public List<Comentario> buscarActivos();

    @Query("SELECT c FROM Hilo h, IN(h.listaComentarios) c WHERE h.id= :id AND c.activo IS TRUE")
    public List<Comentario> comentariosActivosPorHilo(@Param("id") String id);
    
    
    
}

