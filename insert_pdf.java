/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Project2;

/**
 *
 * @author DEBOSHREE BANERJEE
 */



import com.itextpdf.text.pdf.*;
import com.sun.rowset.CachedRowSetImpl;
import java.io.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.*;
import org.apache.pdfbox.util.*;

public class insert_pdf extends PDFTextStripper {

   public static StringBuilder tWord = new StringBuilder();
    public static String seek;
    public static String[] seekA;
    public static List wordList = new ArrayList();
    public static boolean is1stChar = true;
    public static boolean lineMatch;
    public static int pageNo = 1;
    public static double lastYVal;
     static PdfContentByte over;
    float ind;
    static BaseFont bf;
    static PdfStamper stamper;
     static  PdfReader reader ;
   static Map m= new HashMap();
   static float height;
   static CachedRowSetImpl query = null;

    public insert_pdf()
            throws IOException {
        super.setSortByPosition(true);
    }
    

    public static void r2(String file, String blank,CachedRowSetImpl crs )throws Exception 
    {
        
        reader = new PdfReader(file);
        //System.out.println("HERE"+crs);
        stamper = new PdfStamper(reader, new FileOutputStream(blank));
        bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252,BaseFont.NOT_EMBEDDED);
        over = stamper.getOverContent(1);
          
         
        PDDocument document = null;
        String a="capital,Husband`s,DEPTT,DESIGNATION";
        seekA = a.split(",");
        m.put("capital","name");
        m.put("Husband`s","h_f_name");
        m.put("DEPTT","department");
        m.put("DESIGNATION","designation");
        query=crs;
        int j;
       /* for(j=0;j<seekA.length;++j)
        {
            System.out.println(seekA[j]);
            seekA[j]=seekA[j].replaceAll("\\s","");
            System.out.println(seekA[j]);
        }*/
        seek = a;
        
        try 
        {
            File input = new File(file);
            document = PDDocument.load(input);
            if (document.isEncrypted()) 
            {
                try 
                {
                    document.decrypt("");
                } 
                catch (InvalidPasswordException e) 
                {
                    System.err.println("Error: Document is encrypted with a password.");
                    System.exit(1);
                }
            }
            insert_pdf printer = new insert_pdf();
            List allPages = document.getDocumentCatalog().getAllPages();

            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage) allPages.get(i);
                PDRectangle rect = page.getMediaBox();
                 height = rect.getHeight();
                PDStream contents = page.getContents();

                if (contents != null) 
                {
                    printer.processStream(page, page.findResources(), page.getContents().getStream());
                }
                pageNo += 1;
            }
        } 
        finally 
        {
            if (document != null) 
            {
                //System.out.println(wordList);
                  
               

              stamper.close();
               document.close();
            }
        }
    }

    @Override
    protected void processTextPosition(TextPosition text) 
    {
        String tChar = text.getCharacter();
        
       /* System.out.println("String[" + text.getXDirAdj() + ","
                + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
                + text.getXScale() + " height=" + text.getHeightDir() + " space="
                + text.getWidthOfSpace() + " width="
                + text.getWidthDirAdj() + "]" + text.getCharacter());*/
        String REGEX = "[,.\\[\\](:;!?)/]";
        char c = tChar.charAt(0);
        try 
        {
            lineMatch = matchCharLine(text);
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(insert_pdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ((!tChar.matches(REGEX)) && (!Character.isWhitespace(c))) 
        {
            if ((!is1stChar) && (lineMatch == true)) 
            {
                appendChar(tChar);
            } 
            else if (is1stChar == true) 
            {
                setWordCoord(text, tChar);
            }
        } 
        else 
        {
            try 
            {
                endWord();
            } 
            catch (SQLException ex) {
                Logger.getLogger(insert_pdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    protected void appendChar(String tChar) {
        tWord.append(tChar);
        is1stChar = false;
    }

    protected void setWordCoord(TextPosition text, String tChar) {
        tWord.append("(").append(pageNo).append(")[").append(roundVal(Float.valueOf(text.getXDirAdj()))).append(" : ").append(roundVal(Float.valueOf(text.getYDirAdj()))).append("] ").append(tChar);
        is1stChar = false;
    }

    
  protected void endWord() throws SQLException 
    {
        String newWord = tWord.toString().replaceAll("[^\\x00-\\x7F]", "");
        
        String sWord = newWord.substring(newWord.lastIndexOf(' ') + 1);
       
        if (!"".equals(sWord)) 
        {
            if (Arrays.asList(seekA).contains(sWord)) 
            {
                
               
                float y=Float.parseFloat(newWord.substring(newWord.lastIndexOf(':')+2,newWord.lastIndexOf(']')));
                
                String elem = (String) m.get(sWord);
                //System.out.println(sWord+" " +elem);
                String ans=new String();
                
                while(query.next())
                {
                    ans=query.getString(elem);
                    System.out.print(" "+ans);
                    over.beginText();
                    over.setFontAndSize(bf, 8);
                    over.setTextMatrix(130.0f, height-y);
                    over.showText(ans);
                    over.endText();
               
                }
                query.beforeFirst();
              
               
                   
               
                wordList.add(newWord);
            } 
            else if ("SHOWMETHEMONEY".equals(seek)) 
            {
                
                wordList.add(newWord);
            }
        }
        tWord.delete(0, tWord.length());
        is1stChar = true;
    }

    protected boolean matchCharLine(TextPosition text) throws SQLException {
        Double yVal = roundVal(Float.valueOf(text.getYDirAdj()));
        if (yVal.doubleValue() == lastYVal) {
            return true;
        }
        lastYVal = yVal.doubleValue();
        endWord();
        return false;
    }

    protected Double roundVal(Float yVal) {
        DecimalFormat rounded = new DecimalFormat("0.0'0'");
        Double yValDub = new Double(rounded.format(yVal));
        return yValDub;
    }
}
