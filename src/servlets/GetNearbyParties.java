package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import constants.StringConstants;
import database.Database;
import objects.Party;

/**
 * Servlet implementation class GetNearbyParties
 */
@WebServlet("/GetNearbyParties")
public class GetNearbyParties extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static void getDestinations(String JSON) {
		JSONParser parser = new JSONParser();
		try {
			
			JSONObject obj = (JSONObject) parser.parse(JSON);
			JSONArray rows = (JSONArray) obj.get("rows");
			JSONObject elementsBuffer = (JSONObject) rows.get(0);
			JSONArray elements = (JSONArray) elementsBuffer.get("elements");
			JSONObject distanceBuffer = (JSONObject) elements.get(0);
			System.out.println(distanceBuffer);
			JSONObject distance = (JSONObject) distanceBuffer.get("distance");
			System.out.println(distance);
			long value = (long) distance.get("value");
			System.out.println(value);
			
		} catch (org.json.simple.parser.ParseException pe) {
			pe.printStackTrace();
		}
	}
	
	private static void getDistances(String currLocation, Party party) throws IOException {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
		url += currLocation;
		url += "&destinations=";
		url += party.getLatitude() + "," + party.getLongitude();
		url += "&key=";
		url += StringConstants.GOOGLE_API_KEY;
		System.out.println(url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			getDestinations(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currLatitude = (String) request.getParameter("latitude");
		String currLongitude = (String) request.getParameter("longitude");
		Database db = new Database();
		List<Party> partyLocations = db.getPartyLocations();
		db.close();
		getDistances(currLatitude + "," + currLongitude, partyLocations.get(0));
	}
}
