import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

import javax.swing.JScrollPane;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

class FooCityConstants
{
	public static final int TILE_WIDTH  = 32;
	public static final int TILE_HEIGHT = 32;
	public static final int MAP_WIDTH   = 128;
	public static final int MAP_HEIGHT  = 128;
	
	public static final int WATER_TILE = 1;
	public static final int BEACH_TILE = 2;
	public static final int GRASS_TILE = 3;
	public static final int DIRT_TILE = 4;
	public static final int FORREST_TILE = 5;
	public static final int INDUSTRIAL_TILE = 6;
	public static final int COMMERCIAL_TILE = 7;
	public static final int PARK_TILE = 8;
	public static final int SEWAGE_WATER_TREATMENT_TILE = 9;
	public static final int POLICESTATION_TILE = 10;
	public static final int SOLAR_POWER_PLANT_TILE = 11;
	public static final int NATURAL_GAS_PLANT = 12;
	public static final int COAL_PLANT = 13;
	public static final int WIND_FARM = 14;
	public static final int RESIDENTIAL = 15;
	public static final int LAST_TILE = 16;
	public static final char CHAR_TILES[] = {' ', 'W', 'B', 'G', 'D', 'T'};
}
public class FooCityGUI
{

	private JFrame frame;
	private JScrollPane scrollPane;
	private FooPanel rendering_panel;
	private MapGrid m;
	public static FooCityGUI window;
	private JMenuBar menuBar;
	private final Action NewGame = new NewGameAction();
	private final Action Tile = new Place_Tile_Action();
	private int newTile;
	
	
	private final String waterTile   = "Water Tile";
	private final String beachTile   = "Beach Tile";
	private final String grassTile   = "Grass Tile";
	private final String dirtTile	 = "Dirt Tile";
	private final String forrestTile = "Forrest Tile";
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
		initialize();
	}

	public FooCityGUI(MapGrid mapData)
	{
		m = mapData;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		newTile = 0;
		frame = new JFrame();
		frame.setVisible(true);
		AddResizeListener();

		frame.setBounds(100, 100, 800, 600);
		frame.setTitle("FooCity V0.1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		rendering_panel = new FooPanel(Color.white);
		rendering_panel.addMouseListener(new MouseAdapter()
			{
				@Override
				public void mouseClicked(MouseEvent e)
				{
					if (newTile == 0)
						return;
					Point p = e.getPoint();
					int x = p.x / FooCityConstants.TILE_WIDTH;
					int y = p.y / FooCityConstants.TILE_HEIGHT;
					//System.out.print(p + " " + x + " " + y);
					m.setTile(x,y, newTile);
					rendering_panel.repaint();
					rendering_panel.PlacingTile = false;
					
					newTile = 0;
				}
			});
		rendering_panel.addMouseMotionListener(new MouseMotionAdapter()
			{
				@Override
				public void mouseMoved(MouseEvent arg0)
				{
					if (newTile == 0)
						return;
					rendering_panel.setMousePoint(arg0.getPoint(), newTile);
					
				}
			});

		scrollPane = new JScrollPane(rendering_panel);

		scrollPane.setBounds(0, 0, 784, 562);
		scrollPane.setAutoscrolls(true);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(32);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(32);
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
	
	public MapGrid getM() {
		return m;
	}

	public void setM(MapGrid m) {
		this.m = m;
		this.rendering_panel.repaint();
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
			if (m == null)
			{
				NewGame newgame = new NewGame();
				scrollPane.repaint();
				rendering_panel.repaint();
			}
			else
			{
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
				newTile = FooCityConstants.WATER_TILE;
				return;
			}
			if (command == beachTile)
			{
				newTile = FooCityConstants.BEACH_TILE;
				return;
			}
			if (command == grassTile)
			{
				newTile = FooCityConstants.GRASS_TILE;
				return;
			}
			if (command == dirtTile)
			{
				newTile = FooCityConstants.DIRT_TILE;
				return;
			}
			if (command == forrestTile)
			{
				newTile = FooCityConstants.FORREST_TILE;
				return;
			}
		}
	}
	
	private void AddKeyListeners()
	{
		frame.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent arg0)
			{
				char c = arg0.getKeyChar();
				Point p = (Point) scrollPane.getViewport().getViewPosition().clone();
				Rectangle r = (Rectangle) scrollPane.getViewport().getVisibleRect().clone();
				int x = p.x;
				int y = p.y;
				//System.out.println(c + " " + x + " " + y + " " + r);
				switch (Character.toUpperCase(c))
				{
				case 'W':
					if (y < FooCityConstants.TILE_HEIGHT)
						y = 0;
					else
						y -= FooCityConstants.TILE_HEIGHT;
					break;
				case 'A':
					if (x < FooCityConstants.TILE_WIDTH)
						x = 0;
					else
						x -= FooCityConstants.TILE_WIDTH;
					break;
				case 'S':
					if (y > FooCityConstants.MAP_HEIGHT * (FooCityConstants.TILE_HEIGHT - 1) - r.height)
						y = FooCityConstants.MAP_HEIGHT * FooCityConstants.TILE_HEIGHT - r.height;
					else
						y += 32;
					break;
				case 'D':
					if (x > FooCityConstants.MAP_WIDTH * (FooCityConstants.TILE_WIDTH - 1) - r.width)
						x = FooCityConstants.MAP_WIDTH * FooCityConstants.TILE_WIDTH - r.width;
					else
						x += 32;
					break;
				default:
					break;
				}
				scrollPane.getViewport().setViewPosition(new Point(x, y));
			}
		});
	}
	
	private void AddResizeListener()
	{
		frame.addComponentListener(new ComponentAdapter() {
		@Override
			public void componentResized(ComponentEvent arg0) {
	
				Rectangle r = frame.getBounds();
				r.x = r.y = 0;
				r.width -= 15;
				r.height -= 60;
				scrollPane.setBounds(r);
				rendering_panel.setBounds(r);
			}
		});
	}
}
