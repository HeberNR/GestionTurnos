package org.progI.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.progI.dao.TurnoDAO;

import java.io.IOException;

@WebServlet("/turno-delete")
public class TurnoDeleteServlet extends HttpServlet {
  private final TurnoDAO turnoDAO = new TurnoDAO();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 1. Obtenemos el ID del turno que queremos borrar.
    // Lo recibimos de un campo oculto en un formulario.
    int idTurno = Integer.parseInt(request.getParameter("idTurno"));

    // 2. Le ordenamos al DAO que lo elimine.
    turnoDAO.delete(idTurno);

    // 3. Redirigimos al usuario de vuelta a la lista principal.
    response.sendRedirect(request.getContextPath() + "/gestion-turnos");
  }
}