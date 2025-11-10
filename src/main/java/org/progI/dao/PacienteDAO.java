package org.progI.dao;

import org.progI.entities.Paciente;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;
import org.progI.interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO implements AdmConection, DAO<Paciente, Integer> {

  // Consulta base que une usuarios y pacientes.
  private static final String SQL_SELECT_BASE = "SELECT u.*, p.idPaciente, p.obraSocial " +
      "FROM usuarios u JOIN pacientes p ON u.idUsuario = p.usuarios_idUsuario";

  // Consultas específicas.
  private static final String SQL_GETALL = SQL_SELECT_BASE + " ORDER BY u.apellido, u.nombre";
  private static final String SQL_GETBYID = SQL_SELECT_BASE + " WHERE u.idUsuario = ?";
  private static final String SQL_GETBYDNI = SQL_SELECT_BASE + " WHERE u.dni = ?";

  // Consultas para actualizar ambas tablas.
  private static final String SQL_UPDATE_USUARIO = "UPDATE usuarios SET dni = ?, nombre = ?, apellido = ?, telefono = ?, email = ?, pass = ? WHERE idUsuario = ?";
  private static final String SQL_UPDATE_PACIENTE = "UPDATE pacientes SET obraSocial = ? WHERE idPaciente = ?";
  private static final String SQL_DELETE = "DELETE FROM usuarios WHERE idUsuario = ?";


  @Override
  public List<Paciente> getAll() {
    List<Paciente> pacientes = new ArrayList<>();
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETALL);
         ResultSet rs = pst.executeQuery()) {

      while (rs.next()) {
        pacientes.add(construirPaciente(rs));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return pacientes;
  }

  @Override
  public Paciente getById(Integer id) {
    Paciente paciente = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYID)) {

      pst.setInt(1, id);
      try (ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
          paciente = construirPaciente(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return paciente;
  }

  public Paciente getByDni(int dni) {
    Paciente paciente = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_GETBYDNI)) {

      pst.setInt(1, dni);
      try (ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
          paciente = construirPaciente(rs);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return paciente;
  }

  @Override
  public void insert(Paciente objeto) {
    System.err.println("Advertencia: El método insert() de PacienteDAO no debe ser llamado directamente. Use UsuarioDAO.registrar().");
  }

  @Override
  public void update(Paciente paciente) {
    Connection conn = null;
    try {
      conn = obtenerConexion();
      conn.setAutoCommit(false);

      // 1. Actualizar la tabla 'usuarios'
      try (PreparedStatement pstUsuario = conn.prepareStatement(SQL_UPDATE_USUARIO)) {
        pstUsuario.setInt(1, paciente.getDni());
        pstUsuario.setString(2, paciente.getNombre());
        pstUsuario.setString(3, paciente.getApellido());
        pstUsuario.setString(4, paciente.getTelefono());
        pstUsuario.setString(5, paciente.getEmail());
        pstUsuario.setString(6, paciente.getPass());
        pstUsuario.setInt(7, paciente.getIdUsuario());
        pstUsuario.executeUpdate();
      }

      // 2. Actualizar la tabla 'pacientes'
      try (PreparedStatement pstPaciente = conn.prepareStatement(SQL_UPDATE_PACIENTE)) {
        pstPaciente.setString(1, paciente.getObraSocial());
        pstPaciente.setInt(2, paciente.getIdPaciente());
        pstPaciente.executeUpdate();
      }

      conn.commit(); // Confirmar transacción

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

  private Paciente construirPaciente(ResultSet rs) throws SQLException {
    return new Paciente(
        rs.getInt("idUsuario"),
        rs.getInt("dni"),
        rs.getString("nombre"),
        rs.getString("apellido"),
        rs.getString("telefono"),
        rs.getString("email"),
        rs.getString("pass"),
        Roles.valueOf(rs.getString("rol")),
        rs.getInt("idPaciente"),
        rs.getString("obraSocial")
    );
  }
}