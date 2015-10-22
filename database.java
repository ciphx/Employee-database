/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Project;

import java.sql.*;
import javax.swing.JOptionPane;

public class database{
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/db";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   
   Connection conn = null;
   Statement stmt = null;
  public  void createConnection()
   {
   
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      JOptionPane.showMessageDialog(null, "Connecting to a selected database...");
      
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      JOptionPane.showMessageDialog(null, "Connected database successfully...");
      
      //STEP 4: Execute a query
      JOptionPane.showMessageDialog(null,"Creating table in given database...");
      stmt = conn.createStatement();
      
      String sql = "CREATE TABLE REGISTRATION " +
                   "(card_number INTEGER not NULL, " +
                   " name VARCHAR(255), " + 
                   " h_f_name VARCHAR(255), " + 
                   "department VARCHAR(255),"+ 
                   "designation VARCHAR(255),"+
                   " PRIMARY KEY ( card_number ))"; 

      stmt.executeUpdate(sql);
      JOptionPane.showMessageDialog(null,"Created table in given database...");
   }
   
   catch(SQLException se)
   {
      //Handle errors for JDBC
      se.printStackTrace();
   
   }
   
   catch(Exception e)
   
   {
      //Handle errors for Class.forName
       JOptionPane.showMessageDialog(null,"ERROR!");
      e.printStackTrace();
      }
   
   finally
     {
      //finally block used to close resources
      try
      {
         if(stmt!=null)
            conn.close();
      }
      catch(SQLException se)
      {
      }// do nothing
      try
      {
         if(conn!=null)
            conn.close();
      }
      
      catch(SQLException se)
      {
         se.printStackTrace();
      }//end finally try
   }//end try
  JOptionPane.showMessageDialog(null,"Goodbye!");
}//end main
}//end JDBCExample
