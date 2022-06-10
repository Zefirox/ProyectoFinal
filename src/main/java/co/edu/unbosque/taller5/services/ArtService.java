package co.edu.unbosque.taller5.services;

import co.edu.unbosque.taller5.dtos.Art;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ArtService {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    public Optional<List<Art>> getArtPiece() throws IOException{

        Connection connection = null;

        // Object for handling SQL statement
        Statement stmt1 = null;

        // Data structure to map results from database
        List<Art> artPieces = new ArrayList<Art>();

        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // Executing a SQL query
            stmt1 = connection.createStatement();
            String sql1 = "SELECT * FROM Art";
            ResultSet rs1 = stmt1.executeQuery(sql1);

            // Reading data from result set row by row
            while (rs1.next()) {
                // Extracting row values by column name
                Art art = new Art();

                art.setId(rs1.getInt("id"));
                art.setName(rs1.getString("nombre"));
                art.setPrice(rs1.getFloat("price"));
                art.setImagePath(rs1.getString("imagepath"));
                art.setForSale(rs1.getBoolean("forsale"));
                art.setCollection(rs1.getInt("collection"));

                artPieces.add(art);
            }

            // Closing resources
            rs1.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            // Cleaning-up environment
            try {
                if (stmt1 != null) stmt1.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return Optional.of(artPieces);
    }

    public Optional<Boolean> createArt(String email, String title, String price, String filename, String currentCollection, Connection connection) throws IOException {
        PreparedStatement stmt = null;
        Statement stmt1 = null;
        PreparedStatement stmt2 = null;
        try{
            int collectionId = 0;
            stmt1 = connection.createStatement();
            String query1 = "SELECT * FROM Collection x WHERE x.nombre = '"+currentCollection+"'";
            ResultSet rs1 = stmt1.executeQuery(query1);
            rs1.next();
            collectionId = rs1.getInt("id");

            double priceDouble = Double.parseDouble(price);

            String query = "INSERT INTO Art (nombre, price, imagepath, forsale, collection) VALUES (?,?,?,true,'"+collectionId+"')";
            stmt = connection.prepareStatement(query);
            stmt.setString(1,title);
            stmt.setDouble(2,priceDouble);
            stmt.setString(3,filename);
            stmt.executeUpdate();

            List<Art> arts = getArtPiece().get();
            Art artfound = arts.stream().filter(art -> title.equals(art.getName())).findFirst().orElse(null);
            int artId = artfound.getId();

            Date date = new Date();
            long ml = date.getTime();
            Timestamp time = new Timestamp(ml);

            System.out.println(email);
            String query2 = "INSERT INTO Ownership (art, userapp, registeredAt) VALUES (?,?,?)";
            stmt2 = connection.prepareStatement(query2);
            stmt2.setInt(1,artId);
            stmt2.setString(2,email);
            stmt2.setTimestamp(3,time);
            stmt2.executeUpdate();

            rs1.close();
            stmt.close();
            stmt1.close();
            stmt2.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(false);
        }finally {
            // Cleaning-up environment
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt != null) stmt.close();
                if (stmt2 != null) stmt2.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(true);
    }

    public Optional<Boolean> forSale(String name, boolean newForSale){
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "UPDATE Art SET forsale = "+newForSale+" WHERE nombre = '"+name+"'";
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Optional.of(false);
        }finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(true);
    }

    public Optional<Boolean> price(String name, float newPrice){
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String query = "UPDATE Art SET price = "+newPrice+" WHERE nombre = '"+name+"'";
            stmt = conn.prepareStatement(query);
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Optional.of(false);
        }finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(true);
    }
}
