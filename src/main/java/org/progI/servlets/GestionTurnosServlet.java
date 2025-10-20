package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.progI.dao.TurnoDAO;
import org.progI.entities.Turno;
import org.progI.enums.EstadoTurno;

import java.io.IOException;
import java.util.List;

@WebServlet("/gestion-turnos")
public class GestionTurnosServlet extends HttpServlet {
  private final TurnoDAO turnoDAO = new TurnoDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 1. Revisamos si el usuario envió un filtro de estado desde el formulario.
    String filtroEstado = request.getParameter("filtroEstado");

    List<Turno> listaTurnos;

    // 2. Lógica de decisión:
    if (filtroEstado != null && !filtroEstado.isEmpty()) {
      // Si hay un filtro, usamos el nuevo método del DAO.
      System.out.println("Filtrando por estado: " + filtroEstado); // Para depuración
      listaTurnos = turnoDAO.getByEstado(filtroEstado);
    } else {
      // Si no hay filtro, mostramos todos los turnos como antes.
      System.out.println("Mostrando todos los turnos."); // Para depuración
      listaTurnos = turnoDAO.getAll();
    }

    // 3. Guardamos la lista (filtrada o no) en el request.
    request.setAttribute("listaTurnos", listaTurnos);

    // 4. Guardamos el estado actual del filtro para que el dropdown lo recuerde.
    request.setAttribute("filtroEstadoActual", filtroEstado);

    // 5. Pasamos la lista de todos los estados posibles para rellenar el dropdown.
    request.setAttribute("estadosPosibles", EstadoTurno.values());

    // 6. Enviamos todo al JSP.
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/gestion-turnos.jsp");
    dispatcher.forward(request, response);
  }
}
