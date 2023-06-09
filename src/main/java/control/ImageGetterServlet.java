package control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

import dao.ImageDAO;
import model.Image;

@WebServlet("/ImageGetterServlet")
public class ImageGetterServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
      
    public ImageGetterServlet() {
        super();
    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String id = request.getParameter("id");
		
		ImageDAO imageDAO = new ImageDAO((DataSource)getServletContext().getAttribute("DataSource"));
		Image image = null;
		try {
			image = imageDAO.getImageFromID(id);
		} catch (SQLException e) {
			response.sendError(404, "Image Not Found");
			return;
		}
		
		response.setContentType("image/png");
		try(ServletOutputStream out = response.getOutputStream()){
			out.write(image.getBytes());
		}
		catch(IOException e) {
			response.sendError(404, "Image Not Found");
		}
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    	
    	doGet(request, response);
	}
}
