import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class NewGame extends JPanel implements ActionListener {
	
	protected JButton buttonLoadMap, buttonOK, buttonCancel;
	protected JFrame frame;
	private int panelHeight;
	private boolean mapLoaded = false;
	//private int[] mapData;
	private MapGrid mapData;
	public NewGame()
	{
		
		// Create buttons
		buttonLoadMap = new JButton("Load Map");
		buttonLoadMap.addActionListener(this);
		buttonLoadMap.setActionCommand("loadmap");
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setActionCommand("ok");
		buttonOK.setEnabled(false);
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		buttonCancel.setActionCommand("cancel");
		
		// Add buttons to the layout box
		Box box = Box.createHorizontalBox();
		box.add(buttonLoadMap);
		box.add(buttonOK);
		box.add(buttonCancel);
		
		// Add the box to the frame
		this.add(box);
		
		frame = new JFrame("Select a terrain file");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create and set up the content pane.
		frame.setContentPane(this);
		frame.pack();
		
		// Resize the window to make room for drawing the map
		panelHeight = this.getSize().height;
		frame.setSize(392, frame.getSize().height + 384);
		
		// Move it to the center of the screen and show it
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		Graphics2D g2d = (Graphics2D)g;
		
		// Only draw something if we've loaded a map
		if (mapLoaded){
			Color color = Color.white;
			for (int y = 0; y < 128; y++){
				for (int x = 0; x < 128; x++){
					switch(mapData.GetTileAt(x, y))
					{
						case 'W':
							color = Color.blue;
							break;
						case 'B':
							color = Color.yellow;
							break;
						case 'G':
							color = Color.green;
							break;
						case 'D':
							color = Color.orange;
							color.darker();
							break;
						case 'T':
							color = Color.green;
							color.darker();
							break;
					}
					g2d.setColor(color);
					g2d.fillRect(x * 3, panelHeight + y * 3, 3, 3);
				}
			}
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("loadmap")){
			LoadMap();
		} else if (e.getActionCommand().equals("cancel")) {
			// Close this window and show the main menu
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
			MainMenu.createAndShowGUI();
		} else if (e.getActionCommand().equals("ok"))
		{
			if (FooCityGUI.window == null)
			{
				// Show the game UI and close this window
				FooCityGUI.window = new FooCityGUI(mapData);
			}
			else
			{
				FooCityGUI.window.setM(mapData);
			}
			
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
		}
		
	}
	
	private void LoadMap(){
		// Show the Open File dialog
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./terrain"));
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			mapData = new MapGrid();
			mapLoaded = mapData.FromFile(fc.getSelectedFile().getAbsolutePath());
	
			// Force a redraw of the window
			buttonOK.setEnabled(true);
			this.repaint();
			//this.paint(getGraphics());
			
		}
	}

}
