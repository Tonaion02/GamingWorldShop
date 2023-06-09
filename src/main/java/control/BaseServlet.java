package control;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utility.MethodNotSupportedException;

public class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = -6071900375104162065L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new MethodNotSupportedException();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new MethodNotSupportedException();
	}
	
	private boolean isNotValidParam(String s) {
    	return s == null || s.trim().isEmpty();
    }
	
	protected boolean validParameters(HttpServletRequest request, HttpServletResponse response) {
		Enumeration <String> parameters = request.getParameterNames();
		
		while(parameters.hasMoreElements()) {
			String parameterName = parameters.nextElement();
			String parameterValue = request.getParameter(parameterName);
			if(isNotValidParam(parameterValue)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean validParameters(HttpServletRequest request, HttpServletResponse response, List<String> parameters) {
		for(String p : parameters) {
			String parameterValue = request.getParameter(p);
			if(isNotValidParam(parameterValue)) {
				return false;
			}
		}
		
		return true;
	}
	
	protected void showError(HttpServletRequest request, HttpServletResponse response, String message, String path) throws ServletException, IOException {
		request.setAttribute("logError", message);
    	RequestDispatcher rs = request.getRequestDispatcher(path);
    	
		rs.forward(request, response);
    }
}
