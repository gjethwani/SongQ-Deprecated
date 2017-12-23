package servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wrapper.spotify.Api;

import constants.StringConstants;

/**
 * Servlet implementation class AuthenticateSpotify
 */
@WebServlet("/AuthenticateSpotify")
public class AuthenticateSpotify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final Api api = Api.builder()
				  .clientId(StringConstants.CLIENT_ID)
				  .clientSecret(StringConstants.CLIENT_SECRET)
				  .redirectURI(StringConstants.URI + "/AuthenticateSpotifyRedirect")
				  .build();
				
		/* Set the necessary scopes that the application will need from the user */
		final List<String> scopes = Arrays.asList("playlist-modify-public", "playlist-modify-private", "streaming");
		
		/* Set a state. This is used to prevent cross site request forgeries. */
		final String state = "";
		
		String authorizeURL = api.createAuthorizeURL(scopes, state);
		//authorizeURL += "&show_dialog=true";
		response.sendRedirect(authorizeURL);
		/* Continue by sending the user to the authorizeURL, which will look something like
		   https://accounts.spotify.com:443/authorize?client_id=5fe01282e44241328a84e7c5cc169165&response_type=code&redirect_uri=https://example.com/callback&scope=user-read-private%20user-read-email&state=some-state-of-my-choice
		 */
	}

}
