package org.progI.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// La anotación clave para la página principal
@WebServlet("")
public class IndexServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // ¡EL MICRÓFONO! Esto imprimirá en la consola si el servlet se ejecuta.
    System.out.println("******************************************");
    System.out.println("¡IndexServlet SÍ está funcionando!");
    System.out.println("Intentando reenviar a: /WEB-INF/jsp/index.jsp");
    System.out.println("******************************************");

    // Reenviamos a la página de inicio.
    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/index.jsp");
    dispatcher.forward(request, response);
  }
}