package org.example.tennisscoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("title", "Home");
        request.setAttribute("message", "Welcome to Tennis ScoreBoard");
        request.setAttribute("serverTime", new java.util.Date());

        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        response.sendRedirect(request.getContextPath() + "/home");
    }
}
