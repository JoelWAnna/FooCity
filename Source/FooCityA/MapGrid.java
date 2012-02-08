import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

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
	private final static int AREA =  FooCityConstants.MAP_WIDTH * FooCityConstants.MAP_HEIGHT;

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
		return this.tileGrid[x + y*FooCityConstants.MAP_HEIGHT].GetTileInt();
	}

	@Override
	public String toString()
	{
		String s = "";
		for (int y =0; y < FooCityConstants.MAP_HEIGHT; ++y)
		{
			for (int x = 0; x < FooCityConstants.MAP_WIDTH; ++x)
				s += tileGrid[y * FooCityConstants.MAP_HEIGHT + x].GetTileChar();
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
		
		
		for (int y = 0; y < FooCityConstants.MAP_HEIGHT; ++y)
		{
			if (!inScanner.hasNextLine())
				return false;
			
			String currentLine = inScanner.nextLine();
			
			if (currentLine.length() <  FooCityConstants.MAP_WIDTH)
				return false;

			for (int x = 0; x <  FooCityConstants.MAP_WIDTH; ++x)
			{
				this.tileGrid[y *  FooCityConstants.MAP_HEIGHT + x] = Tile.TileFactory(currentLine.charAt(x));
			}
		}
		return true;
	}

	public void setTile(int x, int y, char c) 
	{
		this.tileGrid[y*FooCityConstants.MAP_HEIGHT + x] = Tile.TileFactory(c);
		
	}

	public void setTile(int x, int y, int i) 
	{
		this.tileGrid[y*FooCityConstants.MAP_HEIGHT + x] = Tile.TileFactory(i);
		
	}

}

abstract class Tile
{
	private char tileChar;
	protected int tileInt;
	protected Tile(char tileChar)
	{
		this.tileChar = tileChar;
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
		return TileFactory(FooCityConstants.CHAR_TILES[type]);
	}
}

class GrassTile extends Tile
{
	public GrassTile(char tileChar)
	{
		super(tileChar);
		tileInt = FooCityConstants.GRASS_TILE;
	}
}

class WaterTile extends Tile
{
	public WaterTile(char tileChar)
	{
		super(tileChar);
		tileInt = FooCityConstants.WATER_TILE;
	}
}

class DirtTile extends Tile
{
	public DirtTile(char tileChar)
	{
		super(tileChar);
		tileInt = FooCityConstants.DIRT_TILE;
	}
}

class BeachTile extends Tile
{
	public BeachTile(char tileChar)
	{
		super(tileChar);
		tileInt = FooCityConstants.BEACH_TILE;
	}
}

class ForrestTile extends Tile
{
	public ForrestTile(char tileChar)
	{
		super(tileChar);
		tileInt = FooCityConstants.FORREST_TILE;
	}
}