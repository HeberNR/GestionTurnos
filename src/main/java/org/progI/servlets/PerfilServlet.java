package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// 1. Esta es la URL PÚBLICA a la que vamos a linkear
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);

    // 2. Verificamos que el usuario esté logueado para ver su perfil
    if (session == null || session.getAttribute("usuarioLogueado") == null) {
      response.sendRedirect(request.getContextPath() + "/login");
      return;
    }

    // 3. El usuario está logueado, así que le "abrimos" el JSP privado.
    // (Acá podrías buscar más datos del usuario en la BD si quisieras)
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp");
    dispatcher.forward(request, response);
  }
}