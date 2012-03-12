package foocityFrontend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import foocityBackend.FooCityManager;
import foocityBackend.MapGridConstants;
import foocityBackend.TileMetrics;

class FooCityGUIConstants {
	public static final int TILE_WIDTH = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int WINDOW_HEIGHT = 700;
	public static final int WINDOW_WIDTH = 800;
	public static final int SIDEBAR_WIDTH = 257;

	public static final int NEW_GAME = 0;
	public static final int SAVE_GAME = 1;
	public static final int LOAD_GAME = 2;
	public static final int QUIT = 3;
	public static final int STATUSBAR_HEIGHT = 36;
}

public class FooCityGUI implements FooCityGUIInterface {

	public static FooCityGUI window;
	private JFrame frame;

	private JMenuBar menuBar;

	private JPanel side_panel;
	private JPanel button_grid_panel;
	private JLabel placing_tile_cost;
	private JTextArea placing_tile_description;
	private MiniMapPanel minimap_panel;

	private JButton nextTurn;
	private JLabel currentTurn;
	private JLabel currentFunds;

	private FooCityScrollPane scrollPane;
	private CityViewport map_panel;

	private SelectedTilePanel selected_tile_panel;

	private JToggleButton buttonResidential, buttonCommercial,
			buttonIndustrial, buttonPark, buttonSewage, buttonPolice,
			buttonSolar, buttonRoad, buttonGas, buttonCoal, buttonWindFarm,
			buttonDirt, buttonForrest, buttonGrass, buttonBulldoze;
	// buttonWater, buttonBeach;

	private final Action MainMenu = new MainMenuAction();
	private final Action Tile = new PlaceTileAction();
	private FooCityManager city_manager;

	/**
	 * Launch the application.
	 */
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { window = new FooCityGUI(); } catch
	 * (Exception e) { e.printStackTrace(); } } }); }
	 */


	/**
	 * Create the application.
	 * @wbp.parser.constructor
	 */	
	public FooCityGUI() {
		this(new FooCityManager());
	}

	public FooCityGUI(FooCityManager city_manager2) {
		city_manager = city_manager2;
		initialize();
		if (!city_manager.MapGridLoaded())
			enableButtons(false);
	}

	@Override
	public void updateDisplayCenter(Point center) {
		Rectangle rect = getViewRect();
		scrollPane.getHorizontalScrollBar().setValue(
				center.x * FooCityGUIConstants.TILE_HEIGHT - (rect.width / 2));
		scrollPane.getVerticalScrollBar().setValue(
				center.y * FooCityGUIConstants.TILE_WIDTH - (rect.height / 2));
		updateDisplay();
	}

	@Override
	public void updateDisplay(Point NEpoint) {

		scrollPane.getViewport().setViewPosition(NEpoint);
		updateDisplay();
	}

	@Override
	public void updateDisplay() {
		map_panel.repaint();
		minimap_panel.repaint();
		this.currentFunds.setText("Current Funds: "
				+ Integer.toString(city_manager.getAvailableFunds()));
		this.currentTurn.setText("Current Turn: "
				+ Integer.toString(city_manager.getCurrentTurn()));
		updateTileDescription(city_manager.getPlacingTile());
		
		enableButtons(city_manager.MapGridLoaded());
		if (!city_manager.MapGridLoaded())
			selected_tile_panel.clearSelectedTile();
	}

	private void updateTileDescription(int tileType) {

		TileMetrics selectedTile = TileMetrics.GetTileMetrics(tileType);
		if (selectedTile != null) {
			placing_tile_cost.setText("Cost: $"
					+ Integer.toString(selectedTile.getPrice()));
			placing_tile_description.setText(selectedTile.getDescription());
			if (selectedTile.getPrice() > city_manager.getAvailableFunds())
				placing_tile_cost.setForeground(Color.red);
			else
				placing_tile_cost.setForeground(Color.black);
		} else {
			placing_tile_cost.setText(" ");
			placing_tile_description.setText(" ");
		}
	}

	@Override
	public void updateSelectedTile(Point p) {
		selected_tile_panel.updateSelectedTile(p.x, p.y);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		AddResizeListener();

		frame.setBounds(100, 100, FooCityGUIConstants.WINDOW_WIDTH,
				FooCityGUIConstants.WINDOW_HEIGHT);
		frame.setTitle("FooCity V0.1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		map_panel = new CityViewport(this);
		scrollPane = new FooCityScrollPane(map_panel, this);
		scrollPane.setBounds(0, 0, 0, 0);
		createToolPanel();
	
		String[] viewModes = {"Normal", "Pollution", "Crime", "Happiness"};
		JComboBox miniMapViewList = new JComboBox(viewModes);
		miniMapViewList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				// check type, uses <?> instead of String because we are only
				// interested in
				// the index and eliminates the warning of an unchecked
				// conversion
				if (source instanceof JComboBox) {
					JComboBox cb = (JComboBox) source;
					minimap_panel.setViewMode(cb.getSelectedIndex());
					scrollPane.grabFocus();
				}
			}

		});
		miniMapViewList.setSelectedIndex(0);

		placing_tile_cost = new JLabel(" ");
		placing_tile_cost.setHorizontalAlignment(JLabel.LEFT);
		placing_tile_cost.setVerticalAlignment(JLabel.TOP);
		placing_tile_cost.setMaximumSize(new Dimension(256, 15));
		placing_tile_cost.setAlignmentX(Component.CENTER_ALIGNMENT);
		placing_tile_cost.setFont(new Font("Dialog", 1, 16));

		placing_tile_description = new JTextArea(" ");
		placing_tile_description.setMaximumSize(new Dimension(256, 80));
		placing_tile_description.setPreferredSize(new Dimension(256, 80));
		placing_tile_description.setAlignmentX(Component.CENTER_ALIGNMENT);
		placing_tile_description.setBorder(BorderFactory.createEtchedBorder());
		placing_tile_description.setEditable(false);
		placing_tile_description.setCursor(null);
		placing_tile_description.setOpaque(false);
		placing_tile_description.setFocusable(false);
		placing_tile_description.setLineWrap(true);
		placing_tile_description.setWrapStyleWord(true);

		Box side_panel_north = Box.createVerticalBox();
		// side_panel.add(Box.createHorizontalGlue());
		side_panel_north.add(button_grid_panel);
		// side_panel.add(Box.createHorizontalGlue());
		side_panel_north.add(placing_tile_cost);
		// side_panel.add(Box.createHorizontalGlue());
		side_panel_north.add(placing_tile_description);
		// side_panel.add(Box.createHorizontalGlue());
		side_panel_north.add(miniMapViewList);
		side_panel_north.add(minimap_panel);

		

		
		currentFunds = new JLabel("Current Funds: "
				+ city_manager.getAvailableFunds());
		currentTurn = new JLabel("Current Turn: "
				+ city_manager.getCurrentTurn());


		nextTurn = new JButton("Next Turn");
		nextTurn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				advanceTurn();
			}
		});

		Box statusBox = Box.createVerticalBox();
		statusBox.add(currentFunds);
		statusBox.add(currentTurn);

		JPanel advance_turn_panel = new JPanel();
		advance_turn_panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));	
		advance_turn_panel.add(nextTurn);
		advance_turn_panel.add(statusBox);
		
		JPanel side_panel_south = new JPanel();
		side_panel_south.setBorder(new TitledBorder(""));
		side_panel_south.add(advance_turn_panel);

		side_panel.add(side_panel_south, BorderLayout.SOUTH);
		side_panel.add(side_panel_north, BorderLayout.NORTH);
		
		frame.getContentPane().add(side_panel);
		frame.getContentPane().add(scrollPane);

		selected_tile_panel = new SelectedTilePanel(this);	
		selected_tile_panel.setBounds(0, 0, 0, 0);
		frame.getContentPane().add(selected_tile_panel);
		//ResizePanes();
		scrollPane.grabFocus();
		initializeMenuBar();

		AddKeyListeners();
		city_manager.propagateMetrics();

	}

	protected void advanceTurn() {
		switch (city_manager.advanceTurn())
		{
		case FooCityManager.FLAG_WIN:
			break;
		case FooCityManager.FLAG_LOSE:
			JOptionPane.showMessageDialog(frame, "Sorry you lose");
			city_manager.Quit();
			break;
		case FooCityManager.FLAG_MIDGAME:
			showEndOfTurnReport();
			break;
		}
		updateDisplay();
		
	}

	private void showEndOfTurnReport() {
		ReportGUI reportGUI = new ReportGUI(this.frame, city_manager.reports, city_manager.getCurrentTurn());
		reportGUI.showReport(city_manager.reports.getLast());
	}

	private void initializeMenuBar() {
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu file_menu = new JMenu("File");
		menuBar.add(file_menu);

		JMenuItem menuItem_NewGame = new JMenuItem("New Game");
		menuItem_NewGame.addActionListener(MainMenu);
		menuItem_NewGame.setActionCommand(Integer
				.toString(FooCityGUIConstants.NEW_GAME));
		file_menu.add(menuItem_NewGame);

		JMenuItem menuItem_SaveGame = new JMenuItem("Save Game");
		menuItem_SaveGame.addActionListener(MainMenu);
		menuItem_SaveGame.setActionCommand(Integer
				.toString(FooCityGUIConstants.SAVE_GAME));
		file_menu.add(menuItem_SaveGame);

		JMenuItem menuItem_LoadGame = new JMenuItem("Load Game");
		menuItem_LoadGame.addActionListener(MainMenu);
		menuItem_LoadGame.setActionCommand(Integer
				.toString(FooCityGUIConstants.LOAD_GAME));
		file_menu.add(menuItem_LoadGame);

		JMenuItem menuItem_Quit = new JMenuItem("Quit");
		menuItem_Quit.addActionListener(MainMenu);
		menuItem_Quit.setActionCommand(Integer
				.toString(FooCityGUIConstants.QUIT));
		file_menu.add(menuItem_Quit);
		JMenu View = new JMenu("View");
		menuBar.add(View);
		JCheckBoxMenuItem gridLines = new JCheckBoxMenuItem("Show MapGrid");
		
		gridLines.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
				((JCheckBoxMenuItem)e.getSource()).setSelected(selected);
				map_panel.enableGrid(selected);
				updateDisplay();
			}
			
		});
		View.add(gridLines);
		JMenu mnReports = new JMenu("Reports");
		menuBar.add(mnReports);
		
		JMenuItem menuItem_ShowLastReport = new JMenuItem("Show last report");
		menuItem_ShowLastReport.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showEndOfTurnReport();
			}
			
		});
		mnReports.add(menuItem_ShowLastReport);
		
		JMenuItem menuItem_ShowGraph = new JMenuItem("View graphs");
		menuItem_ShowGraph.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new GraphGUI(frame, city_manager.reports, city_manager.getCurrentTurn());
			}
			
		});
		mnReports.add(menuItem_ShowGraph);
	}

	private void createToolPanel() {
		// toolPanel contains the entire left toolbar, including minimap.
		side_panel = new JPanel(new BorderLayout());
		side_panel.setBounds(0, 0, 257, 630);
		// Create a panel to hold the buttons using a grid 3 columns wide
		// (rows get added automatically by Java as needed)
		GridLayout buttonGridLayout = new GridLayout(0, 3);
		button_grid_panel = new JPanel();
		button_grid_panel.setLayout(buttonGridLayout);
		minimap_panel = new MiniMapPanel(this, 2);
		minimap_panel.repaint();

		buttonResidential = new JToggleButton("Residential");
		buttonResidential.addActionListener(Tile);
		buttonResidential.setActionCommand(Integer
				.toString(MapGridConstants.RESIDENTIAL_TILE));
		button_grid_panel.add(buttonResidential);

		buttonCommercial = new JToggleButton("Commercial");
		buttonCommercial.addActionListener(Tile);
		buttonCommercial.setActionCommand(Integer
				.toString(MapGridConstants.COMMERCIAL_TILE));
		button_grid_panel.add(buttonCommercial);

		buttonIndustrial = new JToggleButton("Industrial");
		buttonIndustrial.addActionListener(Tile);
		buttonIndustrial.setActionCommand(Integer
				.toString(MapGridConstants.INDUSTRIAL_TILE));
		button_grid_panel.add(buttonIndustrial);

		buttonRoad = new JToggleButton("Road");
		buttonRoad.addActionListener(Tile);
		buttonRoad.setActionCommand(Integer
				.toString(MapGridConstants.ROAD_TILE));
		button_grid_panel.add(buttonRoad);

		buttonPark = new JToggleButton("Park");
		buttonPark.addActionListener(Tile);
		buttonPark.setActionCommand(Integer
				.toString(MapGridConstants.PARK_TILE));
		button_grid_panel.add(buttonPark);

		buttonSewage = new JToggleButton("Sewage");
		buttonSewage.addActionListener(Tile);
		buttonSewage.setActionCommand(Integer
				.toString(MapGridConstants.SEWAGE_TILE));
		button_grid_panel.add(buttonSewage);

		buttonPolice = new JToggleButton("Police");
		buttonPolice.addActionListener(Tile);
		buttonPolice.setActionCommand(Integer
				.toString(MapGridConstants.POLICE_TILE));
		button_grid_panel.add(buttonPolice);

		buttonSolar = new JToggleButton("Solar");
		buttonSolar.addActionListener(Tile);
		buttonSolar.setActionCommand(Integer
				.toString(MapGridConstants.SOLAR_TILE));
		button_grid_panel.add(buttonSolar);

		buttonGas = new JToggleButton("Gas");
		buttonGas.addActionListener(Tile);
		buttonGas.setActionCommand(Integer.toString(MapGridConstants.GAS_TILE));
		button_grid_panel.add(buttonGas);

		buttonCoal = new JToggleButton("Coal");
		buttonCoal.addActionListener(Tile);
		buttonCoal.setActionCommand(Integer
				.toString(MapGridConstants.COAL_TILE));
		button_grid_panel.add(buttonCoal);

		buttonWindFarm = new JToggleButton("Wind");
		buttonWindFarm.addActionListener(Tile);
		buttonWindFarm.setActionCommand(Integer
				.toString(MapGridConstants.WIND_TILE));
		button_grid_panel.add(buttonWindFarm);

		buttonDirt = new JToggleButton("Dirt");
		buttonDirt.addActionListener(Tile);
		buttonDirt.setActionCommand(Integer
				.toString(MapGridConstants.DIRT_TILE));
		button_grid_panel.add(buttonDirt);

		/*
		 * buttonWater = new JButton("Water");
		 * buttonWater.addActionListener(Tile);
		 * buttonWater.setActionCommand(waterTile);
		 * buttonGridPanel.add(buttonWater);
		 */

		buttonGrass = new JToggleButton("Grass");
		buttonGrass.addActionListener(Tile);
		buttonGrass.setActionCommand(Integer
				.toString(MapGridConstants.GRASS_TILE));
		button_grid_panel.add(buttonGrass);

		buttonForrest = new JToggleButton("Forrest");
		buttonForrest.addActionListener(Tile);
		buttonForrest.setActionCommand(Integer
				.toString(MapGridConstants.FORREST_TILE));
		button_grid_panel.add(buttonForrest);

		buttonBulldoze = new JToggleButton("Bulldoze");
		buttonBulldoze.addActionListener(Tile);
		buttonBulldoze.setActionCommand(Integer.toString(MapGridConstants.BULLDOZE_TILE));
		button_grid_panel.add(buttonBulldoze);

		/*
		 * buttonBeach = new JButton("Beach");
		 * buttonBeach.addActionListener(Tile);
		 * buttonBeach.setActionCommand(beachTile);
		 * buttonGridPanel.add(buttonBeach);
		 */
	}

	@Override
	public FooCityManager getCityManager() {
		return city_manager;
	}

	@Override
	public Rectangle getViewRect() {
		return (Rectangle) scrollPane.getViewport().getVisibleRect().clone();
	}

	@Override
	public Point getViewPoint() {
		return (Point) scrollPane.getViewport().getViewPosition().clone();
	}

	public boolean setCityManager(FooCityManager city_manager2) {
		if (city_manager2 != null) {
			if (city_manager.getCurrentTurn() == 0) {
				city_manager = city_manager2;
				return true;
			}
		}
		return false;
	}

	private class MainMenuAction extends AbstractAction {
		private FileFilter FooCitySaveFilter;
		public MainMenuAction() {
			FooCitySaveFilter = new FileNameExtensionFilter(
					"FooCity Save files (fcs)", "fcs");
			putValue(NAME, "New Game");
			putValue(SHORT_DESCRIPTION, "Select A Map To Load");
		}

		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			int command_int = Integer.parseInt(command);
			switch (command_int) {
				case FooCityGUIConstants.NEW_GAME :
					if (city_manager != null && city_manager.MapGridLoaded()) {
						switch (JOptionPane
								.showInternalConfirmDialog(
										frame.getContentPane(),
										"Save current game before starting a new game?",
										"New game",
										JOptionPane.YES_NO_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE)) {
							case JOptionPane.CANCEL_OPTION :
								return;
							case JOptionPane.YES_OPTION :
								if (!saveGame())
									break;
							default :
						}
						city_manager.Quit();
					}
					updateDisplay();
					GamePreviewWindow.NewGameWindow(frame);
					updateDisplay();
					break;
				case FooCityGUIConstants.LOAD_GAME :
					if (city_manager != null && city_manager.MapGridLoaded()) {
						switch (JOptionPane.showInternalConfirmDialog(
								frame.getContentPane(),
								"Save current game before loading?",
								"Load game", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE)) {
							case JOptionPane.CANCEL_OPTION :
								return;
							case JOptionPane.YES_OPTION :
								if (!saveGame())
									break;
							default :
						}
						city_manager.Quit();
					}
					updateDisplay();
					GamePreviewWindow.LoadGameWindow(frame);
					updateDisplay();
					break;
				case FooCityGUIConstants.SAVE_GAME :
					saveGame();
					break;
				case FooCityGUIConstants.QUIT :
					if (city_manager != null && !city_manager.MapGridLoaded()) {
						switch (JOptionPane.showInternalConfirmDialog(
								frame.getContentPane(),
								"Save game before exiting?", "Quit",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE)) {
							case JOptionPane.CANCEL_OPTION :
								return;
							case JOptionPane.YES_OPTION :
								if (!saveGame())
									break;
							default :
						}
					}
					System.exit(0);
			}
			scrollPane.grabFocus();
		}

		private boolean saveGame() {
			if (city_manager.MapGridLoaded()) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("./saves"));
				fc.setDialogTitle("Save file as...");
				fc.setDialogType(JFileChooser.FILES_ONLY);
				fc.setFileFilter(FooCitySaveFilter);
				fc.setAcceptAllFileFilterUsed(false);
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					{
						File save_file = fc.getSelectedFile();
						if (save_file.getAbsolutePath().lastIndexOf(".fcs") == -1)
							save_file = new File(save_file.getAbsolutePath()
									+ ".fcs");
						if (save_file.exists()) {
							switch (JOptionPane.showInternalConfirmDialog(
									frame.getContentPane(),
									save_file.getAbsolutePath()
											+ " exists, Overwrite?",
									"Confirm Save", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE)) {
								case JOptionPane.NO_OPTION :
									return false;
								case JOptionPane.YES_OPTION :
							}
						}
						return city_manager.SaveGame(save_file
								.getAbsolutePath());
					}
				}

			}
			return false;
		}
	}

	private class PlaceTileAction extends AbstractAction {
		public PlaceTileAction() {
			putValue(NAME, "Place Tile");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			JToggleButton buttonPressed = (JToggleButton) e.getSource();
			boolean selected = buttonPressed.isSelected();
			deselectButtons();
			buttonPressed.setSelected(selected);
			scrollPane.grabFocus();
			int placingTile = Integer.parseInt(e.getActionCommand());
			if (!selected)
				placingTile = 0;
			if (!city_manager.setPlacingTile(placingTile))
				placingTile = 0;
			updateTileDescription(placingTile);
			return;
		}
	}

	private void enableButtons(boolean enable)
	{
		if (buttonResidential.isEnabled() != enable)
		{
			buttonResidential.setEnabled(enable);
			buttonCommercial.setEnabled(enable);
			buttonIndustrial.setEnabled(enable);
			buttonPark.setEnabled(enable);
			buttonSewage.setEnabled(enable);
			buttonPolice.setEnabled(enable);
			buttonSolar.setEnabled(enable);
			buttonRoad.setEnabled(enable);
			buttonGas.setEnabled(enable);
			buttonCoal.setEnabled(enable);
			buttonWindFarm.setEnabled(enable);
			buttonDirt.setEnabled(enable);
			buttonForrest.setEnabled(enable);
			buttonGrass.setEnabled(enable);
			buttonBulldoze.setEnabled(enable);
			nextTurn.setEnabled(enable);
			if (!enable)
				deselectButtons();
		}
	}

	private void deselectButtons() {
		buttonResidential.setSelected(false);
		buttonCommercial.setSelected(false);
		buttonIndustrial.setSelected(false);
		buttonPark.setSelected(false);
		buttonSewage.setSelected(false);
		buttonPolice.setSelected(false);
		buttonSolar.setSelected(false);
		buttonRoad.setSelected(false);
		buttonGas.setSelected(false);
		buttonCoal.setSelected(false);
		buttonWindFarm.setSelected(false);
		buttonDirt.setSelected(false);
		buttonForrest.setSelected(false);
		buttonGrass.setSelected(false);
		buttonBulldoze.setSelected(false);
	}

	private class keyDispatcher extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);
			if (e.getID() == KeyEvent.KEY_PRESSED && scrollPane.hasFocus()
					&& city_manager != null) {
				Dimension map_area = city_manager.getMapArea();
				if (map_area == null)
					return;
				char c = e.getKeyChar();
				Point p = (Point) scrollPane.getViewport().getViewPosition()
						.clone();
				Rectangle r = (Rectangle) scrollPane.getViewport()
						.getVisibleRect().clone();

				int x = p.x;
				int y = p.y;
				// System.out.println(c + " " + x + " " + y + " " + r);
				switch (Character.toUpperCase(c)) {
					case 'W' :
						if (y < FooCityGUIConstants.TILE_HEIGHT)
							y = 0;
						else
							y -= FooCityGUIConstants.TILE_HEIGHT;
						break;
					case 'A' :
						if (x < FooCityGUIConstants.TILE_WIDTH)
							x = 0;
						else
							x -= FooCityGUIConstants.TILE_WIDTH;
						break;
					case 'S' :
						if (y > (int) map_area.getHeight()
								* (FooCityGUIConstants.TILE_HEIGHT - 1)
								- r.height)
							y = (int) map_area.getHeight()
									* FooCityGUIConstants.TILE_HEIGHT
									- r.height;
						else
							y += 32;
						break;
					case 'D' :
						if (x > (int) map_area.getWidth()
								* (FooCityGUIConstants.TILE_WIDTH - 1)
								- r.width)
							x = (int) map_area.getWidth()
									* FooCityGUIConstants.TILE_WIDTH - r.width;
						else
							x += 32;
						break;
					default :
						break;
				}
				updateDisplay(new Point(x, y));
			}
			return;
		}
	}

	private void AddKeyListeners() {
		scrollPane.addKeyListener(new keyDispatcher());
	}

	private void AddResizeListener() {
		frame.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent arg0) {
				ResizePanes();
			}
		});

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				ResizePanes();
			}
		});
	}

	private void ResizePanes() {
		Rectangle r = frame.getBounds();
		r.x = FooCityGUIConstants.SIDEBAR_WIDTH;
		r.y = 0;
		r.width -= FooCityGUIConstants.SIDEBAR_WIDTH + 15;
		r.height -= 60 + FooCityGUIConstants.STATUSBAR_HEIGHT;
		scrollPane.setBounds(r);
		scrollPane.revalidate();
		side_panel.setBounds(0, 0, FooCityGUIConstants.SIDEBAR_WIDTH, r.height);
		side_panel.revalidate();
		selected_tile_panel.setBounds(0, r.height, r.width+FooCityGUIConstants.SIDEBAR_WIDTH, FooCityGUIConstants.STATUSBAR_HEIGHT);
		selected_tile_panel.revalidate();
	}
}

interface FooCityGUIInterface {

	Point getViewPoint();

	Rectangle getViewRect();

	public void updateDisplayCenter(Point center);

	public void updateDisplay(Point NEpoint);

	public void updateDisplay();

	public FooCityManager getCityManager();

	void updateSelectedTile(Point p);
}

class FooCityScrollPane extends JScrollPane {
	FooCityGUIInterface Interface;
	FooCityScrollPane(Component viewport, FooCityGUIInterface i) {
		super(viewport);
		Interface = i;
		setAutoscrolls(true);
		setWheelScrollingEnabled(true);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollBar verticalScrollBar = getVerticalScrollBar();
		JScrollBar horizontalScrollBar = getHorizontalScrollBar();
		verticalScrollBar.setUnitIncrement(32);
		horizontalScrollBar.setUnitIncrement(32);

		if (Interface != null) {
			verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {

					Interface.updateDisplay();
				}
			});

			horizontalScrollBar.addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					Interface.updateDisplay();
				}
			});
		}
	}
}
