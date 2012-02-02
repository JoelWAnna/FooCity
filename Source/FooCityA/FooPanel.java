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
	private char mouseTile;
    public FooPanel(Color faceColor)
    {
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


	public void setMousePoint(Point point, char newTile)
	{
		PlacingTile = true;
		this.cursor = point;
		this.mouseTile = newTile;
		repaint();
		
	}


}