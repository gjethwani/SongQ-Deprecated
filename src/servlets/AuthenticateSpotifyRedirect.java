package servlets;

import java.io.IOException;

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
import com.wrapper.spotify.models.AuthorizationCodeCredentials;
import com.wrapper.spotify.models.User;

import constants.StringConstants;
import database.Database;

/**
 * Servlet implementation class AuthenticateSpotifyRedirect
 */
@WebServlet("/AuthenticateSpotifyRedirect")
public class AuthenticateSpotifyRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
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
		    //String userInfoString = (String) request.getSession().getAttribute("userInfo");
		   // if (userInfoString != null) {
	    			//String [] userInfo = userInfoString.split(","); //CHECK FOR NULL
	    		Database db = new Database();
	    		final CurrentUserRequest userRequest = api.getMe().build();
	    		 try {
	    		   final User user = userRequest.get();
	    		   request.getSession().setAttribute("userId",user.getId());
	    		   db.registerUser(user.getId(), user.getDisplayName());
	    		} catch (Exception e) {
	    		   e.printStackTrace();
	    		}
	    		db.close();
		   // }
		    /* Set the access token and refresh token so that they are used whenever needed */
		  //  try {
	    			request.getSession().setAttribute("api",api);
	    			//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/AuthenticateHost?redirect=SpotifyRedirect");
	    			//dispatcher.forward(request,response);
	    			response.setStatus(response.SC_MOVED_TEMPORARILY);
	    			response.setHeader("Location", StringConstants.URI + "/RoomCodes.jsp");    
		    /*} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		    } catch (ServletException e) {
			// TODO Auto-generated catch block
		    		e.printStackTrace(); */
		 //   } 
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
