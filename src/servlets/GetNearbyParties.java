package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
	
	private static List<Integer> getDestinations(String JSON) {
		JSONParser parser = new JSONParser();
		List<Integer> approvedDestinations = new ArrayList<Integer>();
		try {
			JSONObject obj = (JSONObject) parser.parse(JSON);
			JSONArray rows = (JSONArray) obj.get("rows");
			JSONObject elementsBuffer = (JSONObject) rows.get(0);
			JSONArray elements = (JSONArray) elementsBuffer.get("elements");
			for (int i = 0; i < elements.size(); i++) {
				JSONObject distanceBuffer = (JSONObject) elements.get(i);
				JSONObject distance = (JSONObject) distanceBuffer.get("distance");
				long value = (long) distance.get("value");
				System.out.println(value);
				if (value <= 50) {
					System.out.println("here");
					approvedDestinations.add(i);
				}
			}
		} catch (org.json.simple.parser.ParseException pe) {
			pe.printStackTrace();
		}
		return approvedDestinations;
	}
	
	private static List<Party> getDistances(String currLocation, List<Party> parties) throws IOException {
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
		url += currLocation;
		url += "&destinations=";
		for (int i = 0; i < parties.size() - 1; i++) {
			url += parties.get(i).getLatitude() + "," + parties.get(i).getLongitude();
			url += "|";
		}
		url += parties.get(parties.size()-1).getLatitude() + "," + parties.get(parties.size()-1).getLongitude();
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
			List<Integer> approvedDestinations = getDestinations(response.toString());
			List<Party> toReturn = new ArrayList<Party>();
			for (int i = 0; i < approvedDestinations.size(); i++) {
				System.out.println(parties.get(i).getRoomCode());
				toReturn.add(parties.get(i));
			}
			return toReturn;
		} else {
			System.out.println("GET request not worked");
		}
		return null;
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String currLatitude = (String) request.getParameter("latitude");
		String currLongitude = (String) request.getParameter("longitude");
		Database db = new Database();
		List<Party> partyLocations = db.getPartyLocations((Float.valueOf(currLatitude)).floatValue(),(Float.valueOf(currLongitude)).floatValue());
		db.close();
		List<Party> approvedParties = getDistances(currLatitude + "," + currLongitude, partyLocations);
		System.out.println(approvedParties.size());
		request.getSession().setAttribute("approvedParties", approvedParties);
	}
}
