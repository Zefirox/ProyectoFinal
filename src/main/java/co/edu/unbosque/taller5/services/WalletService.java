package co.edu.unbosque.taller5.services;

import co.edu.unbosque.taller5.dtos.UserApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WalletService {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost/proyectofinal";

    // Database credentials
    static final String USER = "postgres";
    static final String PASS = "Zeref29714526?";

    public Optional<Boolean> loadFcoins(String username, String Fcoins){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            List<UserApp> users = new UserAppService().getUsers(conn).get();

            UserApp user1 = users.stream().filter(user-> username.equals(user.getEmail())).findFirst().get();

            float numFcoins = Float.parseFloat(Fcoins);

            String type = "recharge";
            java.util.Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);

            String sql = "INSERT INTO WalletHistory (userapp, type, fcoins, registeredAt) VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user1.getEmail());
            stmt.setString(2, type);
            stmt.setFloat(3,numFcoins);
            stmt.setTimestamp(4, time);

            stmt.executeUpdate();

            stmt.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (SQLException se) {
            se.printStackTrace();
            return Optional.of(false);
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
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

    public Optional<Boolean> buy(String userBuyer, String Fcoins, int artId){
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            List<UserApp> users = new UserAppService().getUsers(conn).get();

            UserApp user1 = users.stream().filter(user-> userBuyer.equals(user.getEmail())).findFirst().get();

            float numFcoins = Float.parseFloat(Fcoins);

            float numUser = getFcoins(userBuyer);

            float newFcoins = 0;
            String type = "buy";
            Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);

            float aux = numUser-numFcoins;
            if(aux<0){
                return Optional.of(false);
            }

            newFcoins =numFcoins*(-1);

            String sql = "INSERT INTO WalletHistory (userapp, type, fcoins, registeredAt) VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user1.getEmail());
            stmt.setString(2, type);
            stmt.setFloat(3,newFcoins);
            stmt.setTimestamp(4, time);
            stmt.executeUpdate();

            String sql1 = "UPDATE Ownership SET userapp = ? WHERE art = ?";
            stmt1 = conn.prepareStatement(sql1);
            stmt1.setString(1,userBuyer);
            stmt1.setInt(2,artId);
            stmt1.executeUpdate();

            stmt.close();
            stmt1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.of(false);
        } catch (SQLException se) {
            se.printStackTrace();
            return Optional.of(false);
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
            return Optional.of(false);
        }finally {
            // Cleaning-up environment
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return Optional.of(true);
    }

    public void sale(String username, String Fcoins){
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            List<UserApp> users = new UserAppService().getUsers(conn).get();

            UserApp user1 = users.stream().filter(user-> username.equals(user.getEmail())).findFirst().orElse(null);

            float numFcoins = Float.parseFloat(Fcoins);

            String type = "sale";
            Date date = new Date();
            long mili = date.getTime();
            Timestamp time = new Timestamp(mili);

            String sql = "INSERT INTO WalletHistory (userapp, type, fcoins, registeredAt) VALUES (?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user1.getEmail());
            stmt.setString(2, type);
            stmt.setFloat(3,numFcoins);
            stmt.setTimestamp(4, time);

            stmt.executeUpdate();

            stmt.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }finally {
            // Cleaning-up environment
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public float getFcoins(String username){
        Connection conn = null;
        Statement stmt1 = null;
        float numUser = 0;
        try {
            // Registering the JDBC driver
            Class.forName(JDBC_DRIVER);

            // Opening database connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            List<UserApp> users = new UserAppService().getUsers(conn).get();

            UserApp user1 = users.stream().filter(user-> username.equals(user.getEmail())).findFirst().get();

            stmt1 = conn.createStatement();
            String query1 = "SELECT fcoins FROM WalletHistory x WHERE x.userapp = '"+user1.getEmail()+"'";
            ResultSet rs = stmt1.executeQuery(query1);

            while (rs.next()){
                numUser += Float.parseFloat(rs.getString("fcoins"));
            }

            rs.close();
            stmt1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (ClassNotFoundException ce) {
            ce.printStackTrace();
        }finally {
            // Cleaning-up environment
            try {
                if (stmt1 != null) stmt1.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return numUser;
    }
}
