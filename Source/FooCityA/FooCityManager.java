// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

public class FooCityManager
{
	private MapGrid current_map;
	
	public static void main(String[] args)
	{
	}
	
	public FooCityManager()
	{
		current_map = null;
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
}
