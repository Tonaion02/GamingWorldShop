<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="model.User" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Personal Area</title>
	</head>
	<body>
		<%User user = (User)session.getAttribute("user");
		String username = user.getUsername();
		%>
	
		<h1>Welcome in you personal area: <%=username %></h1>
	</body>
</html>