package general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import scraperThread.ScraperThreadSku;

// This is the main thread that launches the subthreads to download the skus for a specific category 

public class DownloadSkusThread extends Thread {

	long sleepTime = 0;
	
	public DownloadSkusThread(long sleepTime) {
		
		this.sleepTime = sleepTime;
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean forever = true;
		
		try {
			
			while(forever) {
				downloadSku();
				this.sleep(sleepTime);
				if(sleepTime == 0) forever = false;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	// gets all the categories URLs from the database, and for each one launches a dedicated thread to download their skus
	
	public void downloadSku() {
		
		Connection input = null;
		Statement statement_in = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			input = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cloth","root","ILCAVI87");
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//input = DriverManager.getConnection("jdbc:mysql://localhost/cloth","root","ILCAVI87");
			
			statement_in = input.createStatement();
			
			String sql = "SELECT * FROM categories";
			
			rs = statement_in.executeQuery(sql);

			ExecutorService executor = Executors.newFixedThreadPool(12);
				   

				   // run the service
			System.out.println("parte benetton");
				       while (rs.next()) {
				    	   	try{
							String threadClassName = rs.getString("threadname");
							String threadUrl = rs.getString("url");
							int idCategory = rs.getInt("idCategory");
				            							
							//System.out.println(threadClassName);
							
							Class threadClass = Class.forName("scraperThread.Sku."+threadClassName);
							
							ScraperThreadSku newThread = (ScraperThreadSku)threadClass.newInstance();
							
							
							newThread.setUrl(threadUrl);
							newThread.setIdCategory(idCategory);
							executor.execute(newThread);
							//System.out.println("parte il thread");
				    	   	}catch(Exception e){
				    	   		
				    	   		String threadClassName = rs.getString("threadname");
								String threadUrl = rs.getString("url");
								int idCategory = rs.getInt("idCategory");
					            
								//System.out.println(threadClassName);
								
								Class threadClass = Class.forName("scraperThread.Sku."+threadClassName);
								
								ScraperThreadSku newThread = (ScraperThreadSku)threadClass.newInstance();
								
								
								newThread.setUrl(threadUrl);
								newThread.setIdCategory(idCategory);
								executor.execute(newThread);
								//System.out.println("parte il thread");
				    	   		
				    	   	}
				       }
				       executor.shutdown();
				       executor.awaitTermination(10, TimeUnit.MINUTES);
				       

			
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
		System.out.println("finito");
	}
}
