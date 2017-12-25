<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<form action="VerifyHostRegistration">
			First Name: <input type="text" name="firstName"> <br>
			Last Name: <input type="text" name="lastName"> <br>
			Username: <input type="text" name="username"> <br>
			Password: <input type="password" name="password"> <br>
			Confirm Password: <input type="password" name="confirmPassword"> <br>
			<input type="submit">
		</form>
		<p style="color: red;" id="message"></p>
		<% String message = (String) request.getAttribute("registrationMessage"); 
		   if (message == null) { %> 
		   		<script>console.log("1");document.getElementById("message").innerHTML = "";</script>
		   <% } else { %>
		   		<script>console.log("2");document.getElementById("message").innerHTML = "<%= message %>";</script>
		   <% } %>
	</body>
</html>