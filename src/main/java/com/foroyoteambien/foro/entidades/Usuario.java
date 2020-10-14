package com.foroyoteambien.foro.entidades;

import com.foroyoteambien.foro.enumeraciones.Diagnostico;
import com.foroyoteambien.foro.enumeraciones.Pais;
import com.foroyoteambien.foro.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String apellido;
    private String nickname;
    private String mail;
    private String clave1;
    private String clave2;
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    private Pais pais;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Enumerated(EnumType.STRING)
    private Diagnostico diagnostico;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Temporal(TemporalType.DATE)
    private Date fechaAlta;

    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    
    private boolean activo;
    
    @OneToOne
    private Foto foto;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String apellido, String nickname, String mail, Pais pais, Date fechaNacimiento, String descripcion, String clave1, String clave2, Diagnostico diagnostico, Rol rol, Date fechaAlta, Date fechaModificacion, boolean activo, Foto foto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nickname = nickname;
        this.mail = mail;
        this.pais = pais;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
        this.clave1 = clave1;
        this.clave2 = clave2;
        this.diagnostico = diagnostico;
        this.rol = rol;
        this.fechaAlta = fechaAlta;
        this.fechaModificacion = fechaModificacion;
        this.activo = activo;
        this.foto = foto;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClave1() {
        return clave1;
    }

    public void setClave1(String clave1) {
        this.clave1 = clave1;
    }

    public String getClave2() {
        return clave2;
    }

    public void setClave2(String clave2) {
        this.clave2 = clave2;
    }

    public Diagnostico getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(Diagnostico diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
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

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", nickname=" + nickname + ", mail=" + mail + ", pais=" + pais + ", fechaNacimiento=" + fechaNacimiento + ", descripcion=" + descripcion + ", clave1=" + clave1 + ", clave2=" + clave2 + ", diagnostico=" + diagnostico + ", rol=" + rol + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", activo=" + activo + ", foto=" + foto + '}';
    }

   
}
