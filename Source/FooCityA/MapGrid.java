// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

class MapGridConstants
{	
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
	public static final int RESIDENTIAL_TILE = 15;
	public static final int LAST_TILE = 16;
	public static final char CHAR_TILES[] = {' ', 'W', 'B', 'G', 'D', 'T'};
}
/**
 * 
 */

/**
 * @author Joel Anna
 *
 */
public class MapGrid
{
	private Tile[] tileGrid;
	public static PrintStream out = new PrintStream(System.out);

	public static void main(String[] args)
	{
		MapGrid m = new MapGrid();
		m.Print();
	}
	private final static int AREA =  MapGridConstants.MAP_WIDTH * MapGridConstants.MAP_HEIGHT;

	public MapGrid()
	{
		this.tileGrid = new Tile[MapGrid.AREA];
		init();		
	}

	public void Print()
	{
		out.print(this.toString());
	}

	public int GetTileAt(int x,int y) 
	{
		if (tileGrid != null &&
		   (x >= 0 && y >= 0) &&
		   (x < MapGridConstants.MAP_WIDTH) &&
		   (y < MapGridConstants.MAP_HEIGHT))
		{
			Tile current_tile = tileGrid[x + y*MapGridConstants.MAP_HEIGHT];
			if (current_tile != null)
			{
				return current_tile.GetTileInt();
			}
		}
		return 0;
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y =0; y < MapGridConstants.MAP_HEIGHT; ++y)
		{
			for (int x = 0; x < MapGridConstants.MAP_WIDTH; ++x)
				s += tileGrid[y * MapGridConstants.MAP_HEIGHT + x].GetTileChar();
			s += "\n";
		}
		return s;
	}

	private void init()
	{
		for (int i = 0; i < AREA; ++i)
		{
			this.tileGrid[i] = Tile.TileFactory('W');
		}
	}

	public boolean FromFile(String filename)
	{
		if (filename == null)
			return false;
		File infile = new File(filename);
		long length = infile.length();
		if (length == 0 || length < MapGrid.AREA)
			return false;

		Scanner inScanner;
		try
		{
			inScanner = new Scanner(infile);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		for (int y = 0; y < MapGridConstants.MAP_HEIGHT; ++y)
		{
			if (!inScanner.hasNextLine())
				return false;
			
			String currentLine = inScanner.nextLine();
			
			if (currentLine.length() <  MapGridConstants.MAP_WIDTH)
				return false;

			for (int x = 0; x <  MapGridConstants.MAP_WIDTH; ++x)
			{
				this.tileGrid[y *  MapGridConstants.MAP_HEIGHT + x] = Tile.TileFactory(currentLine.charAt(x));
			}
		}
		return true;
	}

	public boolean setTile(int x, int y, int i) 
	{
		if (i >= MapGridConstants.WATER_TILE || i < MapGridConstants.LAST_TILE)
		{
			Tile oldTile = this.tileGrid[y*MapGridConstants.MAP_HEIGHT + x];
			if (oldTile.isReplaceable())
			{
				this.tileGrid[y*MapGridConstants.MAP_HEIGHT + x] = Tile.TileFactory(i);//, oldTile.GetTileInt());
				return true;
			}
		}

		return false;
	}

}

abstract class Tile
{
	private char tileChar;
	protected int tileInt;
	protected boolean replaceable;
	protected Tile(char tileChar)
	{
		this.tileChar = tileChar;
		replaceable = true;
	}

	public boolean isReplaceable()
	{
		return replaceable;
	}

	public char GetTileChar()
	{
		return tileChar;
	}
	
	public int GetTileInt()
	{
		return tileInt;
	}

	public static Tile TileFactory(char type)
	{
		switch (type)
		{
		case 'G':
			return new GrassTile(type);
		case 'W':
			return new WaterTile(type);
		case 'D':
			return new DirtTile(type);
		case 'B':
			return new BeachTile(type);
		case 'T':
			return new ForrestTile(type);
		}

		return new WaterTile(type);
	}

	public static Tile TileFactory(int type)
	{
		return TileFactory(MapGridConstants.CHAR_TILES[type]);
	}
}

class GrassTile extends Tile
{
	public GrassTile(char tileChar)
	{
		super(tileChar);
		tileInt = MapGridConstants.GRASS_TILE;
	}
}

class WaterTile extends Tile
{
	public WaterTile(char tileChar)
	{
		super(tileChar);
		tileInt = MapGridConstants.WATER_TILE;
		replaceable = false;
	}
}

class DirtTile extends Tile
{
	public DirtTile(char tileChar)
	{
		super(tileChar);
		tileInt = MapGridConstants.DIRT_TILE;
	}
}

class BeachTile extends Tile
{
	public BeachTile(char tileChar)
	{
		super(tileChar);
		tileInt = MapGridConstants.BEACH_TILE;
	}
}

class ForrestTile extends Tile
{
	public ForrestTile(char tileChar)
	{
		super(tileChar);
		tileInt = MapGridConstants.FORREST_TILE;
	}
}