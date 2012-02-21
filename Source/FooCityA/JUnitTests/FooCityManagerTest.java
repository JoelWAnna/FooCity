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

}
