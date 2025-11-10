package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp");
    dispatcher.forward(request, response);
  }
}