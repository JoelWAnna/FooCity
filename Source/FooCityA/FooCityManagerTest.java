import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;


public class FooCityManagerTest extends TestCase{
	private FooCityManager city_manager;
	 protected void setUp() { 
		 city_manager = new FooCityManager();
    } 
	 protected void tearDown() { 
		 city_manager = null; 
    }
	@Test
	public void testAdvanceTurn()
	{
		Assert.assertEquals("Current Turn Should = 0", 0, city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should still = 0", 0, city_manager.getCurrentTurn());
		Assert.assertEquals("Terrain File Should Exist", true, new File("./terrain/001.txt").exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame("./terrain/001.txt"));
		Assert.assertEquals("Current Turn should still = 0", 0, city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should = 1", 1, city_manager.getCurrentTurn());
		
	}
	@Test
	public void testNewGame()
	{
		Assert.assertEquals("Should Not load a null map", false, city_manager.NewGame(null));
		Assert.assertEquals("Terrain File Should Exist", true, new File("./terrain/001.txt").exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame("./terrain/001.txt"));
	}

	@Test
	public void testQuit()
	{
		Assert.assertEquals("Terrain File Should Exist", true, new File("./terrain/001.txt").exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame("./terrain/001.txt"));
		city_manager.advanceTurn();
		Assert.assertNotNull("GetMapGrid Should != null", city_manager.GetMapGrid());
		Assert.assertEquals("Current Turn Should > 0", true, city_manager.getCurrentTurn() >0);
		city_manager.Quit();
		Assert.assertNull("Map Should not Be Loaded", city_manager.GetMapGrid());

		Assert.assertEquals("Current Turn Should = 0", 0, city_manager.getCurrentTurn());
		Assert.assertEquals("FundsAvailable Should = 0", 0, city_manager.getAvailableFunds());
	}

	@Test
	public void testSaveGame()
	{
		Assert.assertEquals("Should Not Save an empty game", false, city_manager.SaveGame("./saves/SampleSave.fcs"));
		Assert.assertEquals("Terrain File Should Exist", true, new File("./terrain/001.txt").exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame("./terrain/001.txt"));
		city_manager.startGame();
		Assert.assertEquals(true, city_manager.SaveGame("./saves/SampleSave.fcs"));
	}

	@Test
	public void testLoadGame()
	{
		Assert.assertEquals("LoadGame(null) should return false", false, city_manager.LoadGame(null));
		Assert.assertEquals("LoadGame(\"invalid filename\") should return false", false, city_manager.LoadGame("//////"));
		Assert.assertEquals("Sample Save File should exist", true, new File("./saves/SampleSave.fcs").exists());
		
		Assert.assertEquals("LoadGame(\"existing filename\") should return true", true, city_manager.LoadGame("./saves/SampleSave.fcs"));
		Assert.assertEquals("Current turn should be greater than 0 after a successfull load", true, city_manager.getCurrentTurn() > 0);
	}
	
	@Test
	public void testSetPlacingTileGetPlacingTile()
	{
		for (int i = MapGridConstants.WATER_TILE; i < MapGridConstants.LAST_TILE; ++i)
		{
			Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[i], city_manager.setPlacingTile(i));
			Assert.assertEquals(i, city_manager.getPlacingTile());
		}
		Assert.assertFalse("Place Tile 0" , city_manager.setPlacingTile(0));
		Assert.assertEquals(0, city_manager.getPlacingTile());

		Assert.assertFalse("Place Tile " + Integer.MAX_VALUE , city_manager.setPlacingTile(Integer.MAX_VALUE));
		Assert.assertEquals(0, city_manager.getPlacingTile());
		Assert.assertFalse("Place Tile " + Integer.MIN_VALUE , city_manager.setPlacingTile(Integer.MIN_VALUE));
		Assert.assertEquals(0, city_manager.getPlacingTile());	
	}
	
	
	@Test
	public void testPlaceTile()
	{
		MapGrid sample_map = new MapGrid(10, 10);
		Assert.assertTrue("FromString", sample_map.FromString("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager.SetMapGrid(sample_map));
		city_manager.startGame();
		Assert.assertNotNull(city_manager.GetMapGrid());
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x)
			{
				Assert.assertEquals(MapGridConstants.DIRT_TILE, city_manager.GetMapGrid().getTileAt(x, y));
			}
		Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[6], city_manager.setPlacingTile(6));
		Assert.assertTrue("Place Tile 0,0", city_manager.placeTile(0, 0));
		Assert.assertEquals(MapGridConstants.INDUSTRIAL_TILE, city_manager.GetMapGrid().getTileAt(0, 0));
		
	}
	public void testGenerateHappinessMetrics()
	{
		MapGrid sample_map = new MapGrid(10, 10);
		Assert.assertTrue("FromString", sample_map.FromString("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager.SetMapGrid(sample_map));
		city_manager.startGame();
		Assert.assertNotNull(city_manager.GetMapGrid());
		city_manager.propagateMetrics();
		
		int [][] expectedHappiness =
				{{ 4, 5, 5, 5, 5, 5, 5, 5, 5, 4},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{5, 6, 6, 6, 6, 6, 6, 6, 6, 5},
				{ 4, 5, 5, 5, 5, 5, 5, 5, 5, 4}};
				
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x)
			{
				int actual_happiness = city_manager.GetMapGrid().getTile(x, y).happinessActual;
				String error = "\nTile(x,y) (" + x + "," + y + ")";//\nExpected: " + expectedHappiness[x][y] + " Found : " + actual_happiness;
				Assert.assertEquals(error, expectedHappiness[x][y] , actual_happiness);
			}
	}
}
