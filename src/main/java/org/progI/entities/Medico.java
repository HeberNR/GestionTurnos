package org.progI.entities;

import org.progI.enums.Roles;

public class Medico extends Usuario {
  private int idMedico;
  private String matricula;
  private String especialidad;

  public Medico(int idUsuario, int dni, String nombre, String apellido, String telefono,
                String email, String pass, Roles rol, int idMedico, String matricula, String especialidad) {
    super(idUsuario, dni, nombre, apellido, telefono, email, pass, rol);
    this.idMedico = idMedico;
    this.matricula = matricula;
    this.especialidad = especialidad;
  }

  // Getters y Setters
  public int getIdMedico() { return idMedico; }
  public void setIdMedico(int idMedico) { this.idMedico = idMedico; }
  public String getMatricula() { return matricula; }
  public void setMatricula(String matricula) { this.matricula = matricula; }
  public String getEspecialidad() { return especialidad; }
  public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
