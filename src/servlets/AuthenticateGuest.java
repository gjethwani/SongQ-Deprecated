package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;

import constants.StringConstants;
import database.Database;
import spotify.Spotify;

/**
 * Servlet implementation class AuthenticateGuest
 */
@WebServlet("/AuthenticateGuest")
public class AuthenticateGuest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String roomCodeFromForm = request.getParameter(StringConstants.ROOM_CODE);
		String roomCodeFromLocation = request.getParameter("locationDropdown");
		String roomCode = "";
		if (roomCodeFromForm != null) {
			roomCode = roomCodeFromForm;
		} else if (roomCodeFromLocation != null) {
			roomCode = roomCodeFromLocation;
		}
		request.getSession().setAttribute(StringConstants.ROOM_CODE, roomCode);
		Database db = new Database();
		boolean roomCodePassed = db.authenticateRoomCode(roomCode);
		db.close();
		request.setAttribute("roomCodePassed", roomCodePassed);
		if (roomCodePassed) {
			//response.setStatus(response.SC_MOVED_TEMPORARILY);
			//response.setHeader("Location", StringConstants.URI + "/RequestSong.jsp");
			getServletContext().getRequestDispatcher("/RequestSong.jsp").forward(request, response);
		} else {
			request.setAttribute("message", "Invalid room code");
			getServletContext().getRequestDispatcher("/GuestLogin.jsp").forward(request, response);
		}
	}
}
