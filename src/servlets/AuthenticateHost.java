package servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;

import constants.StringConstants;
import database.Database;

/**
 * Servlet implementation class AuthenticateHost
 */
@WebServlet("/AuthenticateHost")
public class AuthenticateHost extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		String redirect = request.getParameter("redirect");
		if (redirect != null && redirect.equals("SpotifyRedirect")) {
			//request.setAttribute("username", username);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/RoomCodes.jsp");
			dispatcher.forward(request,response);
		} else {
			String username = request.getParameter(StringConstants.USERNAME);
			request.getSession().setAttribute("username", username);
			String password = request.getParameter(StringConstants.PASSWORD);
			String hostLoginMessage = " ";
			if (username.length() == 0 || password.length() == 0 ) {
				hostLoginMessage = "Please enter both a username or password";
				request.setAttribute("hostLoginMessage", hostLoginMessage);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostLogin.jsp");
				dispatcher.forward(request,response);
			} else {
				Database db = new Database();
				int authenticationResult = db.authenticateHost(username, password);
				if (authenticationResult == 1) {
					hostLoginMessage = " ";
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AuthenticateSpotify");
	    				dispatcher.forward(request,response);
				} else if (authenticationResult == -1) {
					hostLoginMessage = "Invalid Password";
					request.setAttribute("hostLoginMessage", hostLoginMessage);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostLogin.jsp");
					dispatcher.forward(request,response);
				} else if (authenticationResult == 0) {
					hostLoginMessage = "User not found";
					request.setAttribute("hostLoginMessage", hostLoginMessage);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostLogin.jsp");
					dispatcher.forward(request,response);
				} else {
					hostLoginMessage = "Error";
					request.setAttribute("hostLoginMessage", hostLoginMessage);
					RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/HostLogin.jsp");
					dispatcher.forward(request,response);
				}
			}
		}
	}
}
