package org.progI.interfaces;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface AdmConection {
  default Connection obtenerConexion() {
    String dbDriver = "com.mysql.cj.jdbc.Driver";
    String dbCadenaDeConexion = "jdbc:mysql://127.0.0.1:3306/gestor_turnos";
    String dbUsuario = "root";
    String dbPass = "123456heber";
    Connection conn = null;
    try {
      Class.forName(dbDriver);
      conn = DriverManager.getConnection(dbCadenaDeConexion, dbUsuario, dbPass);
    } catch (ClassNotFoundException e) {
      System.out.println("No se encontro el driver de la BD");
      throw new RuntimeException(e);
    } catch (SQLException e) {
      System.out.println("No se pudo conectar a la BD");
      throw new RuntimeException(e);
    }
    System.out.println("Conexion exitosa a la BD");
    return conn;
  }
}