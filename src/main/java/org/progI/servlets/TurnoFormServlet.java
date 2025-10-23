package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.progI.dao.MedicoDAO;
import org.progI.dao.PacienteDAO;
import org.progI.dao.TurnoDAO;
import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Turno;
import org.progI.enums.EstadoTurno;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/turno-form")
public class TurnoFormServlet extends HttpServlet {
  private final TurnoDAO turnoDAO = new TurnoDAO();
  private final MedicoDAO medicoDAO = new MedicoDAO();
  private final PacienteDAO pacienteDAO = new PacienteDAO();

  // Este método se encarga de MOSTRAR el formulario.
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String idTurnoStr = request.getParameter("idTurno");

    // Si nos pasaron un ID, es porque quieren EDITAR.
    if (idTurnoStr != null && !idTurnoStr.isEmpty()) {
      int idTurno = Integer.parseInt(idTurnoStr);
      Turno turnoAEditar = turnoDAO.getById(idTurno);
      request.setAttribute("turno", turnoAEditar); // Mandamos el turno al JSP
      request.setAttribute("titulo", "Editar Turno");
    } else {
      // Si no hay ID, es porque quieren CREAR uno nuevo.
      request.setAttribute("titulo", "Nuevo Turno");
    }

    // Mandamos las listas de médicos y pacientes para rellenar los dropdowns
    request.setAttribute("listaMedicos", medicoDAO.getAll());
    request.setAttribute("listaPacientes", pacienteDAO.getAll());
    request.setAttribute("estadosPosibles", EstadoTurno.values());

    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/turno-form.jsp");
    dispatcher.forward(request, response);
  }

  // Este metodo se encarga de procesar los datos del formulario cuando hacemos click en guardar.
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // 1. Recogemos todos los datos del formulario
    String idTurnoStr = request.getParameter("idTurno");
    int medicoId = Integer.parseInt(request.getParameter("medicoId"));
    int pacienteId = Integer.parseInt(request.getParameter("pacienteId"));
    LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
    LocalTime hora = LocalTime.parse(request.getParameter("hora"));
    String motivo = request.getParameter("motivo");
    EstadoTurno estado = EstadoTurno.valueOf(request.getParameter("estado"));

    // 2. Obtenemos los objetos Medico y Paciente completos usando sus IDs
    Medico medico = medicoDAO.getById(medicoId);
    Paciente paciente = pacienteDAO.getById(pacienteId);

    // 3. Decidimos si es una ACTUALIZACIÓN o una INSERCIÓN
    if (idTurnoStr != null && !idTurnoStr.isEmpty()) {
      // Es una actualización
      int idTurno = Integer.parseInt(idTurnoStr);
      Turno turnoActualizado = new Turno(idTurno, medico, paciente, fecha, hora, motivo, estado);
      turnoDAO.update(turnoActualizado);
    } else {
      // Es una inserción
      Turno nuevoTurno = new Turno(medico, paciente, fecha, hora, motivo, estado);
      turnoDAO.insert(nuevoTurno);
    }

    // 4. Redirigimos al usuario de vuelta a la lista principal de turnos
    response.sendRedirect(request.getContextPath() + "/gestion-turnos");
  }
}