package foocityBackend;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

public class FooCityManager
{
	public static final int STARTING_FUNDS = 10000;
	private int availableFunds;
	private MapGrid current_map;
	private int tile_to_place;
	private int turn;
	public int powerConsumed = 0, powerGenerated = 0;
	public int waterConsumed = 0, waterGenerated = 0;
	public int income = 0, expenses = 0;
	public int cashFlow = 0;
	public int jobs = 0, residents = 0;

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

	public int getTileInt(int x, int y)
	{
		if (current_map != null)
			return current_map.getTileInt(x, y);
		return 0;
	}

	public Tile getTile(int x, int y)
	{
		if (current_map != null)
			return current_map.getTile(x, y);
		return null;
	}

	public boolean MapGridLoaded()
	{
		return current_map != null;
	}

	public boolean SetMapGrid(MapGrid new_map)
	{
		if (new_map != null)
		{
			if (current_map == null)
			{
				current_map = new_map;
				startGame();
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
			{
				startGame();
				return true;
			}
			current_map = null;
		}
		return false;
	}

	public void Quit()
	{
		current_map = null;
		turn = 0;
		availableFunds = 0;
	}

	private void startGame()
	{
		availableFunds = FooCityManager.STARTING_FUNDS;
		propagateMetrics();
	}

	public int getCurrentTurn() {
		return turn;
	}

	public void advanceTurn()
	{
		long timeBegun = System.nanoTime();
		if (current_map == null)
			return;
		//Advance the turn
		this.turn++;
		//Propagate the metrics
		this.propagateMetrics();

		// Reset some variables
		powerConsumed = 0; powerGenerated = 0;
		waterConsumed = 0; waterGenerated = 0;
		income = 0; expenses = 0;
		jobs = 0; residents = 0;
		float factor;
		
		//For each tile...
		for (int y = 0; y < current_map.getMapArea().getHeight(); y++){
			for (int x = 0; x < current_map.getMapArea().getWidth(); x++){
				// Get the tile
				Tile tile = current_map.getTile(x, y);
				
				//Add to power consumed if its consuming, otherwise "add" it to generated
				//(Remember that if its generating, the consumed amount is negative
				//So we SUBTRACT it to ADD it)
				if (tile.powerConsumed > 0)
					powerConsumed += tile.powerConsumed;
				else
					powerGenerated -= tile.powerConsumed;
				
				//Do the same with water
				if (tile.waterConsumed > 0)
					waterConsumed += tile.waterConsumed;
				else
					waterGenerated -= tile.waterConsumed;
				
				//Do a similar thing with residents versus jobs
				if (tile.jobs > 0)
					jobs += tile.jobs;
				else
					residents -= tile.jobs;
				
				//If the residents are employed (Or the job location has a resident nearby)
				//Then add it to the budget
				if (tile.employed){
					if (tile.monthlyCost > 0)
						expenses += tile.monthlyCost;
					else
						income -= tile.monthlyCost;
				}
				
				
				
			}
		}
		//Now, we have to reduce the budget based on a lack of jobs, residents, water, or power
		//The formula in each case is basically:
		// budget = budget * factor
		// Where factor = Provided / Needed (capped to a value of 1)
		// (Just make sure we're not going to divide by zero!)
		
		if (waterConsumed > 0){
			factor = (float) waterGenerated / waterConsumed;
			if (factor < 1)
				income *= factor;
		}
		
		if (powerConsumed > 0) {
			factor = (float) powerGenerated / powerConsumed;
			if (factor < 1)
				income *= factor;
		}
		
		if (residents > 0) {
			factor = (float) jobs / residents;
			if (factor < 1)
				income *= factor;
		}
		
		if (jobs > 0) {
			factor = (float) residents / jobs;
			if (factor < 1)
				income *= factor;
		}
		
		
		// We're done with the budget!  Make the change.
		cashFlow = (int) (income - expenses);
		this.availableFunds += cashFlow;
		this.findJobs();
		System.out.print("advanceTurn took " + Long.toString((System.nanoTime() - timeBegun)/ 1000000) + " ms\n");
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
			Tile tempTile = new Tile(tile_to_place);
			if (tempTile.price <= this.availableFunds)
			{
				if (current_map.setTile(x, y, tile_to_place))
				{
					this.availableFunds -= tempTile.price;
					// Set the graphic variation for this and the neighboring tiles
					for (int xv = x - 1; xv < x + 2; xv++){
						for (int yv = y - 1; yv < y + 2; yv++){
							setVariation(xv, yv);
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	public void setVariation(int x, int y){
		// First, make sure the coordinates are valid
		if (x < 0 || x >= MapGridConstants.MAP_WIDTH || y < 0 || y >= MapGridConstants.MAP_HEIGHT)
			return;
		Tile tile = getTile(x,y);
		int variation = 0;
		switch(tile.getTileInt()){
		case MapGridConstants.ROAD_TILE:
			if (y > 0)
				if (getTile(x, y - 1).getTileInt() == MapGridConstants.ROAD_TILE)
					variation += 1;
			if (x < MapGridConstants.MAP_WIDTH - 2)
				if (getTile(x + 1, y).getTileInt() == MapGridConstants.ROAD_TILE)
					variation += 2;
			if (y < MapGridConstants.MAP_HEIGHT - 2)
				if (getTile(x, y + 1).getTileInt() == MapGridConstants.ROAD_TILE)
					variation += 4;
			if (x > 0)
				if (getTile(x - 1, y).getTileInt() == MapGridConstants.ROAD_TILE)
					variation += 8;
			break;
		default:
			variation = 0;
				
		}
		
		tile.variation = variation;
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
				current_map.getTile(x, y).resetMetrics();
			}
			
		}
		
		// For each metric...
		for (int metric = 0; metric < 3; metric++){
			for (int y = 0; y < map_height; y++){
				for (int x = 0; x < map_width; x++){
					int c = current_map.getTile(x, y).metricsContributed[metric];
					// If we're ADDING crime...
					if (c > 0){
						// Branch to the right
						for (int xx = 0; xx < c; xx++){
							// Make sure we haven't gone past the right edge
							if (x + xx < map_width) {
								// Set the current value
								current_map.getTile(x + xx, y).metricsActual[metric] += (c - xx);
								// Branch up/down, start 1 unit past the horizontal branch
								for (int yy = 1; yy < c - xx; yy++){
									// Don't go past the top of the map
									if (y - yy >= 0)
										current_map.getTile(xx + x, y - yy).metricsActual[metric] += (c - xx - yy);
									// Don't go below the map
									if (y + yy < map_height)
										current_map.getTile(xx + x, yy + y).metricsActual[metric] += (c - xx - yy);
								}
							}
						}
						// Branch to the left
						// We have to start 1 unit to the left to avoid
						// double-adding the center column
						for (int xx = -1; xx > -c; xx--){
							// Make sure we haven't gone past the left edge
							if (x + xx > -1) {
								// Set the current value
								current_map.getTile(x + xx, y).metricsActual[metric] += c + xx;
								// Branch up/down, start 1 unit past the horizontal branch
								for (int yy = 1; yy < c + xx; yy++){
									// Don't go past the top of the map
									if (y - yy >= 0)
										current_map.getTile(x + xx, y - yy).metricsActual[metric] += (c + xx - yy);
									// Don't go below the map
									if (y + yy < map_height)
										current_map.getTile(x + xx, yy + y).metricsActual[metric] += (c + xx - yy);
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
								current_map.getTile(x + xx, y).metricsActual[metric] -= (c - xx);
								// Branch up/down, start 1 unit past the horizontal branch
								for (int yy = 1; yy < c - xx; yy++){
									// Don't go past the top of the map
									if (y - yy >= 0)
										current_map.getTile(xx + x, y - yy).metricsActual[metric] -= (c - xx - yy);
									// Don't go below the map
									if (y + yy < map_height)
										current_map.getTile(xx + x, yy + y).metricsActual[metric] -= (c - xx - yy);
								}
							}
						}
						// Branch to the left
						// We have to start 1 unit to the left to avoid
						// double-adding the center column
						for (int xx = -1; xx > -c; xx--){
							// Make sure we haven't gone past the left edge
							if (x + xx > -1) {
								// Set the current value
								current_map.getTile(x + xx, y).metricsActual[metric] -= c + xx;
								// Branch up/down, start 1 unit past the horizontal branch
								for (int yy = 1; yy < c + xx; yy++){
									// Don't go past the top of the map
									if (y - yy >= 0)
										current_map.getTile(x + xx, y - yy).metricsActual[metric] -= (c + xx - yy);
									// Don't go below the map
									if (y + yy < map_height)
										current_map.getTile(x + xx, yy + y).metricsActual[metric] -= (c + xx - yy);
								}
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
		 (savePath != null))
		{
			try
			{
				File save_file = new File(savePath);
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
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		boolean valid_save = true;
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
				while (sc.hasNextLine() && valid_save)
				{
					String line2 = sc.nextLine();
					if (line2.length() == 0)
						break;
					
					String[] parsedline = line2.split(":");
					if (parsedline.length != 2)
					{
						FooCityLog("parsedline length != 2 \"" + parsedline.length +"\"\n");
						valid_save = false;
						break;
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
									valid_save = false;
									break;
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
							valid_save = false;
						}
					}
					else if (parsedline[0].equals("CurrentTurn"))
					{
						try
						{
							turn = Integer.parseInt(parsedline[1]);
							if (turn < 0)
								valid_save = false;
						}
						catch (NumberFormatException e)
						{
							System.err.println("CurrentTurn");
							e.printStackTrace();
							valid_save = false;
						}
					}
					else if (parsedline[0].equals("AvailableFunds"))
					{
						try
						{
							availableFunds = Integer.parseInt(parsedline[1]);
							if (availableFunds < 0)
								valid_save = false;
						}
						catch (NumberFormatException e)
						{
							System.err.println("AvailableFunds");
							e.printStackTrace();
							valid_save = false;
						}
					}
				}
				
				if (!valid_save)
				{
					Quit();
				}
				return valid_save;
			}
		}
		return false;
	}

	public int getAvailableFunds()
	{
		return availableFunds;
	}

	public void findJobs()
	{
		int[][] residential_matrix = this.current_map.getResidentialMatrix();
		int[][] job_matrix = current_map.getJobMatrix();
		@SuppressWarnings("unused")
		int[][] road_matrix = current_map.getRoadMatrix();
		int jobsFound = 0;
		Stack<Dimension> found = new Stack<Dimension>();
		for (int y = 0; y < current_map.getMapArea().height; ++y)
			for (int x = 0; x < current_map.getMapArea().width; ++x)
			{
				if (residential_matrix[x][y] > 0)
				{
					for (int y2 = y-2; y2 < y+3; ++y2)
						for (int x2 = x-2; x2 < x+3; ++x2)
						{
							//FooCityLog(x + " " + y + " " + Integer.toString(job_matrix[x][y]));
							if (y2 < 0 || y2 >= current_map.getMapArea().height)
								continue;
							if (x2 < 0 || x2 >= current_map.getMapArea().width)
								continue;

							if (job_matrix[x2][y2] > 0)
								found.push(new Dimension(x2,y2));
						}
					if (found.size() > 0)
					{
						while (found.size() > 0)
						{
							int number = residential_matrix[x][y] / found.size();
							if (residential_matrix[x][y] % found.size() > 0)
								number++;
						
							Dimension location = found.pop();
							FooCityLog("Trying to find " + number);
							FooCityLog("residents " + residential_matrix[x][y]);
							FooCityLog("jobs " + job_matrix[location.width][location.height]);
							number = residential_matrix[x][y] > number ? number : residential_matrix[x][y];
							number = job_matrix[location.width][location.height] > number ? number : job_matrix[location.width][location.height];
							
							FooCityLog("Found " + number);
							
							job_matrix[location.width][location.height] -= number;
							residential_matrix[x][y] -= number;
							jobsFound += number;
						}
					}
				}
			}
		this.jobs = jobsFound;
		FooCityLog("Final Found " + jobsFound);
		
	}

	public int getJobs() {
		// TODO Auto-generated method stub
		return jobs;
	}
}
