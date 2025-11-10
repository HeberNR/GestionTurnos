package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Usuario;
import org.progI.interfaces.AdmConection;

import java.sql.*;

public class UsuarioDAO implements AdmConection {

  private static final String SQL_INSERT_USUARIO = "INSERT INTO usuarios (dni, nombre, apellido, " +
      "telefono, email, pass, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";

  private static final String SQL_INSERT_MEDICO = "INSERT INTO medicos (matricula, especialidad, usuarios_idUsuario) VALUES (?, ?, ?)";

  private static final String SQL_INSERT_PACIENTE = "INSERT INTO pacientes (obraSocial, usuarios_idUsuario) VALUES (?, ?)";

  /**
   * Registra un nuevo usuario (Medico o Paciente) en la base de datos de forma transaccional.
   * Inserta primero en la tabla 'usuarios' y luego en la tabla específica del rol.
   * Si alguna de las dos inserciones falla, revierte todos los cambios.
   *
   * @param usuario El objeto Medico o Paciente a registrar.
   * @throws SQLException Si ocurre un error en la base de datos.
   */
  public void registrar(Usuario usuario) throws SQLException {
    Connection conn = null;
    try {
      conn = obtenerConexion();
      conn.setAutoCommit(false);
      // Insertar en la tabla 'usuarios' y obtener el ID generado
      try (PreparedStatement pstUsuario = conn.prepareStatement(SQL_INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
        pstUsuario.setInt(1, usuario.getDni());
        pstUsuario.setString(2, usuario.getNombre());
        pstUsuario.setString(3, usuario.getApellido());
        pstUsuario.setString(4, usuario.getTelefono());
        pstUsuario.setString(5, usuario.getEmail());
        pstUsuario.setString(6, usuario.getPass());
        pstUsuario.setString(7, usuario.getRol().name());
        pstUsuario.executeUpdate();

        try (ResultSet rs = pstUsuario.getGeneratedKeys()) {
          if (rs.next()) {
            usuario.setIdUsuario(rs.getInt(1)); // Asignamos el ID generado al objeto
          } else {
            throw new SQLException("Fallo al crear usuario, no se obtuvo ID.");
          }
        }
      }

      // Insertar en la tabla específica del rol (Medico o Paciente)
      if (usuario instanceof Medico) {
        try (PreparedStatement pstRol = conn.prepareStatement(SQL_INSERT_MEDICO)) {
          Medico medico = (Medico) usuario;
          pstRol.setString(1, medico.getMatricula());
          pstRol.setString(2, medico.getEspecialidad());
          pstRol.setInt(3, medico.getIdUsuario()); // Usamos el ID recién generado
          pstRol.executeUpdate();
        }
      } else if (usuario instanceof Paciente) {
        try (PreparedStatement pstRol = conn.prepareStatement(SQL_INSERT_PACIENTE)) {
          Paciente paciente = (Paciente) usuario;
          pstRol.setString(1, paciente.getObraSocial());
          pstRol.setInt(2, paciente.getIdUsuario()); // Usamos el ID recién generado
          pstRol.executeUpdate();
        }
      }

      // Si tod0 salió bien, confirmamos la transacción
      conn.commit();
      System.out.println("Transacción exitosa: Usuario y rol registrados.");

    } catch (SQLException e) {
      // Si algo falló, revertimos todos los cambios
      if (conn != null) {
        System.err.println("Ocurrió un error en la transacción. Realizando rollback...");
        conn.rollback();
      }
      // Relanzamos la excepción para que el servlet sepa que algo salió mal
      throw e;

    } finally {
      // Cerramos la conexión y restauramos el modo auto-commit
      if (conn != null) {
        conn.setAutoCommit(true);
        conn.close();
      }
    }
  }
}
