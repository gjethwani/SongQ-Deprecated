package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.StringConstants;
import database.Database;

/**
 * Servlet implementation class ChooseType
 */
@WebServlet("/ChooseType")
public class ChooseType extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter(StringConstants.TYPE);
		if (type.equals(StringConstants.FORM_GUEST)) {
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GuestLogin.jsp");
    			dispatcher.forward(request,response);
		} else if (type.equals(StringConstants.FORM_HOST)) {
			//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher();
			//dispatcher.forward(request,response);
		}
	}

}
