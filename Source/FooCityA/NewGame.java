// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class NewGame extends JPanel implements ActionListener {
	
	protected JButton buttonLoadMap, buttonOK, buttonCancel;
	protected JFrame frame;
	private int panelHeight;
	private boolean mapLoaded = false;
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
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		// Only draw something if we've loaded a map
		if (mapLoaded)
		{
			for (int y = 0; y < 128; y++){
				for (int x = 0; x < 128; x++){
					switch(mapData.getTileAt(x, y))
					{
					case (MapGridConstants.BEACH_TILE):
						g2d.setColor(new Color(255,225,0));  //A yellowish sandy color
						break;
					case (MapGridConstants.WATER_TILE):
						g2d.setColor(new Color(0,0,180));  // A deep ocean blue
						break;
					case (MapGridConstants.GRASS_TILE):
						g2d.setColor(new Color(0,180,0));  // A somewhat dark green
						break;
					case (MapGridConstants.COAL_TILE):   
						g2d.setColor(new Color (50, 50, 50)); // Very dark grey
						break;
					case (MapGridConstants.COMMERCIAL_TILE):
						g2d.setColor(new Color (0,0,255));   // Vivid blue
						break;
					case (MapGridConstants.DIRT_TILE):
						g2d.setColor(new Color (140,110,0));  //Brown
						break;
					case (MapGridConstants.FORREST_TILE):
						g2d.setColor(new Color (0, 75, 0));  // Deep forest green
						break;
					case (MapGridConstants.INDUSTRIAL_TILE):  // Dirty yellow
						g2d.setColor(new Color (225, 225, 0));
						break;
					case (MapGridConstants.GAS_TILE):
						g2d.setColor(new Color (96, 112, 204)); // Pale blue 
						break;
					case (MapGridConstants.PARK_TILE):
						g2d.setColor(new Color (100, 255, 100));		// Pale green 
						break;
					case (MapGridConstants.POLICE_TILE):
						g2d.setColor(new Color (0,0,200));		//Deep blue
						break;
					case (MapGridConstants.RESIDENTIAL_TILE):
						g2d.setColor(new Color (0,255, 0));   // Bright green
						break;
					case (MapGridConstants.SEWAGE_TILE):
						g2d.setColor(new Color (110, 72, 20));   //Dark murkey brown
						break;
					case (MapGridConstants.SOLAR_TILE):
						g2d.setColor(new Color (255, 244, 128));	// Bright yellow
						break;
					case (MapGridConstants.WIND_TILE):
						g2d.setColor(new Color (0, 200, 255));	// Sky blue
						break;
					default:
						g2d.setColor(Color.green);
						System.err.print("Unknown tile drawn on minimap\n");
						break;
					}
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
			if (FooCityGUI.window == null)
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
				FooCityGUI.window.setMap(mapData);
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
			
			buttonOK.setEnabled(mapLoaded);
			// Force a redraw of the window
			this.repaint();
		}
	}

}
