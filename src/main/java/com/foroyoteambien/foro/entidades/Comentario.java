package com.foroyoteambien.foro.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    
    private Boolean activo;
    
    @ManyToOne
    private Usuario usuario;

    public Comentario() {
    }

    public Comentario(String id, String descripcion, Date fechaAlta, Date fechaModificacion, Boolean activo, Usuario usuario) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.fechaModificacion = fechaModificacion;
        this.activo = activo;
        this.usuario = usuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.descripcion = nombre;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Comentario [id=" + id + ", nombre=" + descripcion + ", fechaAlta=" + fechaAlta + ", fechaModificacion="
                + fechaModificacion + ", usuario=" + usuario + "]";
    }

}
