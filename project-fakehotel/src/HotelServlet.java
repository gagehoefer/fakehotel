

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
	private static RequestDispatcher requestDispatcher;
       
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
		if (request == null) {
			System.out.println("What happened?");
		}
		if(request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("show_rooms")) {
			if(user_name != null) {
				String price = request.getParameter("price");
				String check_in = request.getParameter("checkIn");
				String check_out = request.getParameter("checkOut");
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
				 			room.setNumber(rs.getInt("roomid"));
				 			room.setCheckIn(rs.getString("check_in"));
				 			room.setCheckOut(rs.getString("check_out"));
				 			rooms.add(room);
			         }
			      } catch (Exception e) {
			         e.printStackTrace();
			      }
				request.setAttribute("rooms", rooms);
				requestDispatch("rooms.jsp", request, response);
			} else {
				requestDispatch("sign_in_notice.jsp", request, response);
			}
			
		} else if (request.getParameter("eventPage") != null && request.getParameter("eventPage").contentEquals("events_page")) {
			List<Event> events = new ArrayList<Event>();		
			Connection connection = null;
		    boolean roomCheck = false;
		    
		    try {
		         DBConnection.getDBConnection(getServletContext());
		         connection = DBConnection.connection;
		         
		         String selectSQL = "SELECT * FROM EVENTS";
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
			requestDispatch("hotel_events.jsp", request, response);
		} else if (request.getParameter("signInPage") != null && request.getParameter("signInPage").contentEquals("sign_in")) {
			/*
			 * Switch to "Sign In" page.
			 */
			requestDispatch("hotel_sign_in.html", request, response);
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
			         requestDispatch("new-user.html", request, response);
		         } else {
			        System.out.println("Welcome Back");
			        requestDispatch("hotel-mockup.html", request, response);
		         }
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("book_room")) {
			String room_number = (String) request.getParameter("roomNum");
			System.out.println("ROOM NUMBER: " + room_number);
			String room_check_in = (String) request.getParameter("checkInRoom");
			String room_check_out = (String) request.getParameter("checkOutRoom");
			request.setAttribute("room_number", room_number);
			String booking = "Room Number: " + room_number + " Check In Date: 05/01/2020" + " Check Out Date: 05//04/2020";
			/*
			 * ***************************************************************************************************
			 * Write a query here to add 'booking' to a user's previous bookings- should just be a string entry on the User table
			 * ***************************************************************************************************
			 */
			
			 Connection connection = null;
			    
			    
			    try {
			         DBConnection.getDBConnection(getServletContext());
			         connection = DBConnection.connection;
			         String selectSQL = "UPDATE USERS SET reservations = ? WHERE username LIKE ? AND password LIKE ?";
			         PreparedStatement preparedStmt = connection.prepareStatement(selectSQL);
			         preparedStmt.setString(1, booking);
			         preparedStmt.setString(2, user_name);
			         preparedStmt.setString(3, password);
			         preparedStmt.execute();
			         
			    	} catch (Exception e) {
			         e.printStackTrace();
			    	}
			
			requestDispatch("successful_room_booking.jsp", request, response);
			
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("after_booking")) {
			requestDispatch("hotel-mockup.html", request, response);
		} else if (request.getParameter("mainPage") != null && request.getParameter("mainPage").contentEquals("after_registering")) {
			requestDispatch("hotel-mockup.html", request, response);
		} else if (request.getParameter("reservationPage") != null && request.getParameter("reservationPage").contentEquals("see_reservation")) {
			if(user_name != null) {
				request.setAttribute("username", user_name);
				Connection connection = null;
				String booking = "";
				try {
					DBConnection.getDBConnection(getServletContext());
					connection = DBConnection.connection;
					String selectSQL = "SELECT * FROM USERS WHERE username LIKE ? AND password LIKE ?";
					PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
					preparedStatement.setString(1, user_name);
					preparedStatement.setString(2, password);
					ResultSet rs = preparedStatement.executeQuery();
					while(rs.next()) {
						booking = rs.getString("reservations");
					}
					request.setAttribute("booking", booking);
					requestDispatch("show_reservation.jsp", request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				requestDispatch("sign_in_notice.jsp", request, response);
			}
		} else {
			System.out.println("Whatever else:");
			System.out.println(request.getParameter("mainPage") + request.getParameter("eventPage"));
			requestDispatch("hotel-mockup.html", request, response);
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	/**
	 * Simple setter method for the static username and password; this is used at various points of the servlet to 
	 * see if a user has signed in or not, determining if they are allowed to check out a room. 
	 * @param username
	 * @param pass_word
	 */
	private void setUserAndPassword(String username, String pass_word) {
		user_name = username;
		password = pass_word;
	}
	
	/**
	 * Generic dispatch method used to switch between HTML/jsp pages, based on the given path. 
	 * @param path
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void requestDispatch(String path, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			requestDispatcher = request.getRequestDispatcher(path);
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
