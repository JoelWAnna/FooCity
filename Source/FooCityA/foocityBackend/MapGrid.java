package foocityBackend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
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
			FooLogger.errorLog("MapGrid: width != height (" + width + ", " + height + ")");
		map_area = new Dimension(width, height);
		this.tileGrid = new Tile[(int) map_area.getWidth()][(int) map_area.getHeight()];
		map_size = width * height;
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
			FooLogger.errorLog("MapGrid:FromFile FileNotFoundException");
			FooLogger.errorLog(e.getMessage());
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
		if ((MapGridConstants.WATER_TILE <= i && i < MapGridConstants.LAST_TILE)
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
				matrix[x][y] = TileMetrics.GetTileMetrics(tileGrid[x][y].getTileInt()).getJobs() < 0 ? -TileMetrics.GetTileMetrics(tileGrid[x][y].getTileInt()).getJobs() : 0;
		return matrix;
	}

	public int[][] getJobMatrix()
	{
		int [][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
			{
				matrix[x][y] = (TileMetrics.GetTileMetrics(tileGrid[x][y].getTileInt()).getJobs() > 0) ? TileMetrics.GetTileMetrics(tileGrid[x][y].getTileInt()).getJobs() : 0;
			}
		return matrix;
	}

	public int[][] getRoadMatrix()
	{
		int [][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
			{
				switch (tileGrid[x][y].getTileInt())
				{
				case MapGridConstants.WATER_TILE:
					matrix[x][y] = JobManager.NO_PATH;
					break;
				case MapGridConstants.ROAD_TILE:
					matrix[x][y] = JobManager.PATH_ROAD;
					break;
				default: 
					matrix[x][y] = JobManager.PATH_WALK;
				}
			}
		return matrix;
	}

	public void updateMetrics(int x, int y, int metric, int value)
	{
		if (tileInRange(x, y))
		{
			if (MapGridConstants.METRIC_CRIME <= metric && metric < MapGridConstants.METRIC_LAST)
				tileGrid[x][y].metricsActual[metric] += value;
		}
	}

}



