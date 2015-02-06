package scraperThread.Category;
import general.MySQLUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import scraperThread.ScraperThreadSku;

public class ZaraCategoriesEng extends ScraperThreadSku {
		
	long sleepTime = 0;

	

	
	public void run() {
		//deleteCategory("BenettonSkuEng");		
		
		Connection input = null;
		Statement statement_in = null;
		ResultSet rs = null;
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			input = DriverManager.getConnection("jdbc:mysql://127.0.0.1/cloth","root","ILCAVI87");
			//Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			//input = DriverManager.getConnection("jdbc:mysql://localhost/cloth","root","ILCAVI87");
			
			statement_in = input.createStatement();
			
			String sql = "SELECT * FROM websites WHERE threadname = 'ZaraSkuEng'";
			
			rs = statement_in.executeQuery(sql);

				       while (rs.next()) {

				    	   scrapZaraCategories(rs.getString("url"));

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
		System.out.println("finito");
		
		
		removeDuplicates(this.update);
		cleanDB();
		//cleanDB("/age/");
		//cleanDB("/eta/");
		//cleanDB("/edad/");
		//cleanParticulars();
		insertCategory();
	}
	
	public void scrapZaraCategories(String sites){
		
		WebClient webClient = null;
		
		try {
			webClient = new WebClient(BrowserVersion.FIREFOX_24, "proxy.iese.org", 8080);
			//webClient = new WebClient(BrowserVersion.FIREFOX_24);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			System.out.println(sites);
			HtmlPage category = webClient.getPage(sites);
			//System.out.println(category.asXml());
			/*
			 * Step 1: Parse product code	 
			 */
			
			ArrayList<HtmlElement> skuList = (ArrayList<HtmlElement>)category.getByXPath("//*/li[contains(@id,'menuItemData')]");
																						
			//ul id="mainNavigationMenu"
		
			DomNode imm = skuList.get(0);	
			
			//System.out.println(imm.asXml());
			
			ArrayList<HtmlElement> prova = (ArrayList<HtmlElement>) imm.getByXPath("//*/a");

			
			for(int i = 0; i <=skuList.size();i++){
								
				if (prova.get(i).getAttribute("href").contains("/sale/") || prova.get(i).getAttribute("href").contains("/collection")){
										
					category = webClient.getPage(prova.get(i).getAttribute("href"));					
					
					try{
						//just for current selected, sometime happens
						
						ArrayList<HtmlElement> catS = (ArrayList<HtmlElement>) category.getByXPath("//ul/li[@class='current selected']");
						
						String temp =  catS.get(0).asXml();
						
						temp = temp.split("href=")[1].split(">")[0].replace('"',' ').replace(" ", "");
						
						this.update.add("INSERT INTO categories(url, threadname, title) "+
								"SELECT * FROM (SELECT '"+temp+"', 'ZaraSkuEng', '"+MySQLUtils.mysql_real_escape_string(catS.get(0).asText())+"') AS tmp "+
								"WHERE NOT EXISTS ("+
								"SELECT url FROM categories WHERE url = '"+temp+"') LIMIT 1;");
						
					}catch(Exception e ){
						
						//do nothing go on
					}
					
					//for all the others
					
					ArrayList<HtmlElement> cat = (ArrayList<HtmlElement>) category.getByXPath("//ul/li[@class=' ']");					
					
					for(int x = 0; x < cat.size();x++){
						
						String temp =  cat.get(x).asXml();	
						
						temp = temp.split("href=")[1].split(">")[0].replace('"',' ').replace(" ", "");
						
						this.update.add("INSERT INTO categories(url, threadname, title) "+
								"SELECT * FROM (SELECT '"+temp+"', 'ZaraSkuEng', '"+MySQLUtils.mysql_real_escape_string(cat.get(x).asText())+"') AS tmp "+
								"WHERE NOT EXISTS ("+
								"SELECT url FROM categories WHERE url = '"+temp+"') LIMIT 1;");
						
					}

				}

			}
			
		
			}catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(webClient != null) webClient.closeAllWindows();
			
		}
	}
	
	private ArrayList<String> removeDuplicates(ArrayList<String> list) {

		// Store unique items in result.
		ArrayList<String> result = new ArrayList<String>();

		// Record encountered Strings in HashSet.
		HashSet<String> set = new HashSet<String>();

		// Loop over argument list.
		for (String item : list) {

		    // If String is not in set, add it to the list and the set.
		    if (!set.contains(item)) {
			result.add(item);
			set.add(item);
		    }
		}
		return result;
	    }
	
	private void cleanDB(){
		
		this.update.add("DELETE FROM categories " +
				"WHERE title = '#zaradaily';");

	System.out.println("cancellato ");
	}
	
	private void cleanParticulars(){
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/koritsi.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/agori.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/toddler-agori.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/toddler-koritsi.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/baby-koritsi.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/ilikia/baby-agori.html%';");		
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/menina.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/menino.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/toddler-menino.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/toddler-menina.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/bebe-menina.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/idade/bebe-menino.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-devochek.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-malychikov.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-malychikov-toddler.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-devochek-toddler.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-devochek-baby.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/vozrast/dlya-malychikov-baby.html%';");
		this.update.add("DELETE FROM categories " +
		"WHERE url LIKE '%/category/view/%';");
		
		
		System.out.println("cancellato i particolari");
	}
	
}




