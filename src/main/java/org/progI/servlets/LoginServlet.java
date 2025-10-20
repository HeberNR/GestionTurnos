package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.progI.dao.LoginDAO;
import org.progI.entities.Usuario;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  private final LoginDAO loginDAO = new LoginDAO();

  // Muestra el formulario de login
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
    dispatcher.forward(request, response);
  }

  // Procesa las credenciales del formulario
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String email = request.getParameter("email");
    String pass = request.getParameter("pass");

    Usuario usuarioValidado = loginDAO.validarUsuario(email, pass);

    if (usuarioValidado != null) {
      // Si el usuario es válido, creamos una sesión y lo guardamos en ella.
      HttpSession session = request.getSession(true); // 'true' crea la sesión si no existe
      session.setAttribute("usuarioLogueado", usuarioValidado);

      System.out.println("Login exitoso para: " + usuarioValidado.getEmail());
      // Redirigimos al usuario a la página de inicio (index)
      response.sendRedirect(request.getContextPath() + "/");
    } else {
      // Si el usuario no es válido, enviamos un mensaje de error de vuelta al formulario.
      System.out.println("Login fallido para: " + email);
      request.setAttribute("error", "Email o contraseña incorrectos.");
      doGet(request, response); // Reenviamos al formulario de login
    }
  }
}
