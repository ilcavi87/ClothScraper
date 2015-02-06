package general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import scraperThread.ScraperThreadSku;
import scraperThread.Category.BenettonCategoriesEng;
import scraperThread.Category.ZaraCategoriesEng;


public class UpdateCategoriesThread extends Thread {

	long sleepTime = 0;
	
	public UpdateCategoriesThread(long sleepTime) {
		
		this.sleepTime = sleepTime;
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		boolean forever = true;
		
		try {
			
			while(forever) {
				updateCategories();
				this.sleep(sleepTime);
				if(sleepTime == 0) forever = false;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void updateCategories() {
		
		try {
			
			//new BenettonCategoriesEng().run();
			new ZaraCategoriesEng().run();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
