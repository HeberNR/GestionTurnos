package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      System.out.println("Intento de acceso NO AUTORIZADO a /gestion-turnos. Redirigiendo a /login.");
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String filtroEstado = request.getParameter("filtroEstado");
    List<Turno> listaTurnos;

    if (filtroEstado == null) {
      // 1. Carga inicial (no hay filtro) -> Muestra solo los próximos
      System.out.println("Carga inicial. Mostrando turnos próximos.");
      listaTurnos = turnoDAO.getProximos();

    } else if (filtroEstado.isEmpty()) {
      // 2. Filtro "Mostrar Todos" (string vacío) -> Muestra TODOS
      System.out.println("Filtrando por 'Mostrar Todos'.");
      listaTurnos = turnoDAO.getAll();

    } else {
      // 3. Filtro Específico (PENDIENTE, etc) -> Muestra por estado
      System.out.println("Filtrando por estado: " + filtroEstado);
      listaTurnos = turnoDAO.getByEstado(filtroEstado);
    }

    // 3. Guardamos la lista (de próximos, todos, o filtrada) en el request.
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