package app;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class jFreeChart extends JFrame{
	/*
	 * Uses the jFreeChart.java library to construct good-looking graphs;
	 * it accepts both Arrays and HashTables.
	 * It is used to produce the moving averages plot.
	 * 
	 * 
	 * For more information see jFreeChart's documentation
	 */
	
	
	
	private static final long serialVersionUID = 1L;
	
	public jFreeChart (String applicationTitle, String chartTitle, String stockName, ArrayList<Object[]> dohlcvac) {
		super(applicationTitle);
		XYDataset dataset = arraylistCreateDataset(dohlcvac);
		JFreeChart chart = createChart(dataset, stockName);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
		
	}
	
	public jFreeChart (String applicationTitle, String chartTitle, String Portfolio, HashMap<String, Double> dateToValue) {
		super(applicationTitle);
		XYDataset dataset = HashMapCreateDataset(dateToValue);
		JFreeChart chart = createChart(dataset, Portfolio);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}
	
	private XYDataset arraylistCreateDataset(ArrayList<Object[]> dohlcvac) {
		TimeSeries result = createStockTimeSeries(dohlcvac);
		TimeSeries timeSeries1 = MovingAverage.createMovingAverage(result, "15 day moving average", 15, 15);
		TimeSeries timeSeries2 = MovingAverage.createMovingAverage(result, "40 day moving average", 40, 40);
		TimeSeries timeSeries3 = MovingAverage.createMovingAverage(result, "90 day moving average", 90, 90);
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		timeSeriesCollection.addSeries(result);
		timeSeriesCollection.addSeries(timeSeries1);
		timeSeriesCollection.addSeries(timeSeries2);
		timeSeriesCollection.addSeries(timeSeries3);
		return timeSeriesCollection;
	}
	
	private XYDataset HashMapCreateDataset(HashMap<String, Double> dateToValue) {
		TimeSeries result = createDrawdownTimeSeries(dateToValue);
		TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
		timeSeriesCollection.addSeries(result);
		return timeSeriesCollection;
		
	}
	
	private static TimeSeries createStockTimeSeries(ArrayList<Object[]> dohlcvac) {
		TimeSeries timeSeries = new TimeSeries("Stock");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			Calendar date = Calendar.getInstance();
			for (int i=0; i<dohlcvac.size(); i++) {
				
				try {
					date.setTime(format.parse((String) dohlcvac.get(i)[0]));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				timeSeries.add(new Day(date.getTime()), (double)dohlcvac.get(i)[4]);			
			}
		} catch (Exception exception) {
			System.err.println(exception.getMessage());
		}
		return timeSeries;
	}
	
	private static TimeSeries createDrawdownTimeSeries(HashMap<String, Double> dateToValue) {
		TimeSeries timeSeries = new TimeSeries("Value");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar date = Calendar.getInstance();
		for (String hashDate : dateToValue.keySet()) {
			try {
				date.setTime(sdf.parse(hashDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			timeSeries.add(new Day(date.getTime()), dateToValue.get(hashDate));
		}
		return timeSeries;
	}
	
	private JFreeChart createChart(XYDataset xyDataset, String stockName) {
		
		
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart(stockName,  "Date", "Value", xyDataset, true, true, false);
		XYPlot xyPlot = (XYPlot)jfreechart.getPlot();
		XYItemRenderer xyItemRenderer = xyPlot.getRenderer();
		StandardXYToolTipGenerator standardXYToolTipGenerator = new StandardXYToolTipGenerator("{0}: ({1}, {2})", new SimpleDateFormat("yyyy-MM-dd"), new DecimalFormat("0.00"));
		xyItemRenderer.setDefaultToolTipGenerator(standardXYToolTipGenerator);
		return jfreechart;
	}
	
}
