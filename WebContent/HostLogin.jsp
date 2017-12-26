<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import = "constants.StringConstants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<a href="HostRegister.jsp">Don't have an account? Register here</a>
		<form action = "<%= StringConstants.AUTHENTICATE_HOST %>" method="POST">
			Username: <input type="text" name="<%= StringConstants.USERNAME %>"> <br>
			Password: <input type="password" name="<%= StringConstants.PASSWORD %>"> <br>
			<input type="submit">
 		</form>
 		<p style="color: red;" id="message"></p>
		<% String message = (String) request.getAttribute("hostLoginMessage"); 
		   if (message == null) { %> 
		   		<script>console.log("1");document.getElementById("message").innerHTML = "";</script>
		   <% } else { %>
		   		<script>console.log("2");document.getElementById("message").innerHTML = "<%= message %>";</script>
		   <% } %>
	</body>
</html>