package objects;

public class Party {
	private String latitude;
	private String longitude;
	private String roomCode;
	public Party(String latitude, String longitude, String roomCode) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.roomCode = roomCode;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
}
