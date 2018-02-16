<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = "constants.StringConstants"
    import = "database.Database" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<form id="tracksForm">
			Search for tracks: <input type="text" name="trackSearch" id="trackSearch">
			<input type="submit">
		</form>
		<p id = "loading"></p>
		<p id = "message" style = "color: red;"></p>
		<table id = "resultsTable">
		</table>
	</body>
			<script>
			function requestThisSong(id, name, artists, album) {
				if (artists[artists.length-1] === ',') {
					artists = artists.substring(0, artists.length - 1);
				}
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						var clickedButton = document.getElementById(id);
						clickedButton.style.display = "none";
						var requestedText = document.getElementById("requested" + id);
						requestedText.style.display = "block";
					}
				};
				var path = "<%= StringConstants.URI %>";
				<% Database db = new Database(); 
				   String roomCode = (String) request.getSession().getAttribute(StringConstants.ROOM_CODE);
				   String owner = db.getOwner(roomCode); 
				   db.close(); %>
				xhttp.open("GET", path + "/AddRequest?roomCode=" + '<%= roomCode %>' + "&owner=" + '<%= owner %>' + "&songId=" + id + "&songName=" + name + "&artists=" + artists + "&album=" + album, true);
				xhttp.send();
			}

			var form = document.getElementById("tracksForm").onsubmit = function(event) {
				document.getElementById("loading").innerHTML = "Loading...";
				document.getElementById("message").innerHTML = "";
				var ids = [];
				var names = [];
				var albums = [];
				var artistsArray = [];
				event.preventDefault();
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						document.getElementById("loading").innerHTML = "";
						document.getElementById("resultsTable").innerHTML = "";
				   		var response = this.response;
				   		var individualTracks = response.split(";");
				   		if (individualTracks.length === 0 || individualTracks == null) {
				   			document.getElementById("message").innerHTML = "No Results";
				   		}
				   		for (var i = 0; i < individualTracks.length-1; i++) {
				   			var song = individualTracks[i].split(",");
				   			var id = song[0];
				   			var name = song[1];
				   			var album = song[2];
				   			var artists = [];
				   			for (var j = 3; j < song.length; j++) {
				   				artists.push(song[j]);
				   			}
				   			ids.push(id);
				   			names.push(name);
				   			albums.push(album);
				   			artistsArray.push(artists.join());
				   			var nameTag = document.createElement("p");
				   			nameTag.innerHTML = "Song: " + name;
				   			var albumTag = document.createElement("p");
				   			albumTag.innerHTML = "Album: " + album;
				   			var artistTag = document.createElement("p");
							var artistText = "";
				   			if (artists.length > 2) {
				   				for (var j = 0; j < artists.length-2; j++) {
				   					artistText += artists[j];
				   					artistText += ", ";
				   				}
				   				artistText += artists[artists.length-2];
				   			} else {
				   				artistText += artists[0];
				   			}
				   			artistTag.innerHTML = "Artist(s): " + artistText;
				   			var button = document.createElement("input");
				   			button.type = "button";
				   			button.id = id;
				   			button.addEventListener("click", function() {
				   				var index = this.classList.item(0);
				   				requestThisSong(ids[index], names[index], artistsArray[index], albums[index]);
				   			});
				   			button.value = "Request " + name;
				   			button.classList.add(i);
				   			var requestedText = document.createElement("p");
				   			requestedText.innerHTML = "Requested!";
				   			requestedText.id = "requested" + id;
				   			requestedText.style.display = "none";
				   			var tableCell = document.createElement("tr");
				   			tableCell.appendChild(nameTag);
				   			tableCell.appendChild(artistTag);
				   			tableCell.appendChild(albumTag);
				   			tableCell.appendChild(button);
				   			tableCell.appendChild(requestedText);
				   			document.getElementById("resultsTable").appendChild(tableCell);
				   		}
					} else if (this.readyState == 4 && this.status != 200) {
						document.getElementById("message").innerHTML = "Error";
					}
				};
				//var path = "/"+window.location.pathname.split("/")[1];
				var path = "<%= StringConstants.URI %>";
				xhttp.open("GET", path + "/SearchTracks?query=" + document.getElementById("trackSearch").value, true);
				xhttp.setRequestHeader('Access-Control-Allow-Origin','*');
				xhttp.send();
			}
		</script>
</html>