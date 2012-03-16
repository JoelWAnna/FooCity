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
 * @author Joel Anna
 * 
 */

class MapGrid {
	private Tile[][] tileGrid;
	private Dimension map_area;
	private boolean valid;

	public static MapGrid FromString(String map_string) {
		if (map_string != null) {
			long length = map_string.length();
			if (length >= 25) {
				int width, height;
				width = height = (int) Math.sqrt((double) length);
				Scanner inScanner;
				inScanner = new Scanner(map_string);

				MapGrid grid = new MapGrid(new Dimension(width, height),
						inScanner);
				if (grid.isValid())
					return grid;
			}
		}
		return null;
	}

	public static MapGrid FromFile(String filename) {
		if (filename != null) {
			File infile = new File(filename);
			long length = infile.length();
			if (length >= 25) {
				int width, height;
				width = height = (int) Math.sqrt((double) length);
				Scanner inScanner;
				try {
					inScanner = new Scanner(infile);
				} catch (FileNotFoundException e) {
					FooLogger
							.errorLog("MapGrid:FromFile FileNotFoundException");
					FooLogger.errorLog(e.getMessage());
					return null;
				}

				MapGrid grid = new MapGrid(new Dimension(width, height),
						inScanner);
				if (grid.isValid())
					return grid;
			}
		}

		return null;
	}

	private MapGrid(Dimension map_area, Scanner inscanner) {
		valid = false;
		if ((map_area.width >= 5) || (map_area.height >= 5)) {
			this.map_area = (Dimension) map_area.clone();
			tileGrid = new Tile[this.map_area.width][this.map_area.height];
			valid = fromScanner(inscanner);
			if (!valid)
				tileGrid = null;
			else
				setAllVariations();
		}

	}

	private boolean fromScanner(Scanner inScanner) {
		for (int y = 0; y < map_area.getHeight(); ++y) {
			if (!inScanner.hasNextLine())
				return false;

			String currentLine = inScanner.nextLine();

			if (currentLine.length() < map_area.getWidth())
				return false;

			for (int x = 0; x < map_area.getWidth(); ++x) {
				char nextTile = currentLine.charAt(x);
				if (TileCharToInt(nextTile) == 0)
					return false;
				this.tileGrid[x][y] = new Tile(nextTile);
			}
		}
		return true;
	}

	private boolean isValid() {
		return valid;
	}

	public Dimension getMapArea() {
		return (Dimension) map_area.clone();
	}

	private boolean tileInRange(int x, int y) {
		return (tileGrid != null && (x >= 0 && y >= 0)
				&& (x < map_area.getHeight()) && (y < map_area.getWidth()));
	}

	public int getTileInt(int x, int y) {
		if (tileInRange(x, y)) {
			Tile current_tile = tileGrid[x][y];
			if (current_tile != null) {
				return current_tile.getTileInt();
			}
		}
		return 0;
	}

	public Tile getTile(int x, int y) {
		if (tileInRange(x, y))
			return tileGrid[x][y];
		return null;
	}

	@Override
	public String toString() {
		String s = "";
		for (int y = 0; y < map_area.getHeight(); ++y) {
			for (int x = 0; x < map_area.getWidth(); ++x)
				s += tileGrid[x][y].getTileChar();
			s += "\n";
		}
		return s;
	}
	public String toStringResidents(){
		String s = "";
		for (int y = 0; y < map_area.getHeight(); ++y) {
			for (int x = 0; x < map_area.getWidth(); ++x)
				s += tileGrid[x][y].residents + " ";
			s += "\n";
		}
		return s;
	}

	public static int TileCharToInt(char c) {
		for (int i = 0; i < MapGridConstants.CHAR_TILES.length; ++i) {
			if (MapGridConstants.CHAR_TILES[i] == c)
				return i;
		}
		return 0;
	}
	public boolean setTile(int x, int y, int i) {
		if ((MapGridConstants.WATER_TILE <= i && i < MapGridConstants.LAST_TILE)
				&& tileInRange(x, y) || i == MapGridConstants.BULLDOZE_TILE){
			Tile oldTile = this.tileGrid[x][y];
			
			if (i == MapGridConstants.BULLDOZE_TILE) {
				
				if (oldTile.getTileInt() > MapGridConstants.FORREST_TILE) {
					this.tileGrid[x][y] = new Tile(MapGridConstants.DIRT_TILE);
					setVariations(x, y);
					return true;
				}
			}
			else if (oldTile.isReplaceable() && oldTile.getTileInt() != i && oldTile.getTileInt() <=  MapGridConstants.FORREST_TILE) {
				this.tileGrid[x][y] = new Tile(i);
				//if (this.tileGrid[x][y].hasVariations())
				setVariations(x, y);
			return true;
			}
		}

		return false;
	}

	private void setAllVariations(){
		for (int x = 0; x < MapGridConstants.MAP_WIDTH; x++){
			for (int y = 0; y < MapGridConstants.MAP_HEIGHT; y++){
				setVariation(x, y);
			}
		}
	}
	
	private void setVariations(int x, int y) {
		// Set the graphic variation for this and the neighboring
		// tiles
		for (int xv = x - 1; xv < x + 2; xv++) {
			for (int yv = y - 1; yv < y + 2; yv++) {
				setVariation(xv, yv);
			}
		}

	}

	private void setVariation(int x, int y) {
		// First, make sure the coordinates are valid
		if (((x < 0 || x >= MapGridConstants.MAP_WIDTH) || (y < 0 || y >= MapGridConstants.MAP_HEIGHT)))
			return;

		int variation = 0;
		switch (this.tileGrid[x][y].getTileInt()) {
			case MapGridConstants.ROAD_TILE :
				if (y > 0)
					if (getTileInt(x, y - 1) == MapGridConstants.ROAD_TILE)
						variation += 1;
				if (x < MapGridConstants.MAP_WIDTH - 1)
					if (getTileInt(x + 1, y) == MapGridConstants.ROAD_TILE)
						variation += 2;
				if (y < MapGridConstants.MAP_HEIGHT - 1)
					if (getTileInt(x, y + 1) == MapGridConstants.ROAD_TILE)
						variation += 4;
				if (x > 0)
					if (getTileInt(x - 1, y) == MapGridConstants.ROAD_TILE)
						variation += 8;
				break;
			default :
				variation = 0;

		}

		this.tileGrid[x][y].setVariation(variation);
	}

	public int[][] getResidentialMatrix() {
		int[][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x)
				/*matrix[x][y] = TileMetrics.GetTileMetrics(
						tileGrid[x][y].getTileInt()).getJobs() < 0
						? -TileMetrics.GetTileMetrics(
								tileGrid[x][y].getTileInt()).getJobs()
						: 0;*/
				if (tileGrid[x][y].getTileInt() == MapGridConstants.RESIDENTIAL_TILE)
					matrix[x][y] = tileGrid[x][y].residents;
		return matrix;
	}

	public int[][] getJobMatrix() {
		int[][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x) {
				/*matrix[x][y] = (TileMetrics.GetTileMetrics(
						tileGrid[x][y].getTileInt()).getJobs() > 0)
						? TileMetrics.GetTileMetrics(
								tileGrid[x][y].getTileInt()).getJobs()
						: 0;*/
				if (tileGrid[x][y].getTileInt() != MapGridConstants.RESIDENTIAL_TILE)
					matrix[x][y] = TileMetrics.GetTileMetrics(tileGrid[x][y].getTileInt()).getJobs();
			}
		return matrix;
	}

	public int[][] getRoadMatrix() {
		int[][] matrix = new int[this.map_area.width][this.map_area.height];
		for (int y = 0; y < this.map_area.height; ++y)
			for (int x = 0; x < this.map_area.width; ++x) {
				switch (tileGrid[x][y].getTileInt()) {
					case MapGridConstants.WATER_TILE :
						matrix[x][y] = JobManager.NO_PATH;
						break;
					case MapGridConstants.ROAD_TILE :
						matrix[x][y] = JobManager.PATH_ROAD;
						break;
					default :
						matrix[x][y] = JobManager.PATH_WALK;
				}
			}
		return matrix;
	}

	public void updateMetrics(int x, int y, int metric, int value) {
		if (tileInRange(x, y)) {
			if (MapGridConstants.METRIC_CRIME <= metric
					&& metric < MapGridConstants.METRIC_LAST)
				tileGrid[x][y].metricsActual[metric] += value;
		}
	}

}
