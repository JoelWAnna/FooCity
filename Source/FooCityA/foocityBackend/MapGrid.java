package foocityBackend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Dimension;
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
class MapGrid
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

	public MapGrid(Dimension mapArea)
	{
		this((int)mapArea.getWidth(), (int)mapArea.getHeight());
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

	public int getTileInt(int x,int y) 
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

	public int[][] getResidentialMatrix()
	{
		int [][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
				matrix[x][y] = (tileGrid[x][y].jobs < 0) ? -tileGrid[x][y].jobs : 0;
		return matrix;
	}

	public int[][] getJobMatrix()
	{
		int [][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
			{
				matrix[x][y] = (tileGrid[x][y].jobs > 0) ? tileGrid[x][y].jobs : 0;
				//System.out.println(x + " " + y + " " + matrix[x][y]);
			}
		return matrix;
	}

	public int[][] getRoadMatrix()
	{
		int [][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
			{
				if (tileGrid[x][y].tileInt == MapGridConstants.ROAD_TILE)
					matrix[x][y] = 1;
				else
					matrix[x][y] = 0;
			}
		return matrix;
	}

}



