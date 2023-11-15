package app;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class jTable extends JPanel {
	
	/*
	 * An object that displays a table of the basic statistics of a stock;
	 * it is also used to display the transaction history of your portfolio.
	 */
	
	private static final long serialVersionUID = 1L;
	private boolean DEBUG = false;
	
	public jTable(ArrayList<Object[]> information, ArrayList<Object[]> returns) {
		super(new GridLayout(1,0));
		
		String[] columnNames = {"Date", "Open", "High", "Low", "Volume", "Close", "Returns"};
		
		int len = information.size();
		
		//Build data
		Object[][] data = new Object[len][7];
		for (int i=0; i<len; i++) {
			data[i][0] = information.get(i)[0];
			data[i][1] = information.get(i)[1];
			data[i][2] = information.get(i)[2];
			data[i][3] = information.get(i)[3];
			data[i][4] = information.get(i)[5];
			data[i][5] = information.get(i)[4];
			if (i<len-1)
				data[i][6] = returns.get(i)[1];
		}
		
		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 700));
		table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 13));
		table.setFillsViewportHeight(true);
		
		if (DEBUG) {
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					printDebugData(table);
				}
			});
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		add(scrollPane);
	}
	
	//Stock, Date, numShares, costPerShare
	public jTable(ArrayList<Object[]> transactions) {
		super(new GridLayout(1,0));
		
		String[] columnNames = {"Date", "Stock", "# of Shares", "Cost per Share"};
		
		int len = transactions.size();
		
		//Build data
		Object[][] data = new Object[len][4];
		for (int i=0; i<len; i++) {
			data[i][1] = ((Stock)transactions.get(i)[0]).getName(); //stock
			data[i][0] = transactions.get(i)[1]; //date
			data[i][2] = transactions.get(i)[2]; //numShares
			data[i][3] = transactions.get(i)[3]; //Cost per SHare
		}
		
		final JTable table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(300, 400));
		table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 13));
		table.setFillsViewportHeight(true);
		
		if (DEBUG) {
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					printDebugData(table);
				}
			});
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		add(scrollPane);
	}
	
	private void printDebugData(JTable table) {
		int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
 
        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
	}
	
}
