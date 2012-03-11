package foocityFrontend;

import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import foocityBackend.Report;

public class ReportGUI {
	
	private JFrame parentFrame;
	private JDialog reportDialog;
	private JPanel topPanel;
	private LinkedList<Report> reports;
	private int currentTurn;
	
	public ReportGUI(JFrame parent, LinkedList<Report> reports, int currentTurn){
		this.parentFrame = parent;
		reportDialog = new JDialog(parentFrame, "End of Turn Report");
		reportDialog.setModalityType(ModalityType.DOCUMENT_MODAL);
		this.reports = reports;
		this.currentTurn = currentTurn;
	}
	
	public void showReport(Report report){

		//Because we're not saving the window objects to re-use them, we need to destroy them
		reportDialog.getContentPane().removeAll();
		
		topPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 3);
		gridLayout.setHgap(15);
		gridLayout.setVgap(5);
		topPanel.setLayout(gridLayout);
		Scanner reportScanner = new Scanner(report.getReportString());

		while (reportScanner.hasNextLine()) {
			topPanel.add(new JLabel(reportScanner.nextLine()));
		}

		reportDialog.add(topPanel);
		
		JButton buttonPrev = new JButton("Previous report");
		JButton buttonNext = new JButton("Next report");
		JButton buttonClose = new JButton("Close");
		buttonPrev.setActionCommand(Integer.toString(report.getTurn() - 1));
		buttonNext.setActionCommand(Integer.toString(report.getTurn() + 1));
		buttonClose.setActionCommand("close");
		
		ActionListener buttonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getActionCommand() == "close"){
					reportDialog.setVisible(false);
					return;
				}
				showReport((Report) reports.toArray()[Integer.parseInt(e.getActionCommand())]);
			}	
		};
		
		buttonPrev.addActionListener(buttonListener);
		buttonNext.addActionListener(buttonListener);
		buttonClose.addActionListener(buttonListener);
		
		topPanel.add(buttonPrev);
		topPanel.add(buttonNext);
		topPanel.add(buttonClose);
		
		if (report.getTurn() == 0)
			buttonPrev.setEnabled(false);
		
		// We subtract 1 here because the last turn (and therefore the last report)
		// came BEFORE the current turn
		if (report.getTurn() == currentTurn - 1)
			buttonNext.setEnabled(false);

		reportDialog.pack();
		reportDialog.setLocationRelativeTo(null);
		reportDialog.setVisible(true);

	}
	
	
	
}
