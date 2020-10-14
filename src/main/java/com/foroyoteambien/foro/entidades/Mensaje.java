package com.foroyoteambien.foro.entidades;

import com.foroyoteambien.foro.enumeraciones.Asunto;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Enumerated(EnumType.STRING)
    private Asunto asunto;

    private String descripcion;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    
    private Boolean solucionado;
    
    @ManyToOne
    private Usuario remitente;

    public Mensaje() {

    }

    public Mensaje(String id, Asunto asunto, String descripcion, Date fechaAlta, Boolean solucionado, Usuario remitente) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fechaAlta = fechaAlta;
        this.solucionado = solucionado;
        this.remitente = remitente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Asunto getAsunto() {
        return asunto;
    }

    public void setAsunto(Asunto asunto) {
        this.asunto = asunto;
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

    public Boolean isSolucionado() {
        return solucionado;
    }

    public void setSolucionado(Boolean solucionado) {
        this.solucionado = solucionado;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "id=" + id + ", asunto=" + asunto + ", descripcion=" + descripcion + ", fechaAlta=" + fechaAlta + ", solucionado=" + solucionado + ", remitente=" + remitente + '}';
    }

   
}
