package co.edu.unbosque.taller5.services;

import co.edu.unbosque.taller5.dtos.UserApp;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserAppService {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    public Optional<List<UserApp>> getUsers(Connection connection) throws IOException{

        // Object for handling SQL statement
        Statement stmt = null;

        // Data structure to map results from database
        List<UserApp> users = new ArrayList<UserApp>();

        try {
            // Executing a SQL query
            stmt = connection.createStatement();
            String sql = "SELECT * FROM UserApp";
            ResultSet rs = stmt.executeQuery(sql);

            // Reading data from result set row by row
            while (rs.next()) {

                UserApp user = new UserApp();

                user.setName(rs.getString("nombre"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));

                users.add(user);
            }

            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return Optional.of(users);
    }

    public Optional<UserApp> createUser(String name, String email, String password, String role, Connection connection) throws IOException {

        PreparedStatement stmt = null;

        try{
            String sql = "INSERT INTO Userapp (nombre, email, password, role) VALUES (?, ?, ?, ?)";
            stmt=connection.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,email);
            stmt.setString(3,password);
            stmt.setString(4,role);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        UserApp newuser = new UserApp();

        newuser.setEmail(email);
        newuser.setName(name);
        newuser.setPassword(password);
        newuser.setRole(role);

        return Optional.of(newuser);
    }
}
