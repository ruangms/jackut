package br.ufal.ic.p2.jackut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class does the operations on the database such as getting, updating and inserting data
 */ 


public class DatabaseManager {

    private final String url;

    public DatabaseManager(String dbName) {
        this.url = "jdbc:sqlite:" + dbName;
    }

    // Method to establish a connection to the SQLite database.
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    // Method to execute an SQL Update/Insert/Delete query.
    public void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }

    // Method to execute an SQL Select query and return the result set.
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeQuery();
    } 

    // Method to close the result set.
    public void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

