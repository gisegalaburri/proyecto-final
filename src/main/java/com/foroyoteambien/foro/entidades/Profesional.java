package com.foroyoteambien.foro.entidades;

import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Profesion;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "profesionales")
public class Profesional {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String apellido;
    private String mail;

    @Enumerated(EnumType.STRING)
    private Pais pais;

    @Enumerated(EnumType.STRING)
    private Profesion profesion;

    private String descripcion;
    private Integer telefono;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    
    private boolean activo;

    public Profesional() {
    }

    public Profesional(String id, String nombre, String apellido, String mail, Pais pais, Profesion profesion, String descripcion, Integer telefono, Date fechaAlta, Date fechaModificacion, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.pais = pais;
        this.profesion = profesion;
        this.descripcion = descripcion;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
        this.fechaModificacion = fechaModificacion;
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Profesion getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesion profesion) {
        this.profesion = profesion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Profesional{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", mail=" + mail + ", pais=" + pais + ", profesion=" + profesion + ", descripcion=" + descripcion + ", telefono=" + telefono + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", activo=" + activo + '}';
    }

}
