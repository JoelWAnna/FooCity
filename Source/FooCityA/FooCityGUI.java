// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

import javax.swing.JScrollPane;

import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JPanel;

class FooCityGUIConstants
{
	public static final int TILE_WIDTH  = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int WINDOW_HEIGHT = 600;
	public static final int WINDOW_WIDTH = 800;
	public static final int SIDEBAR_WIDTH = 257;
}

public class FooCityGUI
{

	private MiniMapPanel minimap_panel;
	private JFrame frame;
	private JPanel toolPanel;
	private JScrollPane scrollPane;
	private FooPanel map_panel;
	private FooCityManager city_manager;
	public static FooCityGUI window;
	private JMenuBar menuBar;
	private final Action NewGame = new NewGameAction();
	private final Action Tile = new Place_Tile_Action();
	private int newTile;
	
	private JButton buttonResidential, buttonCommercial, buttonIndustrial,
	buttonPark, buttonSewage, buttonPolice, buttonSolar, buttonRoad, 
	buttonGas, buttonCoal, buttonWindFarm, buttonDirt, buttonForrest, buttonGrass;
	//buttonWater, buttonBeach;
	
	private final String waterTile   		= "Water Tile";
	private final String beachTile  		= "Beach Tile";
	private final String grassTile  		= "Grass Tile";
	private final String dirtTile	 		= "Dirt Tile";
	private final String forrestTile 		= "Forrest Tile";
	private final String residentialTile	= "Residential Tile";
	private final String commercialTile 	= "Commercial Tile";
	private final String industrialTile		= "Industrial Tile";
	private final String parkTile			= "Park Tile";
	private final String sewageTile 		= "Sewage Tile";
	private final String policeTile			= "Police Tile";
	private final String solarTile			= "Solar Tile";
	private final String gasTile			= "Gas Tile";
	private final String coalTile			= "Coal Tile";
	private final String windTile			= "Wind Tile";
	private final String roadTile			= "Road Tile";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new FooCityGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FooCityGUI()
	{
		city_manager = new FooCityManager();
		initialize();
	}

	public FooCityGUI(MapGrid new_map)
	{
		city_manager = new FooCityManager(new_map);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		//newTile = 0;
		frame = new JFrame();
		frame.setVisible(true);
		AddResizeListener();

		frame.setBounds(100, 100, FooCityGUIConstants.WINDOW_WIDTH, FooCityGUIConstants.WINDOW_HEIGHT);
		frame.setTitle("FooCity V0.1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		map_panel = new FooPanel(Color.white);
		map_panel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mousePressed(MouseEvent e)
				{
					if (city_manager.getPlacingTile() == 0)
						return;
					Point p = e.getPoint();
					int x = p.x / FooCityGUIConstants.TILE_WIDTH;
					int y = p.y / FooCityGUIConstants.TILE_HEIGHT;
					//System.out.print(p + " " + x + " " + y);
					if (city_manager.placeTile(x,y))
					{
						map_panel.repaint();
						minimap_panel.repaint();
						city_manager.propagateMetrics();
					}
				}

				@Override
				public void mouseExited(MouseEvent arg0)
				{
					map_panel.setMousePoint(null);
					map_panel.repaint();
				}
			});
		map_panel.addMouseMotionListener(new MouseMotionAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent arg0)
				{
					map_panel.setMousePoint(arg0.getPoint());
				}
			});

		scrollPane = new JScrollPane(map_panel);

		scrollPane.setAutoscrolls(true);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(32);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(32);
		
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
				minimap_panel.repaint();
			}
		});
		
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
				minimap_panel.repaint();
			}
		});
		
		
		// toolPanel contains the entire left toolbar, including minimap.
		toolPanel = new JPanel(new BorderLayout());
		// Create a panel to hold the buttons using a grid 3 columns wide
		// (rows get added automatically by Java as needed)
		GridLayout buttonGridLayout = new GridLayout(0,3);
		JPanel buttonGridPanel = new JPanel();
		buttonGridPanel.setLayout(buttonGridLayout);
		toolPanel.setBounds(0, 0, FooCityGUIConstants.SIDEBAR_WIDTH, FooCityGUIConstants.WINDOW_HEIGHT - 38);
		minimap_panel = new MiniMapPanel(Color.BLUE);
		minimap_panel.repaint();
		
		buttonResidential = new JButton("R");
		buttonResidential.addActionListener(Tile);
		buttonResidential.setActionCommand(residentialTile);
		buttonGridPanel.add(buttonResidential);
		
		buttonCommercial = new JButton("C");
		buttonCommercial.addActionListener(Tile);
		buttonCommercial.setActionCommand(commercialTile);
		buttonGridPanel.add(buttonCommercial);
		
		buttonIndustrial = new JButton("I");
		buttonIndustrial.addActionListener(Tile);
		buttonIndustrial.setActionCommand(industrialTile);
		buttonGridPanel.add(buttonIndustrial);
		
		buttonRoad = new JButton("Road");
		buttonRoad.addActionListener(Tile);
		buttonRoad.setActionCommand(roadTile);
		buttonGridPanel.add(buttonRoad);
		
		buttonPark = new JButton("Park");
		buttonPark.addActionListener(Tile);
		buttonPark.setActionCommand(parkTile);
		buttonGridPanel.add(buttonPark);
		
		buttonSewage = new JButton("Sewage");
		buttonSewage.addActionListener(Tile);
		buttonSewage.setActionCommand(sewageTile);
		buttonGridPanel.add(buttonSewage);
		
		buttonPolice = new JButton("Police");
		buttonPolice.addActionListener(Tile);
		buttonPolice.setActionCommand(policeTile);
		buttonGridPanel.add(buttonPolice);
		
		buttonSolar = new JButton("Solar");
		buttonSolar.addActionListener(Tile);
		buttonSolar.setActionCommand(solarTile);
		buttonGridPanel.add(buttonSolar);
		
		buttonGas = new JButton("Gas");
		buttonGas.addActionListener(Tile);
		buttonGas.setActionCommand(gasTile);
		buttonGridPanel.add(buttonGas);
		
		buttonCoal = new JButton("Coal");
		buttonCoal.addActionListener(Tile);
		buttonCoal.setActionCommand(coalTile);
		buttonGridPanel.add(buttonCoal);
		
		buttonWindFarm = new JButton("Wind");
		buttonWindFarm.addActionListener(Tile);
		buttonWindFarm.setActionCommand(windTile);
		buttonGridPanel.add(buttonWindFarm);
		
		buttonDirt = new JButton("Dirt");
		buttonDirt.addActionListener(Tile);
		buttonDirt.setActionCommand(dirtTile);
		buttonGridPanel.add(buttonDirt);
		
		/*buttonWater = new JButton("Water");
		buttonWater.addActionListener(Tile);
		buttonWater.setActionCommand(waterTile);
		buttonGridPanel.add(buttonWater);*/
		
		buttonGrass = new JButton("Grass");
		buttonGrass.addActionListener(Tile);
		buttonGrass.setActionCommand(grassTile);
		buttonGridPanel.add(buttonGrass);
		
		buttonForrest = new JButton("Forrest");
		buttonForrest.addActionListener(Tile);
		buttonForrest.setActionCommand(forrestTile);
		buttonGridPanel.add(buttonForrest);
		
		/*buttonBeach = new JButton("Beach");
		buttonBeach.addActionListener(Tile);
		buttonBeach.setActionCommand(beachTile);
		buttonGridPanel.add(buttonBeach);*/
		
		toolPanel.add(buttonGridPanel, BorderLayout.PAGE_START);
		toolPanel.add(minimap_panel, BorderLayout.PAGE_END);
		
		frame.getContentPane().add(toolPanel);
		frame.getContentPane().add(scrollPane);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu file_menu = new JMenu("File");
		menuBar.add(file_menu);
		
		JMenuItem menuItem_NewGame = new JMenuItem("New Game");
		menuItem_NewGame.setAction(NewGame);
		file_menu.add(menuItem_NewGame);

		JMenu mnBuild = new JMenu("Build");
		menuBar.add(mnBuild);
		
		
		JMenuItem waterTile_menuItem = new JMenuItem("Place " + waterTile);
		waterTile_menuItem.addActionListener(Tile);
		waterTile_menuItem.setActionCommand(waterTile);
		mnBuild.add(waterTile_menuItem);
		JMenuItem beachTile_menuItem = new JMenuItem("Place " + beachTile);
		beachTile_menuItem.addActionListener(Tile);
		beachTile_menuItem.setActionCommand(beachTile);
		mnBuild.add(beachTile_menuItem);
		JMenuItem grassTile_menuItem = new JMenuItem("Place " + grassTile);
		grassTile_menuItem.addActionListener(Tile);
		grassTile_menuItem.setActionCommand(grassTile);
		mnBuild.add(grassTile_menuItem);
		JMenuItem dirtTile_menuItem = new JMenuItem("Place " + dirtTile);
		dirtTile_menuItem.addActionListener(Tile);
		dirtTile_menuItem.setActionCommand(dirtTile);
		mnBuild.add(dirtTile_menuItem);
		JMenuItem forrestTile_menuItem = new JMenuItem("Place " + forrestTile);
		forrestTile_menuItem.addActionListener(Tile);
		forrestTile_menuItem.setActionCommand(forrestTile);
		mnBuild.add(forrestTile_menuItem);
		
		AddKeyListeners();
	}
	
	public MapGrid getMap() {
		return city_manager.GetMapGrid();
	}
	
	public FooCityManager getCityManager()
	{
		return city_manager;
	}

	public void updateMiniMap(){
		minimap_panel.repaint();
	}
	
	public Rectangle getViewRect(){
		return (Rectangle) scrollPane.getViewport().getVisibleRect().clone();
	}
	
	public void setView(Point center){
		Rectangle rect = getViewRect();
		scrollPane.getHorizontalScrollBar().setValue(center.x * FooCityGUIConstants.TILE_HEIGHT - (rect.width / 2));
		scrollPane.getVerticalScrollBar().setValue(center.y * FooCityGUIConstants.TILE_WIDTH - (rect.height / 2));		
	}
	
	public Point getViewPoint(){
		return (Point) scrollPane.getViewport().getViewPosition().clone();
	}

	public void setMap(MapGrid new_map)
	{
		if (city_manager.SetMapGrid(new_map))
		{
			this.map_panel.repaint();
			this.minimap_panel.repaint();
		}
	}
	private class NewGameAction extends AbstractAction
	{
		public NewGameAction()
		{
			putValue(NAME, "New Game");
			putValue(SHORT_DESCRIPTION, "Select A Map To Load");
		}
		public void actionPerformed(ActionEvent e)
		{
			if (city_manager == null || city_manager.GetMapGrid() == null)
			{
				NewGame newgame = new NewGame();
				scrollPane.repaint();
				map_panel.repaint();
				minimap_panel.repaint();
			}
			else
			{
				/*
				city_manager.Quit();
				 */
				//TODO verify that the user wants to start a new game
			}
		}
	}

	private class Place_Tile_Action extends AbstractAction
	{
		public Place_Tile_Action()
		{
			putValue(NAME, "Place Tile");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e)
		{
			String command = e.getActionCommand();
			if (command == waterTile)
			{
				city_manager.setPlacingTile(MapGridConstants.WATER_TILE);
				return;
			}
			if (command == beachTile)
			{
				city_manager.setPlacingTile(MapGridConstants.BEACH_TILE);
				return;
			}
			if (command == grassTile)
			{
				city_manager.setPlacingTile(MapGridConstants.GRASS_TILE);
				return;
			}
			if (command == dirtTile)
			{
				city_manager.setPlacingTile(MapGridConstants.DIRT_TILE);
				return;
			}
			if (command == forrestTile)
			{
				city_manager.setPlacingTile(MapGridConstants.FORREST_TILE);
				return;
			}
			if (command == industrialTile)
			{
				city_manager.setPlacingTile(MapGridConstants.INDUSTRIAL_TILE);
				return;
			}
		}
	}
	private class keyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED){
				char c = e.getKeyChar();
				Point p = (Point) scrollPane.getViewport().getViewPosition().clone();
				Rectangle r = (Rectangle) scrollPane.getViewport().getVisibleRect().clone();
				int x = p.x;
				int y = p.y;
				//System.out.println(c + " " + x + " " + y + " " + r);
				switch (Character.toUpperCase(c))
				{
				case 'W':
					if (y < FooCityGUIConstants.TILE_HEIGHT)
						y = 0;
					else
						y -= FooCityGUIConstants.TILE_HEIGHT;
					break;
				case 'A':
					if (x < FooCityGUIConstants.TILE_WIDTH)
						x = 0;
					else
						x -= FooCityGUIConstants.TILE_WIDTH;
					break;
				case 'S':
					if (y > MapGridConstants.MAP_HEIGHT * (FooCityGUIConstants.TILE_HEIGHT - 1) - r.height)
						y = MapGridConstants.MAP_HEIGHT * FooCityGUIConstants.TILE_HEIGHT - r.height;
					else
						y += 32;
					break;
				case 'D':
					if (x > MapGridConstants.MAP_WIDTH * (FooCityGUIConstants.TILE_WIDTH - 1) - r.width)
						x = MapGridConstants.MAP_WIDTH * FooCityGUIConstants.TILE_WIDTH - r.width;
					else
						x += 32;
					break;
				default:
					break;
				}
				scrollPane.getViewport().setViewPosition(new Point(x, y));
			}
			return true;
		}
	}
	
	private void AddKeyListeners(){
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new keyDispatcher());
	}
	
	private void AddResizeListener()
	{
		frame.addWindowStateListener(new WindowStateListener()
		{
			public void windowStateChanged(WindowEvent arg0) 
			{	
				Rectangle r = frame.getBounds();
				r.x = FooCityGUIConstants.SIDEBAR_WIDTH; 
				r.y = 0;
				r.width -= FooCityGUIConstants.SIDEBAR_WIDTH + 15;
				r.height -= 60;
				scrollPane.setBounds(r);
				scrollPane.revalidate();
				toolPanel.setBounds(0, 0, FooCityGUIConstants.SIDEBAR_WIDTH, r.height);
				toolPanel.revalidate();
			}
		});
		
		frame.addComponentListener(new ComponentAdapter()
		{
		@Override
			public void componentResized(ComponentEvent arg0) {
	
				Rectangle r = frame.getBounds();
				r.x = FooCityGUIConstants.SIDEBAR_WIDTH; 
				r.y = 0;
				r.width -= FooCityGUIConstants.SIDEBAR_WIDTH + 15;
				r.height -= 60;
				scrollPane.setBounds(r);
				scrollPane.revalidate();
				toolPanel.setBounds(0, 0, FooCityGUIConstants.SIDEBAR_WIDTH, r.height);
				toolPanel.revalidate();
			}
		});
	}
}
