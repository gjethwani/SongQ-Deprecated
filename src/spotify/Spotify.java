package spotify;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

import constants.StringConstants;

public class Spotify {
	public Api clientCredentialsAuthentication() {
		final String clientId = StringConstants.CLIENT_ID;
		final String clientSecret = StringConstants.CLIENT_SECRET;

		final Api api = Api.builder()
		  .clientId(clientId)
		  .clientSecret(clientSecret)
		  .build();

		/* Create a request object. */
		final ClientCredentialsGrantRequest request = api.clientCredentialsGrant().build();

		/* Use the request object to make the request, either asynchronously (getAsync) or synchronously (get) */
		final SettableFuture<ClientCredentials> responseFuture = request.getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(responseFuture, new FutureCallback<ClientCredentials>() {
		  @Override
		  public void onSuccess(ClientCredentials clientCredentials) {
		    /* The tokens were retrieved successfully! */
		    System.out.println("Successfully retrieved an access token! " + clientCredentials.getAccessToken());
		    System.out.println("The access token expires in " + clientCredentials.getExpiresIn() + " seconds");
		    
		    /* Set access token on the Api object so that it's used going forward */
		    api.setAccessToken(clientCredentials.getAccessToken());
		    
		    /* Please note that this flow does not return a refresh token.
		   * That's only for the Authorization code flow */
		  }

		  @Override
		  public void onFailure(Throwable throwable) {
		    /* An error occurred while getting the access token. This is probably caused by the client id or
		     * client secret is invalid. */
		  }
		});
		return api;
	}
}
