package org.progI.dao;

import org.progI.entities.Usuario;
import org.progI.enums.Roles;
import org.progI.interfaces.AdmConection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class LoginDAO implements AdmConection {
  private static final String SQL_BUSCAR_POR_EMAIL = "SELECT * FROM usuarios WHERE email = ?";

  public Usuario validarUsuario(String email, String passPlano) {
    Usuario usuario = null;

    try (Connection conn = obtenerConexion();
         PreparedStatement pst = conn.prepareStatement(SQL_BUSCAR_POR_EMAIL)) {

      pst.setString(1, email);
      try (ResultSet rs = pst.executeQuery()) {

        if (rs.next()) {

          String passHasheadoDB = rs.getString("pass");
          if (BCrypt.checkpw(passPlano, passHasheadoDB)) {

            int idUsuario = rs.getInt("idUsuario");
            Roles rol = Roles.valueOf(rs.getString("rol"));
            if (rol == Roles.MEDICO) {

              usuario = new MedicoDAO().getById(idUsuario);
            } else if (rol == Roles.PACIENTE) {

              usuario = new PacienteDAO().getById(idUsuario);
            }
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return usuario;
  }
}