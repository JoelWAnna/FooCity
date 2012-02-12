// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;


class FooPanel extends JPanel
{

	private TileLoader tiles;
	private Point cursor;
    public FooPanel(Color faceColor)
    {
    	super();
    	setForeground(faceColor);
        tiles = new TileLoader();
        cursor = null;
    }
	

    public Dimension getPreferredSize()
    {
        return new Dimension(MapGridConstants.MAP_WIDTH * FooCityGUIConstants.TILE_WIDTH,
        		MapGridConstants.MAP_HEIGHT * FooCityGUIConstants.TILE_HEIGHT);
    }


    @Override
    public void paint(Graphics g1)
    {
    	super.paint(g1);
    	Graphics2D g = (Graphics2D) g1;
    	FooCityManager city_manager = null;
    	MapGrid current_map = null;
    	if (FooCityGUI.window != null)
    		city_manager = FooCityGUI.window.getCityManager();
    	if (city_manager == null)
    		return;
    	current_map = city_manager.GetMapGrid();
    	if (current_map == null)
    		return;
    	Rectangle r = this.getVisibleRect();
    	g.clearRect(r.x, r.y, r.width, r.height);

    	//System.out.print("visiblerect = " + r +"\n"); 
        g.setColor(Color.BLACK);
    	for (int y = 0; y < MapGridConstants.MAP_HEIGHT; ++y)
        {
        	int yCoord = y * FooCityGUIConstants.TILE_HEIGHT;
        	// render to the entire area plus 1 tile extra to eliminate the tearing when rapidly scrolling
        	if ((yCoord < r.y - FooCityGUIConstants.TILE_HEIGHT) || (yCoord >= r.y + r.height + FooCityGUIConstants.TILE_HEIGHT))
        		continue;
        	for (int x = 0; x < MapGridConstants.MAP_WIDTH; ++x)
        	{
        		int xCoord = x * FooCityGUIConstants.TILE_WIDTH;
        		// render to the entire area plus 1 tile extra to eliminate the tearing when rapidly scrolling
            	if ((xCoord < r.x - FooCityGUIConstants.TILE_WIDTH) || (xCoord >= r.x + r.width + FooCityGUIConstants.TILE_WIDTH))
            		continue;
        		BufferedImage bI = null;
        		bI = tiles.GetTitle(current_map.getTileAt(x, y));
        		if (bI != null)
        			g.drawImage(bI, xCoord, yCoord , null);
        		g.drawLine(xCoord, yCoord, xCoord, yCoord+r.height);
        	}
    		g.drawLine(r.x, yCoord, r.x + r.width, yCoord);
        }
    	
    	int mouse_tile = city_manager.getPlacingTile();
    	if (cursor != null && mouse_tile > 0)
    	{
    		BufferedImage mImage = tiles.GetTitle(mouse_tile);
    		if (mImage != null)
    		{
    			final float [] scales = {1f, 1f, 1f, 0.5f};
        		final float [] offsets = new float[4];
        		final RescaleOp rop = new RescaleOp(scales, offsets, null);
        		g.drawImage(mImage, rop, cursor.x & ~(FooCityGUIConstants.TILE_WIDTH-1) , (cursor.y & ~(FooCityGUIConstants.TILE_HEIGHT-1)));
    		}
    	}
		
    }


	public void setMousePoint(Point point)
	{
		this.cursor = point;
		repaint();
	}

}


class MiniMapPanel extends JPanel
{
    public MiniMapPanel(Color faceColor)
    {
    	super();
    	setForeground(faceColor);
    	this.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				Point point = new Point(arg0.getX() / 2, arg0.getY() / 2);
				FooCityGUI.window.setView(point);
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
    	});
    	
    	this.addMouseMotionListener(new MouseMotionAdapter()
    	{
    		@Override
    		public void mouseDragged(MouseEvent arg0)
    		{
    			Point point = new Point(arg0.getX() / 2, arg0.getY() / 2);
    			FooCityGUI.window.setView(point);
    		}
    	});
    }

    @Override
    public void paint(Graphics g1)
    {
    	MapGrid current_map = null;
    	if (FooCityGUI.window != null)
    		current_map = FooCityGUI.window.getMap();
    	if (current_map == null)
    		return;
    	for (int y = 0; y < MapGridConstants.MAP_HEIGHT; y++){
    		for (int x = 0; x < MapGridConstants.MAP_WIDTH; x++)
    		{
    			switch(current_map.getTileAt(x, y))
    			{
    			case (MapGridConstants.BEACH_TILE):
					g1.setColor(new Color(255,225,0));  //A yellowish sandy color
					break;
    			case (MapGridConstants.WATER_TILE):
					g1.setColor(new Color(0,0,180));  // A deep ocean blue
					break;
    			case (MapGridConstants.GRASS_TILE):
    				g1.setColor(new Color(0,180,0));  // A somewhat dark green
					break;
    			case (MapGridConstants.COAL_TILE):   
    				g1.setColor(new Color (50, 50, 50)); // Very dark grey
					break;
    			case (MapGridConstants.COMMERCIAL_TILE):
    				g1.setColor(new Color (0,0,255));   // Vivid blue
					break;
    			case (MapGridConstants.DIRT_TILE):
    				g1.setColor(new Color (140,110,0));  //Brown
					break;
    			case (MapGridConstants.FORREST_TILE):
    				g1.setColor(new Color (0, 75, 0));  // Deep forest green
					break;
    			case (MapGridConstants.INDUSTRIAL_TILE):  // Dirty yellow
    				g1.setColor(new Color (225, 225, 0));
					break;
    			case (MapGridConstants.GAS_TILE):
    				g1.setColor(new Color (96, 112, 204)); // Pale blue 
					break;
    			case (MapGridConstants.PARK_TILE):
    				g1.setColor(new Color (100, 255, 100));		// Pale green 
					break;
    			case (MapGridConstants.POLICE_TILE):
    				g1.setColor(new Color (0,0,200));		//Deep blue
					break;
    			case (MapGridConstants.RESIDENTIAL_TILE):
    				g1.setColor(new Color (0,255, 0));   // Bright green
					break;
    			case (MapGridConstants.SEWAGE_TILE):
    				g1.setColor(new Color (110, 72, 20));   //Dark murkey brown
					break;
    			case (MapGridConstants.SOLAR_TILE):
    				g1.setColor(new Color (255, 244, 128));	// Bright yellow
					break;
    			case (MapGridConstants.WIND_TILE):
    				g1.setColor(new Color (0, 200, 255));	// Sky blue
					break;
    			default:
    				g1.setColor(Color.green);
    				System.err.print("Unknown tile drawn on minimap\n");
					break;
    			}
    			/*
    			int c = current_map.getTile(x, y).crimeActual * 10 ;
    			if (c > 255) c = 255;
    			g1.setColor(new Color(c,c,c));*/
    			g1.fillRect(x * 2, y * 2, 2, 2);
    		}
    	}
    	// Draw the frame around our current view
    	Rectangle r = FooCityGUI.window.getViewRect();
    	Point p = FooCityGUI.window.getViewPoint();
    	//System.out.print("visiblerect = " + r +"\n"); 
    	r.x = 2 * p.x / FooCityGUIConstants.TILE_WIDTH;
    	r.y = 2 * p.y / FooCityGUIConstants.TILE_HEIGHT;
    	r.width = 2 * r.width / FooCityGUIConstants.TILE_WIDTH;
    	r.height = 2 * r.height / FooCityGUIConstants.TILE_HEIGHT;
    	g1.setColor(Color.black);
    	g1.drawRect(r.x, r.y, r.width, r.height);
    }
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(256, 256);
    }
    
}