package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.jfree.ui.RefineryUtilities;


public class GUI implements ActionListener{
	
	/*
	 * This class manages all the buttons and the GUI interface of the program.
	 */
		
	public void loadView() {
		
		//update PF
		portfolio.updatePf();
		
		//Set time for DEMO
		//enddate.add(Calendar.MONTH, -1);
		startdate.add(Calendar.YEAR, -1);
		//startdate.add(Calendar.MONTH, -1);
		
		
		//set up frame
		frame = new JFrame("Portfolio Manager");
		frame.setSize(430, 300);
		frame.setMinimumSize(new Dimension(400, 300));
		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		stockframe = new JFrame("Stock Manager");
		stockframe.setSize(370, 400);;
		stockframe.setMaximumSize(new Dimension(370, 400));
		stockframe.setLayout(new FlowLayout());
		stockframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		pfFrame = new JFrame("Portfolio Manager");
		pfFrame.setSize(480, 400);
		pfFrame.setMaximumSize(new Dimension(500, 400));
		pfFrame.setLayout(new FlowLayout());
		pfFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				
		searchStock.addActionListener(this);
		searchAnother.addActionListener(this);
		buyStock.addActionListener(this);
		movAverages.addActionListener(this);
		viewReturns.addActionListener(this);
		sellStock.addActionListener(this);
		removeStock.addActionListener(this);
		viewSharpe.addActionListener(this);
		loadPF.addActionListener(this);
		viewPF.addActionListener(this);
		savePF.addActionListener(this);
		information.addActionListener(this);
		Quit.addActionListener(this);
		whatToDo.addActionListener(this);
		seeDetails.addActionListener(this);
		viewTransactions.addActionListener(this);
		viewInvested.addActionListener(this);
		viewBalance.addActionListener(this);
		viewPFDetails.addActionListener(this);
		Drawdown.addActionListener(this);
		update.addActionListener(this);
		Simulate.addActionListener(this);
		changeName.addActionListener(this);
		clear.addActionListener(this);
		
		stockframe.add(searchAnother);
		stockframe.add(buyStock);
		stockframe.add(viewSharpe);
		stockframe.add(viewReturns);
		stockframe.add(movAverages);
		stockframe.add(whatToDo);
		stockframe.add(seeDetails);
		
		//pfFrame.add(sellStock);
		//pfFrame.add(removeStock);
		pfFrame.add(viewBalance);
		pfFrame.add(viewInvested);
		pfFrame.add(viewPFDetails);
		pfFrame.add(savePF);
		pfFrame.add(viewTransactions);
		pfFrame.add(Drawdown);
		pfFrame.add(update);
		pfFrame.add(changeName);
		pfFrame.add(clear);
			
		frame.add(searchStock);
		frame.add(loadPF);
		frame.add(viewPF);
		frame.add(information);
		frame.add(Quit);
		frame.add(Simulate);
		
		textDisplayMain.setText("Welcome!");
		textDisplayMain.setSize(new Dimension(400, 400));
		textDisplayMain.setBackground(Color.white);
		frame.add(textDisplayMain);
		
		textDisplayPF.setSize(new Dimension(400, 400));
		textDisplayPF.setBackground(Color.white);
		pfFrame.add(textDisplayPF);
		
		textDisplayStock.setSize(new Dimension(400, 400));
		textDisplayStock.setBackground(Color.white);
		stockframe.add(textDisplayStock);
		
		
		frame.setVisible(true);
		RefineryUtilities.positionFrameOnScreen(frame, 0.5, 0.15);
	}
	
	public void actionPerformed(ActionEvent event) {
		
		if (event.getSource() == clear) {
			portfolio.obliterate();
			viewPortfolio();
			textDisplayMain.setText("Welcome!");
		}
		
		if (event.getSource() == loadPF) {
			textDisplayMain.setText("Please wait while we load your Portfolio...");
			load();
		}
		
		if (event.getSource() == viewPF) {
			viewPortfolio();
			pfFrame.setVisible(true);
			RefineryUtilities.positionFrameOnScreen(pfFrame, 1, 0.15);
		}
		
		if (event.getSource() == changeName) {
			changeName();
		}
		
		if (event.getSource() == savePF) {
			save();
		}
		
		if (event.getSource() == update) {
			updatePF();
		}
		
		if (event.getSource() == Simulate) {
			simulate();
		}
		
		if (event.getSource() == viewTransactions) {
			viewTransactions();
		}
		
		if (event.getSource() == searchStock) {
			int x = searchForStock();
			if (x==0) {
				stockframe.setVisible(true);
				RefineryUtilities.positionFrameOnScreen(stockframe, 0.1, 0.15);
			}
		}
		
		if (event.getSource() == seeDetails) {
			seeStockDetails();
		}
		
		if (event.getSource() == searchAnother) {
			searchForStock();
		}
		
		if (event.getSource() == buyStock) {
			buy();
		}
		
		if (event.getSource() == viewReturns) {
			viewReturns();
		}
		
		if (event.getSource() == viewBalance) {
			viewBalance();
		}
		
		if (event.getSource() == viewInvested) {
			viewInvested();
		}
		
		if (event.getSource() == viewPFDetails) {
			viewPortfolio();
		}
		
		if (event.getSource() == Drawdown) {
			Drawdown();
		}
		
		if (event.getSource() == viewSharpe) {
			sharpe();
		}
				
		if (event.getSource() == movAverages) {
			movingAverages();
		}
		
		if (event.getSource() == information) {
			information();
		}
		
		if (event.getSource() == whatToDo) {
			//0 for today, 1 for yesterday, 2 for the day before, etc...
			whatToDo(0);
		}
		
		if (event.getSource() == Quit) {
			System.exit(0);
		}
	}
	
	
	//Main Frame
	private void simulate() {
		int days = 0;
		try {
			String input = JOptionPane.showInputDialog("How many days do you want to go back? (365 max)");
			if (input == null)
				return;
			days = Integer.parseInt(input);
		} catch (NumberFormatException exception) {
			simulate();
		}
		if (days < 0) {
			textDisplayMain.setText("You cannot go into the future");
			return;
		}
		days = days*(-1);
		enddate.add(Calendar.DATE, days);
		startdate.add(Calendar.DATE, days);
		textDisplayMain.setText(sdf.format(enddate.getTime()));
	}
	private void information() {
		Date date = new Date();
		String text = ("Author: Diego F Rincon Santana\nUNI: dfr2113\nHello \"" + System.getProperty("user.name") + "\" (if that is your real name)\nYou are using Java " + System.getProperty("java.version")
				+ "\nIt is currently: " + date + "\nAnd you are running this on: " + System.getProperty("os.name") +
				"\nThis program provides information and statistics about finantial securities\n" +
				"\nIf you're looking for more detailed information about how to\noperate this program please go to the ReadMe file.");
		textDisplayMain.setText(text);
	}
	private void load() {
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		int val = chooser.showOpenDialog(loadPF);
		if (val == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			System.out.println(file.getAbsolutePath());
			if (portfolio.load(file)==false) {
				textDisplayMain.setText("\"" + file.getName() +"\" was not loaded into the system. You may have already loaded it before.");
			} else {
				portfolio.updatePf();
				textDisplayMain.setText("Success! You have loaded \"" + portfolio.getName() + "\" into your system.\nYour Portfolio has also been updated.");
			}
		} else {
			textDisplayMain.setText("Operation cancelled.");
		}
	}
	
	
	//Portfolio Frame
	private void changeName() {
		String input = null;
		try {
			String prompt = String.format("Current name: \"%s\"", portfolio.getName());
			input = JOptionPane.showInputDialog(prompt);
			if (input == null)
				return;
		} catch (NumberFormatException exception) {
			changeName();
		}
		portfolio.setName(input);
		textDisplayPF.setText("Changed Portfolio's name to: " + input);
		sentinel = 1;
	}
	private void updatePF() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("Your Portfolio is empty");
			return;
		}
		portfolio.updatePf();
		Calendar today = Calendar.getInstance();
		enddate.setTime(today.getTime());
		startdate.setTime(enddate.getTime());
		startdate.add(Calendar.YEAR, -1);
		textDisplayMain.setText(sdf.format(enddate.getTime()));
		viewPortfolio();
	}
	private void viewInvested() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("Your Portfolio is empty");
			return;
		}
		double invested = portfolio.getTotalSpent();
		int numStocks = portfolio.getNumStocks();
		Stock[] stocks = new Stock[numStocks];
		String output = String.format("You have invested %.2f$ in the following stocks:\n\n", invested);
		for (int i=0; i<numStocks; i++) {
			stocks[i] = portfolio.getPortfolio().get(i); 
			output += stocks[i].getNumShares() + " shares of " + stocks[i].getName() + "\n";
		}
		textDisplayPF.setText(output);
		
		
	}
	private void viewBalance() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("Your Portfolio is empty");
			return;
		}
		double balance = portfolio.getBalance();
		double invested = portfolio.getTotalSpent();
		double value = portfolio.getCurrentValue();
		String output = String.format("Your current balance is: %.2f$\nYou invested: %.2f$\nThe current value of your portfolio is: %.3f$", balance, invested, value);
		textDisplayPF.setText(output);
	}
	private void save() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("You cannot save an empty Portfolio");
			return;
		}
		
		//Sets the chooser to accept only directories
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setSelectedFile(new File(portfolio.getName()));
		//Displays the chooser
		int save = chooser.showSaveDialog(savePF);
		//Body of the method
		if (save == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			//Sets the saving directory
			portfolio.SavingPath = file.getAbsolutePath();
		} else {
			return;
		}
		try {
			//Calls the portfolio save() method
			textDisplayMain.setText("Saving...");
			portfolio.save();
			textDisplayMain.setText("Success! You have saved your portfolio");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	private void Drawdown() {
		Object[] Max = portfolio.getMaxValue();
//		for (String date: portfolio.getDateToValue().keySet()) {
//			System.out.println(date+ "    " + portfolio.getDateToValue().get(date));
//		}
		System.out.println(Arrays.toString(Max));
		double drawdown = (1-(portfolio.getCurrentValue()/(double)Max[1]))*100;
		String output = String.format("Drawdown: %.2f%%\nMax attained on %s: %.2f$\nCurrent Value: %.2f$", drawdown, (String)Max[0], (double)Max[1], portfolio.getCurrentValue());
		textDisplayPF.setText(output);
		
		
		jFreeChart timeseries = new jFreeChart("Value", "DW", portfolio.getName() + " Value", portfolio.getDateToValue());
		jFreeChart timeseries2 = new jFreeChart("Drawdown", "DW", portfolio.getName() + " Drawdown", portfolio.getDateToDrawdown());
		timeseries.pack();
		timeseries2.pack();
		RefineryUtilities.positionFrameOnScreen(timeseries, 0.7, 0.85);
		RefineryUtilities.positionFrameOnScreen(timeseries2, 0.3, 0.85);
		timeseries.setVisible(true);
		timeseries2.setVisible(true);
		timeseries.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		timeseries2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	private void viewTransactions() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("Your Portfolio is empty");
			return;
		}
		JFrame frame = new JFrame("Portfolio");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		jTable newPane = new jTable(portfolio.getTransactions());
		newPane.setOpaque(true);
		frame.setContentPane(newPane);
		
		frame.pack();
		frame.setVisible(true);
	}
	private void viewPortfolio() {
		if (portfolio.isEmpty()) {
			textDisplayPF.setText("Your Portfolio is empty");
			return;
		}
		String output1 = "You have the following stocks:\n";
		for (int i = 0; i<portfolio.getPortfolio().size(); i++) {
			Stock stock = portfolio.getPortfolio().get(i);
			output1 += String.format("%d shares of %s\n", stock.getNumShares(), stock.getName());
		}
		
		String output2 = String.format("\n%s\nPorfolio's balance: %.2f$\nPortfolio's value: %.2f$\nTotal spent: %.2f$\n", sdf.format(enddate.getTime()), portfolio.getBalance(), portfolio.getCurrentValue(), portfolio.getTotalSpent());
		textDisplayPF.setText(output1 + output2);
	}
	
	
	
	//Stock Frame
	private void viewReturns() {

		stockHeld.calcAnnualReturn();
		stockHeld.calcMonthReturns();
		String output = "";
		output = String.format("Annual Return: %.3f%%\n\n", (100*stockHeld.getAnnualReturn()));
		for (int i=0; i<stockHeld.getMonthReturns().size(); i++) {
			output += String.format("Monthly Return %s,  %.3f%%\n", stockHeld.getMonthReturns().get(i)[0], (100*(double)stockHeld.getMonthReturns().get(i)[1]));
		}
		
		textDisplayStock.setText(output);
	}
	private void sharpe() {
		System.out.println(Double.toString(portfolio.getYearRiskFree()));
		System.out.println(Double.toString(stockHeld.getSharpeRatio(portfolio.getYearRiskFree())));
		double riskfree = stockHeld.getSharpeRatio(portfolio.getYearRiskFree());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(enddate.getTime());
		String output = String.format("The Sharpe Ratio of %s is: %.4f\n\n", stockHeld.getName(), riskfree);
		output += String.format("The risk free rate (1 year OIS) for %s is: %.4f\n", date, portfolio.getYearRiskFree());
		output += String.format("%s's volatility is: %.2f%%", stockHeld.getName(), stockHeld.get1YearVolatility());
		textDisplayStock.setText(output);
	}
	private void buy() {
		if (sentinel == 0) {
			String input = null;
			try {
				input = JOptionPane.showInputDialog("Please enter a name for your Portfolio");
				if (input == null)
					return;
			} catch (NumberFormatException exception) {
				buy();
			}
			portfolio.setName(input);
			sentinel = 1;
		}
		int numShares= -1;
		if (portfolio.hasStock(stockHeld.getName())) {
			try {
				Stock stock2 = portfolio.getStockbyTicker(stockHeld.getTicker());
				String prompt = String.format("You have %d number of shares of %s, how many more do you want to buy?", stock2.getNumShares(), stock2.getName());
				String input = JOptionPane.showInputDialog(prompt);
				if (input == null)
					return;
				numShares = Integer.parseInt(input);
			} catch (NumberFormatException exception) {
				buy();
			}
		} else {
			try {
				String input = JOptionPane.showInputDialog("How many shares of " + stockHeld.getTicker() + " would you like to buy?");
				if (input == null)
					return;
				numShares = Integer.parseInt(input);
			} catch (NumberFormatException exception) {
				buy();
			}
		}
		if (numShares<0) {
			textDisplayStock.setText("You cannot buy a negative amount of shares!");
			return;
		}
		
		int x = portfolio.buyStock(stockHeld, numShares, enddate.getTime());
		double total = numShares*stockHeld.getCloseValues().get(0);
		String output = String.format("You have bought %d shares of:\n%s at %.2f$ for %.2f$", numShares, stockHeld.getName(), stockHeld.getCloseValues().get(0), total);
		if (x==-1)
			textDisplayStock.setText("You already have that stock in your Portfolio!");
		textDisplayStock.setText(output);
	}
	private int searchForStock() {
		String stock = JOptionPane.showInputDialog("Search for ticker: ");
		if (stock == null)
			return -1;
		//textDisplayMain.setText("Searching stock details for " + stock);
		//Get data from Yahoo Finance
		//Calendar endDate = Calendar.getInstance();
		//Calendar startDate = Calendar.getInstance();
		
		//Set endDate to today - using private variable
		//endDate.setTime(enddate.getTime());
		//endDate.add(Calendar.DATE, -20);
		
		//set startDate to a year from now - using private variable
		//One Year from now
		//startDate.add(Calendar.YEAR, -1);
		//startDate.add(Calendar.DATE, -20);
		

		YahooFinanceURLConstruct KEY = new YahooFinanceURLConstruct(startdate, enddate, stock);
		YahooFinanceHttp testobj = new YahooFinanceHttp(KEY.constructURL());
	
		if (testobj.getData().size() == 0) {
			textDisplayMain.setText("No results were found");
			return -1;
		}
		Stock newStock = new Stock(stock, testobj);
		textDisplayMain.setText(newStock.getName());
		//Set stockHeld, so you can buy the stock you just searched for
		stockHeld = newStock;
		
		String output = String.format("%s: %s is selling for %.2f$", sdf.format(enddate.getTime()), stockHeld.getName(), stockHeld.getCloseValues().get(0));
		textDisplayStock.setText(output);
		return 0;
	}
	private void seeStockDetails() {
		//Create and set up the window
		String title = stockHeld.getName() +" Information";
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		//Create and set up the content pane
		jTable newPane = new jTable(stockHeld.getData(), stockHeld.getReturns());
		newPane.setOpaque(true);
		frame.setContentPane(newPane);
		
		//Display the window
		frame.setSize(new Dimension(500, 600));
		//frame.pack();
		frame.setVisible(true);
		RefineryUtilities.positionFrameOnScreen(frame, 0, 0.4);

	}
	private void movingAverages() {
		jFreeChart timseries = new jFreeChart("Moving Averages", "Mov", stockHeld.getName(), stockHeld.getData());
		timseries.pack();
		RefineryUtilities.positionFrameOnScreen(timseries, 0.5, 0.85);
		timseries.setVisible(true);
		timseries.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	/*0 for today, 1 for yesterday, 2 for the day before, etc...
	 * 100 for good short term, bad mid and long
	 * 011 for bad short, good mid, long
	 * etc...
	 * 0 for bad everything
	 * 111 for good everything.
	 */
	private int whatToDo(int day) {
		int x = 0;
		//Short
		System.out.println((double)stockHeld.getData().get(0)[4]);
		System.out.println(stockHeld.getMovAvgs().get(0)[0]);
		System.out.println(stockHeld.getMovAvgs().get(0)[1]);
		System.out.println(stockHeld.getMovAvgs().get(0)[2]);
		if ((double)stockHeld.getData().get(day)[4] > stockHeld.getMovAvgs().get(day)[0]) {
			x += 100;
		}
		//Mid
		if ((double)stockHeld.getData().get(day)[4] > stockHeld.getMovAvgs().get(day)[1]) {
			x += 10; 
		}
		//Long
		if ((double)stockHeld.getData().get(day)[4] > stockHeld.getMovAvgs().get(day)[2]) {
			x += 1;
		}
		
		if (x==111) {
			textDisplayStock.setText("Good short, mid, and long opportunity");
		}
		if (x==110) {
			textDisplayStock.setText("Good short and mid opportunity");
		}
		if (x==101) {
			textDisplayStock.setText("Good short and long opportunity");
		}
		if (x==100) {
			textDisplayStock.setText("Good short opportunity");
		}
		if (x==11) {
			textDisplayStock.setText("Good mid and long opportunity");
		}
		if (x==10) {
			textDisplayStock.setText("Good mid opportunity");
		}
		if (x==1) {
			textDisplayStock.setText("Good long opportunity");
		}
		if (x==0) {
			textDisplayStock.setText("Bad trend-based opportunity");
		}
		return x;
	}
	
	

	
						/* VARIABLES */
	
	public Portfolio portfolio = new Portfolio("My PF");
	public Stock stockHeld;
	
	//Frames:
	private JFrame frame;
	private JFrame stockframe;
	private JFrame pfFrame;
	
	
			//BUTTONS
	
	//For Stocks
	private JButton buyStock = new JButton("Buy Stock");
	private JButton movAverages = new JButton("See trends");
	private JButton viewSharpe = new JButton("View Sharpe Ratio");
	private JButton viewReturns = new JButton("View Returns");
	private JButton whatToDo = new JButton("What to do?");
	private JButton searchAnother = new JButton("Search another Stock");
	private JButton seeDetails = new JButton("See Details");
	
	//For Portfolio
	private JButton sellStock = new JButton("Sell stock");
	private JButton removeStock = new JButton("Remove Stock");
	private JButton viewBalance = new JButton("View Balance");
	private JButton viewInvested = new JButton("View Invested");
	private JButton viewPFDetails = new JButton("Your Portfolio's Details");
	private JButton viewTransactions = new JButton("View Transaction History");
	private JButton Drawdown = new JButton("View Drawdown");
	private JButton update = new JButton("Update Portfolio");
	private JButton changeName = new JButton("Change Name");
	private JButton clear = new JButton("Clear Portfolio");
		
	//For Frame
	private JButton searchStock = new JButton("Search a stock");
	private JButton loadPF = new JButton("Load Portfolio");
	private JButton viewPF = new JButton("View Details of Current Portfolio");
	private JButton savePF = new JButton("Save Portfolio");
	private JButton Quit = new JButton("Quit");
	private JButton information = new JButton("Information");
	private JButton Simulate = new JButton("Simulate");
	
	
	//Chooser and text Displays
	private JFileChooser chooser = new JFileChooser();
	public JTextPane textDisplayMain = new JTextPane();
	public JTextPane textDisplayPF = new JTextPane();
	public JTextPane textDisplayStock = new JTextPane();
	
	//Sentinel variable for name of portfolio
	//0 -> name not set
	//1 -> name set
	int sentinel = 0;
	
	
	//Calendar variables
	
	//Today
	Calendar enddate = Calendar.getInstance();
	
	//A year from today
	Calendar startdate = Calendar.getInstance();
	
	//sdf
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
}
