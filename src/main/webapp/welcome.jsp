<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang = en>
	<head>
		<meta charset="ISO-8859-1">
		<title>Welcome to Gaming World!</title>
	</head>
	
	<body>
		
		<form action="Content/JSP/UploadImmagine.jsp">
			<input type="submit" name="summitta" value="Test upload immagine"> <br>
		</form>
		<form action="/GamingWorldShop/ImageGetterServlet?id=aa">
			<input type="submit" name="summitta" value="Test visualizzazione immagine"> <br>
		</form>
	</body>
</html>