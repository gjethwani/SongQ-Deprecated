<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="constants.StringConstants"
    import="objects.Party"
    import="java.util.ArrayList"
	import="java.util.List" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<form action="<%= StringConstants.AUTHENTICATE_GUEST %>">
			Enter room code: <input type="text" name="<%= StringConstants.ROOM_CODE %>"> <br>
			<p>Or pick a party near you</p></br>
			<select id="locationDropdown">
			</select>
			<input type="submit" onclick="return loading();">
		</form>
		<div id="loading"></div>
		<div id="message" style="color: red"></div>
	</body>
	<script>
	
		window.onload = function() {
			if ("geolocation" in navigator) {
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
						var locationDropdown = document.getElementById("locationDropdown");
						console.log("hello");
						<% if (request.getSession().getAttribute("approvedParties") != null) {
							 List<Party> approvedParties = (List<Party>) request.getSession().getAttribute("approvedParties"); 
							   for (int i = 0; i < approvedParties.size(); i++) { %>
							   		var optionTag = document.createElement("option");
							   		console.log("<%= approvedParties.get(i).getRoomCode() %>");
							   		optionTag.value = "<%= approvedParties.get(i).getRoomCode() %>";
							   		optionTag.text = "<%= approvedParties.get(i).getRoomCode() %>";
							   		locationDropdown.appendChild(optionTag);
						<%	   }
						} %>
						//} else { 
						//	System.out.println("here");
						//} %>
					}
				};
				var url = "<%= StringConstants.URI %>" + "/GetNearbyParties?"; 
				navigator.geolocation.getCurrentPosition(function(position) {
					console.log(position);
					url += "latitude=";
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
	
		function loading() {
			document.getElementById("loading").innerHTML = "Loading..."; 
		}
		<% String message = (String) request.getAttribute("message"); 
		if (message == null) { %>
			document.getElementById("message").innerHTML = "";
		<% } else { %>
			document.getElementById("message").innerHTML = "<%= message %>";
		<% } %>
	</script>
</html>