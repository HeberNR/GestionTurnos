package org.progI.entities;

import org.progI.enums.Roles;

public class Paciente extends Usuario {
  private int idPaciente;
  private String obraSocial;

  public Paciente(int idUsuario, int dni, String nombre, String apellido, String telefono, String email, String pass, Roles rol, int idPaciente, String obraSocial) {
    super(idUsuario, dni, nombre, apellido, telefono, email, pass, rol);
    this.idPaciente = idPaciente;
    this.obraSocial = obraSocial;
  }

  public Paciente(int idUsuario, String nombre, String apellido, int idPaciente, String obraSocial) {
    super(idUsuario, 0, nombre, apellido, null, null, null, null);
    this.idPaciente = idPaciente;
    this.obraSocial = obraSocial;
  }

  public Paciente() {
    super();
  }

  // Getters y Setters
  public int getIdPaciente() {
    return idPaciente;
  }

  public void setIdPaciente(int idPaciente) {
    this.idPaciente = idPaciente;
  }

  public String getObraSocial() {
    return obraSocial;
  }

  public void setObraSocial(String obraSocial) {
    this.obraSocial = obraSocial;
  }
}
