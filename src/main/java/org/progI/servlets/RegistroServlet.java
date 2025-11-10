package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.progI.dao.UsuarioDAO;
import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Usuario;
import org.progI.enums.Roles;

import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
  private final UsuarioDAO usuarioDAO = new UsuarioDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/registro.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    System.out.println("--- INICIANDO PROCESO DE REGISTRO ---");
    try {
      // Obtener todos los parámetros del request
      String dniStr = request.getParameter("dni");
      String telefono = request.getParameter("telefono");
      String nombre = request.getParameter("nombre");
      String apellido = request.getParameter("apellido");
      String email = request.getParameter("email");
      String passPlano = request.getParameter("pass");
      String rolStr = request.getParameter("rol");

      /* --- Imprimimos lo que recibimos del formulario ---
      System.out.println("DNI recibido: " + dniStr);
      System.out.println("Teléfono recibido: " + telefono);
      System.out.println("Nombre recibido: " + nombre);
      System.out.println("Apellido recibido: " + apellido);
      System.out.println("Email recibido: " + email);
      System.out.println("Password recibida: " + (passPlano != null && !passPlano.isEmpty() ? "Sí" : "No"));
      System.out.println("Rol recibido: " + rolStr); */

      if (rolStr == null || rolStr.isEmpty()) {
        System.err.println("¡ERROR! El rol es nulo o vacío.");
        throw new ServletException("El rol no fue seleccionado.");
      }

      String salt = BCrypt.gensalt();
      String passHasheado = BCrypt.hashpw(passPlano, salt);

      int dni = Integer.parseInt(dniStr);
      Roles rol = Roles.valueOf(rolStr);
      //System.out.println("Datos parseados correctamente. Rol: " + rol);

      Usuario nuevoUsuario = null;

      // Crear el objeto Medico o Paciente según el rol
      if (rol == Roles.MEDICO) {
        System.out.println("Creando un objeto MÉDICO...");
        String matricula = request.getParameter("matricula");
        String especialidad = request.getParameter("especialidad");
        System.out.println("Matrícula: " + matricula + ", Especialidad: " + especialidad);
        nuevoUsuario = new Medico(0, dni, nombre, apellido, telefono, email, passHasheado, rol, 0, matricula, especialidad);
      } else if (rol == Roles.PACIENTE) {
        System.out.println("Creando un objeto PACIENTE...");
        String obraSocial = request.getParameter("obraSocial");
        System.out.println("Obra Social: " + obraSocial);
        nuevoUsuario = new Paciente(0, dni, nombre, apellido, telefono, email, passHasheado, rol, 0, obraSocial);
      }

      // Intentar registrar el usuario en la base de datos
      if (nuevoUsuario != null) {
        System.out.println("Llamando a usuarioDAO.registrar() con el usuario: " + nuevoUsuario.getNombre());
        usuarioDAO.registrar(nuevoUsuario);
        System.out.println("¡REGISTRO EXITOSO EN LA BASE DE DATOS!");

        // 4. Si todo va bien, redirigir al login con un mensaje de éxito
        HttpSession session = request.getSession();
        session.setAttribute("mensajeFlash", "¡Registro exitoso! Ya puedes iniciar sesión.");
        response.sendRedirect(request.getContextPath() + "/login");
      } else {
        throw new ServletException("Rol de usuario inválido.");
      }

    } catch (SQLException e) {
      // Imprimimos el error de SQL de forma muy visible
      System.err.println("!!!!!!!! ERROR DE SQL !!!!!!!");
      e.printStackTrace(); // Esto nos dará la causa exacta del error de BD
      request.setAttribute("error", "Error en la base de datos. Es posible que el DNI o el Email ya estén registrados.");
      doGet(request, response);
    } catch (Exception e) {
      // Imprimimos cualquier otro error de forma muy visible
      System.err.println("!!!!!!!! ERROR GENERAL !!!!!!!");
      e.printStackTrace(); // Esto nos dirá si es un NumberFormatException, etc.
      request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
      doGet(request, response);
    }
  }
}