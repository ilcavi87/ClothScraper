package scraperThread;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.mysql.jdbc.Blob;

public class ScraperThreadSku extends Thread {
	
	private String url = "";
	private int idCategory;
	private ArrayList<String> inserts = new ArrayList<String>();
	private String connStr = "jdbc:mysql://127.0.0.1/cloth";
	public ArrayList<String> imageList = new ArrayList<String>();
	public ArrayList<String> update = new ArrayList<String>();
	public int numb; 

	
	public ScraperThreadSku() {
		
	}
	
	public void setUrl(String url) {
		
		this.url = url;
	}
	
	public String getUrl() {
		
		return url;
	}
	
	public void setIdCategory(int id) {
		
		this.idCategory = id;
	}
	
	public int getIdCategory() {
		
		return idCategory;
	}
	
	public void addInserts(String newInsert) {
		
		inserts.add(newInsert);
		
	}
	
	public int insertDB() {
		
int result = 0;
		
		Connection input = null;
		Statement statement_in = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			input = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cloth","root","ILCAVI87");
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//input = DriverManager.getConnection("jdbc:mysql://localhost/cloth","root","ILCAVI87");
			statement_in = input.createStatement();
			
			for (String riga : inserts){
				try{
				if (riga.contains("C9D3H3R3")){
					riga = riga.replace("C9D3H3R3", Integer.toString(this.numb));
					//System.out.println(riga);
					statement_in.executeUpdate(riga);
					System.out.println("DETTAGLI INSERITI");
					
				}else{
					//System.out.println(riga);
					statement_in.executeUpdate(riga,Statement.RETURN_GENERATED_KEYS);
					rs = statement_in.getGeneratedKeys();
					if (rs.next()){
					numb = rs.getInt(1);
					System.out.println("OGGETTO INSERITO");
						}
					} 
			
			
			}catch(Exception e ){
				
				System.out.println ("ERROR ON "+ riga);
			}
			
			}
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(statement_in != null) statement_in.close();
				if(input != null) input.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	
	public Boolean checkProduct(String code, String url){				
		boolean answer = false;
		Connection input = null;
		Statement statement_in = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			input = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cloth","root","ILCAVI87");
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//input = DriverManager.getConnection("jdbc:mysql://localhost/cloth","root","ILCAVI87");
			statement_in = input.createStatement();			
			
			String sql = "SELECT idsku FROM cloth.skus " +
					"WHERE sku_code = '"+code+"' AND url = '"+url+"';";
			
			rs = statement_in.executeQuery(sql);
			
			answer = rs.next();
			
			if (answer) {
					this.numb = Integer.parseInt(rs.getString("idsku"));

			}
			
			System.out.println(answer);
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(statement_in != null) statement_in.close();
				if(input != null) input.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		
	}
	
	
	return answer;
	
}
	
	public void downloadImage(String url) throws IOException{
		
		 System.setProperty("http.proxyHost", "proxy.iese.org");
		 System.setProperty("http.proxyPort", "8080");
		
		 URL site = new URL(url);
		 InputStream in = new BufferedInputStream(site.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 
		 
		 out.close();
		 in.close();
		 byte[] image = out.toByteArray();
		 String codec = url.substring(url.length() - 3);
		 
		 addInserts("INSERT INTO "
					+ "imgs(idsku, img, entry_date, codec)"
					+ " VALUES('C9D3H3R3','"+image+"',NOW(),'"+codec+"')");
		 
		
	}
	
	
public void insertCategory(){
		
		Connection input = null;
		Statement statement_in = null;
		ResultSet rs;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			input = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cloth","root","ILCAVI87");
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//input = DriverManager.getConnection("jdbc:mysql://localhost/cloth","root","ILCAVI87");
			statement_in = input.createStatement();
			
			for (String riga : this.update){
				//System.out.println(riga);
				statement_in.executeUpdate(riga,Statement.RETURN_GENERATED_KEYS);
				rs = statement_in.getGeneratedKeys();
				
				while (rs.next()) {
	                System.out.println("Key returned from getGeneratedKeys():"
	                        + rs.getInt(1));
	            } 
			
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				if(statement_in != null) statement_in.close();
				if(input != null) input.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		
		}
		
}

public void deleteCategory(String category){
	
	update.add("DELETE FROM cloth.categories WHERE threadname = '"+category+"';");
}


}

