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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import scraperThread.ScraperThreadSku;

// this thread downloads the skus for Benetton

public class BenettonSkuEng extends ScraperThreadSku {
	
	private String prodSku, price, country, special_price, discount,color, description,html;
	String currency;
	private ArrayList<HtmlElement> qualcosa = new ArrayList<HtmlElement>();
	private List sizes;
	private HtmlPage page;
	
	
	public BenettonSkuEng() {
		
	}
	
	@Override
	public void run() {

		
		scrapSkuList();
		
	}
	
	// Downloads the list of SKUs from the specific category page associated to this thread and then downloads one by one
	
	public void scrapSkuList() {
				
		try {
							
			checkIfMore(this.getUrl(),0);
			
			ArrayList<HtmlElement> skuList = (ArrayList<HtmlElement>)this.page.getByXPath("//a[contains(@id,'product_cover')]");
			
			for(HtmlElement skuLink : skuList){
				System.out.println(skuLink.getAttribute("href"));
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
						+ "VALUES('"+getIdCategory()+"','"+url+"','"+this.prodSku+"',NOW(),'"+MySQLUtils.mysql_real_escape_string(this.description)+"','"+this.color+"','Benetton', '"+this.country+"')");
				
				System.out.println("INSERT INTO "
						+ "skus(idcategory, url, sku_code, entry_date, description, color, fabric, country) "
						+ "VALUES('"+getIdCategory()+"','"+url+"','"+this.prodSku+"',NOW(),'"+MySQLUtils.mysql_real_escape_string(this.description)+"','"+this.color+"','Benetton', '"+this.country+"')");
				
				getPrice(page,"C9D3H3R3");

				//SCRIVERE ADD INSERT PER PAGINA HTML
				
				String sito = MySQLUtils.mysql_real_escape_string(page.asXml());
				
				addInserts("INSERT INTO "
						+ "htmls(idsku, html, entry_date) "
						+ "VALUES('C9D3H3R3','"+sito+"',NOW())");
				
				//SCRIVERE ADD INSERT PER SIZES E PRENDERE ANCHE SIZE NON AVAILABLE
				
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
			System.out.println("ECCEZIONE IN " + url);
			
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
			
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/span[contains(@class,'product-sku')]");
			
			String prodSku = qualcosa.get(0).asText().replace("Product code: ", "");
			
			return this.prodSku = this.qualcosa.get(0).asText().replace("Product code: ", "") ;
			
			}catch(Exception e){
				
				return this.prodSku = "";
			}
			}
		
		
		
		public void getSizes(HtmlPage page, String iders){
			
			
		try {
		
		this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/div[contains(@class,'swatch_active')]");
		
		this.qualcosa.addAll((Collection<? extends HtmlElement>) page.getByXPath("//*/div/div[@class='swatches_single-swatch simple_swatch']"));
		} catch(Exception e){
			
			
		}
		
		for(int i = 0; i<this.qualcosa.size(); i++) {
			addInserts("INSERT INTO "
					+ "sizes(idsku, size, avail, entry_date) "
					+ "VALUES('"+iders+"','"+this.qualcosa.get(i).asText().replace(" ", "")+"', 1,NOW())");			
		}
		
		try{
		
		this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/div[contains(@class,'swatch_disabled')]");
		
		this.qualcosa.addAll((Collection<? extends HtmlElement>) page.getByXPath("//*/div/div[@class='swatches_single-swatch simple_swatch']"));
		
		} catch(Exception e){
			
			
		}
		
		for(int i = 0; i<this.qualcosa.size(); i++) {
			
			addInserts("INSERT INTO "
					+ "sizes(idsku, size, avail, entry_date) "
					+ "VALUES('"+iders+"','"+this.qualcosa.get(i).asText().replace(" ","")+"', 0,NOW())");
		}
		
		}
		
		public void getPrice(HtmlPage page, String iders){
			
				try{
				this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/span[contains(@class,'regular-price')]");
				
				String priceCurr = qualcosa.get(0).asText().replace(" ","");
				
				this.price = priceCurr.replace(",",".");
				this.currency =priceCurr.replaceAll("[0-9]","").replace(".","").replace(",", "");
				
				
				this.currency = "'"+this.currency +"'";
				this.price= this.price.replaceAll("[^\\d.]", "");
				
				
				if (StringUtils.countMatches(this.price, ".") >= 2){
					
					this.price = this.price.replace(".", "");
					this.price = this.price.substring(0, this.price.length()-2) + "." + this.price.substring(this.price.length()-2,this.price.length());
					
				}
				
				this.price = "'"+ this.price+"'";
				this.discount = this.special_price = "NULL";
				
				}
				
				//if we have an exception it means that we don't have the REGULAR PRICE tag so we have a discount !
				
				catch(Exception e){
					
				try{
					
					//get discount
					
					this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/p[contains(@class,'percentage-discount')]");
					
					this.discount = this.qualcosa.get(0).asText().substring(1);
					
					//get old price
					
					this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/p[contains(@class,'old-price')]");
					
					String priceCurr = qualcosa.get(0).asText().replace(" ","");
					
					this.price = priceCurr.replace(",",".");
					this.price= this.price.replaceAll("[^\\d.]", "");
					this.currency =priceCurr.replaceAll("[0-9]","").replace(".","").replace(",", "");
					
										
					//get new price
					
					this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/p[contains(@class,'special-price')]");
					
					this.special_price = this.qualcosa.get(0).asText().replace(" ", "");
					
					this.special_price = this.special_price.replace(",", ".") +"'";
					this.special_price= this.special_price.replaceAll("[^\\d.]", "");
					
					
					if (StringUtils.countMatches(this.special_price, ".") >= 2){
						
						this.special_price = this.special_price.replace(".", "");
						this.special_price = this.special_price.substring(0, this.special_price.length()-2) + "." + this.special_price.substring(this.special_price.length()-2,this.special_price.length());
											
					}
					
					if (StringUtils.countMatches(this.price, ".") >= 2){
						
					this.price = this.price.replace(".", "");
					this.price = this.price.substring(0, this.price.length()-2) + "." + this.price.substring(this.price.length()-2,this.price.length());
										
					}
					
					this.special_price= "'"+this.special_price+"'";
					
					
					this.currency = "'"+this.currency +"'";
					this.price = "'"+ this.price+"'";
					this.discount = "'"+ this.discount+"'";
				}
				catch(Exception e1){
					
					this.price = "NULL";
					
				}

					
				}
				
				addInserts("INSERT INTO "
						+ "prices(idsku, price, currency, entry_date, special_price, discount)"
						+ "VALUES('"+iders+"', "+this.price+", "+this.currency +",NOW() ,"+this.special_price+","+this.discount+")");
				
				System.out.println("INSERT INTO "
						+ "prices(idsku, price, currency, entry_date, special_price, discount)"
						+ "VALUES('"+iders+"', "+this.price+", "+this.currency +",NOW() ,"+this.special_price+","+this.discount+")");
				
				
		}
		
		public String getColors(HtmlPage page){
		
			try{
				
				this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/p[contains(@class,'attribute_color')]");
				
				this.color = this.qualcosa.get(0).asText().replace("Color: ", "");
				
			}catch(Exception e1){
				
				this.color = "";
			}
			return this.color;
		}
			
		public String getDescription(HtmlPage page){
			
			try {
				
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/p[contains(@class,'shipping_disclaimer_productpage static-text')]");
			
			return this.description = this.qualcosa.get(0).asText();
		
			}catch(Exception e ){
				
			return this.description = "";
			}
		}
		
		public void getImage(HtmlPage page) throws IOException{
			
			try{
			
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/a[@class='cloud-zoom-gallery item']");
			
			}catch(Exception e){
				
			}
			
			for(HtmlElement image : this.qualcosa) {
									
				downloadImage(image.getAttribute("href"));
													
			}
					
		}

		
		public String getCountry(HtmlPage page){
			
			try {
				
			this.qualcosa = (ArrayList<HtmlElement>) page.getByXPath("//*/div/a[contains(@class,'select-country')]");
			
			return this.country = this.qualcosa.get(0).getAttribute("title");
			
			}catch(Exception e){
				
			return this.country = "";
				
			}
			
		}
		
		
		/*<div class="country-selector">
        <a href="http://us.benetton.com/countries/" title="United States" class="select-country"><span class="flag flag-us">&nbsp;</span>United States<
		*/
		
		
		public HtmlPage checkIfMore(String url_page, int more) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
			more++;	
			WebClient webClient = null;
			
			try {
				
				webClient = new WebClient(BrowserVersion.FIREFOX_24, "proxy.iese.org", 8080);
				//webClient = new WebClient(BrowserVersion.FIREFOX_24);
				webClient.getOptions().setThrowExceptionOnScriptError(false);
				this.page = webClient.getPage(url_page);
				webClient.waitForBackgroundJavaScriptStartingBefore(8000);
			
			try{	

				ArrayList<HtmlElement> qualcosa2 = (ArrayList<HtmlElement>) this.page.getByXPath("//*/input[@style='display: none;']");
				
				System.out.println(qualcosa2.get(0).asXml());
			
			}catch (Exception e){

					System.out.println(url_page +" - tentativo "+more);
					if (more == 1) {
						checkIfMore(url_page+"#more"+more, more);
					}
					else{
						checkIfMore(url_page.substring(0,url_page.length() - Integer.toString(more-1).length())+more, more);

					}
					
				}
			
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(webClient != null) webClient.closeAllWindows();
			
		}
		return this.page;
		
		}
}