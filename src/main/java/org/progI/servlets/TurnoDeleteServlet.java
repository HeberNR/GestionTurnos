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
    int idTurno = Integer.parseInt(request.getParameter("idTurno"));

    turnoDAO.delete(idTurno);

    response.sendRedirect(request.getContextPath() + "/gestion-turnos");
  }
}