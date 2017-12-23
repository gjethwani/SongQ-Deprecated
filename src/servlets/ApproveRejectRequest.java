package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.AddTrackToPlaylistRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimplePlaylist;

import database.Database;

/**
 * Servlet implementation class ApproveRejectRequest
 */
@WebServlet("/ApproveRejectRequest")
public class ApproveRejectRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer requestId = Integer.parseInt((String)request.getParameter("requestId"));
		String approvedRejected = (String) request.getParameter("approvedRejected");
		String songId = (String) request.getParameter("songId");
		songId = "spotify:track:" + songId;
		Database db = new Database(); 
		db.requestServiced(requestId, approvedRejected); 
		db.close();
		Api api = (Api) request.getSession().getAttribute("api");
		String userId = "";
		try {
			userId = api.getMe().build().get().getId();
		} catch (WebApiException wae) {
			wae.printStackTrace();
		}
		final UserPlaylistsRequest playlistRequest = api.getPlaylistsForUser(userId).build();
		String playlistId = "";
		try {
		   final Page<SimplePlaylist> playlistsPage = playlistRequest.get();
		   for (SimplePlaylist playlist : playlistsPage.getItems()) {
			      if (playlist.getName().equals("API Test")) {
			    	  	  playlistId = playlist.getId();
			      }
			   }
		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
		final List<String> tracksToAdd = new ArrayList<String>();
		tracksToAdd.add(songId);
		final AddTrackToPlaylistRequest addRequest = api.addTracksToPlaylist(userId, playlistId, tracksToAdd)
		  .build();
		  
		try {
		  addRequest.get();
		} catch (Exception e) {
		   System.out.println("Something went wrong!" + e.getMessage());
		}
	}
}
