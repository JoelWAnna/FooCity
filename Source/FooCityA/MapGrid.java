// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Dimension;
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
	public static final int SEWAGE_TILE = 9;
	public static final int POLICE_TILE = 10;
	public static final int SOLAR_TILE = 11;
	public static final int GAS_TILE = 12;
	public static final int COAL_TILE = 13;
	public static final int WIND_TILE = 14;
	public static final int RESIDENTIAL_TILE = 15;
	public static final int ROAD_TILE = 16;
	public static final int LAST_TILE = 17;
	public static final char CHAR_TILES[] = {' ', 'W', 'B', 'G', 'D', 'T', 'I', 'C', 
											 'P', 'S', 'O', 'L', 'A', 'E', 'N', 'R', 'F'};
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
	private Tile[][] tileGrid;
	private Dimension map_area;
	private int map_size;
	
	public Dimension getMapArea() {
		return (Dimension)map_area.clone();
	}

	public static PrintStream out = new PrintStream(System.out);

	public MapGrid()
	{
		this(MapGridConstants.MAP_WIDTH, MapGridConstants.MAP_HEIGHT);
	}
	
	public MapGrid(int width, int height)
	{
		if (width < 5 || height < 5)
		{
			width = MapGridConstants.MAP_WIDTH;
			height = MapGridConstants.MAP_HEIGHT;
		}
		if (width != height)
			System.out.println("width != height");
		map_area = new Dimension(width, height);
		this.tileGrid = new Tile[(int) map_area.getWidth()][(int) map_area.getHeight()];
		map_size = width * height;
	}

	public void Print()
	{
		out.print(this.toString());
	}

	private boolean tileInRange(int x, int y)
	{
		return (tileGrid != null &&
				   (x >= 0 && y >= 0) &&
				   (x < map_area.getHeight()) &&
				   (y < map_area.getWidth()));
	}

	public int getTileAt(int x,int y) 
	{
		if (tileInRange(x, y))
		{
			Tile current_tile = tileGrid[x][y];
			if (current_tile != null)
			{
				return current_tile.getTileInt();
			}
		}
		return 0;
	}
	
	public Tile getTile(int x, int y)
	{
		if (tileInRange(x, y))
			return tileGrid[x][y];
		return null;
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y =0; y < map_area.getHeight(); ++y)
		{
			for (int x = 0; x < map_area.getWidth(); ++x)
				s += tileGrid[x][y].getTileChar();
			s += "\n";
		}
		return s;
	}

	public boolean FromString(String MapGridString)
	{
		if (MapGridString != null)// && MapGridString.length() != map_area.getHeight() * (map_area.getWidth()+1))
		{
			Scanner sc = new Scanner(MapGridString);
			return fromScanner(sc);
		}
		return false;			
	}

	public boolean FromFile(String filename)
	{
		if (filename == null)
			return false;
		File infile = new File(filename);
		long length = infile.length();

		if (length == 0 || length < this.map_size)
		{
			if (length < 25)
				return false;
			int width = (int) Math.sqrt((double)length);
			
			map_area = new Dimension(width, width);
			map_size = width*width;
			tileGrid = new Tile[(int) map_area.getWidth()][(int) map_area.getHeight()];
				
		}

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
		
		return fromScanner(inScanner);
	}

	public boolean fromScanner(Scanner inScanner)
	{
		for (int y = 0; y < map_area.getHeight(); ++y)
		{
			if (!inScanner.hasNextLine())
				return false;
			
			String currentLine = inScanner.nextLine();
			
			if (currentLine.length() < map_area.getWidth())
				return false;

			for (int x = 0; x <  map_area.getWidth(); ++x)
			{
				this.tileGrid[x][y] = new Tile(currentLine.charAt(x));
			}
		}
		return true;
	}

	public int TileCharToInt(char c)
	{
		for (int i = 0; i < MapGridConstants.CHAR_TILES.length; ++i)
		{
			if ( MapGridConstants.CHAR_TILES[i] == c)
				return i;
		}
		return 0;
	}
	public boolean setTile(int x, int y, int i) 
	{
		if ((0 < i && i < MapGridConstants.LAST_TILE)
			&& tileInRange(x,y))
		{
			Tile oldTile = this.tileGrid[x][y];
			if (oldTile.isReplaceable())
			{
				this.tileGrid[x][y] = new Tile(i);//, oldTile.GetTileInt());
				return true;
			}
		}

		return false;
	}

}



