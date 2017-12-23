package objects;

import java.util.Arrays;
import java.util.List;

public class Request {
	private int requestId;
	private String roomCode;
	private String owner;
	private String songId;
	private String songName;
	private List<String> artists;
	private String album;
	private boolean serviced;
	private boolean accepted;
	private boolean rejected;
	public String getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSongId() {
		return songId;
	}
	public void setSongId(String songId) {
		this.songId = songId;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public List<String> getArtists() {
		return artists;
	}
	public void setArtists(String artists) {
		this.artists = Arrays.asList(artists.split("\\s*,\\s*")); //https://stackoverflow.com/questions/7488643/how-to-convert-comma-separated-string-to-arraylist
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public boolean isServiced() {
		return serviced;
	}
	public void setServiced(boolean serviced) {
		this.serviced = serviced;
	}
	public boolean isAccepted() {
		return accepted;
	}
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}
	public boolean isRejected() {
		return rejected;
	}
	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
}
