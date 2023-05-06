package control;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import model.User;
import model.UserDAO;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@WebServlet("/common/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = -8697651045570564505L;

	public LoginServlet() {
        super();
    }
    
    private boolean isNotValidParam(String s) {
    	return s == null || s.trim().isEmpty();
    }
    
    private void errorLogin(HttpServletRequest request, HttpServletResponse response) {
    	RequestDispatcher rs = request.getRequestDispatcher("/Content/common/Login.jsp");
    	try {
			rs.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		} 
    }

//    private String toHash(String password) throws NoSuchAlgorithmException {
//		String hashString = null;
//		//convert password to hashed version
//		java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-512");
//		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
//		hashString = "";
//		
//		for (int i = 0; i < hash.length; i++) {
//			hashString += Integer.toHexString((hash[i] & 0xFF) | 0x100).toLowerCase().substring(1, 3);
//		}
//		//convert password to hashed version
//		return hashString;
//	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession().getAttribute("user") != null) {
			response.sendRedirect(request.getContextPath());
		}
		else {
			request.setAttribute("logError", null);
			errorLogin(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Retrieve username and password from form and check if it is empty
		String username = request.getParameter("username");
		if(isNotValidParam(username)) {
			request.setAttribute("logError","Missing Username");
			errorLogin(request, response);
			return;
		}
		
		String password = request.getParameter("password");

		if(isNotValidParam(password)) {
			request.setAttribute("logError","Missing Password");
			errorLogin(request, response);
			return;
		}
		//Retrieve username and password from form and check if it is empty
		
		
		//Retrieve from the database the user from username
		
		//Hash the password
		String hashPassword = null;
		try {
			hashPassword = Hasher.toHash(password);
		} catch (NoSuchAlgorithmException e) {
			request.setAttribute("logError","Fatal error");
			errorLogin(request, response);
			return;
		}
		//Hash the password

		
		UserDAO userDAO = new UserDAO((DataSource)getServletContext().getAttribute("DataSource"));
		User user = null;
		try {
			user = userDAO.getUserFromUsername(username);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//Retrieve from the database the user from username
		
		//Check if found a user in database
		if(user == null) {
			request.setAttribute("logError", "Wrong Username");
			errorLogin(request, response);
			return;
		}
		//Check if found a user in database
		
		//Check if password matches
		if(! user.getPassword().equals(hashPassword)) {
			request.setAttribute("logError", "Wrong Password");
			errorLogin(request, response);
			return;
		}
		//Check if password matches
		
		//Add attribute user in the session(to remember the login)
		request.getSession().setAttribute("user", user);
		//Add attribute user in the session(to remember the login)
		
		response.sendRedirect("/GamingWorldShop/Content/user/PersonalArea.jsp");	
	}
}
