

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import event.Event;
import room.Room;

/**
 * Servlet implementation class HotelServlet
 */
@WebServlet("/HotelServlet")
public class HotelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String user_name;
	private static String password;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HotelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Sign In:" + request.getParameter("signInPage"));
		System.out.println("Event Page: " + request.getParameter("eventPage"));
		System.out.println("Main Page: " + request.getParameter("mainPage"));
		if (request == null) {
			System.out.println("What happened?");
		}
		if(request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("show_rooms")) {
			System.out.println("go to show rooms");
			/*
			 * Partially hard-coded room which is used to test if the page is retrieving information from the front page correctly. 
			 */
			if(user_name != null) {
				String price = request.getParameter("price");
				String check_in = request.getParameter("checkIn");
				String check_out = request.getParameter("checkOut");
				System.out.println("Price: " + price + " Check In Date: " + check_in + " Check Out Date: " + check_out);
				//response.getWriter().append("Price: " + price + " Check In Date: " + check_in + " Check Out Date: " + check_out);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("rooms.jsp");
				List<Room> rooms = new ArrayList<Room>();
				
				Connection connection = null;
				try {
			         DBConnection.getDBConnection(getServletContext());
			         connection = DBConnection.connection;
			         String selectSQL = "SELECT * FROM ROOMS WHERE pricerange LIKE ? AND check_in LIKE ? AND check_out LIKE ?";
			         
			         PreparedStatement preparedStmt = connection.prepareStatement(selectSQL);
			         preparedStmt.setString(1, price);
			         preparedStmt.setString(2, check_in);
			         preparedStmt.setString(3, check_out);
			         ResultSet rs = preparedStmt.executeQuery();
			         
			         while (rs.next()) {
				        	Room room = new Room();
				 			room.setPrice(rs.getString("price"));
				 			room.setFloor(rs.getString("floor"));
				 			room.setGuests(rs.getInt("guests"));
				 			room.setBeds(rs.getInt("beds"));
				 			rooms.add(room);
			         }
			      } catch (Exception e) {
			         e.printStackTrace();
			      }
				request.setAttribute("rooms", rooms);
				requestDispatcher.forward(request, response);
			} else {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("sign_in_notice.jsp");
				requestDispatcher.forward(request, response);
			}
			
		} else if (request.getParameter("eventPage") != null && request.getParameter("eventPage").contentEquals("events_page")) {
			/*
			 * Hard coded events used to test if the events page is retrieving everything correctly- it will draw actual
			 * events dynamically from the database at run time.
			 */
			System.out.println("go to event page");
			List<Event> events = new ArrayList<Event>();
			
			Connection connection = null;
		    boolean roomCheck = false;
		    
		    try {
		         DBConnection.getDBConnection(getServletContext());
		         connection = DBConnection.connection;
		         
		         String selectSQL = "SELECT * FROM USERS WHERE username LIKE ? AND password LIKE ?";
		         PreparedStatement preparedStmt = connection.prepareStatement(selectSQL);
		         ResultSet rs = preparedStmt.executeQuery();
		         while (rs.next()) {
		        	 	Event newevent = new Event();
		        	 	newevent.setDateDay(rs.getString("DateDay"));
		        	 	newevent.setDateName(rs.getString("DateName"));
		        	 	newevent.setEventDescription(rs.getString("description"));
		        	 	newevent.setEventTitle(rs.getString("title"));
		        	 	events.add(newevent);
		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
			request.setAttribute("events", events);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("hotel_events.jsp");
			requestDispatcher.forward(request, response);
		} else if (request.getParameter("signInPage") != null && request.getParameter("signInPage").contentEquals("sign_in")) {
			/*
			 * Switch to "Sign In" page.
			 */
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("hotel_sign_in.html");
			requestDispatcher.forward(request, response);
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("main_page")) {
			/*
			 * Transfer back to main page, but with log in information- this info should be stored in the database. 
			 */
			setUserAndPassword(request.getParameter("username"), request.getParameter("password"));
			
			/*
			 * SQL checking for user and inserting new info if not existing user
			 */
		    
		    Connection connection = null;
		    
		    boolean userCheck = false;
		    
		    try {
		         DBConnection.getDBConnection(getServletContext());
		         connection = DBConnection.connection;
		         String selectSQL = "SELECT * FROM USERS WHERE username LIKE ? AND password LIKE ?";
		         PreparedStatement preparedStmt = connection.prepareStatement(selectSQL);
		         preparedStmt.setString(1, user_name);
		         preparedStmt.setString(2, password);
		         ResultSet rs = preparedStmt.executeQuery();
		         while (rs.next()) {
			            int userid = rs.getInt("userid");
			            String username = rs.getString("username").trim();
			            userCheck = true;  
			     }
		         
		       
		         if (!userCheck) {
		        	 String insertSql = " INSERT INTO USERS (userid, username, password) values (default, ?, ?)";
			         PreparedStatement preparedStmt2 = connection.prepareStatement(insertSql);
			         preparedStmt2.setString(1, user_name);
			         preparedStmt2.setString(2, password);
			         preparedStmt2.execute();
			         connection.close();
		        	 
		         }
		         else {
			         System.out.println("Welcome Back");
		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("hotel-mockup.html");
			requestDispatcher.forward(request, response);
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("book_room")) {
			System.out.println(request.getAttribute("testVar"));
			request.setAttribute("room_number", request.getAttribute("testVar"));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("successful_room_booking.jsp");
			requestDispatcher.forward(request, response);
			
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("after_booking")) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("hotel-mockup.html");
			requestDispatcher.forward(request, response);
		} else {
			System.out.println("Whatever else:");
			System.out.println(request.getParameter("mainPage") + request.getParameter("eventPage"));
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("hotel-mockup.html");
			requestDispatcher.forward(request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void setUserAndPassword(String username, String pass_word) {
		user_name = username;
		password = pass_word;
	}

}
