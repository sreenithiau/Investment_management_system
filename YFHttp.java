package app;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class YFHttp implements Serializable {
	      
/*
 * This class connects to Yahoo Finanace using the url constructor
 * in YahooFinanceURLConstruct.java and obtains the data sought.
 */	
	
	YFHttp(String urlStr){
	      try {
	    	//Open Connection the Yahoo Finance URL
			URL url  = new URL(urlStr);

		    //Start Reading
			URLConnection urlConn = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
			BufferedReader buff= new BufferedReader(inStream);
			String stringLine;
			
			buff.readLine(); //Read the firstLine. This is the header. 
			
			
				while((stringLine = buff.readLine()) != null) //While not in the header
				{
					  
					  String [] dohlcav = stringLine.split(","); //date, ohlc, adjustedclose
					    
					    String date = dohlcav[0];
					    double open = Double.parseDouble(dohlcav[1]);
		                double high = Double.parseDouble(dohlcav[2]);
		                double low = Double.parseDouble(dohlcav[3]);
		                double close = Double.parseDouble(dohlcav[4]);
		                
		                double adjClose = Double.parseDouble(dohlcav[5]);
		                long volume = Long.parseLong(dohlcav[6]);
		                
		                
		                Object data[] = new Object[] {date, open, high, low, close, volume, adjClose};
		                information.add(data);
		                
		                
		                //Set the Data
		                dateStrList.add(date);
		                openList.add(open);
		                highList.add(high);
		                lowList.add(low);
		                closeList.add(close);
		                volumeList.add(volume);
		                adjCloseList.add(adjClose);                        
				}
				
			}catch (MalformedURLException e) {
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
				System.out.println("No results found.");	
			}	          
	   } 
	  
	     //return everything
	     public ArrayList<Object[]> getData() {
	    	 //dohlcvac
	    	 //date, open, high, low, close, volume, adj. close
	    	 return information;
	     }
	     
	     public ArrayList<String> getDate(){
	    	 return dateStrList;
	     }
	     public ArrayList<Double> getOpen(){
	    	 return openList;
	     }
	     public ArrayList<Double> getHigh(){
	    	 return highList;
	     }
	     public ArrayList<Double> getLow(){
	    	 return lowList;
	     }
	     public ArrayList<Double> getClose(){
	    	 return closeList;
	     }
	     public ArrayList<Double> getAdjClose(){
	    	 return adjCloseList;
	     }
	     public ArrayList<Long> getVolume(){
	    	 return volumeList;
	     }

	     private static final long serialVersionUID = 1L;

			//private Object[] information = new Object[7];
			private ArrayList<Object[]> information = new ArrayList<Object[]>();
			
			private ArrayList<String> dateStrList = new ArrayList<String>();
			private ArrayList<Double> openList = new ArrayList<Double>();
			private ArrayList<Double> lowList = new ArrayList<Double>();
			private ArrayList<Double> highList = new ArrayList<Double>();
			private ArrayList<Double> closeList = new ArrayList<Double>();
			private ArrayList<Long> volumeList = new ArrayList<Long>();
			private ArrayList<Double> adjCloseList = new ArrayList<Double>();	     
	     
}