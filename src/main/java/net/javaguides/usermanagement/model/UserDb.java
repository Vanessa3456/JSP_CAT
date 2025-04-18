package net.javaguides.usermanagement.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguides.usernamanagemt.model.User;

public class UserDb {
	private String jdbcUrl="jdbc:mysql://localhost:3306/crud?useSSL=false&serverTimezone=UTC";
	private String jdbcUsername="root";
	private String jdbcPassword="root";
	
    private static final String INSERT_USERS = "INSERT INTO employee (f_name, l_name, email) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "SELECT id, f_name, l_name, email FROM employee WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM employee";
    private static final String DELETE_USERS = "DELETE FROM employee WHERE id=?";
    private static final String UPDATE_USERS = "UPDATE employee SET f_name=?, l_name=?, email=? WHERE id=?;";
	
    public Connection get_connection() {
		
		Connection connect=null;
		
		try {		
			Class.forName("com.mysql.cj.jdbc.Driver");
		    connect=DriverManager.getConnection(
		    		jdbcUrl,
		    		jdbcUsername,
		    		jdbcPassword);
				}
		catch(ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver not found");
			e.printStackTrace();

		}
		catch(SQLException e) {
			System.out.println("Connection failed");
			e.printStackTrace();
			
		}
		
		return connect;
		
	}
    
    public void insertUser(User user) throws SQLException {
        try(Connection connection = get_connection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS)) {
            preparedStatement.setString(1, user.getFname());
            preparedStatement.setString(2, user.getLname());
            // Fixed parameter index for email
            preparedStatement.setString(3, user.getEmail());
            
            preparedStatement.executeUpdate();                
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try(Connection connection = get_connection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS)) {
            preparedStatement.setString(1, user.getFname());
            preparedStatement.setString(2, user.getLname());
            // Fixed parameter index for email
            preparedStatement.setString(3, user.getEmail());
            // Added parameter for id
            preparedStatement.setInt(4, user.getId());
            
            rowUpdated = preparedStatement.executeUpdate() > 0;                
        }
        return rowUpdated;          
    }
    
    public User selectUser(int id) throws SQLException {
    	User user=null;
    	try(Connection connect= get_connection();
    			PreparedStatement statement=connect.prepareStatement(SELECT_USER_BY_ID);){
    		statement.setInt(1, id);
    		System.out.println(statement);
    		ResultSet rs=statement.executeQuery();
    		
    		while(rs.next()) {
    			String f_name=rs.getString("f_name");
    			String l_name=rs.getString("l_name");
    			String email=rs.getString("email");
    			user=new User(id,f_name,l_name,email);   			
   			
    		}
    		    	}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return user;
    }
	
    
    public List<User> selectAllUser() throws SQLException {
        List<User> users = new ArrayList<>();
        try(Connection connect = get_connection();
            PreparedStatement statement = connect.prepareStatement(SELECT_ALL)) {
            
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String f_name = rs.getString("f_name");
                String l_name = rs.getString("l_name");
                String email = rs.getString("email");
                // Fixed missing closing parenthesis
                users.add(new User(id, f_name, l_name, email));
            }
        }

        catch(SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean deleteUser(int id) throws SQLException{
    	boolean rowDeleted;
    	try(Connection connect=get_connection();
    			PreparedStatement statement=connect.prepareStatement(DELETE_USERS);){
    		statement.setInt(1, id);
    		rowDeleted=statement.executeUpdate()>0;
    	}
    	return rowDeleted;
    	
    }
	

}
