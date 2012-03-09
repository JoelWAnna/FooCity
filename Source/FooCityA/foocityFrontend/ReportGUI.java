package foocityFrontend;

import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
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
	
	public ReportGUI(JFrame parent){
		this.parentFrame = parent;
	}
	
	public void showReport(Report report){

		JDialog reportDialog = new JDialog(parentFrame, "End of Turn Report");
		reportDialog.setModalityType(ModalityType.DOCUMENT_MODAL);

		JPanel topPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(0, 3);
		gridLayout.setHgap(15);
		gridLayout.setVgap(5);
		topPanel.setLayout(gridLayout);
		Scanner reportScanner = new Scanner(report.getReportString());

		while (reportScanner.hasNextLine()) {
			topPanel.add(new JLabel(reportScanner.nextLine()));
		}

		reportDialog.add(topPanel);
		
		Box buttonBox = Box.createHorizontalBox();
		JButton buttonPrev = new JButton("Previous report");

		reportDialog.pack();
		reportDialog.setLocationRelativeTo(null);
		reportDialog.setVisible(true);

	}
	
	
}
