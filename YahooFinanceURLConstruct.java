package app;
import java.util.Calendar;
public class YahooFinanceURLConstruct {
	
/*
 * Creates a URL for Yahoo Finance HTTP.
 */
	
	
	//Data
	public	String tickerSymbol;
	public String period11;
	public String period22;
	

	// IGNORE: http://ichart.finance.yahoo.com/table.csv?s=AAPL&d=6&e=3&f=2013&g=d&a=8&b=7&c=1984&ignore=.csv
	
	//Create a constructor to set the data
	 public YahooFinanceURLConstruct(Calendar date1,Calendar date2,String tickerSymbol){
	
		  try {
	            long period1 = date1.getTimeInMillis() / 1000;
	            long period2 = date2.getTimeInMillis() / 1000;

	            this.period11 = String.valueOf(period1);
	            this.period22 = String.valueOf(period2);
	        } catch (NullPointerException e) {
	            // Handle the case where date1 or date2 is null
	            e.printStackTrace(); // or log the error
	        }
	        this.tickerSymbol = tickerSymbol;
	    }
	//Create a method to construct the URL given the startDate, endDate, tickerSymbol as input
		public String constructURL(){
			String baseURL= "https://query1.finance.yahoo.com/v7/finance/download/";

			//YahooFinance URL Reconstructed
		
			String urlStr = (baseURL + tickerSymbol+"?period1="+period11+"&period2="+period22+"&interval=1d&events=history");
			//System.out.println(urlStr);
			return urlStr;
		}
		
	
} 
