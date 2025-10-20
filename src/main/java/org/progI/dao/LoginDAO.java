package org.progI.dao;

import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Usuario;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO implements AdmConection {

  // Consulta que busca un usuario por email y contraseña.
  private static final String SQL_LOGIN = "SELECT * FROM usuarios WHERE email = ? AND pass = ?";

  /**
   * Valida las credenciales de un usuario.
   * Si son correctas, busca el rol y devuelve el objeto Medico o Paciente completo.
   *
   * @param email El email del usuario.
   * @param pass  La contraseña del usuario (sin encriptar en este ejemplo).
   * @return El objeto Usuario (Medico o Paciente) si las credenciales son válidas, de lo contrario null.
   */
  public Usuario validarUsuario(String email, String pass) {
    Usuario usuario = null;
    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_LOGIN)) {

      pst.setString(1, email);
      pst.setString(2, pass);

      try (ResultSet rs = pst.executeQuery()) {
        if (rs.next()) {
          // Si encontramos un usuario, obtenemos su ID y rol
          int idUsuario = rs.getInt("idUsuario");
          Roles rol = Roles.valueOf(rs.getString("rol"));

          // Dependiendo del rol, usamos el DAO correspondiente para traer el objeto completo
          if (rol == Roles.MEDICO) {
            usuario = new MedicoDAO().getById(idUsuario);
          } else if (rol == Roles.PACIENTE) {
            usuario = new PacienteDAO().getById(idUsuario);
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace(); // Manejo básico de errores
    }
    return usuario;
  }
}