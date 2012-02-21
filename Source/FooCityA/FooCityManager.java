import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

public class FooCityManager
{
	private MapGrid current_map;
	private int tile_to_place;
	private int turn;
	public void FooCityLog(String msg)
	{
		System.out.println(msg);
	}
	
	public FooCityManager()
	{
		current_map = null;
		tile_to_place = 0;
		turn = 0;
	}
	
	public FooCityManager(MapGrid new_map)
	{
		this();
		SetMapGrid(new_map);
	}

	public Dimension getMapArea()
	{
		if (current_map != null)
			return current_map.getMapArea();
		return null;
	}

	public MapGrid GetMapGrid()
	{
		if (turn > 0)
			return current_map;
		return null;
	}

	public boolean SetMapGrid(MapGrid new_map)
	{
		if (new_map != null)
		{
			if (current_map == null)
			{
				current_map = new_map;
				return true;
			}
		}
		return false;
	}

	public boolean NewGame(String map_name)
	{
		if (current_map == null)
		{
			current_map = new MapGrid();
			if (current_map.FromFile(map_name))
				return true;
			current_map = null;
		}
		return false;
	}

	public void Quit()
	{
		current_map = null;
		turn = 0;
	}

	public void startGame()
	{
		advanceTurn();		
	}

	public int getCurrentTurn() {
		return turn;
	}

	public void advanceTurn()
	{
		if (current_map != null)
			this.turn++;
	}

	public boolean setPlacingTile(int i)
	{
		if (i >= MapGridConstants.WATER_TILE && i < MapGridConstants.LAST_TILE)
		{
			this.tile_to_place = i;
			return true;
		}
		tile_to_place = 0;
		return false;
	}
	
	public int getPlacingTile()
	{
		return tile_to_place;
	}

	public boolean placeTile(int x, int y)
	{
		if (current_map != null && tile_to_place > 0)
		{
			// CheckFundsAvailable
			// propagateMetrics()
			return current_map.setTile(x, y, tile_to_place);
			
		}
		return false;
	}
	
	public void propagateMetrics(){
		if (current_map == null)
			return;
		// First, clear all the metrics
		Dimension map_area = current_map.getMapArea();
		if (map_area == null)
			return;
		int map_width = (int) map_area.getHeight();
		int map_height = (int) map_area.getWidth();
		for (int y = 0; y < map_height; y++){
			for (int x = 0; x < map_width; x++){
				current_map.getTile(x, y).crimeActual = 0;
				current_map.getTile(x, y).happinessActual = 0;
				current_map.getTile(x, y).pollutionActual = 0;
			}
			
		}
		
		// Propagate crime
		for (int y = 0; y < map_height; y++){
			for (int x = 0; x < map_width; x++){
				int c = current_map.getTile(x, y).crimeContributed;
				// If we're ADDING crime...
				if (c > 0){
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).crimeActual += (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).crimeActual += (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).crimeActual += (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).crimeActual += c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).crimeActual += (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).crimeActual += (c + xx - yy);
							}
						}
					}
				} else if (c < 0){  // But if we're SUBTRACTING crime...
					//Treat it like we're adding it, but we'll be subtracting
					c = -c;
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).crimeActual -= (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).crimeActual -= (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).crimeActual -= (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).crimeActual -= c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).crimeActual -= (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).crimeActual -= (c + xx - yy);
							}
						}
					}
				}
			}
		}
		
		// Propagate pollution
		for (int y = 0; y < map_height; y++){
			for (int x = 0; x < map_width; x++){
				int c = current_map.getTile(x, y).pollutionContributed;
				// If we're ADDING crime...
				if (c > 0){
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).pollutionActual += (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).pollutionActual += (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).pollutionActual += (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).pollutionActual += c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).pollutionActual += (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).pollutionActual += (c + xx - yy);
							}
						}
					}
				} else if (c < 0){  // But if we're SUBTRACTING crime...
					//Treat it like we're adding it, but we'll be subtracting
					c = -c;
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).pollutionActual -= (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).pollutionActual -= (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).pollutionActual -= (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).pollutionActual -= c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).pollutionActual -= (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).pollutionActual -= (c + xx - yy);
							}
						}
					}
				}
			}
		}
		
		// Propagate happiness
		for (int y = 0; y < map_height; y++){
			for (int x = 0; x < map_width; x++){
				int c = current_map.getTile(x, y).happinessContributed;
				// If we're ADDING crime...
				if (c > 0){
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).happinessActual += (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).happinessActual += (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).happinessActual += (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).happinessActual += c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).happinessActual += (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).happinessActual += (c + xx - yy);
							}
						}
					}
				} else if (c < 0){  // But if we're SUBTRACTING crime...
					//Treat it like we're adding it, but we'll be subtracting
					c = -c;
					// Branch to the right
					for (int xx = 0; xx < c; xx++){
						// Make sure we haven't gone past the right edge
						if (x + xx < map_width) {
							// Set the current value
							current_map.getTile(x + xx, y).happinessActual -= (c - xx);
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c - xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(xx + x, y - yy).happinessActual -= (c - xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(xx + x, yy + y).happinessActual -= (c - xx - yy);
							}
						}
					}
					// Branch to the left
					// We have to start 1 unit to the left to avoid
					// double-adding the center column
					for (int xx = -1; xx > -c; xx--){
						// Make sure we haven't gone past the left edge
						if (x + xx > 0) {
							// Set the current value
							current_map.getTile(x + xx, y).happinessActual -= c + xx;
							// Branch up/down, start 1 unit past the horizontal branch
							for (int yy = 1; yy < c + xx; yy++){
								// Don't go past the top of the map
								if (y - yy >= 0)
									current_map.getTile(x + xx, y - yy).happinessActual -= (c + xx - yy);
								// Don't go below the map
								if (y + yy < map_height)
									current_map.getTile(x + xx, yy + y).happinessActual -= (c + xx - yy);
							}
						}
					}
				}
			}
		}
	}

	public boolean SaveGame(String savePath)
	{
		if ((current_map != null) &&
			(turn > 0) &&			
		 (savePath != null))
		{
			File save_file = new File(savePath);
			try
			{
				BufferedWriter bw = new BufferedWriter(new FileWriter(save_file));

				if (bw != null)
				{
					bw.write("FOOCITYMAGIC\n");
					bw.write("CurrentTurn:" + turn + "\n");
					bw.write("AvailableFunds:" + 100 + "\n");
					bw.write("MapGrid:1\n");
					bw.write("width:" + (int)current_map.getMapArea().getWidth() + "\n");
					bw.write("height:" + (int)current_map.getMapArea().getHeight() + "\n");
					bw.write(current_map.toString() + "\n");
					bw.close();
					return true;
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean LoadGame(String savePath) 
	{
		if (savePath != null)
		{
			File save_file = new File(savePath);
			Scanner sc;
			try {
				sc = new Scanner(save_file);
			} catch (FileNotFoundException e) {
				return false;
			}
			if (sc.hasNextLine())
			{
				String line = sc.nextLine();
				if (0==line.compareTo("FOOCITYMAGIC\n"))
				{
					FooCityLog("Invalid Magic \"" + line +"\"\n");
					return false;
				}
				turn = 0;
				while (sc.hasNextLine())
				{
					String line2 = sc.nextLine();
					if (line2.length() == 0)
						break;
					
					String[] parsedline = line2.split(":");
					if (parsedline.length != 2)
					{
						FooCityLog("parsedline length != 2 \"" + parsedline.length +"\"\n");
						return false;
					}
					
					FooCityLog(parsedline[0]);
							
					if (parsedline[0].equals("MapGrid"))
					{
						try
						{
							switch (Integer.parseInt(parsedline[1]))
							{
							case 1:

								int width = 0;
								int height = 0;
								if (sc.hasNextLine())
								{
									String[] widthLine = sc.nextLine().split(":");
									if (widthLine.length == 2)
										width = Integer.parseInt(widthLine[1]);
								}
								if (sc.hasNextLine())
								{
									String[] heightLine = sc.nextLine().split(":");
									if (heightLine.length == 2)
										height = Integer.parseInt(heightLine[1]);
								}
								if (width < 5 || height < 5)
								{
									FooCityLog("width = " + width + " height = " + height);
									return false;
								}
								current_map = new MapGrid(width, height);
								current_map.fromScanner(sc);
									break;
							default:
							}
						
							
						}
						catch (NumberFormatException e)
						{
							System.err.println("MapGrid");
							e.printStackTrace();
							return false;
						}
					}
					else if (parsedline[0].equals("CurrentTurn"))
					{
						try
						{
							turn = Integer.parseInt(parsedline[1]);
						}
						catch (NumberFormatException e)
						{
							System.err.println("CurrentTurn");
							e.printStackTrace();
							return false;
						}
					}
					else if (parsedline[0].equals("AvailableFunds"))
					{
						try
						{
							Integer.parseInt(parsedline[1]);
						}
						catch (NumberFormatException e)
						{
							System.err.println("AvailableFunds");
							e.printStackTrace();
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public int getAvailableFunds()
	{
		return 0;
	}
}
