package com.example.eecs4443lab3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class SQLiteDatabase {

        // Connection string for the SQLite database file
        private static final String URL = "jdbc:sqlite:./my_database.db";
        private static String title = "";
        private static String deadline = "";
        private static String description = "";
        private static int currentSize = 0;

        //Inserts the tasks information into the database.
        public static void insertNote(String taskTitle, String taskDeadline, String taskDescription) {
            // SQL INSERT statement with placeholders (?)
            String sql = "INSERT INTO users(taskTitle, taskDeadline, taskDescription) VALUES(?, ?, ?)";

            //Connects to the local database and stores the information for that task using the URL.
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // Bind the values to the placeholders
                pstmt.setString(1, taskTitle);
                pstmt.setString(2, taskDeadline);
                pstmt.setString(3, taskDescription);

                // Execute the INSERT statement
                int rowsAffected = pstmt.executeUpdate();
            
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }

            currentSize++;
        }

        //Select the row from where the getters will grab the data from (0 based index).
        private static void selectRow(int index)
        {
            // To read the data back
            String selectSql = "SELECT title, deadline, description FROM users WHERE id = ?";
            String returnValue;
          
            try(Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, 1);  // Find user with id = 1

                ResultSet rs = pstmt.executeQuery();
                while (rs.next() && index >= 0) {

                    title = rs.getString(1);    // gets the title column
                    deadline = rs.getString(2); // gets the deadline column
                    description = rs.getString(3); // gets the description column
                    index--;
                }
            }
            catch(Exception e){}
        }

        //Getter methods
        public static String getTitle()
        {
          return title;
        }

        public static String getDeadline()
        {
            return deadline;
        }

        public static String getDescription()
        {
          return description;
        }

        //index of last element added to the database.
        public static int getCurrentSize()
        {
            return num - 1;
        }

        

}
