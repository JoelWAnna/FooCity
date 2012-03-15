package foocityFrontend;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import foocityBackend.FooCityManager;
import foocityBackend.TaxRates;


public class TaxPanel extends JDialog implements ActionListener {
	private String [] names = {"Business Tax: ", "Income Tax: ", "Occupation Tax: ", "Property Tax: ", "Sales Tax: "};
	private JButton minus[], plus[];
	private JLabel amount[];
	private FooCityManager city_manager;

	public static void ShowTaxPanel(FooCityGUIInterface fcInterface) {
		if (fcInterface != null) {
			TaxPanel panel = new TaxPanel(fcInterface.getCityManager());
			panel.setVisible(true);
			
		}
	}
	protected TaxPanel(FooCityManager foocity_manager) {
		this();
		city_manager = foocity_manager;
		update();
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	protected TaxPanel() {
		super();
		super.setTitle("Adjust Tax Rates");
		this.setSize(210, 210);		
		super.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);

		amount = new JLabel[5];
		minus = new JButton[5];
		plus = new JButton[5];

		JPanel border_panel = new JPanel();
		border_panel.setBorder(new TitledBorder(""));
		JPanel inner_panel = new JPanel();
		inner_panel.setLayout(new GridLayout(0,3));
		for (int i=0; i < 5; ++i)
		{
			amount[i] = new  JLabel(names[i]);
			minus[i] = new JButton("-");
			minus[i].addActionListener(this);
			minus[i].setActionCommand(Integer.toString(-(i+1)));
			plus[i] = new JButton("+");
			plus[i].addActionListener(this);
			plus[i].setActionCommand(Integer.toString(i+1));
			
			JPanel parent_panel = new JPanel();
			parent_panel.add(minus[i]);
			parent_panel.setSize(25,25);
			inner_panel.add(parent_panel);
			inner_panel.add(amount[i]);
			parent_panel = new JPanel();
			parent_panel.add(plus[i]);
			inner_panel.add(parent_panel);
			
		}
	
		border_panel.add(inner_panel);
	
		getContentPane().add(border_panel);
		this.pack();
		this.setResizable(false);
	}

	private void update() {
		//{"Business Tax: ", "Income Tax: ", "Occupation Tax: ", "Property Tax: ", "Sales Tax:"};
		
		if (city_manager != null) {
			TaxRates rates = city_manager.getTaxRates();
			double []taxes = {rates.getBusiness_tax(),
			rates.getIncome_tax(),
			rates.getOccupation_tax(),
			rates.getProperty_tax(),
			rates.getSales_tax()};

			for (int i=0; i < 5; ++i) {
				amount[i].setText(names[i] + taxes[i]);
			}

		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		TaxRates rates = city_manager.getTaxRates();
		switch (Integer.parseInt(arg0.getActionCommand())) {
			case -1:
				rates.decrementBusiness_tax();
				break;
			case 1:
				rates.incrementBusiness_tax();
				break;
			case -2:
				rates.decrementIncome_tax();
				break;
			case 2:
				rates.incrementIncome_tax();
				break;
			case -3:
				rates.decrementOccupation_tax();
				break;
			case 3:
				rates.incrementOccupation_tax();
				break;
			case -4:
				rates.decrementProperty_tax();
				break;
			case 4:
				rates.incrementProperty_tax();
				break;
			case -5:
				rates.decrementSales_tax();
				break;
			case 5:
				rates.incrementSales_tax();
				break;
		}
		update();
	}
}