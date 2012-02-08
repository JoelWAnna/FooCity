import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.util.Scanner;

import javax.swing.JPanel;


class FooPanel extends JPanel
{

	private TileLoader tiles;
	private Point cursor;
	public boolean PlacingTile;
	private int mouseTile;
    public FooPanel(Color faceColor)
    {
    	super();
    	setForeground(faceColor);
        PlacingTile = false;
        tiles = new TileLoader();
    }
	

    public Dimension getPreferredSize()
    {
        return new Dimension(FooCityConstants.MAP_WIDTH * FooCityConstants.TILE_WIDTH,
        					FooCityConstants.MAP_HEIGHT * FooCityConstants.TILE_HEIGHT);
    }


    @Override
    public void paint(Graphics g1)
    {
    	super.paint(g1);
    	Graphics2D g = (Graphics2D) g1;
    	MapGrid m = null;
    	if (FooCityGUI.window != null)
    		m = FooCityGUI.window.getM();
    	if (m == null)
    		return;
    	Rectangle r = this.getVisibleRect();
    	g.clearRect(r.x, r.y, r.width, r.height);

    	//System.out.print("visiblerect = " + r +"\n"); 
        g.setColor(Color.BLACK);
    	for (int y = 0; y < FooCityConstants.MAP_HEIGHT; ++y)
        {
        	int yCoord = y * FooCityConstants.TILE_HEIGHT;
        	// render to the entire area plus 1 tile extra to eliminate the tearing when rapidly scrolling
        	if ((yCoord < r.y - FooCityConstants.TILE_HEIGHT) || (yCoord >= r.y + r.height + FooCityConstants.TILE_HEIGHT))
        		continue;
        	for (int x = 0; x < FooCityConstants.MAP_WIDTH; ++x)
        	{
        		int xCoord = x * FooCityConstants.TILE_WIDTH;
        		// render to the entire area plus 1 tile extra to eliminate the tearing when rapidly scrolling
            	if ((xCoord < r.x - FooCityConstants.TILE_WIDTH) || (xCoord >= r.x + r.width + FooCityConstants.TILE_WIDTH))
            		continue;
        		BufferedImage bI = null;
        		bI = tiles.GetTitle(m.GetTileAt(x, y));
        		if (bI != null)
        			g.drawImage(bI, xCoord, yCoord , null);
        		g.drawLine(xCoord, yCoord, xCoord, yCoord+r.height);
        	}
    		g.drawLine(r.x, yCoord, r.x + r.width, yCoord);
        }
    	
    	if (PlacingTile)
    	{
    		BufferedImage mImage = tiles.GetTitle(mouseTile);
    		if (mImage != null)
    		{
    			final float [] scales = {1f, 1f, 1f, 0.5f};
        		final float [] offsets = new float[4];
        		final RescaleOp rop = new RescaleOp(scales, offsets, null);
        		g.drawImage(mImage, rop, cursor.x & ~(FooCityConstants.TILE_WIDTH-1) , (cursor.y & ~(FooCityConstants.TILE_HEIGHT-1)));
    		}
    	}
		
    }


	public void setMousePoint(Point point, int newTile)
	{
		PlacingTile = true;
		this.cursor = point;
		this.mouseTile = newTile;
		repaint();
		
	}

}


class MiniMapPanel extends JPanel
{
    public MiniMapPanel(Color faceColor)
    {
    	super();
    	setForeground(faceColor);
    }
    @Override
    public void paint(Graphics g1)
    {
    	MapGrid m = null;
    	if (FooCityGUI.window != null)
    		m = FooCityGUI.window.getM();
    	if (m == null)
    		return;
    	for (int y = 0; y < FooCityConstants.MAP_HEIGHT; y++){
    		for (int x = 0; x < FooCityConstants.MAP_WIDTH; x++) {
    			switch(m.GetTileAt(x, y)){
    			case (FooCityConstants.BEACH_TILE):
					g1.setColor(new Color(255,225,0));  //A yellowish sandy color
					break;
    			case (FooCityConstants.WATER_TILE):
					g1.setColor(new Color(0,0,180));  // A deep ocean blue
					break;
    			case (FooCityConstants.GRASS_TILE):
    				g1.setColor(new Color(0,180,0));  // A somewhat dark green
					break;
    			case (FooCityConstants.COAL_PLANT):   
    				g1.setColor(new Color (50, 50, 50)); // Very dark grey
					break;
    			case (FooCityConstants.COMMERCIAL_TILE):
    				g1.setColor(new Color (0,0,255));   // Vivid blue
					break;
    			case (FooCityConstants.DIRT_TILE):
    				g1.setColor(new Color (140,110,0));  //Brown
					break;
    			case (FooCityConstants.FORREST_TILE):
    				g1.setColor(new Color (0, 75, 0));  // Deep forest green
					break;
    			case (FooCityConstants.INDUSTRIAL_TILE):  // Dirty yellow
    				g1.setColor(new Color (225, 225, 0));
					break;
    			case (FooCityConstants.NATURAL_GAS_PLANT):
    				g1.setColor(new Color (96, 112, 204)); // Pale blue 
					break;
    			case (FooCityConstants.PARK_TILE):
    				g1.setColor(new Color (100, 255, 100));		// Pale green 
					break;
    			case (FooCityConstants.POLICESTATION_TILE):
    				g1.setColor(new Color (0,0,200));		//Deep blue
					break;
    			case (FooCityConstants.RESIDENTIAL_TILE):
    				g1.setColor(new Color (0,255, 0));   // Bright green
					break;
    			case (FooCityConstants.SEWAGE_WATER_TREATMENT_TILE):
    				g1.setColor(new Color (110, 72, 20));   //Dark murkey brown
					break;
    			case (FooCityConstants.SOLAR_POWER_PLANT_TILE):
    				g1.setColor(new Color (255, 244, 128));	// Bright yellow
					break;
    			case (FooCityConstants.WIND_FARM):
    				g1.setColor(new Color (0, 200, 255));	// Sky blue
					break;
    			default:
    				g1.setColor(Color.green);
    				System.err.print("Unknown tile drawn on minimap\n");
					break;
    			}
    			g1.fillRect(x * 2, y * 2, 2, 2);
    		}
    	}
    	// Draw the frame around our current view
    	Rectangle r = FooCityGUI.window.getViewRect();
    	Point p = FooCityGUI.window.getViewPoint();
    	System.out.print("visiblerect = " + r +"\n"); 
    	r.x = 2 * p.x / FooCityConstants.TILE_WIDTH;
    	r.y = 2 * p.y / FooCityConstants.TILE_HEIGHT;
    	r.width = 2 * r.width / FooCityConstants.TILE_WIDTH;
    	r.height = 2 * r.height / FooCityConstants.TILE_HEIGHT;
    	g1.setColor(Color.black);
    	g1.drawRect(r.x, r.y, r.width, r.height);
    }
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(256, 256);
    }
    
}