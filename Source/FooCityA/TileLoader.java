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
		if (i < 0 || i > tiles.length)
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
    	tileNames[MapGridConstants.SEWAGE_WATER_TREATMENT_TILE] = "";
    	tileNames[MapGridConstants.POLICESTATION_TILE] = "";
    	tileNames[MapGridConstants.SOLAR_POWER_PLANT_TILE] = "";
    	tileNames[MapGridConstants.NATURAL_GAS_PLANT] = "";
    	tileNames[MapGridConstants.COAL_PLANT] = "";
    	tileNames[MapGridConstants.WIND_FARM] = "";
    	tileNames[MapGridConstants.RESIDENTIAL_TILE] = "";
    }

/*	public BufferedImage GetTitle(char c)
	{
		if (m_valid)
		{
			switch (c)
			{
			case 'G':
				return tiles[MapGridConstants.GRASS_TILE];
			case 'W':
				return tiles[MapGridConstants.WATER_TILE];
			case 'D':
				return tiles[MapGridConstants.DIRT_TILE];
			case 'B':
				return tiles[MapGridConstants.BEACH_TILE];
			case 'T':
				return tiles[MapGridConstants.FORREST_TILE];
			default:
				System.out.print(c + " ");
				break;
			}
		}
		return null;
	}
*/
}