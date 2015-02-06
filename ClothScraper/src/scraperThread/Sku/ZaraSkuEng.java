
package scraperThread.Sku;

import general.MySQLUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

import general.DownloadSkusThread;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import scraperThread.ScraperThreadSku;

// this thread downloads the skus for Benetton

public class ZaraSkuEng extends ScraperThreadSku {
	
	private String prodSku, price, country, special_price, discount,color, description,html;
	String currency;
	private ArrayList<HtmlElement> qualcosa = new ArrayList<HtmlElement>();
	private List sizes;
	private HtmlPage page;
	
	
	public ZaraSkuEng() {
		
	}
	
	@Override
	public void run() {

		
		scrapSkuList();
		
	}
	
	// Downloads the list of SKUs from the specific category page associated to this thread and then downloads one by one
	
	public void scrapSkuList() {
				
		try {
							
			GetPage(this.getUrl(),0);
			
			ArrayList<HtmlElement> skuList = (ArrayList<HtmlElement>)this.page.getByXPath("//a[@class='name item']");
			int i = 1;																	
			for(HtmlElement skuLink : skuList){
				System.out.println(i + " - "+skuLink.getAttribute("href"));
				i++;
			}
			
			for(HtmlElement skuLink : skuList) {
				
				long start = System.currentTimeMillis();
				
				scrapSku(skuLink.getAttribute("href"), this.getIdCategory());
				
				long end = System.currentTimeMillis();
				System.out.println((end-start)+" ms.");
				
			}
			insertDB();
			
		} catch(Exception e) {
			
			e.printStackTrace();
		}		
	}
	
	// Downloads info from a specific sku
	
	public void scrapSku(String url, int idCategory) {
		
		WebClient webClient = null;
		
		try {
			
			webClient = new WebClient(BrowserVersion.FIREFOX_24, "proxy.iese.org", 8080);
			//webClient = new WebClient(BrowserVersion.FIREFOX_24);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			this.page = webClient.getPage(url);
						
			/*
			 * Step 1: Parse product code	 
			 */

			getProductCode(page);	
			
			
			// Step 2: check in sku table if record with same url and same product_code exists
			
			if (checkProduct(prodSku, url) != true){
				
				// * Step 4: enter sizes				
				// * Step 5: enter price
								
				// get colors 
				
				getColors(page);
				
				// get description
				
				getDescription(page);
				
				// get image
												
				
				// * Step 7: launch inserts (this.insertDB())
				//adding sku
				addInserts("INSERT INTO "
						+ "skus(idcategory, url, sku_code, entry_date, description, color, fabric, country) "
						+ "VALUES('"+getIdCategory()+"','"+url+"','"+this.prodSku+"',NOW(),'"+MySQLUtils.mysql_real_escape_string(this.description)+"','"+this.color+"','Zara', '"+this.country+"')");
				
				getPrice(page,"C9D3H3R3");

				//SCRIVERE ADD INSERT PER PAGINA HTML
				
				String sito = MySQLUtils.mysql_real_escape_string(page.asXml());
				
				addInserts("INSERT INTO "
						+ "htmls(idsku, html, entry_date) "
						+ "VALUES('C9D3H3R3','"+sito+"',NOW())");
				
				//SCRIVERE ADD INSERT PER SIZES E PRENDERE ANCHE SIZE NON AVAILABLE				
				
				//TODO da qui con zara
				
				getSizes(page,"C9D3H3R3");
				
				getImage(page);
				
				getCountry(page);//PROVA PER COUNTRY
				
			}else{

				//SCRIVERE INSERT PREZZI
				
				getPrice(page, Integer.toString(this.numb));
				
				//SCRIVERE INSERT SIZES E NON AVAILABLE
				
				getSizes(page, Integer.toString(this.numb));
				
				String sito = MySQLUtils.mysql_real_escape_string(page.asXml());
				
				addInserts("INSERT INTO "
						+ "htmls(idsku, html, entry_date) "
						+ "VALUES('"+this.numb+"','"+sito+"',NOW())");
			}
				
			
			
			System.out.println(url);
			
		
			}catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(webClient != null) webClient.closeAllWindows();
			
		}
	}
		
	
	/*
	 * 
	 *  HERE YOU CAN FIND ALL THE METHODS 
	 * 
	 *  THAT SCRAPES THE DATAS
	 * 
	 */
	

		public String getProductCode(HtmlPage page){
			
			try{
			
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/p[contains(@class,'reference')]");
						
			return this.prodSku = this.qualcosa.get(0).asText().replace("Ref. ", "") ;
			
			}catch(Exception e){
				
				return this.prodSku = "";
			}
			
			}
		
		
		
		public void getSizes(HtmlPage page, String iders) throws Exception{
			
			
		try {
		
		this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/tr[contains(@class,'product-size   gaTrack gaViewEvent')]");

		} catch(Exception e){
						
		}
		
		for(int i = 0; i<this.qualcosa.size(); i++) {
			addInserts("INSERT INTO "
					+ "sizes(idsku, size, avail, entry_date) "
					+ "VALUES('"+iders+"','"+this.qualcosa.get(i).asText().replaceAll("\\s", "")+"', 1,NOW())");	
			
		}
		
		try{
		
		this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/div[contains(@class,'product-size disabled  gaTrack gaViewEvent')]");
				
		} catch(Exception e){
			
			
		}
		
		for(int i = 0; i<this.qualcosa.size(); i++) {
			
			addInserts("INSERT INTO "
					+ "sizes(idsku, size, avail, entry_date) "
					+ "VALUES('"+iders+"','"+this.qualcosa.get(i).asText().replaceAll("\\s","")+"', 0,NOW())");
		}
		
		}
		
		public void getPrice(HtmlPage page, String iders){
			
				try{
				
				this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/span[@class='price']");
				String priceCurr = qualcosa.get(0).getAttribute("data-price");
				
				this.price = priceCurr.replace(",",".");
				this.currency =priceCurr.replaceAll("[0-9]","").replace(".","").replace(",", "").replace(" ", "");
				
				this.currency = "'"+this.currency +"'";
				this.price= this.price.replaceAll("[^\\d.]", "");
				this.price = "'"+ this.price+"'";
				this.discount = this.special_price = "NULL";

				}
				
				//if we have an exception it means that we don't have the REGULAR PRICE tag so we have a discount !
				
				catch(Exception e){
					
				try{
					
					//get old price
					
					this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/span[@class='line-through diagonal-line']");
					
					String priceCurr = qualcosa.get(0).getAttribute("data-price");
										
					this.price = priceCurr.replace(",",".");
					this.price= this.price.replaceAll("[^\\d.]", "");
					this.currency =priceCurr.replaceAll("[0-9]","").replace(".","").replace(",", "").replace(" ","");
						
					//get new price
					
					this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/span[@class='sale']");
					
					
					this.special_price = this.qualcosa.get(0).getAttribute("data-price").replace(" ", "");		
					
					this.special_price = this.special_price.replace(",", ".") +"'";
					this.special_price= this.special_price.replaceAll("[^\\d.]", "");
					this.currency = "'"+this.currency +"'";
					
					
					//get discount
															
					this.discount = Float.toString((Float.parseFloat(this.price) - Float.parseFloat(this.special_price)) / (Float.parseFloat(this.price)));
					
					double newKB = Math.round((Float.parseFloat(this.discount))*100.0);
					int disc =(int) Math.floor(newKB);
					
					this.discount = "'"+ disc +"'";
					this.price = "'"+ this.price+"'";
					this.special_price="'"+ this.special_price + "'";
					
				}
				catch(Exception e1){
					
					this.price =this.discount=this.special_price= "NULL";
				}
				
		
				}
				
				addInserts("INSERT INTO "
						+ "prices(idsku, price, currency, entry_date, special_price, discount)"
						+ "VALUES('"+iders+"', "+this.price+", "+this.currency +",NOW() ,"+this.special_price+","+this.discount + "%"+")");
				
		}
		
		public String getColors(HtmlPage page){
		
			try{
				
				this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/label[contains(@class,'colorEl')]");
				
				this.color = this.qualcosa.get(0).asText();
								
			}catch(Exception e1){
				
				this.color = "";
			}
			return this.color;
		}
			
		public String getDescription(HtmlPage page){
			
			try {
				
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/h1");
				
			return this.description = StringUtils.swapCase(this.qualcosa.get(0).asText());
		
			}catch(Exception e ){
				
			return this.description = "";
			}
		}
		
		public void getImage(HtmlPage page) throws IOException{
			
			try{
			
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div[contains(@class, 'media-wrap image-wrap imageZoom')]");
			
			DomNode imm = this.qualcosa.get(0);	
			
			for(HtmlElement image : this.qualcosa) {
				
				ArrayList<HtmlElement> imm2 = (ArrayList<HtmlElement>) imm.getByXPath("//*/div/img[@class='image-big gaViewEvent gaColorInfo sbdraggable draggableMain']");
				String link = imm2.get(0).getAttribute("data-zoom-url").split("\\?")[0];
				downloadImage("http:"+ link);
																	
			}
			
			}catch(Exception e){
				
				System.out.println("successo in "+page.getUrl());
				e.printStackTrace();
				
			}
	
		}

		
		public String getCountry(HtmlPage page){
			
			try {
				
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/nav[contains(@id,'countries')]");
			
			String[] country = this.qualcosa.get(0).asText().split("\\r?\\n");
			
			return this.country = this.qualcosa.get(0).asText();
			
			}catch(Exception e){
			
				e.printStackTrace();
			return this.country = "";
				
			}
			
		}
		
		
		/*<div class="country-selector">
        <a href="http://us.benetton.com/countries/" title="United States" class="select-country"><span class="flag flag-us">&nbsp;</span>United States<
		*/
		
		
		public HtmlPage GetPage(String url_page, int more) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
			more++;	
			WebClient webClient = null;
			
			try {
				
				webClient = new WebClient(BrowserVersion.FIREFOX_24, "proxy.iese.org", 8080);
				//webClient = new WebClient(BrowserVersion.FIREFOX_24);
				webClient.getOptions().setThrowExceptionOnScriptError(false);
				this.page = webClient.getPage(url_page);
				//webClient.waitForBackgroundJavaScriptStartingBefore(5000);	
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(webClient != null) webClient.closeAllWindows();
			
		}
		return this.page;
		
		}
}