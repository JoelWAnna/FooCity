package foocityFrontend;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import foocityBackend.Report;

public class GraphGUI {
	private MyCanvas canvas;
	private JDialog dialog;
	private JFrame parentFrame;
	private LinkedList<Report> reports;
	private int currentTurn;
	private JComboBox reportList;
	private JComboBox dateRange;
	
	private final int CANVAS_WIDTH = 500;
	private final int CANVAS_HEIGHT = 300;
	
	public GraphGUI(JFrame parent, LinkedList<Report> reports, int currentTurn){
		this.parentFrame = parent;
		dialog = new JDialog(parentFrame, "Graphical Report");
		dialog.setModalityType(ModalityType.DOCUMENT_MODAL);
		this.reports = reports;
		this.currentTurn = currentTurn;
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		Box bigBox = Box.createVerticalBox();
		Box topBox = Box.createHorizontalBox();
		
		String[] reportTypes = {"Employment", "Electricity", "Water", "Cash Flow"};
		reportList = new JComboBox(reportTypes);
		String[] dateRanges = {"Last 10 turns", "Last 25 turns", "Since the beginning"};
		dateRange = new JComboBox(dateRanges);
		
		ActionListener listAction = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.repaint();
			}
		};	
		dateRange.addActionListener(listAction);
		reportList.addActionListener(listAction);
		
		
		topBox.add(reportList);
		topBox.add(dateRange);
		
		canvas = new MyCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		
		bigBox.add(topBox);
		bigBox.add(canvas);
		
		panel.add(bigBox);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
		
	}
	
	public int getRangeStart(){
		switch(dateRange.getSelectedIndex()){
		case 0:
			if (currentTurn < 10)
				return 0;
			else
				return currentTurn - 10;
		case 1:
			if (currentTurn < 25)
				return 0;
			else
				return currentTurn - 25;
		default:
			return 0;
		}
	}
	
	class MyCanvas extends Canvas {
		public MyCanvas(){
			super();
		}
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			
			switch (reportList.getSelectedIndex()){
			case 0: // Employement
				drawEmployment(g);
				break;
			case 1: // Electricity
				drawElectricity(g);
				break;
			case 2: // Water
				drawWater(g);
				break;
			case 3: // Cash Flow
				drawCashFlow(g);
				break;
			}
			
			
		}
		
		private void drawCashFlow(Graphics g){
			
			// Store a copy of the linked list as an array to make later coding easier to type
			Report[] reportArray = reports.toArray(new Report[0]) ;
			
			// First, we need to find the max values so we know what to scale by (min will always be zero)
			int max = 0;
			for (int x = getRangeStart(); x < currentTurn; x++){
				if (reportArray[x].getExpenses() > max)
					max = reportArray[x].getExpenses();
				if (reportArray[x].getIncome() > max)
					max = reportArray[x].getIncome();
			}
			
			g.setColor(Color.green);
			g.fillRect(2, 2, 15, 15);
			g.drawString("Income", 18, 14);
			g.setColor(Color.red);
			g.fillRect(2, 17, 15, 15);
			g.drawString("Expenses", 18, 31);
			
			// If max == 0, then there's no usable data yet.
			// Exit the function to avoid divide by zero errors.
			if (max == 0)
				return;
			
			// Now lets actually draw some stuff
			Point lastPoint1 = null;
			Point currentPoint1 = null;
			Point lastPoint2 = null;
			Point currentPoint2 = null;
			int start = getRangeStart();
			
			for (int x = start; x < currentTurn; x++){

				currentPoint1 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getExpenses() * CANVAS_HEIGHT) / max);
				currentPoint2 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getIncome() * CANVAS_HEIGHT) / max);
				
				g.setColor(Color.red);
				if (lastPoint1 != null)
					g.drawLine(currentPoint1.x, currentPoint1.y, lastPoint1.x, lastPoint1.y);
				g.setColor(Color.green);
				if (lastPoint2 != null)
					g.drawLine(currentPoint2.x, currentPoint2.y, lastPoint2.x, lastPoint2.y);
				lastPoint1 = currentPoint1;
				lastPoint2 = currentPoint2; 
				
			}
			
		}
		
		private void drawWater(Graphics g){
			
			// Store a copy of the linked list as an array to make later coding easier to type
			Report[] reportArray = reports.toArray(new Report[0]) ;
			
			// First, we need to find the max values so we know what to scale by (min will always be zero)
			int max = 0;
			for (int x = getRangeStart(); x < currentTurn; x++){
				if (reportArray[x].getWaterConsumed() > max)
					max = reportArray[x].getWaterConsumed();
				if (reportArray[x].getWaterGenerated() > max)
					max = reportArray[x].getWaterGenerated();
			}
			
			g.setColor(Color.green);
			g.fillRect(2, 2, 15, 15);
			g.drawString("Water Demanded", 18, 14);
			g.setColor(Color.red);
			g.fillRect(2, 17, 15, 15);
			g.drawString("Water Generated", 18, 31);
			
			// If max == 0, then there's no usable data yet.
			// Exit the function to avoid divide by zero errors.
			if (max == 0)
				return;
			
			// Now lets actually draw some stuff
			Point lastPoint1 = null;
			Point currentPoint1 = null;
			Point lastPoint2 = null;
			Point currentPoint2 = null;
			int start = getRangeStart();
			
			for (int x = start; x < currentTurn; x++){

				currentPoint1 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getWaterGenerated() * CANVAS_HEIGHT) / max);
				currentPoint2 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getWaterConsumed() * CANVAS_HEIGHT) / max);
				
				g.setColor(Color.red);
				if (lastPoint1 != null)
					g.drawLine(currentPoint1.x, currentPoint1.y, lastPoint1.x, lastPoint1.y);
				g.setColor(Color.green);
				if (lastPoint2 != null)
					g.drawLine(currentPoint2.x, currentPoint2.y, lastPoint2.x, lastPoint2.y);
				lastPoint1 = currentPoint1;
				lastPoint2 = currentPoint2; 
				
			}
			
		}
		
		private void drawElectricity(Graphics g){
			
			// Store a copy of the linked list as an array to make later coding easier to type
			Report[] reportArray = reports.toArray(new Report[0]) ;
			
			// First, we need to find the max values so we know what to scale by (min will always be zero)
			int max = 0;
			for (int x = getRangeStart(); x < currentTurn; x++){
				if (reportArray[x].getPowerConsumed() > max)
					max = reportArray[x].getPowerConsumed();
				if (reportArray[x].getPowerGenerated() > max)
					max = reportArray[x].getPowerGenerated();
			}
			
			g.setColor(Color.green);
			g.fillRect(2, 2, 15, 15);
			g.drawString("Power Demanded", 18, 14);
			g.setColor(Color.red);
			g.fillRect(2, 17, 15, 15);
			g.drawString("Power Generated", 18, 31);
			
			// If max == 0, then there's no usable data yet.
			// Exit the function to avoid divide by zero errors.
			if (max == 0)
				return;
			
			// Now lets actually draw some stuff
			Point lastPoint1 = null;
			Point currentPoint1 = null;
			Point lastPoint2 = null;
			Point currentPoint2 = null;
			int start = getRangeStart();
			
			for (int x = start; x < currentTurn; x++){

				currentPoint1 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getPowerGenerated() * CANVAS_HEIGHT) / max);
				currentPoint2 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getPowerConsumed() * CANVAS_HEIGHT) / max);
				
				g.setColor(Color.red);
				if (lastPoint1 != null)
					g.drawLine(currentPoint1.x, currentPoint1.y, lastPoint1.x, lastPoint1.y);
				g.setColor(Color.green);
				if (lastPoint2 != null)
					g.drawLine(currentPoint2.x, currentPoint2.y, lastPoint2.x, lastPoint2.y);
				lastPoint1 = currentPoint1;
				lastPoint2 = currentPoint2; 
				
			}
			
		}
		
		private void drawEmployment(Graphics g){
			
			// Store a copy of the linked list as an array to make later coding easier to type
			Report[] reportArray = reports.toArray(new Report[0]) ;
			
			// First, we need to find the max values so we know what to scale by (min will always be zero)
			int max = 0;
			for (int x = getRangeStart(); x < currentTurn; x++){
				if (reportArray[x].getJobs() > max)
					max = reportArray[x].getJobs();
				if (reportArray[x].getResidents() > max)
					max = reportArray[x].getResidents();
			}
			
			g.setColor(Color.green);
			g.fillRect(2, 2, 15, 15);
			g.drawString("Residents", 18, 14);
			g.setColor(Color.blue);
			g.fillRect(2, 17, 15, 15);
			g.drawString("Jobs", 18, 31);
			
			// If max == 0, then there's no usable data yet.
			// Exit the function to avoid divide by zero errors.
			if (max == 0)
				return;
			
			// Now lets actually draw some stuff
			Point lastPoint1 = null;
			Point currentPoint1 = null;
			Point lastPoint2 = null;
			Point currentPoint2 = null;
			int start = getRangeStart();
			
			for (int x = start; x < currentTurn; x++){

				currentPoint1 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getResidents() * CANVAS_HEIGHT) / max);
				currentPoint2 = new Point(((x - start) * CANVAS_WIDTH) / (currentTurn - start - 1),
						CANVAS_HEIGHT - (reportArray[x].getJobs() * CANVAS_HEIGHT) / max);
				
				g.setColor(Color.green);
				if (lastPoint1 != null)
					g.drawLine(currentPoint1.x, currentPoint1.y, lastPoint1.x, lastPoint1.y);
				g.setColor(Color.blue);
				if (lastPoint2 != null)
					g.drawLine(currentPoint2.x, currentPoint2.y, lastPoint2.x, lastPoint2.y);
				lastPoint1 = currentPoint1;
				lastPoint2 = currentPoint2; 
				
			}
			
		}
	}
	
}


