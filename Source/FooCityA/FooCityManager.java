// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

public class FooCityManager
{
	private MapGrid current_map;
	private int tile_to_place;
	public static void main(String[] args)
	{
	}
	
	public FooCityManager()
	{
		current_map = null;
		tile_to_place = 0;
	}
	
	public FooCityManager(MapGrid new_map)
	{
		current_map = new_map;
	}

	public MapGrid GetMapGrid()
	{
		return current_map;
	}

	public boolean SetMapGrid(MapGrid new_map)
	{
		if (current_map == null)
		{
			current_map = new_map;
			return true;
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
	
	public int GetPlacingTile()
	{
		return tile_to_place;
	}

	public boolean placeTile(int x, int y)
	{
		if (current_map != null && tile_to_place > 0)
		{
			// CheckFundsAvailable
			return current_map.setTile(x, y, tile_to_place);
			
		}
		return false;
	}
}
