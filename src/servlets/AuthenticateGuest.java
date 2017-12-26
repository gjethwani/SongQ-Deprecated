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
		String roomCode = request.getParameter(StringConstants.ROOM_CODE);
		request.getSession().setAttribute(StringConstants.ROOM_CODE, roomCode);
		Database db = new Database();
		boolean roomCodePassed = db.authenticateRoomCode(roomCode);
		db.close();
		request.setAttribute("roomCodePassed", roomCodePassed);
		if (roomCodePassed) {
			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", StringConstants.URI + "/RequestSong.jsp");
		} else {
			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", StringConstants.URI + "/GuestLogin.jsp");
		}
	}
}
