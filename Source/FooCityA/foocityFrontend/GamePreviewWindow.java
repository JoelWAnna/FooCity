package foocityFrontend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Dialog;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import terraingenerator.Generate;
import foocityBackend.FooCityManager;

public class GamePreviewWindow extends JDialog
		implements
			ActionListener,
			FooCityGUIInterface {
	private FileFilter FooCitySaveFilter;
	protected JButton buttonLoad, buttonGenerate, buttonOK, buttonCancel;
	protected int panelHeight;
	protected FooCityManager city_manager;
	public static final int NEW_GAME = 1;
	public static final int LOAD_GAME = 2;
	public static final int OK_BUTTON = 1001;
	public static final int CANCEL_BUTTON = 1002;
	public static final int LOAD_MAP_BUTTON = 1003;
	public static final int LOAD_SAVE_BUTTON = 1004;
	public static final int GENERATE_MAP_BUTTON = 1005;

	public static GamePreviewWindow NewGameWindow(JFrame parent) {
		return new GamePreviewWindow(parent, NEW_GAME);
	}

	public static GamePreviewWindow LoadGameWindow(JFrame parent) {
		return new GamePreviewWindow(parent, LOAD_GAME);
	}

	private GamePreviewWindow(JFrame parent, int type) {
		super(parent);
		super.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		FooCitySaveFilter = new FileNameExtensionFilter(
				"FooCity Save files (fcs)", "fcs");

		Box button_box = Box.createHorizontalBox();

		if (type == LOAD_GAME) {
			super.setTitle("Select a SaveGame");
			buttonLoad = new JButton("Load Save File");
			buttonLoad.addActionListener(this);
			buttonLoad.setActionCommand(Integer.toString(LOAD_SAVE_BUTTON));
		} else {
			super.setTitle("Generate a New Map or Select a Terrain File");
			buttonLoad = new JButton("Load Map");
			buttonLoad.addActionListener(this);
			buttonLoad.setActionCommand(Integer.toString(LOAD_MAP_BUTTON));

			buttonGenerate = new JButton("Generate New Map");
			buttonGenerate.addActionListener(this);
			buttonGenerate.setActionCommand(Integer
					.toString(GENERATE_MAP_BUTTON));
			button_box.add(buttonGenerate);
		}
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		buttonOK.setActionCommand(Integer.toString(OK_BUTTON));
		buttonOK.setEnabled(false);

		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(this);
		buttonCancel.setActionCommand(Integer.toString(CANCEL_BUTTON));
		// Add buttons to the layout box
		button_box.add(buttonLoad);
		button_box.add(buttonOK);
		button_box.add(buttonCancel);

		MiniMapPanel minimap_panel = new MiniMapPanel(this, 3);
		Box minimap_box = Box.createHorizontalBox();
		minimap_box.add(Box.createHorizontalGlue());
		minimap_box.add(minimap_panel);
		// Add the box to the frame
		Box layout_box = Box.createVerticalBox();
		layout_box.add(Box.createVerticalGlue());
		layout_box.add(button_box);
		layout_box.add(Box.createVerticalGlue());
		layout_box.add(minimap_box);
		add(layout_box);
		pack();
		// Move it to the center of the screen and show it
		setLocationRelativeTo(null);
		this.setResizable(false);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Integer.parseInt(e.getActionCommand())) {
			case OK_BUTTON :
				if (FooCityGUI.window == null) {
					// Show the game UI and close this window
					FooCityGUI.window = new FooCityGUI(city_manager);
				} else {
					if (!FooCityGUI.window.setCityManager(city_manager))
						System.out.println("setCityManager failed");
				}
				super.dispose();
				break;
			case CANCEL_BUTTON :
				// Close this window and show the main menu
				super.dispose();
				if (FooCityGUI.window == null)
					MainMenu.createAndShowGUI();
				break;
			case LOAD_MAP_BUTTON :
				LoadMap();
				break;
			case GENERATE_MAP_BUTTON :
				GenerateMap();
				break;
			case LOAD_SAVE_BUTTON :
				LoadSave();
				break;
		}
	}

	private void GenerateMap() {
		city_manager = new FooCityManager();
		boolean mapLoaded = city_manager.NewGeneratedGame(Generate.generate());
		buttonOK.setEnabled(mapLoaded);
		repaint();
	}

	private void LoadMap() {
		// Show the Open File dialog
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./terrain"));
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			city_manager = new FooCityManager();
			boolean mapLoaded = city_manager.NewGame(fc.getSelectedFile()
					.getAbsolutePath());
			buttonOK.setEnabled(mapLoaded);
			// Force a redraw of the window
			repaint();
		}
	}

	private void LoadSave() {
		// Show the Open File dialog
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./saves"));
		fc.setDialogTitle("Choose save to load");
		fc.setDialogType(JFileChooser.FILES_ONLY);
		fc.setFileFilter(FooCitySaveFilter);
		fc.setAcceptAllFileFilterUsed(false);
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File save_file = fc.getSelectedFile();
			if (save_file.exists()) {
				city_manager = new FooCityManager();
				boolean mapLoaded = city_manager.LoadGame(save_file
						.getAbsolutePath());
				if (!mapLoaded)
					JOptionPane.showMessageDialog(this, "\"" + save_file
							.getAbsolutePath() + "\"\n is not a valid FooCity save file", "Invalid save chosen", JOptionPane.ERROR_MESSAGE);
				buttonOK.setEnabled(mapLoaded);
			}
			// Force a redraw of the window
			this.repaint();
		}
	}

	@Override
	public Point getViewPoint() {
		return null;
	}

	@Override
	public Rectangle getViewRect() {
		return null;
	}

	@Override
	public void updateDisplayCenter(Point center) {
	}

	@Override
	public void updateDisplay(Point NEpoint) {
	}

	@Override
	public void updateDisplay() {
	}

	@Override
	public FooCityManager getCityManager() {
		return city_manager;
	}

	@Override
	public void updateSelectedTile(Point p) {
	}
}
