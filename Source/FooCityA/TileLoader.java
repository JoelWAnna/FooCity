// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


class TileLoader
{
	private boolean m_valid;

	private String tileNames[];
	private BufferedImage tiles[];
    private String TileSet;

	public TileLoader()
	{
		initStringNames();
		tiles = new BufferedImage[MapGridConstants.LAST_TILE];
		m_valid = true;

		TileSet = "images/" + "SolidColors/";
		for (int i = 1; i < MapGridConstants.LAST_TILE; ++i)
		{
			try
			{
				tiles[i] = ImageIO.read(new File(TileSet + tileNames[i]));
			}
	        catch (Exception e)
	        {
	        //	m_valid = false;
	        }
		}
		m_valid = true;
	}

	public BufferedImage GetTitle(int i)
	{
		if (!m_valid && (i < 0 || i > tiles.length))
			return null;
		return tiles[i];
	}
	
    private void initStringNames()
    {
    	tileNames = new String[MapGridConstants.LAST_TILE];
    	tileNames[MapGridConstants.WATER_TILE] = "water.png";
    	tileNames[MapGridConstants.BEACH_TILE] = "beach.png";
    	tileNames[MapGridConstants.GRASS_TILE] = "grass.png";
    	tileNames[MapGridConstants.DIRT_TILE] = "dirt.png";
    	tileNames[MapGridConstants.FORREST_TILE] = "forrest.png";
    	tileNames[MapGridConstants.INDUSTRIAL_TILE] = "";
    	tileNames[MapGridConstants.COMMERCIAL_TILE] = "";
    	tileNames[MapGridConstants.PARK_TILE] = "";
    	tileNames[MapGridConstants.SEWAGE_TILE] = "";
    	tileNames[MapGridConstants.POLICE_TILE] = "";
    	tileNames[MapGridConstants.SOLAR_TILE] = "";
    	tileNames[MapGridConstants.GAS_TILE] = "";
    	tileNames[MapGridConstants.COAL_TILE] = "";
    	tileNames[MapGridConstants.WIND_TILE] = "";
    	tileNames[MapGridConstants.RESIDENTIAL_TILE] = "";
    	tileNames[MapGridConstants.ROAD_TILE] = "";
    }

}