<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
	</body>
			<script>
			var form = document.getElementById("tracksForm").onsubmit = function(event) {
				event.preventDefault();
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					if (this.readyState == 4 && this.status == 200) {
				   		console.log(this.response);
					}
				};
				var path = "/"+window.location.pathname.split("/")[1];
				xhttp.open("GET", path + "/SearchTracks?query=" + document.getElementById("trackSearch").value, true);
				xhttp.send();
			}
			/*function searchForTracks(event) {
				event.preventDefault();
				var xhttp = new XMLHttpRequest();
				xhttp.onreadystatechange = function() {
					//if (this.readyState == 4 && this.status == 200) {
				   		console.log(xhttp.repsonseText);
					//}
				};
				var path = "/"+window.location.pathname.split("/")[1];
				xhttp.open("GET", path + "/SearchTracks?query=" + document.getElementById("trackSearch").value, true);
				xhttp.send();
			}*/
		</script>
</html>