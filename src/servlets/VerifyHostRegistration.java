package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Database;

/**
 * Servlet implementation class VerifyHostRegistration
 */
@WebServlet("/VerifyHostRegistration")
public class VerifyHostRegistration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String firstName = (String) request.getParameter("firstName");
		String lastName = (String) request.getParameter("lastName");
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		String confirmPassword = (String) request.getParameter("confirmPassword");
		String registrationMessage = "";
		boolean invalid = false;
		if (firstName.length() == 0 || lastName.length() == 0 || username.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
			invalid = true;
			registrationMessage = "No field can be left empty";
			request.setAttribute("registrationMessage", registrationMessage);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostRegister.jsp");
			dispatcher.forward(request,response);
		}
		if (!password.equals(confirmPassword)) {
			invalid = true;
			registrationMessage = "Password and confirm password do not match";
			request.setAttribute("registrationMessage", registrationMessage);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostRegister.jsp");
			dispatcher.forward(request,response);
		}
		Database db = new Database();
		boolean usernameExists = db.usernameExists(username);
		if (usernameExists) {
			invalid = true;
			registrationMessage = "User already exists";
			request.setAttribute("registrationMessage", registrationMessage);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostRegister.jsp");
			dispatcher.forward(request,response);
		}
		if (!invalid) {
			request.getSession().setAttribute("username", username);
			db.registerUser(firstName, lastName, username, password);
			db.close();
			registrationMessage = "";
			request.setAttribute("registrationMessage", registrationMessage);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AuthenticateSpotify?newUser=true");
			dispatcher.forward(request,response);
		}
	}

}
