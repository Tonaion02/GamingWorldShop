package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.sql.DataSource;

import model.Category;
import model.Game;
import model.Game.Pegi;
import model.Game.State;

public class GameDAO extends BaseDAO {

	public GameDAO(DataSource ds) {
		super(ds);
	}
	
	public int insertGame  (int price, String name, String description,
							String state, String shortDescription,
							String releaseDate, String pegi) throws SQLException {
		int id = 0;
		
		String query = "INSERT into game (price, name, description, state,"
				+"shortDescription, releaseDate, pegi) values ( ?, ?, ?, ?, ?, ?, ?)";
		
		// Retrieve connection and make prepared statement with tag to return generated keys
		try (Connection conn = ds.getConnection();PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); ) {
		// Retrieve connection and make prepared statement with tag to return generated keys	

			//Set prepared statement values
			ps.setInt(1, price);
			ps.setString(2, name);
			ps.setString(3, description);
			ps.setString(4, State.valueOf(state.toUpperCase()).getValue());
			ps.setString(5, shortDescription);
			ps.setString(6, releaseDate);
			ps.setInt(7, Pegi.valueOf(pegi.toUpperCase()).getValue());
			//Set prepared statement values

			//Insert user into database
			ps.execute();
			//Insert user into database
			
			//get key from result set
			try (ResultSet keys = ps.getGeneratedKeys()) {
				keys.next();
		        id = keys.getInt(1);
		    }
			//get key from result set
		}
		
		return id;
	}
	
	public List<Game> retrieveGames(List<Category> categories, int maxPrice, int pegi) throws SQLException {
		List<Game> games = new ArrayList<>();
		
		PreparedStatement ps = null;
		
		//Retrieve connection
		try (Connection conn = ds.getConnection()) {
		//Retrieve connection
			
			//Construct query string
			StringBuilder builder = new StringBuilder("");
			builder.append("(");
			for(int i=0;i<categories.size();i++) {
				if(i == categories.size() - 1) 
					builder.append("?");
				else
					builder.append("?,");
			}
			builder.append(")");
						
			String categoriesToSearch = builder.toString();
			//Construct query string
			
			//Construct query
			String query = "SELECT DISTINCT * FROM Game as G, Belongs as B, Category as C "
							+ "WHERE G.id = B.gameId AND C.name = B.categoryName AND C.name in "
							+ categoriesToSearch
							+ "AND G.price <= ? AND "
							+ "G.pegi <= ?";
			
			ps = conn.prepareStatement(query);
			
			int i = 1;
			for(Category c : categories) {
				ps.setString(i, c.getName());
				
				i++;
			}
			
			ps.setInt(i, maxPrice);
			i++;
			ps.setInt(i, pegi);
			i++;
			//Construct query
			
			//Retrieve the categories from database
			ResultSet rs = ps.executeQuery();
			//Retrieve the categories from database
			
			//Create the list of Game
			while(rs.next()) {
				Game game = new Game();
				
				game.setId(rs.getInt("id"));
				game.setName(rs.getString("name"));
				game.setDescription(rs.getString("description"));
				game.setPrice(rs.getInt("price"));
				game.setShortDescription(rs.getString("shortDescription"));
				game.setReleaseDate("realeseDate");
				Pegi pegi1 = Pegi.valueOf("PEGI_" + rs.getString("pegi"));
				game.setPegi(pegi1);
				State state = State.valueOf(rs.getString("state").toUpperCase());
				game.setState(state);
				
				games.add(game);
			}
			//Create the list of Game		
		}
		finally {
			if(ps != null)
				ps.close();
		}

		return games;
	}
	
	public Game retrieveGame(int gameId) throws SQLException {
		Game game = null;
		
		PreparedStatement ps = null;
		
		//Retrieve connection
		try (Connection conn = ds.getConnection()) {
		//Retrieve connection
			
			//Construct query
			String query = "SELECT * FROM game WHERE id = ?";
			
			ps = conn.prepareStatement(query);
			
			ps.setInt(1, gameId);
			//Construct query
			
			//Retrieve game from the database
			ResultSet rs = ps.executeQuery();
			//Retrieve game from the database
			
			//Analize result set
			if(rs.next()) {
				game = new Game();
				
				game.setName(rs.getString("name"));
				game.setPrice(rs.getInt("price"));
				game.setDescription(rs.getString("description"));
				game.setShortDescription(rs.getString("shortDescription"));
				game.setState(State.valueOf(rs.getString("state").toUpperCase()));
				game.setId(rs.getInt("id"));
				game.setPegi(Pegi.valueOf("PEGI_" + rs.getString("pegi")));
			}
			//Analize result set
			
		} finally {
			if(ps != null)
				ps.close();
		}
		
		return game;
	}
	
	public int retrieveMaxPriceGame() throws SQLException {
		int maxPrice = 0;
		
		String query = "SELECT MAX(price) as max FROM Game";
		
		//Retrieve connection and make prepared statement
		try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(query);) {
		//Retrieve connection and make prepared statement
			
			//Retrieve the categories from database
			ResultSet rs = ps.executeQuery();
			//Retrieve the categories from database
			
			//Create the list of Game
			if(rs.next())
				maxPrice = rs.getInt("max");
			//Create the list of Game
		}
				
		return maxPrice;
	}
}
