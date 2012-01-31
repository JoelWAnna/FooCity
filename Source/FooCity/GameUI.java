import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
 
public class GameUI extends JFrame implements ActionListener, MouseListener, KeyListener {
	
	// Buttons for building stuff
	protected JButton buttonResidential, buttonCommercial, buttonIndustrial,
						buttonPark, buttonSewage, buttonPolice, buttonSolar,
						buttonGas, buttonCoal, buttonWindFarm;
	
	// We will paint on a canvas!
	private GameCanvas canvas;
	private JTextPane statusBar;
	private City city;
	private Point camera = new Point();
	
	public GameUI(int[] mapData){
		
		// Create city
		city = new City();
		city.initFromTerrain(mapData);
		// Create Buttons
		// TODO: Change text to icons
		buttonResidential = new JButton("R");
		buttonResidential.addActionListener(this);
		buttonResidential.setActionCommand("buildres");
		
		buttonCommercial = new JButton("C");
		buttonCommercial.addActionListener(this);
		buttonCommercial.setActionCommand("buildcom");
		
		buttonIndustrial = new JButton("I");
		buttonIndustrial.addActionListener(this);
		buttonIndustrial.setActionCommand("buildind");
		
		// Add buttons to a box
		Box box = Box.createVerticalBox();
		box.add(buttonResidential);
		box.add(buttonCommercial);
		box.add(buttonIndustrial);
		
		// Add the box to the left side of the screen
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(box, BorderLayout.LINE_START);
		
		// Create the game canvas and set it to the center
		canvas = new GameCanvas();
		canvas.addMouseListener(this);
		panel.add(canvas, BorderLayout.CENTER);
		
		// Create the status bar and align it to the bottom
		statusBar = new JTextPane();
		statusBar.setText("Status Bar");
		panel.add(statusBar, BorderLayout.PAGE_END);

		// Various game window settings
		this.setTitle("FooCity");
		this.setContentPane(panel);
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.addKeyListener(this);


	}
	class GameCanvas extends Canvas {
		@Override
		public void paint(Graphics g) {
			g.drawLine(5, 5, getWidth() - 5, getHeight() - 5);
			for (int x = 0; x < 128; x++){
				for (int y = 0; y < 128; y++){
					switch(city.getTile(x, y).getTileType()){
					case BEACH:
						g.setColor(Color.orange);
						g.fillRect((x-camera.x)*32, (y-camera.y)*32, 32, 32);
						break;
					case GRASS:
						g.setColor(Color.green);
						g.fillRect((x-camera.x)*32, (y-camera.y)*32, 32, 32);
						break;
						
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		
		// Change the camera offset
		switch(arg0.getKeyChar()){
			case 'w':
				camera.y--;
				break;
			case 's':
				camera.y++;
				break;
			case 'a':
				camera.x--;
				break;
			case 'd':
				camera.x++;
		}
		
		// Don't let the player scroll off the city
		if (camera.x < 0)
			camera.x = 0;
		if (camera.y < 0)
			camera.y = 0;
		if (camera.x > 127)
			camera.x = 127;
		if (camera.y > 127)
			camera.y = 127;
		canvas.repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

