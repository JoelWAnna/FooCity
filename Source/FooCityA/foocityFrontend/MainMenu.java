package foocityFrontend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public class MainMenu extends JPanel implements ActionListener {

	protected JButton buttonNewGame, buttonLoadGame;
	static protected JFrame frame;

	public void setLaf() {
		// from http://docs.oracle.com/javase/6/docs/technotes/guides/jweb/otherFeatures/nimbus_laf.html
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				System.out.println(info.getName());
				if ("Metal".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(384, 384 + 32);
	}
	@Override
	public void paint(Graphics g) {
		BufferedImage i = null;
		if (i == null) {
			try {
				i = ImageIO.read(new File("./images/MainMenu.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		super.paint(g);

		if (i != null)
			g.drawImage(i, 0, 32, null);
		String[] fonts = getToolkit().getFontList();

		int font_size = 30;
		g.setColor(Color.BLACK);
		g.setFont(new Font(fonts[0], Font.BOLD, font_size));
		g.drawString("Welcome To Foo City", 50, 384 / 2 + 16);
	}

	public MainMenu() {
		setLaf();
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

	public static void createAndShowGUI() {
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
		frame.repaint();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("newgame".equals(e.getActionCommand())) {
			// Create a new game dialog
			GamePreviewWindow.NewGameWindow(frame);
			// Hide this window
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
		} else if ("loadgame".equals(e.getActionCommand())) {
			// Hide this window
			javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
			// Create a load game dialog
			GamePreviewWindow.LoadGameWindow(frame);
		}

	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
