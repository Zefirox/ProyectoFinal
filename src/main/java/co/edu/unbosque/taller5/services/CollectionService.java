package co.edu.unbosque.taller5.services;

import co.edu.unbosque.taller5.dtos.Collection;
import co.edu.unbosque.taller5.dtos.UserApp;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionService {

    public Optional<Collection> getCollection(Connection connection, int Id) throws IOException {

        // Object for handling SQL statement
        PreparedStatement stmt = null;

        Collection collection = new Collection();

        try {
            // Executing a SQL query
            String sql = "SELECT * FROM Collection x WHERE x.id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1,Id);
            ResultSet rs = stmt.executeQuery();

            // Reading data from result set row by row
            while (rs.next()) {

                collection.setId(rs.getInt("id"));
                collection.setName(rs.getString("nombre"));
                collection.setDescription(rs.getString("description"));
                collection.setCategory(rs.getString("category"));
                collection.setUserApp(rs.getString("userapp"));
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

        return Optional.of(collection);
    }

    public Optional<Collection> createCollection(String name, String description, String category, String userapp, Connection connection) throws IOException {

        PreparedStatement stmt = null;

        try{
            String sql = "INSERT INTO Collection (nombre, description, category, userapp) VALUES (?, ?, ?, ?)";
            stmt=connection.prepareStatement(sql);
            stmt.setString(1,name);
            stmt.setString(2,description);
            stmt.setString(3,category);
            stmt.setString(4,userapp);
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
        Collection newcollection = new Collection();

        newcollection.setName(name);
        newcollection.setDescription(description);
        newcollection.setCategory(category);
        newcollection.setUserApp(userapp);

        return Optional.of(newcollection);
    }
}
