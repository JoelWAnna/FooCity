package foocityBackend;
import java.awt.Dimension;
import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;


/**
 * FooCityManagerTest
 * Unit Tests for FooCityManager Class
 * 
 * 
 * @author Joel Anna <annajoel@cecs.pdx.edu>
 * 2012 February 20
 * 
 * requires ./terrain/001.txt;
 *
 */
public class FooCityManagerTest extends TestCase{
	private String testMap  = "./terrain/001.txt";
	private String testSave = "./saves/SampleSave.fcs";
	private FooCityManager city_manager;
	
	/**
	 * setUp
	 * Initializes city_manager to a new FooCityManager for each test
	 */
	@Override
	protected void setUp() { 
		 city_manager = new FooCityManager();
    }
	
	/**
	 * tearDown
	 * sets city_manager to null after each test
	 */
	@Override
	protected void tearDown() { 
		 city_manager = null; 
    }
	
	/**
	 * testgetMapArea
	 * verifies that getMapArea returns null if no mapGrid is loaded
	 * verifies that getMapArea returns Dimensions equal to mapGrid when one is loaded
	 */
	@Test
	public void testgetMapArea()
	{
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertNull(city_manager.getMapArea());
		Dimension testDimension = new Dimension(5,5);
		MapGrid tempMap = new MapGrid(testDimension);
		Assert.assertTrue(tempMap.FromString("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertTrue(city_manager.SetMapGrid(tempMap));
		Assert.assertEquals(testDimension, city_manager.getMapArea());
		city_manager.Quit();
		Assert.assertEquals("Terrain File Should Exist", true, new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame(testMap));
		Assert.assertEquals(new Dimension(MapGridConstants.MAP_WIDTH, MapGridConstants.MAP_HEIGHT), city_manager.getMapArea());
	}
	
	/**
	 * testGetMapGrid
	 * verifies that GetMapGrid returns null when no map is loaded
	 * verifies that GetMapGrid returns a valid MapGrid when a map is loaded
	 */
	@Test
	public void testGetMapGrid()
	{
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertEquals("Terrain File Should Exist", true, new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame(testMap));
		Assert.assertTrue(city_manager.MapGridLoaded());
	}
	
	/**
	 * testNewGame
	 * verifies that NewGame returns false with input of null or an invalid filename
	 * verifies that NewGame returns true with input of a valid map file
	 * verifies that FundsAvailable = FooCityManager.STARTING_FUNDS
	 */
	@Test
	public void testNewGame()
	{
		Assert.assertEquals("Should Not load a null map", false, city_manager.NewGame(null));
		Assert.assertEquals("Terrain File Should Exist", true, new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame(testMap));
		Assert.assertEquals("FundsAvailable Should = "+ FooCityManager.STARTING_FUNDS, FooCityManager.STARTING_FUNDS, city_manager.getAvailableFunds());
		 
	}

	/**
	 * testQuit
	 * loads a terrain file, advances turn
	 * verifies that Quit will clear the MapGrid, CurrentTurn, and FundsAvailable
	 */
	@Test
	public void testQuit()
	{
		Assert.assertEquals("Terrain File Should Exist", true, new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame(testMap));
		city_manager.advanceTurn();
		Assert.assertTrue("GetMapGrid Should != null", city_manager.MapGridLoaded());
		Assert.assertEquals("Current Turn Should > 0", true, city_manager.getCurrentTurn() >0);
		city_manager.Quit();
		Assert.assertFalse("Map Should not Be Loaded", city_manager.MapGridLoaded());

		Assert.assertEquals("Current Turn Should = 0", 0, city_manager.getCurrentTurn());
		Assert.assertEquals("FundsAvailable Should = 0", 0, city_manager.getAvailableFunds());
	}

	/**
	 * testAdvanceTurn
	 * TODO fill out this test
	 * verifies that initial turn is 0, and that after advancing the turn it is 1
	 */
	@Test
	public void testAdvanceTurn()
	{
		Assert.assertEquals("Current Turn Should = 0", 0, city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should still = 0", 0, city_manager.getCurrentTurn());
		Assert.assertEquals("Terrain File Should Exist", true, new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true, city_manager.NewGame(testMap));
		Assert.assertEquals("Current Turn should still = 0", 0, city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should = 1", 1, city_manager.getCurrentTurn());
		
	}
	
	/**
	 * testSaveGame
	 * verifies that SaveGame returns false if no game has been loaded
	 * verifies that SaveGame returns true if a game has been loaded
	 * verifies that the file is created
	 * verifies that SaveGame returns false for a null or invalid filename
	 */
	@Test
	public void testSaveGame()
	{
		Assert.assertFalse("Should Not Save an empty game", city_manager.SaveGame(testSave));
		Assert.assertTrue("Terrain File Should Exist", new File(testMap).exists());
		Assert.assertTrue("Map should be loaded", city_manager.NewGame(testMap));
		Assert.assertTrue(city_manager.SaveGame(testSave));
		Assert.assertTrue("Save File Should Exist", new File(testSave).exists());
		Assert.assertFalse("Should Not Save to a null filename", city_manager.SaveGame(null));
		Assert.assertFalse("Should Not Save to an invalid filename", city_manager.SaveGame("//////"));
		
	}

	/**
	 * testLoadGame
	 * verifies that LoadGame fails for a null or invalid filename
	 * verifies that the sample savefile exists
	 * verifies that no map is loaded
	 * verifies that LoadGame succeeds for an existing save file
	 * verifies that currentTurn is >=0 after loading
	 * verifies that mapGrid is not null after loading
	 * 
	 * TODO: create invalid save files to test with
	 */
	@Test
	public void testLoadGame()
	{
		Assert.assertFalse("LoadGame(null) should return false", city_manager.LoadGame(null));
		Assert.assertFalse("LoadGame(\"invalid filename\") should return false", city_manager.LoadGame("//////"));
		Assert.assertTrue("Sample Save File should exist", new File(testSave).exists());
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertTrue("LoadGame(\"existing filename\") should return true", city_manager.LoadGame(testSave));
		Assert.assertFalse("Current turn should not be less than 0 after a successfull load", city_manager.getCurrentTurn() < 0);
		Assert.assertFalse("Available funds should not be less than 0 after a successfull load", city_manager.getAvailableFunds() < 0);
		Assert.assertTrue(city_manager.MapGridLoaded());
		
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
		Assert.assertTrue(city_manager.MapGridLoaded());
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x)
			{
				Assert.assertEquals(MapGridConstants.DIRT_TILE, city_manager.getTileInt(x, y));
			}
		Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[6], city_manager.setPlacingTile(6));
		Assert.assertTrue("Place Tile 0,0", city_manager.placeTile(0, 0));
		Assert.assertEquals(MapGridConstants.INDUSTRIAL_TILE, city_manager.getTileInt(0, 0));
		
	}
	public void testWaterGenerateHappinessMetrics()
	{
		int size = 5;
		MapGrid sample_map = new MapGrid(size, size);
		Assert.assertTrue("FromString", sample_map.FromString("WWWWW\nWWWWW\nWWWWW\nWWWWW\nWWWWW\n"));
		Assert.assertTrue(city_manager.SetMapGrid(sample_map));
		Assert.assertTrue(city_manager.MapGridLoaded());
		city_manager.propagateMetrics();
		
		int [][] expectedHappiness =
			{
				{150, 165, 170, 165, 150},
				{165, 180, 185, 180, 165},
				{170, 185, 190, 185, 170},
				{165, 180, 185, 180, 165},
				{150, 165, 170, 165, 150}
			};

		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
			{
				int actual_happiness = city_manager.getTile(x, y).metricsActual[Tile.METRIC_HAPPINESS];
				String error = "\nTile(x,y) (" + x + "," + y + ")";//\nExpected: " + expectedHappiness[x][y] + " Found : " + actual_happiness;
				Assert.assertEquals(error, expectedHappiness[x][y] , actual_happiness);
			}
	}
	
	public void testDirtGenerateHappinessMetrics()
	{
		int size = 10;
		MapGrid sample_map = new MapGrid(size, size);
		Assert.assertTrue("FromString", sample_map.FromString("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager.SetMapGrid(sample_map));
		Assert.assertNotNull(city_manager.MapGridLoaded());
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
				
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
			{
				int actual_happiness = city_manager.getTile(x, y).metricsActual[Tile.METRIC_HAPPINESS];
				String error = "\nTile(x,y) (" + x + "," + y + ")";//\nExpected: " + expectedHappiness[x][y] + " Found : " + actual_happiness;
				Assert.assertEquals(error, expectedHappiness[x][y] , actual_happiness);
			}
	}
	
	public void testJobFinder()
	{
		int size = 5;
		MapGrid sample_map = new MapGrid(size, size);
		Assert.assertTrue("FromString", sample_map.FromString("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertTrue(city_manager.SetMapGrid(sample_map));
		Assert.assertTrue(city_manager.setPlacingTile(MapGridConstants.RESIDENTIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(0, 0));
		Assert.assertTrue(city_manager.setPlacingTile(MapGridConstants.COMMERCIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(0,1));
		Assert.assertTrue(city_manager.placeTile(1,0));
		Assert.assertTrue(city_manager.placeTile(1,1));
		//Assert.assertTrue(city_manager.placeTile(2,0));
		city_manager.advanceTurn();
		Assert.assertEquals(100, city_manager.getJobs());
	}
}
