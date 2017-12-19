<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="constants.StringConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>SongQ</title>
	</head>
	<body>
		<form action="<%= StringConstants.AUTHENTICATE_GUEST %>">
			Enter room code: <input type="text" name="<%= StringConstants.ROOM_CODE %>"> <br>
			<input type="submit">
		</form>
	</body>
</html>