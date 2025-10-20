package org.progI.entities;

import org.progI.enums.EstadoTurno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Turno {
  private int idTurno;
  private Medico medico;
  private Paciente paciente;
  private LocalDate fecha;
  private LocalTime hora;
  private String motivo;
  private EstadoTurno estado;

  // Constructor completo (para traer datos de la BD)
  public Turno(int idTurno, Medico medico, Paciente paciente, LocalDate fecha, LocalTime hora, String motivo, EstadoTurno estado) {
    this.idTurno = idTurno;
    this.medico = medico;
    this.paciente = paciente;
    this.fecha = fecha;
    this.hora = hora;
    this.motivo = motivo;
    this.estado = estado;
  }

  // Constructor para crear un nuevo turno (aún sin ID de la BD)
  public Turno(Medico medico, Paciente paciente, LocalDate fecha, LocalTime hora, String motivo, EstadoTurno estado) {
    this(-1, medico, paciente, fecha, hora, motivo, estado);
  }

  // Getters y Setters
  public int getIdTurno() { return idTurno; }
  public void setIdTurno(int idTurno) { this.idTurno = idTurno; }
  public Medico getMedico() { return medico; }
  public void setMedico(Medico medico) { this.medico = medico; }
  public Paciente getPaciente() { return paciente; }
  public void setPaciente(Paciente paciente) { this.paciente = paciente; }
  public LocalDate getFecha() { return fecha; }
  public void setFecha(LocalDate fecha) { this.fecha = fecha; }
  public LocalTime getHora() { return hora; }
  public void setHora(LocalTime hora) { this.hora = hora; }
  public String getMotivo() { return motivo; }
  public void setMotivo(String motivo) { this.motivo = motivo; }
  public EstadoTurno getEstado() { return estado; }
  public void setEstado(EstadoTurno estado) { this.estado = estado; }

  @Override
  public String toString() {
    return "Turno{" +
        "idTurno=" + idTurno +
        ", fecha=" + fecha +
        ", hora=" + hora +
        ", estado=" + estado +
        ", medico=" + medico.getApellido() +
        ", paciente=" + paciente.getApellido() +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Turno turno = (Turno) o;
    return idTurno == turno.idTurno;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTurno);
  }
}
