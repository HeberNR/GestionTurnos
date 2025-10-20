package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Turno;
import org.progI.enums.EstadoTurno;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;
import org.progI.interfaces.DAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO implements AdmConection, DAO<Turno, Integer> {

  private static final String SQL_INSERT = "INSERT INTO turnos (medicos_idMedico, pacientes_idPaciente, " +
      "fecha, hora, motivo, estado) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE turnos SET medicos_idMedico = ?, pacientes_idPaciente = ?, fecha = ?, " +
      "hora = ?, motivo = ?, estado = ? WHERE idTurno = ?";
  private static final String SQL_DELETE = "DELETE FROM turnos WHERE idTurno = ?";

  // Consulta JOIN compleja para traer todos los datos necesarios de una vez
  private static final String SQL_SELECT_BASE = "SELECT " +
      "t.idTurno, t.fecha, t.hora, t.motivo, t.estado, " +
      "u_med.idUsuario AS med_idUsuario, u_med.dni AS med_dni, u_med.nombre AS med_nombre, u_med.apellido " +
      "AS med_apellido, u_med.telefono AS med_telefono, u_med.email AS med_email, u_med.pass AS med_pass, u_med.rol AS med_rol, " +
      "m.idMedico, m.matricula, m.especialidad, " +
      "u_pac.idUsuario AS pac_idUsuario, u_pac.dni AS pac_dni, u_pac.nombre AS pac_nombre, u_pac.apellido AS pac_apellido, " +
      "u_pac.telefono AS pac_telefono, u_pac.email AS pac_email, u_pac.pass AS pac_pass, u_pac.rol AS pac_rol, " +
      "p.idPaciente, p.obraSocial " +
      "FROM turnos t " +
      "JOIN medicos m ON t.medicos_idMedico = m.idMedico " +
      "JOIN usuarios u_med ON m.usuarios_idUsuario = u_med.idUsuario " +
      "JOIN pacientes p ON t.pacientes_idPaciente = p.idPaciente " +
      "JOIN usuarios u_pac ON p.usuarios_idUsuario = u_pac.idUsuario";

  private static final String SQL_GETBYID = SQL_SELECT_BASE + " WHERE t.idTurno = ?";
  private static final String SQL_GETALL = SQL_SELECT_BASE + " ORDER BY t.fecha, t.hora";

  private static final String SQL_GETBYESTADO = SQL_SELECT_BASE + " WHERE t.estado = ? ORDER BY t.fecha, t.hora";


  @Override
  public List<Turno> getAll() {
    List<Turno> turnos = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETALL);
         ResultSet rs = pst.executeQuery()) {
      while (rs.next()) {
        turnos.add(construirTurno(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return turnos;
  }

  @Override
  public Turno getById(Integer id) {
    Turno turno = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYID)) {
      pst.setInt(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
          turno = construirTurno(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return turno;
  }

  @Override
  public void insert(Turno turno) {
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

      pst.setInt(1, turno.getMedico().getIdMedico());
      pst.setInt(2, turno.getPaciente().getIdPaciente());
      pst.setDate(3, Date.valueOf(turno.getFecha()));
      pst.setTime(4, Time.valueOf(turno.getHora()));
      pst.setString(5, turno.getMotivo());
      pst.setString(6, turno.getEstado().name());

      pst.executeUpdate();
      try (ResultSet rs = pst.getGeneratedKeys()) {
        if (rs.next()) {
          turno.setIdTurno(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Turno turno) {
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_UPDATE)) {

      pst.setInt(1, turno.getMedico().getIdMedico());
      pst.setInt(2, turno.getPaciente().getIdPaciente());
      pst.setDate(3, Date.valueOf(turno.getFecha()));
      pst.setTime(4, Time.valueOf(turno.getHora()));
      pst.setString(5, turno.getMotivo());
      pst.setString(6, turno.getEstado().name());
      pst.setInt(7, turno.getIdTurno());

      pst.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(Integer id) {
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_DELETE)) {
      pst.setInt(1, id);
      pst.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean existsById(Integer id) {
    return getById(id) != null;
  }

  /**
   * Método de ayuda para construir el objeto Turno completo desde un ResultSet.
   * Reconstruye los objetos Medico y Paciente anidados.
   */
  private Turno construirTurno(ResultSet rs) throws SQLException {
    // 1. Construir el objeto Medico
    Medico medico = new Medico(
        rs.getInt("med_idUsuario"), rs.getInt("med_dni"), rs.getString("med_nombre"),
        rs.getString("med_apellido"), rs.getString("med_telefono"), rs.getString("med_email"),
        rs.getString("med_pass"), Roles.valueOf(rs.getString("med_rol")), rs.getInt("idMedico"),
        rs.getString("matricula"), rs.getString("especialidad")
    );

    // 2. Construir el objeto Paciente
    Paciente paciente = new Paciente(
        rs.getInt("pac_idUsuario"), rs.getInt("pac_dni"), rs.getString("pac_nombre"),
        rs.getString("pac_apellido"), rs.getString("pac_telefono"), rs.getString("pac_email"),
        rs.getString("pac_pass"), Roles.valueOf(rs.getString("pac_rol")), rs.getInt("idPaciente"),
        rs.getString("obraSocial")
    );

    // 3. Construir el objeto Turno final
    return new Turno(
        rs.getInt("idTurno"),
        medico,
        paciente,
        rs.getDate("fecha").toLocalDate(),
        rs.getTime("hora").toLocalTime(),
        rs.getString("motivo"),
        EstadoTurno.valueOf(rs.getString("estado"))
    );
  }

  /**
   * Busca y devuelve todos los turnos que coincidan con un estado específico.
   *
   * @param estado El estado por el cual filtrar (PENDIENTE, CONFIRMADO, etc.).
   * @return Una lista de objetos Turno.
   */
  public List<Turno> getByEstado(String estado) {
    List<Turno> turnos = new ArrayList<>();
    // Usamos la nueva consulta SQL que creamos.
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYESTADO)) {

      pst.setString(1, estado); // Le pasamos el estado al '?' de la consulta.
      try (ResultSet rs = pst.executeQuery()) {
        while (rs.next()) {
          turnos.add(construirTurno(rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return turnos;
  }
}