package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import constants.StringConstants;
import objects.Request;

public class Database {
	Connection conn;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;
	public Database() {
		conn = null;
		st = null;
		ps = null;
		rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(StringConstants.DB_CLOUD);			
		} catch (SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println ("ClassNotFoundException: " + cnfe.getMessage());
		}
	}
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean authenticateRoomCode(String roomCode) {
		String query = String.format("SELECT * FROM %s WHERE roomCode='%s'","Playlists", roomCode);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				return true;
			}
			return false;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getOwner(String roomCode) {
		String query = String.format("SELECT %s FROM %s WHERE roomCode='%s'","owner", "Playlists", roomCode);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				return rs.getString("owner");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return "null";
	}
	
	public boolean addRequest(String roomCode, String owner, String songId, String songName, String artists, String album, boolean serviced) {
		String query = String.format("INSERT INTO Requests (roomCode, owner, songId, songName, artists, album, serviced) VALUES (?,?,?,?,?,?,?)");
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, roomCode);
			st.setString(2, owner);
			st.setString(3, songId);
			st.setString(4, songName);
			st.setString(5, artists);
			st.setString(6, album);
			st.setBoolean(7, serviced);
			int result = st.executeUpdate();
			if (result > 0)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public int authenticateHost(String username, String password) {
		String query = String.format("SELECT %s FROM %s WHERE username='%s'","password", "Users", username);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.isBeforeFirst() ) { 
				return 0; //no user
			}
			while (rs.next()) {
				if (rs.getString("password").equals(password)) {
					return 1; //successful
				} else {
					return -1; //failed
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return -2; //error
		}
		return -2; //error
	}
	
	public List<String> getRoomCodes(String username) {
		String query = String.format("SELECT %s FROM %s WHERE owner='%s' ORDER BY identifier ASC","roomCode", "Playlists", username);
		List<String> roomCodes = new ArrayList<String>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				roomCodes.add(rs.getString("roomCode"));
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return roomCodes;
	}
	
	public List<Request> getOutstandingRequests(String roomCode) {
		String query = String.format("SELECT * FROM %s WHERE roomCode='%s'", "Requests", roomCode);
		List<Request> requests = new ArrayList<Request>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				Request request = new Request(); 
				request.setRequestId(rs.getInt("requestId"));
				request.setRoomCode(rs.getString("roomCode"));
				request.setOwner(rs.getString("owner"));
				request.setSongId(rs.getString("songId"));
				request.setSongName(rs.getString("songName"));
				request.setArtists(rs.getString("artists"));
				request.setAlbum(rs.getString("album"));
				request.setServiced(rs.getBoolean("serviced"));
				request.setAccepted(rs.getBoolean("accepted"));
				request.setRejected(rs.getBoolean("rejected"));
				requests.add(request);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return requests;
	}
	
	public void requestServiced(int requestId, String acceptedRejected) {
		String query = String.format("UPDATE %s SET serviced=true, %s=true WHERE requestId='%s'", "Requests", acceptedRejected, requestId);
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void registerUser(String id, String username) {
		String query = String.format("INSERT INTO Users VALUES (?,?)");
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, id);
			st.setString(2, username);
			st.executeUpdate();
		}
		catch (MySQLIntegrityConstraintViolationException e) {
			System.out.println("User Exists");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createRoomCode(String roomCode, String playlistId, String owner) {
		String query = String.format("INSERT INTO Playlists (roomCode, playlistId, owner) VALUES (?,?,?)");
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, roomCode);
			st.setString(2, playlistId);
			st.setString(3, owner);
			st.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createRoomCodeWithLocation(String roomCode, String playlistId, String owner, String latitude, String longitude) {
		String query = String.format("INSERT INTO Playlists (roomCode, playlistId, owner, latitude, longitude) VALUES (?,?,?,?,?)");
		try {
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, roomCode);
			st.setString(2, playlistId);
			st.setString(3, owner);
			st.setString(4, latitude);
			st.setString(5, longitude);
			st.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean roomCodeExists(String roomCode) {
		String query = String.format("SELECT * FROM %s WHERE roomCode='%s'", "Playlists", roomCode);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (!rs.isBeforeFirst() ) { 
				return false; //no user
			} else {
				return true;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public String getPlaylistId(String roomCode) {
		String query = String.format("SELECT %s FROM %s WHERE roomCode='%s'", "playlistId", "Playlists", roomCode);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				return rs.getString("playlistId");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return "null";
	}
}
