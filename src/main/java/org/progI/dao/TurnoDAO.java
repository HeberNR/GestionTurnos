package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Turno;
import org.progI.enums.EstadoTurno;
import org.progI.interfaces.AdmConection;
import org.progI.interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAO implements AdmConection, DAO<Turno, Integer> {

  private static final String SQL_INSERT = "INSERT INTO turnos (medicos_idMedico, pacientes_idPaciente, " +
      "fecha, hora, motivo, estado) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String SQL_UPDATE = "UPDATE turnos SET medicos_idMedico = ?, pacientes_idPaciente = ?, fecha = ?, " +
      "hora = ?, motivo = ?, estado = ? WHERE idTurno = ?";
  private static final String SQL_DELETE = "DELETE FROM turnos WHERE idTurno = ?";

  // Correccion de select
  private static final String SQL_SELECT_BASE = "SELECT " +
      "t.idTurno, t.fecha, t.hora, t.motivo, t.estado, " +
      "u_med.idUsuario AS med_idUsuario, u_med.nombre AS med_nombre, u_med.apellido AS med_apellido, " +
      "m.idMedico, m.matricula, m.especialidad, " +
      "u_pac.idUsuario AS pac_idUsuario, u_pac.nombre AS pac_nombre, u_pac.apellido AS pac_apellido, " +
      "p.idPaciente, p.obraSocial " +
      "FROM turnos t " +
      "INNER JOIN medicos m ON t.medicos_idMedico = m.idMedico " +
      "INNER JOIN pacientes p ON t.pacientes_idPaciente = p.idPaciente " +
      "INNER JOIN usuarios u_med ON m.usuarios_idUsuario = u_med.idUsuario " +
      "INNER JOIN usuarios u_pac ON p.usuarios_idUsuario = u_pac.idUsuario";

  private static final String SQL_GETBYID = SQL_SELECT_BASE + " WHERE t.idTurno = ?";
  private static final String SQL_GETALL = SQL_SELECT_BASE + " ORDER BY t.fecha, t.hora";
  private static final String SQL_GETBYESTADO = SQL_SELECT_BASE + " WHERE t.estado = ? ORDER BY t.fecha, t.hora";

  private static final String SQL_GETPROXIMOS = SQL_SELECT_BASE +
      " WHERE t.fecha = CURDATE() OR t.fecha = CURDATE() + INTERVAL 1 DAY " +
      "ORDER BY t.fecha, t.hora";


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

  public List<Turno> getProximos() {
    List<Turno> turnos = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETPROXIMOS);
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
   * Mét0do de ayuda para construir el objeto Turno completo desde un ResultSet.
   * Reconstruye los objetos Medico y Paciente anidados SÓLO con los datos
   * que traemos en nuestra consulta (SQL_SELECT_BASE).
   */
  private Turno construirTurno(ResultSet rs) throws SQLException {

    // 1. Construir el objeto Medico
    Medico medico = new Medico(
        rs.getInt("med_idUsuario"),
        rs.getString("med_nombre"),
        rs.getString("med_apellido"),
        rs.getInt("idMedico"),
        rs.getString("matricula"),
        rs.getString("especialidad")
    );

    // 2. Construir el objeto Paciente
    Paciente paciente = new Paciente(
        rs.getInt("pac_idUsuario"),
        rs.getString("pac_nombre"),
        rs.getString("pac_apellido"),
        rs.getInt("idPaciente"),
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

  public List<Turno> getByEstado(String estado) {
    List<Turno> turnos = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYESTADO)) {

      pst.setString(1, estado);
      try (ResultSet rs = pst.executeQuery()) {
        while (rs.next()) {
          turnos.add(construirTurno(rs)); // ¡Esto ahora también funcionará!
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return turnos;
  }
}