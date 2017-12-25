package servlets;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.PlaylistCreationRequest;
import com.wrapper.spotify.models.Playlist;

import database.Database;

/**
 * Servlet implementation class CreateRoomCode
 */
@WebServlet("/CreateRoomCode")
public class CreateRoomCode extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected String generateRandomRoomCode() {
		String [] characters = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
		String roomCode = "";
		for (int i = 0; i < 4; i++) {
			int index = ThreadLocalRandom.current().nextInt(0, characters.length);
			roomCode += characters[index];
		}
		return roomCode;
	}
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String owner = (String) request.getParameter("owner");
		String playlistName = (String) request.getParameter("playlistName");
		boolean roomCodeExists = true;
		String roomCode = "";
		Database db = new Database();
		while (roomCodeExists) {
			roomCodeExists = db.roomCodeExists(roomCode);
			if (!roomCodeExists) {
				break;
			} else {
				roomCode = generateRandomRoomCode();
			}
		}
		db.createRoomCode(roomCode, playlistName, owner);
		db.close();
		Api api = (Api) request.getSession().getAttribute("api");
		String userId = 	"";	
		try {
			userId = api.getMe().build().get().getId();
		} catch (WebApiException wae) {
			wae.printStackTrace();
		}
		final PlaylistCreationRequest pcr = api.createPlaylist(userId, playlistName)
		  .publicAccess(true)
		  .build();

		try {
		  final Playlist playlist = pcr.get();
		  
		  System.out.println("You just created this playlist!");
		  System.out.println("Its title is " + playlist.getName());
		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
	}

}
