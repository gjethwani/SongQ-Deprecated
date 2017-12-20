package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.TrackSearchRequest;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.Track;

import spotify.Spotify;

/**
 * Servlet implementation class SearchTracks
 */
@WebServlet("/SearchTracks")
public class SearchTracks extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Spotify spotify = new Spotify();
		Api api = spotify.clientCredentialsAuthentication();
		final TrackSearchRequest searchRequest = api.searchTracks(request.getParameter("query")).build();
		try {
		   final Page<Track> trackSearchResult = searchRequest.get();
		   System.out.println("I got " + trackSearchResult.getTotal() + " results!");
		   String toReturn = "";
		  for (int i = 0; i < trackSearchResult.getItems().size(); i++) {
			   toReturn += trackSearchResult.getItems().get(i).getId();
			   toReturn += ",";
			   toReturn += trackSearchResult.getItems().get(i).getName();
			   toReturn += ",";
			   toReturn += trackSearchResult.getItems().get(i).getAlbum().getName();
			   toReturn += ",";
			   List<SimpleArtist> artists = trackSearchResult.getItems().get(i).getArtists();
			   for (int j = 0; j < artists.size(); j++) {
				   toReturn += artists.get(j).getName();
				   toReturn += ",";
			   }
			   toReturn += ";";
		   }
		   response.getWriter().write(toReturn);
		} catch (Exception e) {
		   e.printStackTrace();
		}
	}
}
