package foocityBackend;

import java.awt.Dimension;
import java.io.File;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import terraingenerator.Generate;

/**
 * FooCityManagerTest Unit Tests for FooCityManager Class
 * 
 * 
 * @author Joel Anna <annajoel@cecs.pdx.edu> 2012 February 20
 * 
 *         requires ./terrain/001.txt;
 * 
 */
public class FooCityManagerTest extends TestCase {
	private String testMap = "./terrain/001.txt";
	private String testSave = "./saves/SampleSave.fcs";
	private FooCityManager city_manager;

	/**
	 * setUp Initializes city_manager to a new FooCityManager for each test
	 */
	@Override
	protected void setUp() {
		city_manager = new FooCityManager();
	}

	/**
	 * tearDown sets city_manager to null after each test
	 */
	@Override
	protected void tearDown() {
		city_manager = null;
	}

	/**
	 * testgetMapArea verifies that getMapArea returns null if no mapGrid is
	 * loaded verifies that getMapArea returns Dimensions equal to mapGrid when
	 * one is loaded
	 */
	@Test
	public void testgetMapArea() {
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertNull(city_manager.getMapArea());
		Dimension testDimension = new Dimension(5, 5);
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertEquals(testDimension, city_manager.getMapArea());
		city_manager.Quit();
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		Assert.assertEquals(new Dimension(MapGridConstants.MAP_WIDTH,
				MapGridConstants.MAP_HEIGHT), city_manager.getMapArea());
		city_manager.Quit();
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		Assert.assertEquals(new Dimension(MapGridConstants.MAP_WIDTH,
				MapGridConstants.MAP_HEIGHT), city_manager.getMapArea());

	}

	/**
	 * testMapGridLoaded verifies that MapGridLoaded returns false when no map
	 * is loaded verifies that GetMapGrid returns true MapGrid when a map is
	 * loaded
	 */
	@Test
	public void testMapGridLoaded() {
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		Assert.assertTrue(city_manager.MapGridLoaded());
	}

	/**
	 * testNewGame verifies that NewGame returns false with input of null or an
	 * invalid filename verifies that NewGame returns true with input of a valid
	 * map file verifies that FundsAvailable = FooCityManager.STARTING_FUNDS
	 */
	@Test
	public void testNewGame() {
		Assert.assertEquals("Should Not load a null map", false,
				city_manager.NewGame(null));
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		Assert.assertEquals("FundsAvailable Should = "
				+ FooCityManager.STARTING_FUNDS, FooCityManager.STARTING_FUNDS,
				city_manager.getAvailableFunds());

	}
	/**
	 * testNewGeneratedGame verifies that NewGame returns false with input of
	 * null or an invalid filename verifies that NewGame returns true with input
	 * of a valid map file verifies that FundsAvailable =
	 * FooCityManager.STARTING_FUNDS
	 */
	@Test
	public void testNewGeneratedGame() {
		Assert.assertFalse("Should Not load a null string",
				city_manager.NewGeneratedGame(null));
		Assert.assertFalse("Should Not load an invalid string",
				city_manager.NewGeneratedGame("invalidstring"));

		Assert.assertTrue("Should load a generated map", city_manager
				.NewGeneratedGame(Generate.generate()));

		Assert.assertTrue("Map should be loaded", city_manager.MapGridLoaded());
		Assert.assertEquals("FundsAvailable Should = "
				+ FooCityManager.STARTING_FUNDS, FooCityManager.STARTING_FUNDS,
				city_manager.getAvailableFunds());
		city_manager.Quit();
		Assert.assertFalse("Map should not be loaded",
				city_manager.MapGridLoaded());
		// Generate.generate();
		String generated = "";
		for (int y = 0; y < 128; ++y) {

			for (int x = 0; x < 128; ++x) {
				generated += 'D';
			}
			generated += '\n';
		}

		Assert.assertTrue("Should load a generated map",
				city_manager.NewGeneratedGame(generated));

		Assert.assertTrue("Map should be loaded", city_manager.MapGridLoaded());
		Assert.assertEquals("FundsAvailable Should = "
				+ FooCityManager.STARTING_FUNDS, FooCityManager.STARTING_FUNDS,
				city_manager.getAvailableFunds());

	}

	/**
	 * testQuit loads a terrain file, advances turn verifies that Quit will
	 * clear the MapGrid, CurrentTurn, and FundsAvailable
	 */
	@Test
	public void testQuit() {
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		city_manager.advanceTurn();
		Assert.assertTrue("GetMapGrid Should != null",
				city_manager.MapGridLoaded());
		Assert.assertEquals("Current Turn Should > 0", true,
				city_manager.getCurrentTurn() > 0);
		city_manager.Quit();
		Assert.assertFalse("Map Should not Be Loaded",
				city_manager.MapGridLoaded());

		Assert.assertEquals("Current Turn Should = 0", 0,
				city_manager.getCurrentTurn());
		Assert.assertEquals("FundsAvailable Should = 0", 0,
				city_manager.getAvailableFunds());
	}

	/**
	 * testAdvanceTurn TODO fill out this test verifies that initial turn is 0,
	 * and that after advancing the turn it is 1
	 */
	@Test
	public void testAdvanceTurn() {
		Assert.assertEquals("Current Turn Should = 0", 0,
				city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should still = 0", 0,
				city_manager.getCurrentTurn());
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		Assert.assertEquals("Map should be loaded", true,
				city_manager.NewGame(testMap));
		Assert.assertEquals("Current Turn should still = 0", 0,
				city_manager.getCurrentTurn());
		city_manager.advanceTurn();
		Assert.assertEquals("Current Turn should = 1", 1,
				city_manager.getCurrentTurn());

	}

	/**
	 * testSaveGame verifies that SaveGame returns false if no game has been
	 * loaded verifies that SaveGame returns true if a game has been loaded
	 * verifies that the file is created verifies that SaveGame returns false
	 * for a null or invalid filename
	 */
	@Test
	public void testSaveGame() {
		Assert.assertFalse("Should Not Save an empty game",
				city_manager.SaveGame(testSave));
		Assert.assertTrue("Terrain File Should Exist",
				new File(testMap).exists());
		Assert.assertTrue("Map should be loaded", city_manager.NewGame(testMap));
		Assert.assertTrue(city_manager.SaveGame(testSave));
		Assert.assertTrue("Save File Should Exist", new File(testSave).exists());
		Assert.assertFalse("Should Not Save to a null filename",
				city_manager.SaveGame(null));
		Assert.assertFalse("Should Not Save to an invalid filename",
				city_manager.SaveGame("//////"));

	}

	/**
	 * testLoadGame verifies that LoadGame fails for a null or invalid filename
	 * verifies that the sample savefile exists verifies that no map is loaded
	 * verifies that LoadGame succeeds for an existing save file verifies that
	 * currentTurn is >=0 after loading verifies that mapGrid is not null after
	 * loading
	 * 
	 * TODO: create invalid save files to test with
	 */
	@Test
	public void testLoadGame() {
		Assert.assertFalse("LoadGame(null) should return false",
				city_manager.LoadGame(null));
		Assert.assertFalse(
				"LoadGame(\"invalid filename\") should return false",
				city_manager.LoadGame("//////"));
		Assert.assertTrue("Sample Save File should exist",
				new File(testSave).exists());
		Assert.assertFalse(city_manager.MapGridLoaded());
		Assert.assertTrue("LoadGame(\"existing filename\") should return true",
				city_manager.LoadGame(testSave));
		Assert.assertFalse(
				"Current turn should not be less than 0 after a successfull load",
				city_manager.getCurrentTurn() < 0);
		Assert.assertFalse(
				"Available funds should not be less than 0 after a successfull load",
				city_manager.getAvailableFunds() < 0);
		Assert.assertTrue(city_manager.MapGridLoaded());

	}

	@Test
	public void testSetPlacingTileGetPlacingTile() {
		for (int i = MapGridConstants.WATER_TILE; i < MapGridConstants.LAST_TILE; ++i) {
			Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[i],
					city_manager.setPlacingTile(i));
			Assert.assertEquals(i, city_manager.getPlacingTile());
		}
		Assert.assertFalse("Place Tile 0", city_manager.setPlacingTile(0));
		Assert.assertEquals(0, city_manager.getPlacingTile());

		Assert.assertFalse("Place Tile " + Integer.MAX_VALUE,
				city_manager.setPlacingTile(Integer.MAX_VALUE));
		Assert.assertEquals(0, city_manager.getPlacingTile());
		Assert.assertFalse("Place Tile " + Integer.MIN_VALUE,
				city_manager.setPlacingTile(Integer.MIN_VALUE));
		Assert.assertEquals(0, city_manager.getPlacingTile());
	}

	@Test
	public void testPlaceTile() {
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager.MapGridLoaded());
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x) {
				Assert.assertEquals(MapGridConstants.DIRT_TILE,
						city_manager.getTileInt(x, y));
			}
		Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[6],
				city_manager.setPlacingTile(6));
		Assert.assertTrue("Place Tile 0,0", city_manager.placeTile(0, 0));
		Assert.assertEquals(MapGridConstants.INDUSTRIAL_TILE,
				city_manager.getTileInt(0, 0));

	}
	public void testWaterGenerateHappinessMetrics() {
		int size = 5;
		Assert.assertTrue(city_manager
				.NewGeneratedGame("WWWWW\nWWWWW\nWWWWW\nWWWWW\nWWWWW\n"));
		Assert.assertTrue(city_manager.MapGridLoaded());
		city_manager.propagateMetrics();

		int[][] expectedHappiness = {{150, 165, 170, 165, 150},
				{165, 180, 185, 180, 165}, {170, 185, 190, 185, 170},
				{165, 180, 185, 180, 165}, {150, 165, 170, 165, 150}};

		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x) {
				int actual_happiness = city_manager.getTileMetrics(x, y,
						MapGridConstants.METRIC_HAPPINESS);
				String error = "\nTile(x,y) (" + x + "," + y + ")";
				Assert.assertEquals(error, expectedHappiness[x][y],
						actual_happiness);
			}
	}

	public void testDirtGenerateHappinessMetrics() {
		int size = 10;
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		Assert.assertNotNull(city_manager.MapGridLoaded());
		city_manager.propagateMetrics();

		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x) {
				int actual_happiness = city_manager.getTileMetrics(x, y,
						MapGridConstants.METRIC_HAPPINESS);
				String error = "\nTile(x,y) (" + x + "," + y + ")";// \nExpected:
																	// " + expectedHappiness[x][y] + "
																	// Found : "
																	// +
																	// actual_happiness;
				Assert.assertEquals(error, 0,
						actual_happiness);
			}
	}

	public void testJobFinder() {
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertTrue(city_manager
				.setPlacingTile(MapGridConstants.RESIDENTIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(0, 0));
		Assert.assertTrue(city_manager.placeTile(2, 0));
		Assert.assertTrue(city_manager
				.setPlacingTile(MapGridConstants.COMMERCIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(0, 1));
		Assert.assertTrue(city_manager.placeTile(1, 0));
		Assert.assertTrue(city_manager.placeTile(1, 1));
		// Assert.assertTrue(city_manager.placeTile(2,0));
		city_manager.advanceTurn();
		Assert.assertEquals(103, city_manager.getJobs());
	}

	public void testJobFinder2() {
		int size = 20;
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager
				.setPlacingTile(MapGridConstants.RESIDENTIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(0, 0));
		Assert.assertTrue(city_manager
				.setPlacingTile(MapGridConstants.ROAD_TILE));
		Assert.assertTrue(city_manager.placeTile(0, 1));
		Assert.assertTrue(city_manager.placeTile(0, 2));
		Assert.assertTrue(city_manager.placeTile(0, 3));
		Assert.assertTrue(city_manager.placeTile(0, 4));
		Assert.assertTrue(city_manager.placeTile(1, 4));
		Assert.assertTrue(city_manager.placeTile(2, 4));
		Assert.assertTrue(city_manager.placeTile(3, 4));
		Assert.assertTrue(city_manager.placeTile(4, 4));
		Assert.assertTrue(city_manager.placeTile(4, 5));
		Assert.assertTrue(city_manager.placeTile(4, 6));
		Assert.assertTrue(city_manager.placeTile(4, 7));
		Assert.assertTrue(city_manager.placeTile(4, 8));
		Assert.assertTrue(city_manager.placeTile(4, 9));
		Assert.assertTrue(city_manager.placeTile(4, 10));
		Assert.assertTrue(city_manager.placeTile(4, 11));
		Assert.assertTrue(city_manager
				.setPlacingTile(MapGridConstants.COMMERCIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(5, 11));
		Assert.assertTrue(city_manager.placeTile(5, 10));
		Assert.assertTrue(city_manager.placeTile(1, 0));
		city_manager.advanceTurn();
		Assert.assertEquals(100, city_manager.getJobs());
	}
	
	@Test
	public void testMetrics() {
		Assert.assertTrue(city_manager
				.NewGeneratedGame("DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n" + "DDDDDDDDDDDDDDDDDDDD\n"
						+ "DDDDDDDDDDDDDDDDDDDD\n"));
		Assert.assertTrue(city_manager.setPlacingTile(MapGridConstants.GAS_TILE));
		Assert.assertTrue(city_manager.placeTile(2, 2));
		city_manager.propagateMetrics();
		FooLogger.printMetric(city_manager, MapGridConstants.METRIC_HAPPINESS);
		Assert.assertTrue(city_manager.getTileMetrics(0, 2, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(1, 2, MapGridConstants.METRIC_POLLUTION) == 2);
		Assert.assertTrue(city_manager.getTileMetrics(2, 2, MapGridConstants.METRIC_POLLUTION) == 3);
		Assert.assertTrue(city_manager.getTileMetrics(3, 2, MapGridConstants.METRIC_POLLUTION) == 2);
		Assert.assertTrue(city_manager.getTileMetrics(4, 2, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(1, 1, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(2, 1, MapGridConstants.METRIC_POLLUTION) == 2);
		Assert.assertTrue(city_manager.getTileMetrics(3, 1, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(2, 0, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(1, 3, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(2, 3, MapGridConstants.METRIC_POLLUTION) == 2);
		Assert.assertTrue(city_manager.getTileMetrics(3, 3, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(2, 4, MapGridConstants.METRIC_POLLUTION) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(2, 2, MapGridConstants.METRIC_HAPPINESS) == -7);
		Assert.assertTrue(city_manager.setPlacingTile(MapGridConstants.INDUSTRIAL_TILE));
		Assert.assertTrue(city_manager.placeTile(5, 5));
		city_manager.propagateMetrics();
		Assert.assertTrue(city_manager.getTileMetrics(5, 5, MapGridConstants.METRIC_CRIME) == 7);
		Assert.assertTrue(city_manager.getTileMetrics(5, 6, MapGridConstants.METRIC_CRIME) == 6);
		Assert.assertTrue(city_manager.getTileMetrics(5, 7, MapGridConstants.METRIC_CRIME) == 5);
		Assert.assertTrue(city_manager.getTileMetrics(5, 8, MapGridConstants.METRIC_CRIME) == 4);
		Assert.assertTrue(city_manager.getTileMetrics(5, 9, MapGridConstants.METRIC_CRIME) == 3);
		Assert.assertTrue(city_manager.getTileMetrics(5, 10, MapGridConstants.METRIC_CRIME) == 2);
		Assert.assertTrue(city_manager.getTileMetrics(5, 11, MapGridConstants.METRIC_CRIME) == 1);
		Assert.assertTrue(city_manager.getTileMetrics(5, 12, MapGridConstants.METRIC_CRIME) == 0);
		Assert.assertTrue(city_manager.setPlacingTile(MapGridConstants.POLICE_TILE));
		Assert.assertTrue(city_manager.placeTile(5, 6));
		city_manager.propagateMetrics();
		Assert.assertTrue(city_manager.getTileMetrics(5, 5, MapGridConstants.METRIC_CRIME) == 0);
		Assert.assertTrue(city_manager.getTileMetrics(5, 6, MapGridConstants.METRIC_CRIME) == 0);
	}
		
}
