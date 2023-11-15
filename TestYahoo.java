package app;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class TestYahoo {

    public static void main(String[] args) {
        // Replace these values with your desired parameters
        String tickerSymbol = "AAPL";
        //@SuppressWarnings("deprecation")
        Calendar calendar = Calendar.getInstance();
        // Set start date
        calendar.set(2022, Calendar.DECEMBER, 2);
        Date startDate = calendar.getTime();

        // Set end date
        calendar.set(2023, Calendar.JANUARY, 10);
        Date endDate = calendar.getTime(); // Replace with your end date

        // Construct the URL
        UrlConstruct urlConstruct = new UrlConstruct(tickerSymbol, startDate, endDate);
        String yahooFinanceURL = urlConstruct.constructURL();

        // Fetch data from Yahoo Finance
        YFHttp yfHttp = new YFHttp(yahooFinanceURL);

        // Retrieve the data
        ArrayList<Object[]> financialData = yfHttp.getData();
      //  ArrayList<String> dateList = yfHttp.getDate();
      //  ArrayList<Double> openList = yfHttp.getOpen();
     //   ArrayList<Double> highList = yfHttp.getHigh();
       // ArrayList<Double> lowList = yfHttp.getLow();
       // ArrayList<Double> closeList = yfHttp.getClose();
       // ArrayList<Long> volumeList = yfHttp.getVolume();
      //  ArrayList<Double> adjCloseList = yfHttp.getAdjClose();

        // Print or process the retrieved data as needed
        System.out.println("Financial Data:");
        for (Object[] data : financialData) {
            System.out.println("Date: " + data[0] + ", Open: " + data[1] + ", High: " + data[2]
                    + ", Low: " + data[3] + ", Close: " + data[4] + 
                     ", Adj Close: " + data[5] + ", Volume: " + data[6]);
        }

        // Example: Print dateList
      //  System.out.println("\nDate List:");
       // dateList.forEach(System.out::println);

        // Example: Print openList
     //   System.out.println("\nOpen List:");
    //    openList.forEach(System.out::println);

        // Add more processing or testing logic as needed
    }
}