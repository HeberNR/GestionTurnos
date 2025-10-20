package org.progI.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false); // 'false' para no crear una nueva sesión si no existe

    if (session != null) {
      session.invalidate(); // Invalida la sesión, borrando todos los atributos
    }

    // Redirige al login con un mensaje de que la sesión se cerró
    response.sendRedirect(request.getContextPath() + "/login");
  }
}