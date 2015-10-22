/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Project2;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/**
 *
 * @author DEBOSHREE BANERJEE
 */
public class select_query 
{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost:3306/db";

   //  Database credentials
   static final String USER = "root";
   static final String PASS = "root";
   static String arr[]= new String[4];
   
   
  public static CachedRowSetImpl createConnection(int card_n)
   {
   Connection conn = null;
   PreparedStatement stmt = null;
   ResultSet rs = null;
   CachedRowSetImpl crs = null;
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
        //String sql = "insert into registration (card_number,name,h_f_name,department,designation) values (?,?,?,?,?)";
        stmt =  (PreparedStatement) conn.prepareStatement("SELECT * FROM registration WHERE card_number = ? ");
        stmt.setObject(1,card_n);
        
            rs=(ResultSet) stmt.executeQuery();
            
            crs = new CachedRowSetImpl();
            crs.populate(rs);
            
            
        }
        
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,"No such record. ");
            flag=1;
        }
         
    
    
      //stmt.executeUpdate(sql);
       if(flag==0)
       {
      JOptionPane.showMessageDialog(null,"Fetched record from table!..");
       
       }
       
           
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
  return crs;
}//end main

    
}
