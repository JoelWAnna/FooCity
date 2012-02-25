package foocityBackend;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Stack;

public class JobManager {
	private int jobs;
	public static final int NO_PATH = -1;
	public static final int PATH_WALK = 1;
	public static final int PATH_ROAD = 1 << 8;
	public static final int PATH_MAX_ROADS = 15 << 8;
	public static final int PATH_MAX_WALKED = 2;
	public JobManager()
	{
	}
	
	public void clearJobs()
	{
		jobs = 0;
	}

	public int findJobs(Dimension map_area, int[][] residential_matrix, int[][] job_matrix, int[][] path_matrix)
	{
		clearJobs();

		int jobsFound = 0;
		Stack<Dimension> found = new Stack<Dimension>();		
		for (int ySource = 0; ySource < map_area.height; ++ySource)
			for (int xSource = 0; xSource < map_area.width; ++xSource)
			{
				if (residential_matrix[xSource][ySource] > 0)
				{
					int yDestMin = Math.max(0, ySource-17);
					int xDestMin = Math.max(0, xSource-17);
					int yDestMax = Math.min(map_area.height, ySource+18);
					int xDestMax = Math.min(map_area.width, xSource+18);
					int [][] resolved_path_matrix = findPath(new Point(xSource,ySource), path_matrix, new Dimension(xDestMax, yDestMax));
					for (int yDest = yDestMin; yDest < yDestMax; ++yDest)
					{
						for (int xDest = xDestMin; xDest < xDestMax; ++xDest)
						{
							if (job_matrix[xDest][yDest] > 0)
							{
								if (resolved_path_matrix[xDest][yDest] > 0)
									found.push(new Dimension(xDest,yDest));
								
							}
						}
					}

					if (found.size() > 0)
					{
						while (found.size() > 0)
						{
							int jobs_to_place = residential_matrix[xSource][ySource] / found.size();
							if (residential_matrix[xSource][ySource] % found.size() > 0)
								jobs_to_place++;
							
							Dimension location = found.pop();
							FooLogger.infoLog("Trying to find " + jobs_to_place);
							FooLogger.infoLog("residents " + residential_matrix[xSource][ySource]);
							FooLogger.infoLog("jobs " + job_matrix[location.width][location.height]);
							jobs_to_place = residential_matrix[xSource][ySource] > jobs_to_place ? jobs_to_place : residential_matrix[xSource][ySource];
							jobs_to_place = job_matrix[location.width][location.height] > jobs_to_place ? jobs_to_place : job_matrix[location.width][location.height];
							
							FooLogger.infoLog("Found " + jobs_to_place);
							
							job_matrix[location.width][location.height] -= jobs_to_place;
							residential_matrix[xSource][ySource] -= jobs_to_place;
							jobsFound += jobs_to_place;
						}
					}
				}
			}
		this.jobs = jobsFound;
		FooLogger.infoLog("Final Found " + jobsFound);
		return jobs;
	}

	private int [][] findPath(Point origin, int[][] path_matrix, Dimension map_area)
	{
		int yMin = Math.max(origin.y - 17, 0);
		int xMin = Math.max(origin.x - 17, 0);

		int yMax = map_area.height;
		int xMax = map_area.width;

		int [][]found_matrix = new int[xMax][yMax];

		// Initialize Paths
		for (int yDestination = 0; yDestination < yMax; ++yDestination)
		{
			for (int xDestination = 0; xDestination < xMax; ++xDestination)
			{
				if (yDestination == origin.y && xDestination == origin.x)
				{
					found_matrix[xDestination][yDestination] = 0;
				}
				else
				{
					found_matrix[xDestination][yDestination] = -1;
				}		
			}
		}
			

		boolean changed = true;
		while (changed)
		{
			changed = false;
			for (int yPath = yMin; yPath <  yMax; yPath++)
			{
				for (int xPath = xMin; xPath < xMax; xPath++)
				{
					if (found_matrix[xPath][yPath] != -1)
					{
						for (int yNext = Math.max(yPath-1, 0); yNext < Math.min(yPath+2, xMax); ++yNext)
						{
							int previousTile = path_matrix[xPath][yPath];
							int totalPath = found_matrix[xPath][yPath];
							int thisTile = path_matrix[xPath][yNext];

							// Check if moving changing from walking to driving
							// only continue if there is no previous driving
							if (thisTile == PATH_ROAD && previousTile == PATH_WALK) // walking
							{
								if (totalPath >= PATH_ROAD) // roads have been taken previously, invalid path
									continue;
							}
						
							
							
							int newValue = path_matrix[xPath][yNext] + found_matrix[xPath][yPath];
							// new Road
							if (((thisTile == PATH_ROAD) && ((newValue >> 8) <= PATH_MAX_ROADS)) ||
							// new walk
								((thisTile == PATH_WALK) && ((newValue & 0xF) <= PATH_MAX_WALKED)))
							{
								
								switch (found_matrix[xPath][yNext])
								{
								case 0: // starting square don't update
									break;
								case -1: // Always update from no path to this path
									changed = true;
									found_matrix[xPath][yNext] = newValue;
									break;
								default:
									// Check for a better path, replacing walking with a road where possible
									if ((found_matrix[xPath][yNext]&0xF) > (newValue&0xF))
									{
										changed = true;
										found_matrix[xPath][yNext] = newValue;
									}
								}

							}
						}
						
						for (int xNext = Math.max(xPath-1, 0); xNext < Math.min(xPath+2, xMax); ++xNext)
						{
							int previousTile = path_matrix[xPath][yPath];
							int totalPath = found_matrix[xPath][yPath];
							int thisTile = path_matrix[xNext][yPath];

							// Check if moving changing from walking to driving
							// only continue if there is no previous driving
							if (thisTile == PATH_ROAD && previousTile == PATH_WALK) // walking
							{
								if (totalPath >= PATH_ROAD) // roads have been taken previously, invalid path
									continue;
							}
							
							int newPathCost = thisTile + totalPath;
							// new Road
							if (((thisTile == PATH_ROAD) && ((newPathCost >> 8) <= PATH_MAX_ROADS)) ||
							// new walk
								((thisTile == PATH_WALK) && ((newPathCost & 0xF) <= PATH_MAX_WALKED)))
							{
								
								switch (found_matrix[xNext][yPath])
								{
								case 0: // starting square don't update
									break;
								case -1: // Always update from no path to this path
									changed = true;
									found_matrix[xNext][yPath] = newPathCost;
									break;
								default:
									// Check for a better path, replacing walking with a road where possible
									if ((found_matrix[xNext][yPath]&0xF) > (newPathCost&0xF))
									{
										changed = true;
										found_matrix[xNext][yPath] = newPathCost;
									}
								}

							}
						}
					}
				}
			}
		}
		printMatrix(found_matrix, yMax, xMax);

		return found_matrix;
	}

	private void printMatrix(int[][] found_matrix, int height, int width) {
		if (FooLogger.MAX_ERROR < FooLogger.INFO_LOG)
			return;
		FooLogger.infoLog("Start printMatrix\n");
		for (int y = 0; y < height; y++)
		{
			String line = "";
			for (int x = 0; x < width; x++)
			{
				if (found_matrix[x][y] == -1)
				{
					line += "   -1";
				}
				else if ( found_matrix[x][y] == 0)
					line += "    0";
				else
				{
					int Road = found_matrix[x][y] >> 8;
					int Walk = found_matrix[x][y] & 0xf;
					if (Road > 0)
					{
						if (Road < 10)
							line += " ";
						line += Road + "R";
					}
					else
						line += "   ";
					if (Walk > 0)
						line += Walk + "W";
					else
						line += "  ";
				}
				line += " ";
			}
			FooLogger.infoLog(line);
		}
		FooLogger.infoLog("End printMatrix\n");
		
	}
}
