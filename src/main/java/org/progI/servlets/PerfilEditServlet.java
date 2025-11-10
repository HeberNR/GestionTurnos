package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.progI.dao.MedicoDAO;
import org.progI.dao.PacienteDAO;
import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Usuario;
import org.progI.enums.Roles;

import java.io.IOException;


@WebServlet("/mi-perfil/editar")
public class PerfilEditServlet extends HttpServlet {

  private final MedicoDAO medicoDAO = new MedicoDAO();
  private final PacienteDAO pacienteDAO = new PacienteDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    request.setAttribute("usuario", session.getAttribute("usuarioLogueado"));
    request.setAttribute("titulo", "Editar Mi Perfil");

    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/perfil-form.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    try {
      Usuario usuarioActual = (Usuario) session.getAttribute("usuarioLogueado");

      String nombre = request.getParameter("nombre");
      String apellido = request.getParameter("apellido");
      String telefono = request.getParameter("telefono");

      usuarioActual.setNombre(nombre);
      usuarioActual.setApellido(apellido);
      usuarioActual.setTelefono(telefono);

      if (usuarioActual.getRol() == Roles.MEDICO) {
        Medico medico = (Medico) usuarioActual;
        medico.setMatricula(request.getParameter("matricula"));
        medico.setEspecialidad(request.getParameter("especialidad"));

        medicoDAO.update(medico);

      } else if (usuarioActual.getRol() == Roles.PACIENTE) {
        Paciente paciente = (Paciente) usuarioActual;
        paciente.setObraSocial(request.getParameter("obraSocial"));

        System.out.println("Actualizando perfil de Paciente (lógica de DAO pendiente)...");
      }

      session.setAttribute("usuarioLogueado", usuarioActual);

      session.setAttribute("mensajeFlash", "¡Perfil actualizado con éxito!");

      response.sendRedirect(request.getContextPath() + "/perfil");

    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute("error", "No se pudo actualizar el perfil: " + e.getMessage());
      doGet(request, response);
    }
  }
}