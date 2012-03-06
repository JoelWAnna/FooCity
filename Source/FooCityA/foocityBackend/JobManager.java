package foocityBackend;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Stack;

/**
 * JobManager Pathfinder class for locating jobs for residents
 * 
 * @author Joel Anna <annajoel@cecs.pdx.edu> 2012 February 23
 * 
 * 
 */
public class JobManager {
	private int jobs;
	public static final int NO_PATH = -1;
	public static final int PATH_ORIGIN = 0;
	public static final int PATH_WALK = 1;
	public static final int PATH_ROAD = 1 << 8;
	public static final int PATH_MAX_ROADS = 15 << 8;
	public static final int PATH_MAX_WALKED = 2;
	private static final int PATH_WALK_MASK = 0xf;

	/**
	 * Constructor no operations necessary at this time
	 */
	public JobManager() {
	}

	/**
	 * clearJobs resets available jobs to 0, to be used when a game is quit
	 */
	public void clearJobs() {
		jobs = 0;
	}

	/**
	 * findJobs
	 * 
	 * @param map_area
	 *            Dimension of the matrices used in calculation
	 * @param residential_matrix
	 *            matrix filled with the number of residents at each square
	 * @param job_matrix
	 *            matrix filled with the number of available jobs at each square
	 * @param path_matrix
	 *            matrix of travel costs for each square, Water = -1, Road = 1
	 *            << 8, everything else = 1
	 * @return number of jobs found by the pathfinding algorithm
	 */
	public int findJobs(Dimension map_area, int[][] residential_matrix,
			int[][] job_matrix, int[][] path_matrix) {
		clearJobs();

		int jobsFound = 0;

		// Stack for storing available job locations
		Stack<Point> found = new Stack<Point>();

		// Traverse through residential_matrix looking for residents
		for (int ySource = 0; ySource < map_area.height; ++ySource)
			for (int xSource = 0; xSource < map_area.width; ++xSource) {
				if (residential_matrix[xSource][ySource] > 0) {
					int yDestMin = Math.max(0, ySource - 17);
					int xDestMin = Math.max(0, xSource - 17);
					int yDestMax = Math.min(map_area.height, ySource + 18);
					int xDestMax = Math.min(map_area.width, xSource + 18);

					// Find path costs for squares within road/walking distance
					int[][] resolved_path_matrix = findPath(new Point(xSource,
							ySource), new Dimension(xDestMax, yDestMax),
							path_matrix);

					// Traverse Job locations within road/walking distance and
					// compare to resolved_path_matrix
					for (int yDest = yDestMin; yDest < yDestMax; ++yDest) {
						for (int xDest = xDestMin; xDest < xDestMax; ++xDest) {
							if (job_matrix[xDest][yDest] > 0) {
								if (resolved_path_matrix[xDest][yDest] > 0)
									found.push(new Point(xDest, yDest));

							}
						}
					}

					while (found.size() > 0) {
						// Divide residents between all available jobs
						// TODO: this should be fixed to relook if all residents
						// do not find jobs
						int jobs_to_place = residential_matrix[xSource][ySource]
								/ found.size();
						if (residential_matrix[xSource][ySource] % found.size() > 0)
							jobs_to_place++;

						Point location = found.pop();
						FooLogger.infoLog("Trying to find " + jobs_to_place);
						FooLogger.infoLog("residents "
								+ residential_matrix[xSource][ySource]);
						FooLogger.infoLog("jobs "
								+ job_matrix[location.x][location.y]);

						jobs_to_place = residential_matrix[xSource][ySource] > jobs_to_place
								? jobs_to_place
								: residential_matrix[xSource][ySource];
						jobs_to_place = job_matrix[location.x][location.y] > jobs_to_place
								? jobs_to_place
								: job_matrix[location.x][location.y];

						FooLogger.infoLog("Found " + jobs_to_place);

						job_matrix[location.x][location.y] -= jobs_to_place;
						residential_matrix[xSource][ySource] -= jobs_to_place;
						jobsFound += jobs_to_place;
					}
				}
			}

		this.jobs = jobsFound;
		FooLogger.infoLog("Final Found " + jobsFound);
		return jobs;
	}

	/**
	 * 
	 * @param origin
	 *            - residential location to start searching
	 * @param map_area
	 *            - Dimensions of the path_matrix
	 * @param path_matrix
	 *            - matrix of travel costs for each square, Water = -1, Road = 1
	 *            << 8, everything else = 1
	 * @return
	 */
	protected int[][] findPath(Point origin, Dimension map_area,
			int[][] path_matrix) {
		int yMin = Math.max(origin.y - 17, 0);
		int xMin = Math.max(origin.x - 17, 0);

		int yMax = map_area.height;
		int xMax = map_area.width;

		int[][] found_matrix = new int[xMax][yMax];

		// Initialize Paths, 0 at origin, -1 elsewhere
		for (int yDestination = 0; yDestination < yMax; ++yDestination) {
			for (int xDestination = 0; xDestination < xMax; ++xDestination) {
				if ((yDestination == origin.y && xDestination == origin.x)
						&& (path_matrix[origin.x][origin.y] != NO_PATH)) {
					found_matrix[xDestination][yDestination] = PATH_ORIGIN;
				} else {
					found_matrix[xDestination][yDestination] = NO_PATH;
				}
			}
		}

		// Loop through and update path costs until no further paths are found
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int yPath = yMin; yPath < yMax; yPath++) {
				for (int xPath = xMin; xPath < xMax; xPath++) {
					// Only Start from a location that can be reached
					if (found_matrix[xPath][yPath] != NO_PATH) {
						// Add N and S
						for (int yNext = Math.max(yPath - 1, 0); yNext < Math
								.min(yPath + 2, yMax); ++yNext) {
							// skip the current tile here for edge cases (yPath
							// == 0 || yPath == yMax)
							if (yNext == yPath)
								continue;

							int previousTile = path_matrix[xPath][yPath];
							int totalPath = found_matrix[xPath][yPath];
							int thisTile = path_matrix[xPath][yNext];

							// Check if moving changing from walking to driving
							// only continue if there is no previous driving
							if (thisTile == PATH_ROAD
									&& previousTile == PATH_WALK) // walking
							{
								if (totalPath >= PATH_ROAD) // roads have been
															// taken previously,
															// invalid path
									continue;
							}

							int newPathCost = thisTile + totalPath;

							// new Road, only add if total roads will be
							// PATH_MAX_ROADS or less
							if (((thisTile == PATH_ROAD) && ((newPathCost & ~PATH_WALK_MASK) <= PATH_MAX_ROADS))
									||
									// new walk, only add it total walking will
									// be PATH_MAX_WALKED or less
									((thisTile == PATH_WALK) && ((newPathCost & PATH_WALK_MASK) <= PATH_MAX_WALKED))) {

								switch (found_matrix[xPath][yNext]) {
								// case PATH_ORIGIN: // starting square don't
								// update
								// break;
									case NO_PATH : // Always update from no path
													// to this path
										changed = true;
										found_matrix[xPath][yNext] = newPathCost;
										break;
									default :
										// Check for a better path, replacing
										// walking with a road where possible
										if ((found_matrix[xPath][yNext] & PATH_WALK_MASK) > (newPathCost & PATH_WALK_MASK)) {
											changed = true;
											found_matrix[xPath][yNext] = newPathCost;
										}
								}

							}
						}

						// Add E W
						for (int xNext = Math.max(xPath - 1, 0); xNext < Math
								.min(xPath + 2, xMax); ++xNext) {
							// skip the current tile here for edge cases (xPath
							// == 0 || xPath == xMax)
							if (xNext == xPath)
								continue;

							int previousTile = path_matrix[xPath][yPath];
							int totalPath = found_matrix[xPath][yPath];
							int thisTile = path_matrix[xNext][yPath];

							// Check if moving changing from walking to driving
							// only continue if there is no previous driving
							if (thisTile == PATH_ROAD
									&& previousTile == PATH_WALK) // walking
							{
								if (totalPath >= PATH_ROAD) // roads have been
															// taken previously,
															// invalid path
									continue;
							}

							int newPathCost = thisTile + totalPath;
							// new Road, only add if total roads will be
							// PATH_MAX_ROADS or less
							if (((thisTile == PATH_ROAD) && ((newPathCost & ~PATH_WALK_MASK) <= PATH_MAX_ROADS))
									||
									// new walk, only add it total walking will
									// be PATH_MAX_WALKED or less
									((thisTile == PATH_WALK) && ((newPathCost & PATH_WALK_MASK) <= PATH_MAX_WALKED))) {

								switch (found_matrix[xNext][yPath]) {
								// case PATH_ORIGIN: // starting square don't
								// update
								// break;
									case NO_PATH : // Always update from no path
													// to this path
										changed = true;
										found_matrix[xNext][yPath] = newPathCost;
										break;
									default :
										// Check for a better path, replacing
										// walking with a road where possible
										if ((found_matrix[xNext][yPath] & PATH_WALK_MASK) > (newPathCost & PATH_WALK_MASK)) {
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

	/**
	 * printMatrix debugging print to view contents of matrix
	 * 
	 * @param found_matrix
	 *            matrix to print
	 * @param height
	 *            maximum height to print
	 * @param width
	 *            maximum width to print
	 */
	private void printMatrix(int[][] found_matrix, int height, int width) {
		if (FooLogger.MAX_ERROR < FooLogger.INFO_LOG)
			return;
		FooLogger.infoLog("Start printMatrix\n");
		for (int y = 0; y < height; y++) {
			String line = "";
			for (int x = 0; x < width; x++) {
				if (found_matrix[x][y] == -1) {
					line += "   -1";
				} else if (found_matrix[x][y] == 0)
					line += "    0";
				else {
					int Road = found_matrix[x][y] >> 8;
					int Walk = found_matrix[x][y] & 0xf;
					if (Road > 0) {
						if (Road < 10)
							line += " ";
						line += Road + "R";
					} else
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
