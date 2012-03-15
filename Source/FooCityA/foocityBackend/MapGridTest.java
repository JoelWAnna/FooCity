package foocityBackend;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import terraingenerator.Generate;

public class MapGridTest {

	MapGrid map_grid;
	private String testMap = "./terrain/001.txt";
	@Before
	public void setUp() throws Exception {
		map_grid = null;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFromString() {
		Assert.assertNotNull(map_grid = MapGrid.FromString("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertEquals(new Dimension( 5, 5), map_grid.getMapArea());
	}

	@Test
	public void testFromFile() {
		Assert.assertEquals("Terrain File Should Exist", true,
			new File(testMap).exists());
	Assert.assertNotNull("Map should be loaded",
			map_grid = MapGrid.FromFile(testMap));
	Assert.assertEquals(new Dimension(MapGridConstants.MAP_WIDTH,
			MapGridConstants.MAP_HEIGHT), map_grid.getMapArea());

	}

	@Test
	public void testGetMapArea() {

		Dimension testDimension = new Dimension(5, 5);
		Assert.assertNotNull(map_grid = MapGrid.FromString("DDDDD\nDDDDD\nDDDDD\nDDDDD\nDDDDD\n"));
		Assert.assertEquals(testDimension, map_grid.getMapArea());
		Assert.assertEquals("Terrain File Should Exist", true,
				new File(testMap).exists());
		map_grid = null;
		Assert.assertNotNull("Map should be loaded",
				map_grid = MapGrid.FromFile(testMap));
		Assert.assertEquals(new Dimension(MapGridConstants.MAP_WIDTH,
				MapGridConstants.MAP_HEIGHT), map_grid.getMapArea());
	}

	@Test
	public void testGetTileInt() {
		String map_string = "DDDDD\nWWWWW\nBBBBB\nTTTTT\nGGGGG\n";
		Assert.assertNotNull(map_grid = MapGrid.FromString(map_string));
		int [][] expected = {
				{MapGridConstants.DIRT_TILE, MapGridConstants.WATER_TILE, MapGridConstants.BEACH_TILE,MapGridConstants.FORREST_TILE, MapGridConstants.GRASS_TILE},
				{MapGridConstants.DIRT_TILE, MapGridConstants.WATER_TILE, MapGridConstants.BEACH_TILE,MapGridConstants.FORREST_TILE, MapGridConstants.GRASS_TILE},
				{MapGridConstants.DIRT_TILE, MapGridConstants.WATER_TILE, MapGridConstants.BEACH_TILE,MapGridConstants.FORREST_TILE, MapGridConstants.GRASS_TILE},
				{MapGridConstants.DIRT_TILE, MapGridConstants.WATER_TILE, MapGridConstants.BEACH_TILE,MapGridConstants.FORREST_TILE, MapGridConstants.GRASS_TILE},
				{MapGridConstants.DIRT_TILE, MapGridConstants.WATER_TILE, MapGridConstants.BEACH_TILE,MapGridConstants.FORREST_TILE, MapGridConstants.GRASS_TILE}
		};
		
		Assert.assertEquals(0, map_grid.getTileInt(-1, -1));
		Assert.assertEquals(0, map_grid.getTileInt(5, 5));
		
		for (int y = 0; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertEquals("" + x + "," + y, MapGrid.TileCharToInt(map_string.charAt(x+y*6)), map_grid.getTileInt(x, y));

	}

	@Test
	public void testGetTile() {
		Assert.assertNotNull(map_grid = MapGrid.FromString("DDDDD\nWWWWW\nBBBBB\nTTTTT\nGGGGG\n"));
		for (int y = 0; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertTrue(map_grid.getTile(x,y) instanceof Tile);
		Assert.assertNull(map_grid.getTile(-1, -1));
		Assert.assertNull(map_grid.getTile(5, 5));
	}

	@Test
	public void testToString() {
		String generated_map = Generate.generate();
		Assert.assertNotNull(map_grid = MapGrid.FromString(generated_map));
		Assert.assertEquals(generated_map, map_grid.toString());
	}

	@Test
	public void testTileCharToInt() {
		for (int tile = MapGridConstants.WATER_TILE; tile < MapGridConstants.LAST_TILE; ++tile)
			Assert.assertEquals(tile, MapGrid.TileCharToInt(MapGridConstants.CHAR_TILES[tile]));
		String unused = "HJKMQUVXYZ";
		for (int i = 0; i < unused.length(); ++i)
			Assert.assertEquals(0, MapGrid.TileCharToInt(unused.charAt(i)));
//		{' ', 'W', 'B', 'G', 'D', 'T', 'I',
	//		'C', 'P', 'S', 'O', 'L', 'A', 'E', 'N', 'R', 'F'
	}

	@Test
	public void testSetTile() {
		Assert.assertNotNull(map_grid = MapGrid.FromString("DDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\nDDDDDDDDDD\n"));
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x) {
				Assert.assertEquals(MapGridConstants.DIRT_TILE,
						map_grid.getTileInt(x, y));
			}
		Assert.assertTrue("Place Tile " + MapGridConstants.CHAR_TILES[MapGridConstants.INDUSTRIAL_TILE], map_grid.setTile(0, 0, MapGridConstants.INDUSTRIAL_TILE));
		Assert.assertEquals(MapGridConstants.INDUSTRIAL_TILE, map_grid.getTileInt(0, 0));
		Assert.assertFalse(map_grid.setTile(0, 0, MapGridConstants.INDUSTRIAL_TILE));
		Assert.assertTrue(map_grid.setTile(0, 0, MapGridConstants.BULLDOZE_TILE));
		Assert.assertEquals(MapGridConstants.DIRT_TILE, map_grid.getTileInt(0, 0));
		
		for (int y = 0; y < 10; ++y)
			for (int x = 0; x < 10; ++x) {
				Assert.assertFalse(map_grid.setTile(x, y, MapGridConstants.BULLDOZE_TILE));
			}

		Assert.assertTrue(map_grid.setTile(0, 0, MapGridConstants.WATER_TILE));
		Assert.assertTrue(map_grid.setTile(0, 1, MapGridConstants.BEACH_TILE));
		for (int i = MapGridConstants.BULLDOZE_TILE; i < MapGridConstants.LAST_TILE; ++i) {
			Assert.assertFalse(map_grid.setTile(0, 0, i));
			Assert.assertFalse(map_grid.setTile(0, 1, i));
		}

		Assert.assertFalse(map_grid.setTile(4, 4, Integer.MAX_VALUE));
		Assert.assertFalse(map_grid.setTile(4, 4, Integer.MIN_VALUE));
		Assert.assertFalse(map_grid.setTile(4, 4, -2));
	}

	@Test
	public void testGetResidentialMatrix() {		
		String test_map = "RRRRR\nRRRRR\nDDDDD\nDDDDD\nDDDDD\n";
		Assert.assertNotNull(map_grid = MapGrid.FromString(test_map));
		int [][] residents = map_grid.getResidentialMatrix();
		for (int y = 0; y < 2; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertTrue(residents[x][y] > 0);
		for (int y = 2; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertTrue(residents[x][y] == 0);
	}

	@Test
	public void testGetJobMatrix() {
		
		String test_map = "ICSOL\nAENIC\nDDDDD\nDDDDD\nDDDDD\n";
		Assert.assertNotNull(map_grid = MapGrid.FromString(test_map));
		int [][] jobs = map_grid.getJobMatrix();
		for (int y = 0; y < 2; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertTrue(jobs[x][y] > 0);
		for (int y = 2; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertTrue(jobs[x][y] == 0);
	}

	@Test
	public void testGetRoadMatrix() {
		String testMap = "WWWWW\nWDDDW\nWDDDW\nWDDDW\nWWWWW";
		int [][]expected = {
				{JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH},
				{JobManager.NO_PATH, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.NO_PATH},
				{JobManager.NO_PATH, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.NO_PATH},
				{JobManager.NO_PATH, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.PATH_WALK, JobManager.NO_PATH},
				{JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH, JobManager.NO_PATH},	
		};
		
		Assert.assertNotNull(map_grid = MapGrid.FromString(testMap));
		int [][] road_matrix = map_grid.getRoadMatrix();
		for (int y = 0; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertEquals(expected[x][y], road_matrix[x][y]);
		
		expected[1][1] = expected [2][2]= expected[3][3] = JobManager.PATH_ROAD;
		Assert.assertTrue(map_grid.setTile(1, 1, MapGridConstants.ROAD_TILE));
		Assert.assertTrue(map_grid.setTile(2, 2, MapGridConstants.ROAD_TILE));
		Assert.assertTrue(map_grid.setTile(3, 3, MapGridConstants.ROAD_TILE));
		
		road_matrix = map_grid.getRoadMatrix();

		for (int y = 0; y < 5; ++y)
			for (int x = 0; x < 5; ++x)
				Assert.assertEquals(expected[x][y], road_matrix[x][y]);
	}

}
