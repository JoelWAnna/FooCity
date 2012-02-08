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
		tiles = new BufferedImage[FooCityConstants.LAST_TILE];
		m_valid = true;

		TileSet = "images/" + "SolidColors/";
		for (int i = 1; i < FooCityConstants.LAST_TILE; ++i)
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
    	tileNames = new String[FooCityConstants.LAST_TILE];
    	tileNames[FooCityConstants.WATER_TILE] = "water.png";
    	tileNames[FooCityConstants.BEACH_TILE] = "beach.png";
    	tileNames[FooCityConstants.GRASS_TILE] = "grass.png";
    	tileNames[FooCityConstants.DIRT_TILE] = "dirt.png";
    	tileNames[FooCityConstants.FORREST_TILE] = "forrest.png";
    	tileNames[FooCityConstants.INDUSTRIAL_TILE] = "";
    	tileNames[FooCityConstants.COMMERCIAL_TILE] = "";
    	tileNames[FooCityConstants.PARK_TILE] = "";
    	tileNames[FooCityConstants.SEWAGE_WATER_TREATMENT_TILE] = "";
    	tileNames[FooCityConstants.POLICESTATION_TILE] = "";
    	tileNames[FooCityConstants.SOLAR_POWER_PLANT_TILE] = "";
    	tileNames[FooCityConstants.NATURAL_GAS_PLANT] = "";
    	tileNames[FooCityConstants.COAL_PLANT] = "";
    	tileNames[FooCityConstants.WIND_FARM] = "";
    	tileNames[FooCityConstants.RESIDENTIAL] = "";
    }

/*	public BufferedImage GetTitle(char c)
	{
		if (m_valid)
		{
			switch (c)
			{
			case 'G':
				return tiles[FooCityConstants.GRASS_TILE];
			case 'W':
				return tiles[FooCityConstants.WATER_TILE];
			case 'D':
				return tiles[FooCityConstants.DIRT_TILE];
			case 'B':
				return tiles[FooCityConstants.BEACH_TILE];
			case 'T':
				return tiles[FooCityConstants.FORREST_TILE];
			default:
				System.out.print(c + " ");
				break;        				
			}
		}
		return null;
	}
*/
}