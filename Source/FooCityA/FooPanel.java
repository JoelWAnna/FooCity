import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import javax.swing.JPanel;


class FooPanel extends JPanel {

	private TileLoader tiles;
	
    public FooPanel(Color faceColor)
    {
        setForeground(faceColor);
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
    	System.out.print("visiblerect = " + r +"\n"); 
        
    	//int yCoord = r.y - FooCityConstants.TILE_HEIGHT;
 
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
        	}
        }
		
    }


}