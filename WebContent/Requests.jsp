<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = "database.Database" 
    import = "java.util.List" 
    import = "objects.Request" %>
<%	Database db = new Database();
	String roomCode = (String) request.getParameter("roomCode");
	List<Request> outstandingRequests = db.getOutstandingRequests(roomCode);
	db.close(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<table id="requests">
		</table>
	</body>
	<script>
		function approveRejectRequest(requestId, approvedRejected, songId) {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
				}
			};
			var path = "/"+window.location.pathname.split("/")[1];
			xhttp.open("GET", path + "/ApproveRejectRequest?requestId=" + requestId + "&approvedRejected=" + approvedRejected + "&songId=" + songId, true);
			xhttp.send();
		}

		var requestsTable = document.getElementById("requests");
		<% for (int i = 0; i < outstandingRequests.size(); i++) { //http://codippa.com/how-to-iterate-over-list-of-objects-jstl/
			Request currRequest = outstandingRequests.get(i); %>
			var trRequest = document.createElement("tr");
			var songNameText = document.createTextNode("Song: " + "<%= currRequest.getSongName() %>");
			var trSong = document.createElement("tr");
			trSong.appendChild(songNameText);
			var artistsText = document.createTextNode("Artists: " + "<%= String.join(", ", currRequest.getArtists()) %>");
			var trArtists = document.createElement("tr");
			trArtists.appendChild(artistsText);
			var albumText = document.createTextNode("Album: " + "<%= currRequest.getAlbum() %>");
			var trAlbums = document.createElement("tr");
			
			var approveButton = document.createElement("input");
			approveButton.type = "button";
			//approveButton.id = <%= currRequest.getSongId() %>;
			approveButton.value = "Accept " + "<%= currRequest.getSongName() %>";
			approveButton.addEventListener("click", function() {
   				approveRejectRequest(<%= currRequest.getRequestId() %>, "accepted", "<%= currRequest.getSongId() %>");
   			});
			
			var rejectButton = document.createElement("input");
			rejectButton.type = "button";
			rejectButton.value = "Reject " + "<%= currRequest.getSongName() %>";
			rejectButton.addEventListener("click", function() {
   				approveRejectRequest(<%= currRequest.getRequestId() %>, "rejected", "<%= currRequest.getSongId() %>");
   			});
			
			trAlbums.appendChild(albumText);
			trRequest.appendChild(trSong);
			trRequest.appendChild(trArtists);
			trRequest.appendChild(trAlbums);
			trRequest.appendChild(approveButton);
			trRequest.appendChild(rejectButton);
			requestsTable.appendChild(trRequest);
			requestsTable.appendChild(document.createElement("br")); //line break
		<% } %>
	</script>
</html>