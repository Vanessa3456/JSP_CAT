package net.javaguides.usermanagement.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.javaguides.usermanagement.model.UserDb;
import net.javaguides.usernamanagemt.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDb userDb;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        this.userDb=new UserDb();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getServletPath();
		switch(action) {
		case "/new":
			showNewForm(request,response);			
			break;
		case "/insert":
			try {
				insertUser(request,response);			
						} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/edit":
			try {
				showEditForm(request, response);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "/update":
			try {
				updateUser(request,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			default:
			try {
				listUser(request,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
				break;
		}
	}
	
	private void showNewForm(HttpServletRequest request,ServletResponse response) throws ServletException, IOException{
		RequestDispatcher dispatcher=request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request,response);
	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
		String f_name=request.getParameter("f_name");
		String l_name=request.getParameter("l_name");
		String email=request.getParameter("email");
		User newUser=new User(f_name,l_name,email);
		userDb.insertUser(newUser);
		response.sendRedirect("list");
		}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,SQLException{
	    int id=Integer.parseInt(request.getParameter("id"));
	    userDb.deleteUser(id);
	    response.sendRedirect("list");	    
	}
	
	private void showEditForm(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException, SQLException{
	    int id=Integer.parseInt(request.getParameter("id"));
	    User exitingUser=userDb.selectUser(id);
	    RequestDispatcher dispatcher= request.getRequestDispatcher("user-form.jsp");
	    request.setAttribute("user", exitingUser);
	    dispatcher.forward(request, response);
	}
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
	    int id=Integer.parseInt(request.getParameter("id"));
		String f_name=request.getParameter("f_name");
		String l_name=request.getParameter("l_name");
		String email=request.getParameter("email");
		User newUser=new User(f_name,l_name,email);
		userDb.updateUser(newUser);
		response.sendRedirect("list");	
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
		List<User> listUser=userDb.selectAllUser();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher= request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);		
	}
	
	
	
}
