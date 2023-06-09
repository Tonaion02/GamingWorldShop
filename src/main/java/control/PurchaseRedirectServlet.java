package control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import utility.BackendException;
import utility.InvalidParameters;

import dao.PurchaseDAO;
import dao.GameDAO;

import model.User;
import model.Cart;

@WebServlet("/user/PurchaseRedirectServlet")
public class PurchaseRedirectServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
       
    public PurchaseRedirectServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Check if parameters are empty
		if( ! validParameters(request, response, Arrays.asList("from")) ) {
			throw new InvalidParameters();
		}
		//Check if parameters are empty
		
		//Retrieve user from the session
		User user = (User)request.getSession().getAttribute("user");
		//Retrieve user from the session
		
		//Retrieve parameters from request
		String from = request.getParameter("from");
		String gameId = request.getParameter("gameId");
		//Retrieve parameters from request
		
		//Check if parameter from are valid
		if( ! (from.equals("personalGamePage") || from.equals("cart")) ) 
			throw new InvalidParameters();
		//Check if parameter from are valid
		
		//Check if cart is empty in the case we came from cart, or want to buy with cart
		if(from.equals("cart")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/RetrieveCartServlet?category=cart");
			dispatcher.include(request, response);
			
			Cart cart = (Cart)request.getAttribute("cartForView");
			
			if(cart == null || cart.getGames().isEmpty())
				throw new InvalidParameters();
		}
		//Check if cart is empty in the case we came from cart, or want to buy with cart
		
		//Check if game id is null in case we came from personalGamePage
		if(from.equals("personalGamePage") && gameId == null)
			throw new InvalidParameters();
		//Check if game id is null in case we came from personalGamePage
		
		//Retrieve data source from the servlet context
		DataSource ds = (DataSource)request.getServletContext().getAttribute("DataSource");
		//Retrieve data source from the servlet context
		
		//Check if the game is unlisted
		if(from.equals("personalGamePage")) {
			GameDAO gameDAO = new GameDAO(ds);
			
			boolean isUnlisted = false;
			try {
				isUnlisted = gameDAO.isUnlisted(Integer.parseInt(gameId));
			} catch (NumberFormatException | SQLException e) {
				throw new BackendException();
			}
			
			if(isUnlisted)
				throw new InvalidParameters();
		}
		//Check if the game is unlisted
		
		//Check if the game is already buyed
		if(gameId != null) {
			PurchaseDAO purchaseDAO = new PurchaseDAO(ds);
		
			boolean isBuyed = false;
			try {
				isBuyed = purchaseDAO.isBuyed(Integer.parseInt(gameId), user.getUsername());
			} catch (NumberFormatException | SQLException e) {
				throw new BackendException();
			}
		
		
			if(isBuyed)
				throw new InvalidParameters();
		}
		//Check if the game is already buyed
		
		//Decide on base of parameter what server you want to call
		String servletToCall = "PurchaseFromCartServlet?category=cart";
		if( from.equals("personalGamePage") ) {
			servletToCall = "AddPurchaseGameServlet?gameId=" + gameId;
		}
		
		request.setAttribute("servletToCall", servletToCall);
		//Decide on base of parameter what server you want to call
		
		//Forward request and response to the jsp page of the purchase
		RequestDispatcher dispatcher = request.getRequestDispatcher("Purchase.jsp");
		dispatcher.forward(request, response);
		//Forward request and response to the jsp page of the purchase
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
