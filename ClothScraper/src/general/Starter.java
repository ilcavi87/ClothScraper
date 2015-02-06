package general;

import java.util.logging.Level;

// This class starts the scraper

public class Starter {

	public static void main(String[] args) {
		
		try {
			
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
			System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");


			
			// launches the thread to update the categories
			//new UpdateCategoriesThread(0).run();
			// launches the main thread to download all the sku for each category 
			new DownloadSkusThread(0).run();
			
		} catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}
}
