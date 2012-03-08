package foocityBackend;
import java.awt.Dimension;
import java.awt.Point;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class JobManagerTest extends TestCase{
	private JobManager job_manager;
	@Override
	protected void setUp() { 
		job_manager = new JobManager();
    }
	
	@Override
	protected void tearDown() { 
		 job_manager = null; 
    }

	@Test
	public void testFindJobs()
	{
		int size = 20;
		Dimension map_area = new Dimension(size, size);
		int [][]resident_map = new int[size][size];
		int [][]job_map = new int[size][size];
		int [][]path_map = new int[size][size];
		job_manager.findJobs(map_area, resident_map, job_map, path_map);
	}
	
	public void testFindWalkingPaths()
	{
		int size = 5;
		Dimension map_area = new Dimension(size, size);
		int [][] path_matrix = new int[size][size];

		
		int [][]expectedD = 
			{
				{-1,-1,-1,-1,-1,-1,-1,-1,-1},
				{-1,-1,-1,-1,-1,-1,-1,-1,-1},
				{-1,-1,-1, -1,2,-1,-1,-1,-1},
				{-1,-1,-1, 2, 1, 2,-1,-1,-1},
				{-1,-1, 2, 1, 0, 1, 2,-1,-1},
				{-1,-1,-1, 2, 1, 2,-1,-1,-1},
				{-1,-1,-1, -1,2,-1,-1,-1,-1},
				{-1,-1,-1,-1,-1,-1,-1,-1,-1},
				{-1,-1,-1,-1,-1,-1,-1,-1,-1},
			};

		
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				path_matrix[x][y] = 1;
		//path_matrix[0][1] = -1;

		
		
		for (int yOrigin = 0; yOrigin < size; ++yOrigin)
			for (int xOrigin = 0; xOrigin < size; ++xOrigin)
			{
				int [][]paths = job_manager.findPath(new Point(xOrigin, yOrigin), map_area, path_matrix);
				for (int y = 0; y < size; ++y)
					for (int x = 0; x < size; ++x)
						Assert.assertEquals("origin " + xOrigin + " " + yOrigin + " point " + x + "," + y, expectedD[x+4-xOrigin][y+4-yOrigin], paths[x][y]);
			
			}
		
		
		
		
		
		
		int [][] paths = job_manager.findPath(new Point(0,0), map_area, path_matrix);
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				Assert.assertEquals("origin 0,0, point " + x + "," + y, expectedD[x+4-0][y+4-0], paths[x][y]);
		paths = job_manager.findPath(new Point(0, 1), map_area, path_matrix);
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				Assert.assertEquals("origin 1,0, point " + x + "," + y, expectedD[x+4-0][y+4-1], paths[x][y]);
		paths = job_manager.findPath(new Point(0, 2), map_area, path_matrix);
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				Assert.assertEquals("origin 2,0, point " + x + "," + y, expectedD[x+4-0][y+4-2], paths[x][y]);
		paths = job_manager.findPath(new Point(0, 3), map_area, path_matrix);
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				Assert.assertEquals("origin 3,0, point " + x + "," + y, expectedD[x+4-0][y+4-3], paths[x][y]);
		paths = job_manager.findPath(new Point(0, 4), map_area, path_matrix);
		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				Assert.assertEquals("origin 4,0, point " + x + "," + y, expectedD[x+4-0][y+4-4], paths[x][y]);


	}
	
	
	@Test
	public void testFindRoadPaths()
	{
		int size = 16;
		Dimension map_area = new Dimension(size, size);
		int [][]path_matrix = new int[size][size];
		

		for (int y = 0; y < size; ++y)
			for (int x = 0; x < size; ++x)
				path_matrix[x][y] = 1<<8;
		//path_matrix[0][1] = -1;

		int [][] expectedD =
			{
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,   -1,	-1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,	  -1,	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,	  -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1, -1},
				{	-1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1},
				{	-1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1},
				{	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 2<<8, 1<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1},
				{15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 2<<8, 1<<8,    0, 1<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8},
				{	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 2<<8, 1<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1},
				{	-1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1},
				{	-1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1},
				{	-1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,	  -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 6<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,	  -1,	-1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 7<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1, -1, -1, -1, -1, -1},
				{	-1,   -1,   -1,   -1,	-1,   -1,	-1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8, 8<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8, 9<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,10<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,11<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,12<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,13<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,14<<8,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,15<<8,   -1,   -1,   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
			};
		
		
	/*	int [][]expected =
			{
				{0, 1<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8},
				{1<<8, 2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1},
				{2<<8, 3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1},
				{3<<8, 4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1},
				{4<<8, 5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1},
				{5<<8, 6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1},
				{6<<8, 7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1},
				{7<<8, 8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1},
				{8<<8, 9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1},
				{9<<8, 10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{10<<8, 11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{11<<8, 12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{12<<8, 13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{13<<8, 14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{14<<8, 15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
				{15<<8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
			};
		*/
		for (int yOrigin = 0; yOrigin < size; ++yOrigin)
			for (int xOrigin = 0; xOrigin < size; ++xOrigin)
			{
				int [][] paths = job_manager.findPath(new Point(xOrigin, yOrigin), map_area, path_matrix);
				for (int y = 0; y < size; ++y)
					for (int x = 0; x < size; ++x)
						Assert.assertEquals("origin " + xOrigin + " " + yOrigin + " point " + x + "," + y, expectedD[x+15-xOrigin][y+15-yOrigin], paths[x][y]);
			//			Assert.assertEquals("origin 0,0, point " + x + "," + y, expected[x][y], paths[x][y]);
			
			}
		
	}
}