package org.progI.entities;

import org.progI.enums.Roles;

import java.util.Objects;

public abstract class Usuario {
  protected int idUsuario;
  protected int dni;
  protected String nombre;
  protected String apellido;
  protected String telefono;
  protected String email;
  protected String pass;
  protected Roles rol;

  public Usuario() {
  }

  public Usuario(int idUsuario, int dni, String nombre, String apellido, String telefono, String email, String pass, Roles rol) {
    this.idUsuario = idUsuario;
    this.dni = dni;
    this.nombre = nombre;
    this.apellido = apellido;
    this.telefono = telefono;
    this.email = email;
    this.pass = pass;
    this.rol = rol;
  }

  public Usuario(int dni, String nombre, String apellido, String telefono,
                 String email, String pass, Roles rol) {
    this(-1, dni, nombre, apellido, telefono, email, pass, rol);
  }

  // Getters y Setters
  public int getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
  }

  public int getDni() {
    return dni;
  }

  public void setDni(int dni) {
    this.dni = dni;
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

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public Roles getRol() {
    return rol;
  }

  public void setRol(Roles rol) {
    this.rol = rol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return idUsuario == usuario.idUsuario && dni == usuario.dni;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario, dni);
  }
}
