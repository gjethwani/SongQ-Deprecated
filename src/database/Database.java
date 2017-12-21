package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SongQ?user=root&password=damansara75&useSSL=false");			
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
}
