package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Stock implements Serializable {

/*
 * This class sets up a Stock object.
 * Manages a stock object that holds all of its information: 
 * data (date, open, high, low, close, adj. close, and volume), name, ticker, 
 * 1 year volatility, etc�
 */
	
	private static final long serialVersionUID = 1L;

	
	// Thread-safety not needed - use arraylist instead of Vector
	private URL url = null;
	private String ticker;
	private String name;
	private ArrayList<Double> closeValues;
	private ArrayList<Object[]> data;
	private ArrayList<double[]> movAvgs = new ArrayList<double[]>();
	private ArrayList<Object[]> returns = new ArrayList<Object[]>();
	private double annReturn;
	private ArrayList<Object[]> monthReturns = new ArrayList<Object[]>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private double sharpe = -100;
	private double volatility = -100;
	private int numShares = 0;
	
	
	public Stock(String Name, YahooFinanceHttp information) {
		this.ticker = Name;
		this.setData(information.getData());
		this.setCloseValues(information.getClose());
		this.calcMovAvgs();
		this.calcReturns();
		this.calcAnnualReturn();
	}
	
	public String getName() {
		try {
			//Open Connection to Yahoo Finance
			String uString = "http://finance.yahoo.com/d/quotes.csv?s=" + ticker + "&f=sn";
			this.url = new URL(uString);
			URLConnection urlConn = url.openConnection();
			
			//Start Reading
			urlConn = this.url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlConn.getInputStream());
			BufferedReader buff = new BufferedReader(inStream);
			
			this.name = buff.readLine();		
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("No results found.");
		}
		return name;
	}
 	public String getTicker() {
		return ticker;
	}
	
 	
 	public void setNumShares(int num) {
 		numShares = num;
 	}
 	public int getNumShares() {
 		return numShares;
 	}
 	
 	
	public double getCurrentValue() {
		double value = (double) getData().get(0)[4];
		return value;		
	}
	public String getCurrentDate() {
		String date = (String) getData().get(0)[0];
		return date;
	}
	
	public String getStartDate() {
		int len = getData().size();
		len--;
		String date = (String) getData().get(len)[0];
		return date;
	}
	
	// Moving verages are: 15, 40, 90 days
	public void calcMovAvgs() {
		double avg15;
		double avg40;
		double avg100;
		int size = this.closeValues.size();
		for (int i = 0; i < size; i++) {
			double x = 0;
			int j=0;
			int y=0;
			for (j = i; j < i+15; j++) {
				if (j >= size)
					break;
				x += closeValues.get(j);
				y++;
			}
			avg15 = x/y;
			x = 0;
			y = 0;
			for (j = i; j < i+40; j++) {
				if (j >= size)
					break;
				x += closeValues.get(j);
				y++;
			}
			avg40 = x/y;
			x = 0;
			y = 0;
			for (j = i; j < i+100; j++) {
				if (j >= size)
					break;
				x += closeValues.get(j);
				y++;
			}
			avg100 = x/y;
			double[] array = {avg15, avg40, avg100};
			movAvgs.add(array);
			//System.out.println(Arrays.toString(array));
		}

		
	}
	public ArrayList<double[]> getMovAvgs() {
		return movAvgs;
	}
	
	//date, return (string), return (double)
	public void calcReturns() {
		//for date x, return is (close at x)/(close at x-1) - 1
		
		for (int i=0; i<data.size()-1; i++) {
			Object returns[] = new Object[3];	
			double dailyreturn;
			returns[0] = this.data.get(i)[0];
			dailyreturn = ((double)this.data.get(i)[4]/(double)this.data.get(i+1)[4]) - 1;
			dailyreturn = dailyreturn*100;
			returns[1] = String.format("%.2f%%", dailyreturn);
			returns[2] = dailyreturn;
			this.returns.add(returns);
		}
	}
	public ArrayList<Object[]> getReturns() {
		return returns;
	}
	
	// Calculates statistics
	private double get1YearReturnMean() {
		double sum = 0.0;
		for(int i=0; i<this.getReturns().size(); i++) {
			sum += (double)this.getReturns().get(i)[2];
		}
		return sum/getReturns().size();
	}
	private double getVarianceReturn() {
		double mean = get1YearReturnMean();
		double temp = 0;
		for (int i=0; i<this.getReturns().size(); i++) {
			temp += Math.pow(mean-(double)getReturns().get(i)[2], 2);
		}
		return temp/getReturns().size();
	}
	public double get1YearVolatility() {
		if (this.volatility == -100) {
			double result = Math.sqrt(getVarianceReturn());
			this.volatility = result;
			return result;
		} else {
			return this.volatility;
		}
	}
	public double getSharpeRatio(double riskFree) {
		if (this.sharpe == -100){
			double effReturn = this.getAnnualReturn()-riskFree;
			double sharpe = effReturn/this.get1YearReturnMean();
			this.sharpe = sharpe;
			return sharpe;
		}
		else {
			return this.sharpe;
		}
	}
	
	//Calculates year-to-date returns
	public void calcAnnualReturn() {
		//Get date one year from today
		Calendar prevYear = Calendar.getInstance();
		prevYear.add(Calendar.YEAR, -1);
		String aYearAgo = sdf.format(prevYear.getTime());
		int i = 0;
		Calendar cal1 = Calendar.getInstance();
		Calendar nextdate = Calendar.getInstance();
		while (true) {
			if (i==(this.getData().size()-1))
				break;
			String date = (String)this.getData().get(i)[0];
			if (date.equals(aYearAgo))
				break;
			try {
				cal1.setTime(sdf.parse(date));
				nextdate.setTime(sdf.parse(aYearAgo));
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
			if (cal1.compareTo(nextdate) < 0)
				break;
			i++;
		}
		
		double todayClose = (double)this.getData().get(0)[4];
		double lastYearClose = (double)this.getData().get(i)[4];
		double Return = (todayClose/lastYearClose) - 1;
		this.annReturn = Return;
		
		
		
		
	}
	public double getAnnualReturn() {
		return annReturn;
	}	
	
	//Calculates month-to-date returns
	public void calcMonthReturns() {
		double todayClose = this.getCloseValues().get(0);
		Calendar prevMonth = Calendar.getInstance();
		//Do this 12 times
		for (int i=0; i<12; i++) {
			//Get date for the month to log into return
			String month = sdf.format(prevMonth.getTime());
			//Go to previous month
			prevMonth.add(Calendar.MONTH, -1);
			String datesought = sdf.format(prevMonth.getTime());
			//System.out.println(month + "    " + datesought);		
			
			
			
			//Get the close of a month ago.
			int j=0;
			Calendar cal1 = Calendar.getInstance();
			Calendar nextDate = Calendar.getInstance();
			while (true) {
				if (j==(this.getData().size()-1))
					break;
				String date1 = (String)this.getData().get(j)[0];
				if (date1.equals(datesought))
					break;
				try {
					cal1.setTime(sdf.parse(date1));
					nextDate.setTime(sdf.parse(datesought));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if (cal1.compareTo(nextDate) < 0)
					break;
				
				//System.out.println(month + "       " + (String)this.getData().get(j)[0]);
				j++;
			}
			double lastMonthClose = (double)this.getData().get(j)[4];
			
			//Calculate the annual return of one month
			double monthReturn = (todayClose/lastMonthClose) - 1;
			
			//Add the date and return to the list of returns
			Object[] array = {month, monthReturn};
			System.out.println(month + " - " +sdf.format(prevMonth.getTime()) + "   " + monthReturn);
			System.out.println();
			todayClose = lastMonthClose;
			this.monthReturns.add(array);
			prevMonth = nextDate;
		}
	}
	public ArrayList<Object[]> getMonthReturns() {
		return monthReturns;
	}
	
	//dohlcvac = date, open, high, low, close, volume, adj. close
	public void setData(ArrayList<Object[]> information) {
		//date, open, high, low, close, volume, adj. close
		this.data = information;
	}
	public ArrayList<Object[]> getData() {
		return data;
	}

	public void setCloseValues(ArrayList<Double> closeValues) {
		this.closeValues = closeValues;
	}
	public ArrayList<Double> getCloseValues() {
		return closeValues;
	}
	
}

