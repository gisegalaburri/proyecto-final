package com.foroyoteambien.foro.entidades;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "salas")
public class Sala {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String titulo;
    private String descripcion;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    
    private boolean activo;
    
    @OneToMany
    private List<Hilo> listaHilos;

    public Sala() {
    }

    public Sala(String id, String titulo, String descripcion, Date fechaAlta, Date fechaModificacion, boolean activa) {
        super();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.fechaModificacion = fechaModificacion;
        this.activo = activa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean isActiva() {
        return activo;
    }

    public void setActiva(boolean activa) {
        this.activo = activa;
    }

    public List<Hilo> getListaHilos() {
        return listaHilos;
    }

    public void setListaHilos(List<Hilo> listaHilos) {
        this.listaHilos = listaHilos;
    }

    @Override
    public String toString() {
        return "Sala [id=" + id + ", titulo=" + titulo + ", descripcion=" + descripcion + ", fechaAlta=" + fechaAlta
                + ", fechaModificacion=" + fechaModificacion + ", activa=" + activo + "]";
    }

}
