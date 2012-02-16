import java.awt.Dimension;

// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

public class FooCityManager
{
	private MapGrid current_map;
	private int tile_to_place;
	private int turn;
	public static void main(String[] args)
	{
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
			return current_map.FromFile(map_name);
		}
		return false;
	}

	public void Quit()
	{
		current_map = null;
	}

	public void startGame()
	{
		advanceTurn();		
	}

	public int getCurrentTurn() {
		return turn;
	}

	public void advanceTurn() {
		this.turn++;
	}

	public boolean setPlacingTile(int i)
	{
		if (i >= MapGridConstants.WATER_TILE || i < MapGridConstants.LAST_TILE)
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
}
