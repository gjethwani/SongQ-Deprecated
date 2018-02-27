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
		<input type="text" name="Playlist Name" placeholder="Enter Playlist Name" id="playlistName"><br>
		<input id="newPlaylistButton" type="radio" name = "playlistSelection" value = "createPlaylist" onclick="return toggleEnableDisable('playlistDropDown', 'playlistName');" checked><label>Create a playlist</label><br>
		<input id="oldPlaylistButton" type="radio" name = "playlistSelection" value = "choosePlaylist" onclick="return toggleEnableDisable('playlistDropDown', 'playlistName');"><label>Choose an existing playlist</label><br>
		<select name = "playlists" id = "playlistDropDown" disabled>
			<% for (int i = 0; i < playlists.size(); i++) { %>
				<option value="<%= playlists.get(i).getId() %>"><%= playlists.get(i).getName() %></option>	
			<% } %>
		</select><br>
		<input id="locationCheckBox" type="checkbox" name="location" value="enableLocation"><label>Use location services?</label><br> 
		<input id="submitButton" style = "margin-top: 20px;" type="button" value="Create New Room Code">
	</body>
	<script>
		var roomCodesDiv = document.getElementById("roomCodes");
		var noOfRoomCodes = <%= roomCodes.size() %>;
		<% if (roomCodes.size() == 0) { %>
			roomCodesDiv.appendChild(document.createTextNode("No Room Codes"));
		<% } else { 
			for (int i = 0; i < roomCodes.size(); i++) { %>
				var aRoomCodes = document.createElement("a");
				<% Database db1 = new Database(); %>
				aRoomCodes.href = "<%= StringConstants.URI %>/Requests.jsp?roomCode=" + "<%= roomCodes.get(roomCodesIndex) %>" + "&playlistId=" + "<%= db.getPlaylistId(roomCodes.get(roomCodesIndex)) %>";
				<% db1.close(); %>
				var textRoomCodes = document.createTextNode("<%= roomCodes.get(roomCodesIndex) %>");
				<% roomCodesIndex += 1; %>
				aRoomCodes.appendChild(textRoomCodes);
				roomCodesDiv.appendChild(aRoomCodes);
				roomCodesDiv.appendChild(document.createElement("br"));
		<%	}
		} %>
		
		document.getElementById("submitButton").onclick = function(event) {
			var path = "<%= StringConstants.URI %>"
			event.preventDefault();
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					window.location.href = path + "/RoomCodes.jsp";
				}
			};
			var locationChecked = document.getElementById("locationCheckBox").checked;
			console.log(locationChecked);
			//console.log(document.getElementById("locationCheckBox"));
			var url;
			if (document.getElementById("newPlaylistButton").checked) {
				url = path + "/CreateRoomCode?&owner=" + "<%= userId %>" + "&playlistName=" + document.getElementById("playlistName").value;
			} else if (document.getElementById("oldPlaylistButton").checked) {
				var dropDown = document.getElementById("playlistDropDown");
				url =  path + "/CreateRoomCode?&owner=" + '<%= userId %>' + "&playlistId=" + dropDown.options[dropDown.selectedIndex].value + "&existingPlaylistName=" + dropDown.options[dropDown.selectedIndex].text;
			}
			if (locationChecked == true) {
				if ("geolocation" in navigator) {
					navigator.geolocation.getCurrentPosition(function(position) {
						console.log(position);
						url += "&latitude=";
						url += position.coords.latitude;
						url += "&longitude=";
						url += position.coords.longitude;
						console.log(url);
						xhttp.open("GET", url , true);
						xhttp.send();	
					});
				} else {
					 console.log("Location unavailable");
				}
			}
			//console.log("here " + url);
		}

		
		function toggleEnableDisable(id1, id2) {
			var element1 = document.getElementById(id1);
			var element2 = document.getElementById(id2);
			if (element1.disabled === true) {
				element1.disabled = false;
				element2.disabled = true;
			} else {
				element1.disabled = true;
				element2.disabled = false;
			}
		}
	</script>
</html>