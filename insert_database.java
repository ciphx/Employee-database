
package Project2;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DEBOSHREE BANERJEE
 */
public class insert_database 
{
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/db";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   
   
  public static void createConnection(int card_n,String arr[])
   {
   Connection conn = null;
   PreparedStatement stmt = null;
   
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      JOptionPane.showMessageDialog(null, "Connecting to a selected database...");
      
      conn = (Connection) DriverManager.getConnection(DB_URL, USER, PASS);
      JOptionPane.showMessageDialog(null, "Connected database successfully...");
      
      //STEP 4: Execute a query
        int flag=0;
      
       
        try 
        {
        String sql = "insert into registration (card_number,name,h_f_name,department,designation) values (?,?,?,?,?)";
        stmt = (PreparedStatement) conn.prepareStatement(sql);
        stmt.setInt(1, card_n);
        stmt.setString(2, arr[0]);
        stmt.setString(3, arr[1]);
        stmt.setString(4, arr[2]);
        stmt.setString(5, arr[3]);
        
        stmt.execute();
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"Record not inerted. Duplicate entry!");
            flag=1;
        }
         
    
    
      //stmt.executeUpdate(sql);
       if(flag==0)
      JOptionPane.showMessageDialog(null,"Inserted record in table!..");
       
           
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

    
}
