package objects;

public class Party {
	private String latitude;
	private String longitude;
	private String roomCode;
	private String partyName;
	public Party(String latitude, String longitude, String roomCode, String partyName) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.roomCode = roomCode;
		this.partyName = partyName;
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
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
}
