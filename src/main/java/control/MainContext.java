package control;

import java.sql.SQLException;
import java.util.List;
import model.Category;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import dao.GameDAO;
import dao.CategoryDAO;

@WebListener
public class MainContext implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		
		//Init data source and add data source to the ServletContext
		DataSource ds = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			ds = (DataSource) envCtx.lookup("jdbc/storage");

		} catch (NamingException e) {
			return;
		}		
		
		context.setAttribute("DataSource", ds);
		//Init data source and add data source to the ServletContext
		
		//Retrieve max price from Games(only listed)
		GameDAO gameDAO = new GameDAO(ds);
		
		int maxPrice = 0;
		try {
			maxPrice = gameDAO.retrieveMaxPriceGame(false);
		} catch (SQLException e) {
			return;
		}
		
		context.setAttribute("maxPrice", maxPrice);
		//Retrieve max price from Games(only listed)
		
		//Retrieve max price from Games(even unlisted games)
		int maxPriceUnlisted = 0;
		try {
			maxPriceUnlisted = gameDAO.retrieveMaxPriceGame(true);
		} catch (SQLException e) {
			return;
		}
		
		context.setAttribute("maxPriceUnlisted", maxPriceUnlisted);
		//Retrieve max price from Games(even unlisted games)
				
		//retrieve all categories
		CategoryDAO categoryDAO = new CategoryDAO(ds);
		List<Category> categoryList = null;
		try {
			categoryList = categoryDAO.retrieveAllCategories();
		} catch (SQLException e) {
			return;
		}
		context.setAttribute("categories", categoryList);
		//retrieve all categories
	}
}
