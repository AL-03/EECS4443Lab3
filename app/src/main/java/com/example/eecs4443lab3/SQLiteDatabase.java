package com.example.eecs4443lab3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class SQLiteDatabase {

        // Connection string for your SQLite database file
        private static final String URL = "jdbc:sqlite:./my_database.db";
        private static String title = "";
        private static String deadline = "";
        private static String description = "";

        public static void insertNote(String taskTitle, String taskDeadline, String taskDescription) {
            // SQL INSERT statement with placeholders (?)
            String sql = "INSERT INTO users(taskTitle, taskDeadline, taskDescription) VALUES(?, ?, ?)";


            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Bind the values to the placeholders
                pstmt.setString(1, taskTitle);
                pstmt.setString(2, taskDeadline);
                pstmt.setString(3, taskDescription);

                // Execute the INSERT statement
                int rowsAffected = pstmt.executeUpdate();
                System.out.println(rowsAffected + " row(s) inserted.");

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        private static void selectRow(int index)
        {
            // To read the data back
            String selectSql = "SELECT title, deadline, description FROM users WHERE id = ?";
            String returnValue;
            // When reading results:         1    2      3
            // ResultSet indices also start at 1
            try(Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, 1);  // Find user with id = 1

                ResultSet rs = pstmt.executeQuery();
                while (rs.next() && index >= 0) {

                    title = rs.getString(1);        // gets the id column
                    deadline = rs.getString(2); // gets the name column
                    description = rs.getString(3); // gets the email column
                    index--;
                }
            }
            catch(Exception e){}
        }

        //Getter methods
        private static String getTitle()
        {
          return title;
        }

        private static String getDeadline()
        {
            return deadline;
        }

        private static String getDescription()
        {
          return description;
        }

}
