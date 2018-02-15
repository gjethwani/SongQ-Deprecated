<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = "database.Database"
    import = "java.util.List"
    import = "constants.StringConstants"
    import = "java.util.Vector"
    import = "com.wrapper.spotify.models.SimplePlaylist" %>
<% 
   String userId = (String) request.getSession().getAttribute("userId");
   Database db = new Database();
   List<String> roomCodes = db.getRoomCodes(userId);
   int roomCodesIndex = 0;
   Vector<SimplePlaylist> playlists = (Vector<SimplePlaylist>) request.getSession().getAttribute("playlists");%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<h2>Room Codes: </h1>
		<div id = "roomCodes" style="margin-bottom: 20px">
		</div>
		<input id="newPlaylistButton" type="radio" name = "playlistSelection" value = "createPlaylist" onclick="return disable('playlistDropDown','playlistName');" checked><label>Create a playlist</label><br>
		<input type="text" name="Playlist Name" placeholder="Enter Playlist Name" id="playlistName"><br>
		<input id="oldPlaylistButton" type="radio" name = "playlistSelection" value = "choosePlaylist" onclick="return disable('playlistName','playlistDropDown');"><label>Choose an existing playlist</label><br>
		<select name = "playlists" id = "playlistDropDown" disabled>
			<% for (int i = 0; i < playlists.size(); i++) { %>
				<option value="<%= playlists.get(i).getId() %>"><%= playlists.get(i).getName() %></option>	
			<% } %>
		</select><br>
		<input style = "margin-top: 20px;" type="button" value="Create New Room Code" onclick="return createRoomCode();">
	</body>
	<script>
		var roomCodesDiv = document.getElementById("roomCodes");
		var noOfRoomCodes = <%= roomCodes.size() %>;
		<% if (roomCodes.size() == 0) { %>
			console.log("here1");
			roomCodesDiv.appendChild(document.createTextNode("No Room Codes"));
		<% } else { 
			for (int i = 0; i < roomCodes.size(); i++) { %>
				var aRoomCodes = document.createElement("a");
				aRoomCodes.href = "<%= StringConstants.URI %>/Requests.jsp?roomCode=" + "<%= roomCodes.get(roomCodesIndex) %>";
				var textRoomCodes = document.createTextNode("<%= roomCodes.get(roomCodesIndex) %>");
				<% roomCodesIndex += 1; %>
				aRoomCodes.appendChild(textRoomCodes);
				roomCodesDiv.appendChild(aRoomCodes);
				roomCodesDiv.appendChild(document.createElement("br"));
		<%	}
		} %>
		function createRoomCode() {
			var path = "<%= StringConstants.URI %>"
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					window.location.href = path + "/RoomCodes.jsp";
				}
			};
			if (document.getElementById("newPlaylistButton").checked) {
				xhttp.open("GET", path + "/CreateRoomCode?&owner=" + '<%= userId %>' + "&playlistName=" + document.getElementById("playlistName").value, true);
			} else if (document.getElementById("oldPlaylistButton").checked) {
				var dropDown = document.getElementById("playlistDropDown");
				xhttp.open("GET", path + "/CreateRoomCode?&owner=" + '<%= userId %>' + "&playlistId=" + dropDown.options[dropDown.selectedIndex].value, true);
			}
			xhttp.send();
		}
		
		function disable(id1, id2) {
			document.getElementById(id1).disabled = true;
			document.getElementById(id2).disabled = false;
		}
	</script>
</html>