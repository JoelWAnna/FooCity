import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class newGame extends JPanel implements ActionListener {
	
	protected JButton buttonLoadMap, buttonOK, buttonCancel;
	protected JFrame frame;
	private int panelHeight;
	private boolean mapLoaded = false;
	private int[] mapData;
	
	
	public newGame(){
		
		// Create buttons
		buttonLoadMap = new JButton("Load Map");
		buttonLoadMap.addActionListener(this);
		buttonLoadMap.setActionCommand("loadmap");
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setActionCommand("ok");
		
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
					switch(mapData[y * 128 + x]){
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
			mainMenu.createAndShowGUI();
		}
		
	}
	
	private void LoadMap(){
		// Show the Open File dialog
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./terrain"));
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				FileReader inputStream = new FileReader(fc.getSelectedFile());
				mapData = new int[16384];
				// Reach each byte of the file into the mapData array
				for(int i = 0; i < 16384; i++){
					// Hastily designed loop to throw out CRs and LFs
					int tmp = inputStream.read();
					while (tmp == 13 || tmp == 10)
						tmp = inputStream.read();
					mapData[i] = tmp;
				}
				// Force a redraw of the window
				mapLoaded = true;
				this.paint(getGraphics());
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

}
