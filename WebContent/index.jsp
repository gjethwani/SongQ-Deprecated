<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="constants.StringConstants"%>
 <%//https://www.w3schools.com/colors/colors_palettes.asp 2016 left %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
		<link rel="stylesheet" type="text/css" href="index.css">
	</head>
	<body>
		<div id="navBar">
			<h2 id="navBarTitle">SongQ</h2>
			<div>
				<form action="ChooseType">
					<p>Get Started</p>
					<input type="submit" name="<%= StringConstants.TYPE %>" value="Host">
					<input type="submit" name="<%= StringConstants.TYPE %>"  value="Guest">
				</form>
			</div>
		</div>
		<h2>I am a...</h2> 
		<form action="ChooseType">
			<input type="submit" name="<%= StringConstants.TYPE %>" value="Host">
			<input type="submit" name="<%= StringConstants.TYPE %>"  value="Guest">
		</form>
	</body>
</html>