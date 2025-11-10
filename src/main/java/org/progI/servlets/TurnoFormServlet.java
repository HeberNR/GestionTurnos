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
import org.progI.dao.TurnoDAO;

import org.progI.dao.UsuarioDAO;
import org.progI.entities.Medico;
import org.progI.entities.Paciente;
import org.progI.entities.Turno;
import org.progI.enums.EstadoTurno;
import org.progI.enums.Roles;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@WebServlet("/turno-form")
public class TurnoFormServlet extends HttpServlet {
  private final TurnoDAO turnoDAO = new TurnoDAO();
  private final MedicoDAO medicoDAO = new MedicoDAO();
  private final PacienteDAO pacienteDAO = new PacienteDAO();
  private final UsuarioDAO usuarioDAO = new UsuarioDAO();

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      System.out.println("Intento de acceso NO AUTORIZADO a /turno-form. Redirigiendo a /login.");
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    String idTurnoStr = request.getParameter("idTurno");

    // Si nos pasaron un ID, es porque quieren editar.
    if (idTurnoStr != null && !idTurnoStr.isEmpty()) {
      int idTurno = Integer.parseInt(idTurnoStr);
      Turno turnoAEditar = turnoDAO.getById(idTurno);
      request.setAttribute("turno", turnoAEditar); // Mandamos el turno al JSP
      request.setAttribute("titulo", "Editar Turno");
    } else {
      // Si no hay ID, es porque quieren crear uno nuevo.
      request.setAttribute("titulo", "Nuevo Turno");
    }

    // Mandamos las listas de médicos y pacientes para rellenar los dropdowns
    request.setAttribute("listaMedicos", medicoDAO.getAll());
    request.setAttribute("listaPacientes", pacienteDAO.getAll());
    request.setAttribute("estadosPosibles", EstadoTurno.values());

    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/turno-form.jsp");
    dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    int idUsuarioPacienteParaElTurno;

    try {
      String idPacienteUsuarioExistenteStr = request.getParameter("idPacienteUsuarioExistente");
      String pacienteDNIStr = request.getParameter("pacienteDNI");

      if (idPacienteUsuarioExistenteStr != null && !idPacienteUsuarioExistenteStr.equals("0")) {
        System.out.println("Usando paciente existente. ID Usuario: " + idPacienteUsuarioExistenteStr);
        idUsuarioPacienteParaElTurno = Integer.parseInt(idPacienteUsuarioExistenteStr);

      } else if (pacienteDNIStr != null && !pacienteDNIStr.isEmpty()) {
        System.out.println("Registrando nuevo paciente con DNI: " + pacienteDNIStr);

        int dni = Integer.parseInt(pacienteDNIStr);
        String nombre = request.getParameter("pacienteNombre");
        String apellido = request.getParameter("pacienteApellido");
        String telefono = request.getParameter("pacienteTelefono");
        String obraSocial = request.getParameter("pacienteObraSocial");

        String emailDummy = dni + "@mail-temporal.com";
        String passPlanoDummy = "temporal123";

        String salt = BCrypt.gensalt();
        String passHasheadoDummy = BCrypt.hashpw(passPlanoDummy, salt);

        Paciente nuevoPaciente = new Paciente(
            0, dni, nombre, apellido, telefono, emailDummy,
            passHasheadoDummy, Roles.PACIENTE, 0, obraSocial
        );

        usuarioDAO.registrar(nuevoPaciente);

        Paciente pacienteRegistrado = pacienteDAO.getByDni(dni);
        if (pacienteRegistrado == null) {
          throw new ServletException("Error: No se pudo encontrar al paciente recién registrado.");
        }

        idUsuarioPacienteParaElTurno = pacienteRegistrado.getIdUsuario();
        System.out.println("Nuevo paciente registrado. Nuevo ID Usuario: " + idUsuarioPacienteParaElTurno);

      } else {
        throw new ServletException("Debe seleccionar un paciente existente o registrar uno nuevo.");
      }

      // 1. Recogemos el resto de datos del turno
      String idTurnoStr = request.getParameter("idTurno");
      int medicoId = Integer.parseInt(request.getParameter("medicoId"));
      LocalDate fecha = LocalDate.parse(request.getParameter("fecha"));
      LocalTime hora = LocalTime.parse(request.getParameter("hora"));
      String motivo = request.getParameter("motivo");
      EstadoTurno estado = EstadoTurno.valueOf(request.getParameter("estado"));

      Medico medico = medicoDAO.getById(medicoId);
      Paciente paciente = pacienteDAO.getById(idUsuarioPacienteParaElTurno);

      if (medico == null || paciente == null) {
        throw new ServletException("El médico o el paciente no se pudieron encontrar en la BD.");
      }

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

      response.sendRedirect(request.getContextPath() + "/gestion-turnos");

    } catch (Exception e) {
      // Si algo falla (ej: DNI duplicado, campos vacíos, etc.)
      System.err.println("!!!!!!!! ERROR EN TurnoFormServlet !!!!!!!");
      e.printStackTrace();
      // Recargamos el formulario CON el mensaje de error
      request.setAttribute("error", e.getMessage());
      // Volvemos a llamar al doGet para que cargue las listas
      doGet(request, response);
    }
  }
}