package app;

//import java.util.Calendar;

import java.util.Date;

public class UrlConstruct {

public	String tickerSymbol;
public String period11;
public String period22;
	
	// IGNORE: http://ichart.finance.yahoo.com/table.csv?s=AAPL&d=6&e=3&f=2013&g=d&a=8&b=7&c=1984&ignore=.csv
	
	//Create a constructor to set the data
	 public UrlConstruct(String tickerSymbol,Date date1,Date date2){
	
		 long period1 = date1.getTime()/1000;
		 long period2 = date2.getTime()/1000;

		 period11 = String.valueOf(period1);
		 period22 = String.valueOf(period2);
		// long unixTimestamp = date.getTime() / 1000;
		
		// System.out.println(date1);
		 
		//tickerSymbol
		this.tickerSymbol = tickerSymbol;
	//	tickerSymbol = "AAPL";
	}
	//Create a method to construct the URL given the startDate, endDate, tickerSymbol as input
	public String constructURL(){
		String baseURL= "https://query1.finance.yahoo.com/v7/finance/download/";

		//YahooFinance URL Reconstructed
	
		String urlStr = (baseURL + tickerSymbol+"?period1="+period11+"&period2="+period22+"&interval=1d&events=history");
		System.out.println(urlStr);
		return urlStr;
	}
} 

