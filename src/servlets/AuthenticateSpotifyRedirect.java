package servlets;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.CurrentUserRequest;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.SimplePlaylist;
import com.wrapper.spotify.models.User;

import constants.StringConstants;
import database.Database;

/**
 * Servlet implementation class AuthenticateSpotifyRedirect
 */
@WebServlet("/AuthenticateSpotifyRedirect")
public class AuthenticateSpotifyRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected Vector<SimplePlaylist> getPlaylists(Api api) {
		final CurrentUserRequest userRequest = api.getMe().build();
		Vector<SimplePlaylist> toReturn = new Vector<SimplePlaylist>();
		 try {
		   final User user = userRequest.get();
		   final UserPlaylistsRequest request = api.getPlaylistsForUser(user.getId()).limit(50).build();
			try {
			   final Page<SimplePlaylist> playlistsPage = request.get();
			   for (SimplePlaylist playlist : playlistsPage.getItems()) {
				  if (playlist.getOwner().getId().equals(user.getId())) {
					  toReturn.add(playlist);
				  }
			   }
			} catch (Exception e) {
			   System.out.println("Could not get playlists");
			   e.printStackTrace();
			}
		} catch (Exception e) {
		   e.printStackTrace();
		}
		return toReturn;
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/* Application details necessary to get an access token */
		String error = (String) request.getParameter("error");
		if (error != null) {
			 response.setStatus(response.SC_MOVED_TEMPORARILY);
 			 response.setHeader("Location", StringConstants.URI + "/HostLogin.jsp");   
 			 return;
		}
		final String code = request.getParameter("code");
		final Api api = Api.builder()
				  .clientId(StringConstants.CLIENT_ID)
				  .clientSecret(StringConstants.CLIENT_SECRET)
				  //.redirectURI("http://localhost:8080/SongQ/AuthenticateHost?redirect=SpotifyRedirect")
				  .redirectURI(StringConstants.URI + "/AuthenticateSpotifyRedirect")
				  .build();
		/* Make a token request. Asynchronous requests are made with the .getAsync method and synchronous requests
		 * are made with the .get method. This holds for all type of requests. */
		final SettableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = api.authorizationCodeGrant(code).build().getAsync();
		/* Add callbacks to handle success and failure */
		Futures.addCallback(authorizationCodeCredentialsFuture, new FutureCallback<AuthorizationCodeCredentials>() {
		  @Override
		  public void onSuccess(AuthorizationCodeCredentials authorizationCodeCredentials) {
		    /* The tokens were retrieved successfully! */
			api.setAccessToken(authorizationCodeCredentials.getAccessToken());
			api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
		    System.out.println("Successfully retrieved an access token! " + authorizationCodeCredentials.getAccessToken());
		    System.out.println("The access token expires in " + authorizationCodeCredentials.getExpiresIn() + " seconds");
		    System.out.println("Luckily, I can refresh it using this refresh token! " +     authorizationCodeCredentials.getRefreshToken());
		    Database db = new Database();
	    		final CurrentUserRequest userRequest = api.getMe().build();
	    		 try {
	    		   final User user = userRequest.get();
	    		   request.getSession().setAttribute("playlists", getPlaylists(api));
	    		   request.getSession().setAttribute("userId",user.getId());
	    		   if (user.getDisplayName() == null) {
	    			   db.registerUser(user.getId(), user.getEmail());
	    		   } else {
	    			   db.registerUser(user.getId(), user.getDisplayName());
	    		   }
	    		} catch (Exception e) {
	    		   e.printStackTrace();
	    		}
	    		db.close();
    			request.getSession().setAttribute("api",api);
    			response.setStatus(response.SC_MOVED_TEMPORARILY);
    			response.setHeader("Location", StringConstants.URI + "/RoomCodes.jsp");    
		  }

		  @Override
		  public void onFailure(Throwable throwable) {
		    /* Let's say that the client id is invalid, or the code has been used more than once,
		     * the request will fail. Why it fails is written in the throwable's message. */
			  System.out.println("failure");
			  throwable.printStackTrace();
			  System.out.println(throwable.getMessage());
			  response.setStatus(response.SC_MOVED_TEMPORARILY);
  			  response.setHeader("Location", StringConstants.URI + "/HostLogin.jsp");   
		  }
		});
	}

}
