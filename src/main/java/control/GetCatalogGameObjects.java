package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import model.Game;


@WebServlet("/GetCatalogGameObjects")
public class GetCatalogGameObjects extends BaseServlet {
	
	public GetCatalogGameObjects() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		response.setContentType("application/json");
		
		//get the IDs of the games that have to be shown through the SearchGames servlet
		RequestDispatcher dispatcher = request.getRequestDispatcher("/SearchGames");
		dispatcher.include(request, response);
		//get the IDs of the games that have to be shown through the SearchGames servlet
		
		//convert game objects to json and send response
		List<Game> games = (List<Game>)request.getAttribute("games");
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("games", games);
		jsonResponse.put("gamesCount", request.getAttribute("numberOfGames"));
		PrintWriter out = response.getWriter();
		out.print(jsonResponse.toString());
		//convert game objects to json and send response
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static final long serialVersionUID = 7039183803624166726L;
}
