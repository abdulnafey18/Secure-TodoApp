package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerInitializer {
    public static void startServer() throws Exception {
        // Set up the Jetty server
        Server server = new Server(8080);

        // Create a context handler
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/"); // Root context
        server.setHandler(handler);

        // Add servlets (endpoints)
        handler.addServlet(new ServletHolder(new UserServlet()), "/users");
        handler.addServlet(new ServletHolder(new TaskServlet()), "/tasks");
        handler.addServlet(new ServletHolder(new LogServlet()), "/logs");
        handler.addServlet(new ServletHolder(new RegisterServlet()), "/register");
        handler.addServlet(new ServletHolder(new LoginServlet()), "/login");

        // Start the server
        System.out.println("Starting server at http://localhost:8080");
        server.start();
        server.join();
    }

    // Servlets (HTTP Endpoints)

    // User Management Endpoint
    public static class UserServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("application/json");
            resp.getWriter().write("[{\"id\":1, \"username\":\"Admin\"}, {\"id\":2, \"username\":\"User\"}]");
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("User created successfully");
        }
    }

    // Task Management Endpoint
    public static class TaskServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("application/json");
            resp.getWriter().write("[{\"id\":1, \"task\":\"Finish project\"}, {\"id\":2, \"task\":\"Fix bugs\"}]");
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Task created successfully");
        }
    }

    // Logs Endpoint
    public static class LogServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setContentType("application/json");
            resp.getWriter().write("[{\"id\":1, \"log\":\"System started\"}, {\"id\":2, \"log\":\"User logged in\"}]");
        }
    }

    // Registration Endpoint
    public static class RegisterServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String password = req.getParameter("password");

            if (username != null && email != null && password != null) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("User registered successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid registration details");
            }
        }
    }

    // Login Endpoint
    public static class LoginServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            if ("admin".equals(username) && "password".equals(password)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Login successful");
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("Invalid credentials");
            }
        }
    }
}