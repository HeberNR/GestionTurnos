package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;
import org.progI.interfaces.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO implements AdmConection, DAO<Medico, Integer> {

  // Consulta base que une usuarios y medicos para obtener todos los datos.
  private static final String SQL_SELECT_BASE = "SELECT u.*, m.idMedico, m.matricula, m.especialidad " +
      "FROM usuarios u JOIN medicos m ON u.idUsuario = m.usuarios_idUsuario";

  // Consultas específicas construidas a partir de la base.
  private static final String SQL_GETALL = SQL_SELECT_BASE + " ORDER BY u.apellido, u.nombre";
  private static final String SQL_GETBYID = SQL_SELECT_BASE + " WHERE u.idUsuario = ?";

  // Consultas para actualizar ambas tablas.
  private static final String SQL_UPDATE_USUARIO = "UPDATE usuarios SET dni = ?, nombre = ?, apellido = ?, telefono = ?, email = ?, pass = ? WHERE idUsuario = ?";
  private static final String SQL_UPDATE_MEDICO = "UPDATE medicos SET matricula = ?, especialidad = ? WHERE idMedico = ?";
  private static final String SQL_DELETE = "DELETE FROM usuarios WHERE idUsuario = ?"; // Borrado en cascada se encargará de la tabla medicos


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
      e.printStackTrace(); // Manejo básico de errores
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

  /**
   * La inserción principal se maneja a través de UsuarioDAO para asegurar la transacción.
   * Este método se deja vacío intencionalmente o podría lanzar una excepción.
   */
  @Override
  public void insert(Medico objeto) {
    // No se implementa aquí. La inserción se hace en UsuarioDAO.registrar(usuario)
    System.err.println("Advertencia: El método insert() de MedicoDAO no debe ser llamado directamente. Use UsuarioDAO.registrar().");
  }

  /**
   * Actualiza los datos de un médico en ambas tablas (usuarios y medicos) de forma transaccional.
   */
  @Override
  public void update(Medico medico) {
    Connection conn = null;
    try {
      conn = obtenerConexion();
      conn.setAutoCommit(false);

      // Actualizar la tabla 'usuarios'
      try (PreparedStatement pstUsuario = conn.prepareStatement(SQL_UPDATE_USUARIO)) {
        pstUsuario.setInt(1, medico.getDni());
        pstUsuario.setString(2, medico.getNombre());
        pstUsuario.setString(3, medico.getApellido());
        pstUsuario.setString(4, medico.getTelefono());
        pstUsuario.setString(5, medico.getEmail());
        pstUsuario.setString(6, medico.getPass());
        pstUsuario.setInt(7, medico.getIdUsuario());
        pstUsuario.executeUpdate();
      }

      // Actualizar la tabla 'medicos'
      try (PreparedStatement pstMedico = conn.prepareStatement(SQL_UPDATE_MEDICO)) {
        pstMedico.setString(1, medico.getMatricula());
        pstMedico.setString(2, medico.getEspecialidad());
        pstMedico.setInt(3, medico.getIdMedico());
        pstMedico.executeUpdate();
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

  /**
   * Borra un usuario. Asume que la base de datos está configurada con
   * ON DELETE CASCADE en la foreign key de la tabla 'medicos'.
   */
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
   * Evita la duplicación de código en getAll() y getById().
   *
   * @param rs El ResultSet posicionado en una fila válida.
   * @return Un nuevo objeto Medico.
   * @throws SQLException Si hay un error al leer el ResultSet.
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
        Roles.valueOf(rs.getString("rol")), // Convierte el String del rol a nuestro Enum
        rs.getInt("idMedico"),
        rs.getString("matricula"),
        rs.getString("especialidad")
    );
  }
}
