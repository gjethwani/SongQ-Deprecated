package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Database;

/**
 * Servlet implementation class AddRequest
 */
@WebServlet("/AddRequest")
public class AddRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String roomCode = (String) request.getParameter("roomCode");
		String owner = (String) request.getParameter("owner");
		String songId = (String) request.getParameter("songId");
		String songName = (String) request.getParameter("songName");
		String artists = (String) request.getParameter("artists");
		String album = (String) request.getParameter("album");
		Database db = new Database();
		db.addRequest(roomCode, owner, songId, songName, artists, album, false);
		db.close();
	}
}
