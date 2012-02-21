// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainMenu extends JPanel implements ActionListener {
	
	protected JButton buttonNewGame, buttonLoadGame;
	static protected JFrame frame;
	
	public MainMenu(){
				
		// Create buttons
		buttonNewGame = new JButton("New Game");
		buttonNewGame.addActionListener(this);
		buttonNewGame.setActionCommand("newgame");
		
		buttonLoadGame = new JButton("Load Game");
		buttonLoadGame.addActionListener(this);
		buttonLoadGame.setActionCommand("loadgame");
		
		// Add buttons to the layout box
		Box box = Box.createHorizontalBox();
		box.add(buttonNewGame);
		box.add(buttonLoadGame);
		
		// Add the box to the frame
		this.add(box);
		
	}
	
	public static void createAndShowGUI(){
		// Create window
		frame = new JFrame("Welcome to FooCity");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create and set up the content pane.
		MainMenu newContentPane = new MainMenu();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		
		// Display the window in the center of the screen
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("newgame".equals(e.getActionCommand())){
			// Create a new game dialog
			GamePreviewWindow.NewGameWindow(frame);
			// Hide this window
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
		}
		else if ("loadgame".equals(e.getActionCommand())){
			// Hide this window
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
			// Create a load game dialog
			GamePreviewWindow.LoadGameWindow(frame);
		}
		
	}
	
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

	
}
