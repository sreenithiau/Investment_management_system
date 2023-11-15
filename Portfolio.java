package app;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Portfolio implements Serializable {
	

	/*
	 * Manages a portfolio object that can hold numerous stocks; 
	 * the portfolio also manages the statistics of a specific collection of stocks.
	 */

	private static final long serialVersionUID = 1L;
	private ArrayList<Stock> portfolioOfStocks = new ArrayList<Stock>();				//List of Stocks objects
	private ArrayList<String> boughtStocksbyName = new ArrayList<String>();				//List of Stocks by name
	private ArrayList<Object[]> transactions = new ArrayList<Object[]>();				//Stock, Date, numShares, costPerShare
	private String name;
	private double totalSpent = 0;
	private double currentValue = 0;
	private int numberOfUniqueStocks = 0;
	public String SavingPath;															//For saving a Portfolio
	public String dateCreated;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private int numShares = 0;															//Number of total shares
	private Date startDate;																//Date the Portfolio was created
	private HashMap<String, Double> dateToValue = new HashMap<String, Double>();		//Maps dates (Strings) to PF values
	private HashMap<String, Double> dateToDrawdown = new HashMap<String, Double>();		//Maps dates (Strings) to PF Drawdown values

	
	public Portfolio(String pfname) {
		this.setName(pfname);
		Calendar date = Calendar.getInstance();
		String temp = sdf.format(date.getTime());
		dateCreated = temp;
	}
	
	// Clears the Portfolio
	public void obliterate() {
		//stocksNshares.clear();
		boughtStocksbyName.clear();
		transactions.clear();
		name = null;
		totalSpent = 0;
		currentValue = 0;
		numberOfUniqueStocks = 0;
		dateCreated = null;
		numShares = 0;
		dateToValue.clear();
		dateToDrawdown.clear();
	}
	
	public boolean isEmpty() {
		if (numberOfUniqueStocks == 0) {
			return true;
		}
		return false;
	}
	
	public int getTotalNumShares() {
		return numShares;
	}
	
	public double getYearRiskFree() {
		try {
			URL url = new URL("http://www.federalreserve.gov/datadownload/Output.aspx?rel=H15&series=c5025f4bbbed155a6f17c587772ed69e&lastObs=&from=&to=&filetype=csv&label=include&layout=seriescolumn");
			URLConnection urlconn = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(urlconn.getInputStream());
			BufferedReader buf = new BufferedReader(inStream);
			String line;
			double result = 0;
			
			//Get rid of headers
			for (int i=0; i<6; i++) {
				buf.readLine();
			}
					
			while((line = buf.readLine()) != null) {
				String [] array = line.split("\\,");
				result = Double.parseDouble(array[1]);
			}
			return result;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public void removeStock(String ticker) {
		for (int i=0; i<portfolioOfStocks.size(); i++) {
			if (ticker.equals(portfolioOfStocks.get(i).getTicker())) {
				portfolioOfStocks.remove(i);
				return;
			}
		}
	}

	public ArrayList<Object[]> getTransactions() {
		return transactions;
	}
	
	public Object[] getMaxValue() {

		Object[] result = new Object[2];
		double maxValue = 0;
		Calendar currentDate = Calendar.getInstance(); //current date

		int j=0;
		System.out.println(startDate);
		System.out.println(currentDate.getTime());
		while (currentDate.getTime().after(startDate)) {
			double value = 0;
			//Date to store in dateToValue
			//On Date j what is the value of PF:
			for (int i=0; i<numberOfUniqueStocks; i++) {
				int numstocks = portfolioOfStocks.get(i).getNumShares();
				value += numstocks*(double)portfolioOfStocks.get(i).getData().get(j)[4];

			}
			
			//Set dateToValue
			dateToValue.put((String)portfolioOfStocks.get(0).getData().get(j)[0], value);
			
			if (value>maxValue) {
				maxValue = value;
				String maxDate = (String) portfolioOfStocks.get(0).getData().get(j)[0];
				result[0] = maxDate;
				result[1] = value;
			}
			
			//Set dateToDrawdown
			double drawdown = (-1)*(1-(value/maxValue))*100;
			System.out.println((String)portfolioOfStocks.get(0).getData().get(j)[0] + "  " + drawdown);
			System.out.println((String)portfolioOfStocks.get(0).getData().get(j)[0] + "  " + value);
			System.out.println(".......");
			dateToDrawdown.put((String)portfolioOfStocks.get(0).getData().get(j)[0], drawdown);
			
			try {
				currentDate.setTime(sdf.parse((String) portfolioOfStocks.get(0).getData().get(j)[0]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			j++;
		}
		return result;
	}
	
	public HashMap<String, Double> getDateToValue() {
		return dateToValue;
	}
	public HashMap<String, Double> getDateToDrawdown() {
		return dateToDrawdown;
	}

	public String getDateCreated() {
		return dateCreated;
	}
	
	public String getCurrentDate() {
		String date = sdf.format(Calendar.getInstance());
		return date;
	}
	
	public ArrayList<Stock> getPortfolio() {
		return portfolioOfStocks;
	}
	public void setPortfolio(ArrayList<Stock> stocks) {
		this.portfolioOfStocks = stocks;
	}

	public String[] getStockTicker() {
		String[] result = new String[numberOfUniqueStocks];
		for (int i=0; i<numberOfUniqueStocks; i++) {
			String name = portfolioOfStocks.get(i).getTicker();
			result[i] = name;
		}
		return result;
	}
	
	public int getNumStocks() {
		return numberOfUniqueStocks;
	}
	
	//return -1 if stock already held, 1 if not
	public int buyStock(Stock stock, int numShares, Date date1) {
		String date = sdf.format(date1);
		String ticker = stock.getTicker();
		if (numberOfUniqueStocks==0) {
			startDate = date1;
		}
		if (hasStock(stock.getName())==true) {
			
			//Get Cost
			double cost = stock.getCurrentValue()*numShares;
			
			//original numShares
			int originalNUM = getStockbyTicker(ticker).getNumShares();
			
			//New numShares
			int newNUM = originalNUM + numShares;
			
			//update shares
			getStockbyTicker(ticker).setNumShares(newNUM);

			//Log the transaction
			Object[] transaction = {stock, date, numShares, stock.getCurrentValue()};
			transactions.add(transaction);
			
			//Update Total Spent
			totalSpent += cost;
			
			//Update total number of shares
			this.numShares += numShares;
			
			//Update current portfolio value
			currentValue += stock.getCurrentValue() * numShares;
			
			
			return -1;
		}
		double cost = stock.getCurrentValue()*numShares;
		stock.setNumShares(numShares);
		//stocksNshares.put(stock.getTicker(), numShares);

		//Log the transaction
		Object[] transaction = {stock, date, numShares, stock.getCurrentValue()};
		transactions.add(transaction);
		
		//Add to portfolio
		portfolioOfStocks.add(stock);
		
		//Update Num of Unique Stocks
		numberOfUniqueStocks++;
		
		//Updates stockNshares
		//stocksNshares.put(stock.getTicker(), numShares);
		
		//Update Total Spent
		totalSpent += cost;
		
		//Update Stock by Name
		boughtStocksbyName.add(stock.getName());
		
		//Update total number of shares
		this.numShares += numShares;
		
		//Update current portfolio value
		currentValue += stock.getCurrentValue() * numShares;
		return 1;
	}
	
	public boolean hasStock(String stock) {
		if (boughtStocksbyName.contains(stock)) {
			return true;
		}
		return false;
	}
	
	//portfolio, name, totalSpent, currentValue, boughStocksbyName,
	//numShares, numberOfUniqueStocks, transactions, dateCreated, stocksNshares
	public boolean save() {
		Object[] allPortfolio = new Object[11];
		allPortfolio[0] = this.getPortfolio();
		allPortfolio[1]	= this.name;
		allPortfolio[2] = this.totalSpent;
		allPortfolio[3] = this.currentValue;
		allPortfolio[4] = this.boughtStocksbyName;
		allPortfolio[5] = this.numShares;
		allPortfolio[6] = this.numberOfUniqueStocks;
		allPortfolio[7] = this.transactions;
		allPortfolio[8] = this.dateCreated;
		allPortfolio[9] = this.dateToValue;
		allPortfolio[10] = this.startDate;
		//allPortfolio[11] = this.getStocksNShares();
		
		
		try {
			//Gets the path from the GUI
			System.out.println(SavingPath);
			FileOutputStream fos = new FileOutputStream(SavingPath + ".xml");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			//Saves the array
			oos.writeObject(allPortfolio);
			oos.flush();
			oos.close();
		} catch (IOException e) {
			System.err.println("Error Saving File.");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public boolean load(File file) {
		System.out.println(file.getAbsolutePath());
		try {
			//Gets the file from the GUI
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream oip = new ObjectInputStream(fis);
			Object[] x = (Object[]) oip.readObject();
			
			//Get all the information
			setPortfolio((ArrayList<Stock>) x[0]);
			this.name = (String) x[1];
			this.totalSpent = (double) x[2];
			this.currentValue = (double) x[3];
			this.boughtStocksbyName = (ArrayList<String>) x[4];
			this.numShares = (int) x[5];
			this.numberOfUniqueStocks = (int) x[6];
			this.transactions = (ArrayList<Object[]>) x[7];
			this.dateCreated = (String) x[8];
			this.dateToValue = (HashMap<String, Double>) x[9];
			this.startDate = (Date) x[10];
			//setStocksNShares((HashMap<String, Integer>) x[12]);
			
			
			oip.close();
		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}
	
	public double getCurrentValue() {
		return currentValue;
	}
	
	public double getTotalSpent() {
		return totalSpent;
	}
	
	public double getBalance() {
		return (currentValue - totalSpent);
	}
	
	
	//UpdatePF needs to get called every time the program starts
	//So you can assume that when you call any function
	//UpdatePF has already been called
	public void updatePf() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.add(Calendar.YEAR, -1);
		System.out.println(sdf.format(startDate.getTime()));
		System.out.println(sdf.format(endDate.getTime()));
		this.currentValue = 0;
		
		//For each stock
		for (int i=0; i<portfolioOfStocks.size(); i++) {
						
			//Get Stock's Most recent Data
			YahooFinanceURLConstruct KEY = new YahooFinanceURLConstruct(startDate, endDate, portfolioOfStocks.get(i).getTicker());
			YahooFinanceHttp dohlcvac = new YahooFinanceHttp(KEY.constructURL());
			
			//Replace stock's data
			portfolioOfStocks.get(i).setData(dohlcvac.getData());
			portfolioOfStocks.get(i).calcMovAvgs();
			portfolioOfStocks.get(i).setCloseValues(dohlcvac.getClose());
			this.currentValue += portfolioOfStocks.get(i).getCurrentValue()*portfolioOfStocks.get(i).getNumShares();
		}
	}

	public Stock getStockbyName(String name) {
		int i=0;
		while (!(portfolioOfStocks.get(i).getName().equals(name))) {
			i++;
			if (i==portfolioOfStocks.size()) {
				return null;
			}
		}
		return portfolioOfStocks.get(i);
	}
	public Stock getStockbyTicker(String ticker) {
		int i=0;
		while (!(portfolioOfStocks.get(i).getTicker().equals(ticker))){
			i++;
			if (i==portfolioOfStocks.size()) {
				return null;
			}
		}
		return portfolioOfStocks.get(i); 
		}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
