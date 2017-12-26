<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = "database.Database"
    import = "java.util.List"
    import = "constants.StringConstants"%>
<% 
   String username = (String) request.getSession().getAttribute("username");
   Database db = new Database();
   List<String> roomCodes = db.getRoomCodes(username);
   int roomCodesIndex = 0;%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<h2>Room Codes: </h1>
		<div id = "roomCodes">
		</div>
		<input type="text" name="Playlist Name" placeholder="Enter Playlist Name" id="playlistName">
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
			xhttp.open("GET", path + "/CreateRoomCode?&owner=" + '<%= username %>' + "&playlistName=" + document.getElementById("playlistName").value, true);
			xhttp.send();
		}
	</script>
</html>