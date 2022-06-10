package co.edu.unbosque.taller5.services;

import jakarta.ws.rs.core.Response;

import java.sql.*;
import java.util.Optional;

public class OwnershipService {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    public Optional<String> getOwner(int artId){
        Connection conn = null;
        Statement stmt = null;
        String owner = "";

        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String query = "SELECT * FROM Ownership x WHERE x.art = "+artId;
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            owner = rs.getString("userapp");

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(owner);
    }

}
