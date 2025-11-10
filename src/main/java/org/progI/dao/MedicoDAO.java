package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;
import org.progI.interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements AdmConection, DAO<Medico, Integer> {

  // Consultas SELECT
  private static final String SQL_SELECT_BASE = "SELECT u.*, m.idMedico, m.matricula, m.especialidad " +
      "FROM usuarios u JOIN medicos m ON u.idUsuario = m.usuarios_idUsuario";
  private static final String SQL_GETALL = SQL_SELECT_BASE + " ORDER BY u.apellido, u.nombre";
  private static final String SQL_GETBYID = SQL_SELECT_BASE + " WHERE u.idUsuario = ?";
  private static final String SQL_DELETE = "DELETE FROM usuarios WHERE idUsuario = ?";
  private static final String SQL_UPDATE_USUARIO_PERFIL = "UPDATE usuarios SET nombre = ?, apellido = ?, telefono = ? WHERE idUsuario = ?";
  private static final String SQL_UPDATE_MEDICO_PERFIL = "UPDATE medicos SET matricula = ?, especialidad = ? WHERE idMedico = ?";


  @Override
  public List<Medico> getAll() {
    List<Medico> medicos = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETALL);
         ResultSet rs = pst.executeQuery()) {

      while (rs.next()) {
        medicos.add(construirMedico(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return medicos;
  }

  @Override
  public Medico getById(Integer id) {
    Medico medico = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYID)) {

      pst.setInt(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
          medico = construirMedico(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return medico;
  }

  @Override
  public void insert(Medico objeto) {
    System.err.println("Advertencia: El método insert() de MedicoDAO no debe ser llamado directamente. Use UsuarioDAO.registrar().");
  }

  @Override
  public void update(Medico medico) {
    Connection conn = null;
    try {
      conn = obtenerConexion();
      conn.setAutoCommit(false);

      // 1. Actualizar la tabla 'usuarios'
      try (PreparedStatement pstUsuario = conn.prepareStatement(SQL_UPDATE_USUARIO_PERFIL)) {
        pstUsuario.setString(1, medico.getNombre());
        pstUsuario.setString(2, medico.getApellido());
        pstUsuario.setString(3, medico.getTelefono());
        pstUsuario.setInt(4, medico.getIdUsuario());
        pstUsuario.executeUpdate();
      }

      // 2. Actualizar la tabla 'medicos'
      try (PreparedStatement pstMedico = conn.prepareStatement(SQL_UPDATE_MEDICO_PERFIL)) {
        pstMedico.setString(1, medico.getMatricula());
        pstMedico.setString(2, medico.getEspecialidad());
        pstMedico.setInt(3, medico.getIdMedico());
        pstMedico.executeUpdate();
      }

      conn.commit(); // Confirmar transacción
      System.out.println("Perfil de " + medico.getNombre() + " actualizado exitosamente.");

    } catch (SQLException e) {
      if (conn != null) {
        try {
          conn.rollback(); // Revertir en caso de error
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      e.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.setAutoCommit(true);
          conn.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
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
   * Método de ayuda privado para construir un objeto Medico a partir de un ResultSet.
   */
  private Medico construirMedico(ResultSet rs) throws SQLException {
    return new Medico(
        rs.getInt("idUsuario"),
        rs.getInt("dni"),
        rs.getString("nombre"),
        rs.getString("apellido"),
        rs.getString("telefono"),
        rs.getString("email"),
        rs.getString("pass"),
        Roles.valueOf(rs.getString("rol")),
        rs.getInt("idMedico"),
        rs.getString("matricula"),
        rs.getString("especialidad")
    );
  }
}