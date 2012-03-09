package foocityBackend;
import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//
/**
 * FooCityManager
 * 
 * @author Joel Anna <annajoel@cecs.pdx.edu>
 * @author David Wiza <>
 * 
 * 
 * 
 * 
 */
public class FooCityManager {
	public static final int STARTING_FUNDS = 20000;
	public static final int FLAG_WIN = 1;
	public static final int FLAG_LOSE = -1;
	public static final int FLAG_MIDGAME = 0;
	private int availableFunds;
	private MapGrid current_map;
	private int tile_to_place;
	private int turn;
	private int powerConsumed = 0, powerGenerated = 0;
	private int waterConsumed = 0, waterGenerated = 0;
	private int income = 0, expenses = 0;
	private int cashFlow = 0;
	private int jobs = 0, residents = 0;
	private JobManager job_manager;
	private int turnsLosing;

	public FooCityManager() {
		current_map = null;
		job_manager = null;
		tile_to_place = 0;
		turn = 0;
	}

	public Dimension getMapArea() {
		if (current_map != null)
			return current_map.getMapArea();
		return null;
	}

	public int getTileInt(int x, int y) {
		if (current_map != null)
			return current_map.getTileInt(x, y);
		return 0;
	}

	public int getTileVariation(int x, int y) {
		if (current_map != null) {
			Tile currentTile = current_map.getTile(x, y);
			if (currentTile != null) {
				return currentTile.variation;
			}
		}
		return 0;
	}

	public int getTileMetrics(int x, int y, int Metric) {
		if ((current_map != null)
				&& (MapGridConstants.METRIC_CRIME <= Metric && Metric < MapGridConstants.METRIC_LAST)) {
			Tile currentTile = current_map.getTile(x, y);
			if (currentTile != null) {
				return currentTile.metricsActual[Metric];
			}
		}
		return 0;
	}

	public boolean MapGridLoaded() {
		return current_map != null;
	}

	public boolean NewGame(String map_name) {
		if (current_map == null) {
			current_map = MapGrid.FromFile(map_name);
			return startGame();
		}
		return false;
	}

	public boolean NewGeneratedGame(String map_string) {
		if (current_map == null) {
			current_map = MapGrid.FromString(map_string);
			return startGame();
		}
		return false;
	}

	public void Quit() {
		current_map = null;
		job_manager = null;
		turn = 0;
		availableFunds = 0;
	}

	private boolean startGame() {
		if (current_map != null) {
			availableFunds = FooCityManager.STARTING_FUNDS;
			propagateMetrics();
			job_manager = new JobManager();
			return true;
		}
		return false;
	}

	public int getCurrentTurn() {
		return turn;
	}

	private int checkWinLose() {
		return FooCityManager.FLAG_MIDGAME;
	}
	public int advanceTurn() {
		long timeBegun = System.nanoTime();
		if (current_map == null)
			return FooCityManager.FLAG_MIDGAME;
		// Advance the turn
		this.turn++;
		// Propagate the metrics
		this.propagateMetrics();

		// Reset some variables
		powerConsumed = 0;
		powerGenerated = 0;
		waterConsumed = 0;
		waterGenerated = 0;
		income = 0;
		expenses = 0;
		jobs = 0;
		residents = 0;
		float factor;

		// For each tile...
		for (int y = 0; y < current_map.getMapArea().getHeight(); y++) {
			for (int x = 0; x < current_map.getMapArea().getWidth(); x++) {
				// Get the tile
				TileMetrics tile_metrics = TileMetrics
						.GetTileMetrics(current_map.getTileInt(x, y));

				// Add to power consumed if its consuming, otherwise "add" it to
				// generated
				// (Remember that if its generating, the consumed amount is
				// negative
				// So we SUBTRACT it to ADD it)
				if (tile_metrics.getPowerConsumed() > 0)
					powerConsumed += tile_metrics.getPowerConsumed();
				else
					powerGenerated -= tile_metrics.getPowerConsumed();

				// Do the same with water
				if (tile_metrics.getWaterConsumed() > 0)
					waterConsumed += tile_metrics.getWaterConsumed();
				else
					waterGenerated -= tile_metrics.getWaterConsumed();

				// Do a similar thing with residents versus jobs
				if (tile_metrics.getJobs() > 0)
					jobs += tile_metrics.getJobs();
				else
					residents -= tile_metrics.getJobs();

				// If the residents are employed (Or the job location has a
				// resident nearby)
				// Then add it to the budget
				// if (tile_metrics.employed){
				if (tile_metrics.getMonthlyCost() > 0)
					expenses += tile_metrics.getMonthlyCost();
				else
					income -= tile_metrics.getMonthlyCost();
				// }

			}
		}
		// Now, we have to reduce the budget based on a lack of jobs, residents,
		// water, or power
		// The formula in each case is basically:
		// budget = budget * factor
		// Where factor = Provided / Needed (capped to a value of 1)
		// (Just make sure we're not going to divide by zero!)

		if (waterConsumed > 0) {
			factor = (float) waterGenerated / waterConsumed;
			if (factor < 1)
				income *= factor;
		}

		if (powerConsumed > 0) {
			factor = (float) powerGenerated / powerConsumed;
			if (factor < 1)
				income *= factor;
		}

		if (residents > 0) {
			factor = (float) jobs / residents;
			if (factor < 1)
				income *= factor;
		}

		if (jobs > 0) {
			factor = (float) residents / jobs;
			if (factor < 1)
				income *= factor;
		}

		// We're done with the budget! Make the change.
		cashFlow = (int) (income - expenses);
		this.availableFunds += cashFlow;
		this.findJobs();
		FooLogger.infoLog("advanceTurn took "
				+ Long.toString((System.nanoTime() - timeBegun) / 1000000)
				+ " ms\n");
		return checkWinLose();
	}
	
	public boolean checkForLoss(){
		return false;
		
	}

	public boolean setPlacingTile(int i) {
		if (i == MapGridConstants.BULLDOZE_TILE || (i >= MapGridConstants.WATER_TILE && i < MapGridConstants.LAST_TILE)) {
			this.tile_to_place = i;
			return true;
		}

		tile_to_place = MapGridConstants.NO_TILE;
		return false;
	}

	public int getPlacingTile() {
		return tile_to_place;
	}

	public boolean placeTile(Point p) {
		return placeTile(p.x, p.y);
	}

	public boolean placeTile(int x, int y) {
		if (current_map != null && tile_to_place != MapGridConstants.NO_TILE) {
			
			int price = TileMetrics.GetTileMetrics(tile_to_place).getPrice();
			if (price <= this.availableFunds) {
				if (current_map.setTile(x, y, tile_to_place)) {
					this.availableFunds -= price;
					return true;
				}
			}
		} 
		return false;
	}

	public void propagateMetrics() {
		if (current_map == null)
			return;
		// First, clear all the metrics
		Dimension map_area = current_map.getMapArea();
		if (map_area == null)
			return;
		int map_width = (int) map_area.getHeight();
		int map_height = (int) map_area.getWidth();
		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				current_map.getTile(x, y).resetMetrics();
			}

		}

		// For each metric...
		for (int metric = 0; metric < MapGridConstants.METRIC_LAST; metric++) {
			for (int yOrigin = 0; yOrigin < map_height; yOrigin++) {
				for (int xOrigin = 0; xOrigin < map_width; xOrigin++) {
					int c = TileMetrics.GetTileMetrics(
							current_map.getTileInt(xOrigin, yOrigin))
							.getMetricsContributed()[metric];
					// If we're ADDING crime...
					if (c > 0) {
						// Branch to the right
						for (int xOffset = 0; xOffset < c; xOffset++) {
							// Make sure we haven't gone past the right edge
							if (xOrigin + xOffset < map_width) {
								// Set the current value
								current_map.updateMetrics(xOrigin + xOffset,
										yOrigin, metric, c - xOffset);
								// Branch up/down, start 1 unit past the
								// horizontal branch
								for (int yOffset = 1; yOffset < c - xOffset; yOffset++) {
									// Don't go past the top of the map
									if (yOrigin - yOffset >= 0)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin - yOffset,
												metric, c - xOffset - yOffset);
									// Don't go below the map
									if (yOrigin + yOffset < map_height)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin + yOffset,
												metric, c - xOffset - yOffset);
								}
							}
						}
						// Branch to the left
						// We have to start 1 unit to the left to avoid
						// double-adding the center column
						for (int xOffset = -1; xOffset > -c; xOffset--) {
							// Make sure we haven't gone past the left edge
							if (xOrigin + xOffset > -1) {
								// Set the current value
								current_map.updateMetrics(xOrigin + xOffset,
										yOrigin, metric, c + xOffset);
								// Branch up/down, start 1 unit past the
								// horizontal branch
								for (int yOffset = 1; yOffset < c + xOffset; yOffset++) {
									// Don't go past the top of the map
									if (yOrigin - yOffset >= 0)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin - yOffset,
												metric, c + xOffset - yOffset);
									// Don't go below the map
									if (yOrigin + yOffset < map_height)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin + yOffset,
												metric, c + xOffset - yOffset);
								}
							}
						}
					} else if (c < 0) { // But if we're SUBTRACTING crime...
						// Treat it like we're adding it, but we'll be
						// subtracting
						c = -c;
						// Branch to the right
						for (int xOffset = 0; xOffset < c; xOffset++) {
							// Make sure we haven't gone past the right edge
							if (xOrigin + xOffset < map_width) {
								// Set the current value
								current_map.updateMetrics(xOrigin + xOffset,
										yOrigin, metric, -(c - xOffset));
								// Branch up/down, start 1 unit past the
								// horizontal branch
								for (int yOffset = 1; yOffset < c - xOffset; yOffset++) {
									// Don't go past the top of the map
									if (yOrigin - yOffset >= 0)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin - yOffset,
												metric,
												-(c - xOffset - yOffset));
									// Don't go below the map
									if (yOrigin + yOffset < map_height)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin + yOffset,
												metric,
												-(c - xOffset - yOffset));
								}
							}
						}
						// Branch to the left
						// We have to start 1 unit to the left to avoid
						// double-adding the center column
						for (int xOffset = -1; xOffset > -c; xOffset--) {
							// Make sure we haven't gone past the left edge
							if (xOrigin + xOffset > -1) {
								// Set the current value
								current_map.updateMetrics(xOrigin + xOffset,
										yOrigin, metric, -(c + xOffset));
								// Branch up/down, start 1 unit past the
								// horizontal branch
								for (int yOffset = 1; yOffset < c + xOffset; yOffset++) {
									// Don't go past the top of the map
									if (yOrigin - yOffset >= 0)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin - yOffset,
												metric,
												-(c + xOffset - yOffset));
									// Don't go below the map
									if (yOrigin + yOffset < map_height)
										current_map.updateMetrics(xOrigin
												+ xOffset, yOrigin + yOffset,
												metric,
												-(c + xOffset - yOffset));
								}
							}
						}
					}
				}
			}
		}
		
		// Don't let crime or pollution be less than zero
		// Subtract crime and pollution from happiness
		for (int y = 0; y < map_height; y++) {
			for (int x = 0; x < map_width; x++) {
				int c = getTileMetrics(x, y, MapGridConstants.METRIC_CRIME);
				int p = getTileMetrics(x, y, MapGridConstants.METRIC_POLLUTION);
				if (c < 0) {
					current_map.updateMetrics(x, y, MapGridConstants.METRIC_CRIME, -c);
					c = 0;
				}
				if (p < 0) {
					current_map.updateMetrics(x, y, MapGridConstants.METRIC_POLLUTION, -p);
					p = 0;
				}
				current_map.updateMetrics(x, y, MapGridConstants.METRIC_HAPPINESS, -(c + p));
			}

		}
	}

	public boolean SaveGame(String savePath) {
		if ((current_map != null) && (savePath != null)) {
			try {
				File save_file = new File(savePath);
				BufferedWriter bw = new BufferedWriter(
						new FileWriter(save_file));

				if (bw != null) {
					bw.write("FOOCITYMAGIC\n");
					bw.write("CurrentTurn:" + turn + "\n");
					bw.write("AvailableFunds:" + getAvailableFunds() + "\n");
					bw.write("Water+:" + waterGenerated + "\n");
					bw.write("Water-:" + waterConsumed + "\n");
					bw.write("Power+:" + powerGenerated + "\n");
					bw.write("Power-:" + powerConsumed + "\n");
					bw.write("Jobs:" + jobs + "\n");
					bw.write("Residents:" + residents + "\n");
					bw.write("Income:" + income + "\n");
					bw.write("Expenses:" + expenses + "\n");
					bw.write("CashFlow:" + cashFlow + "\n");
					bw.write("MapGrid:1\n");
					bw.write("width:"
							+ (int) current_map.getMapArea().getWidth() + "\n");
					bw.write("height:"
							+ (int) current_map.getMapArea().getHeight() + "\n");
					bw.write(current_map.toString() + "\n");
					bw.close();
					return true;
				}
			} catch (FileNotFoundException e) {
				FooLogger.errorLog("SaveGame: FileNotFoundException");
				FooLogger.errorLog(e.getMessage());
			} catch (IOException e) {
				FooLogger.errorLog("SaveGame: IOException");
				FooLogger.errorLog(e.getMessage());
			}
		}
		return false;
	}

	public boolean LoadGame(String savePath) {
		boolean valid_save = true;
		job_manager = new JobManager();
		if (savePath != null) {
			File save_file = new File(savePath);
			Scanner sc;
			try {
				sc = new Scanner(save_file);
			} catch (FileNotFoundException e) {
				return false;
			}
			if (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (0 == line.compareTo("FOOCITYMAGIC\n")) {
					FooLogger.errorLog("LoadGame:Invalid Magic \"" + line
							+ "\"\n");
					return false;
				}
				turn = 0;
				while (sc.hasNextLine() && valid_save) {
					String line2 = sc.nextLine();
					if (line2.length() == 0)
						break;

					String[] parsedline = line2.split(":");
					if (parsedline.length != 2) {
						FooLogger
								.errorLog("LoadGame: parsedline length != 2 \""
										+ parsedline.length + "\"\n");
						valid_save = false;
						break;
					}

					FooLogger.infoLog("LoadGame: " + parsedline[0]);

					if (parsedline[0].equals("MapGrid")) {
						try {
							switch (Integer.parseInt(parsedline[1])) {
								case 1 :

									int width = 0;
									int height = 0;
									if (sc.hasNextLine()) {
										String[] widthLine = sc.nextLine()
												.split(":");
										if (widthLine.length == 2)
											width = Integer
													.parseInt(widthLine[1]);
									}
									if (sc.hasNextLine()) {
										String[] heightLine = sc.nextLine()
												.split(":");
										if (heightLine.length == 2)
											height = Integer
													.parseInt(heightLine[1]);
									}
									if (width < 5 || height < 5) {
										FooLogger
												.errorLog("LoadGame: invalid width or height for MapGrid");
										FooLogger.errorLog("width = " + width
												+ " height = " + height);
										valid_save = false;
										break;
									}
									StringBuilder sb = new StringBuilder();
									for (int y = 0; y < height; ++y) {
										if (sc.hasNextLine())
											sb.append(sc.nextLine() + "\n");
										else {
											valid_save = false;
											break;
										}
											
									}
									current_map = MapGrid.FromString(sb.toString());
									valid_save = current_map != null;
									break;
								default :
							}

						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Mapgrid:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("CurrentTurn")) {
						try {
							turn = Integer.parseInt(parsedline[1]);
							if (turn < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at CurrentTurn:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("AvailableFunds")) {
						try {
							availableFunds = Integer.parseInt(parsedline[1]);
							if (availableFunds < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at AvailableFunds:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Water+")) {
						try {
							waterGenerated = Integer.parseInt(parsedline[1]);
							if (waterGenerated < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Water+:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Water-")) {
						try {
							waterConsumed = Integer.parseInt(parsedline[1]);
							if (waterConsumed < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Water-:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Power+")) {
						try {
							powerGenerated = Integer.parseInt(parsedline[1]);
							if (powerGenerated < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Power+:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Power-")) {
						try {
							powerConsumed = Integer.parseInt(parsedline[1]);
							if (powerConsumed < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Power-:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Jobs")) {
						try {
							jobs = Integer.parseInt(parsedline[1]);
							if (jobs < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Jobs:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Residents")) {
						try {
							residents = Integer.parseInt(parsedline[1]);
							if (residents < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Residents:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Income")) {
						try {
							income = Integer.parseInt(parsedline[1]);
							if (income < 0)
								valid_save = false;
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Income:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("Expenses")) {
						try {
							expenses = Integer.parseInt(parsedline[1]);
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at Expenses:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					} else if (parsedline[0].equals("CashFlow")) {
						try {
							cashFlow = Integer.parseInt(parsedline[1]);
						} catch (NumberFormatException e) {
							FooLogger
									.errorLog("LoadGame: NumberFormatException at CashFlow:");
							FooLogger.errorLog(e.getMessage());
							valid_save = false;
						}
					}
				}

				if (!valid_save) {
					Quit();
				}
				return valid_save;
			}
		}
		return false;
	}

	public int getAvailableFunds() {
		return availableFunds;
	}

	private void findJobs() {
		jobs = job_manager.findJobs(current_map.getMapArea(),
				current_map.getResidentialMatrix(), current_map.getJobMatrix(),
				current_map.getRoadMatrix());
	}

	public int getJobs() {
		// TODO Auto-generated method stub
		return jobs;
	}

	public String getEndOfTurnReport() {
		final String PLACEHOLDER = " \n";
		StringBuilder sb = new StringBuilder();

		sb.append("End of Turn Report for Turn: \n");
		sb.append(Integer.toString(turn) + "\n");
		sb.append(PLACEHOLDER);

		sb.append(PLACEHOLDER);
		sb.append("Produced\n");
		sb.append("Consumed\n");
		sb.append("Water\n");
		sb.append(Float.toString(waterGenerated) + "\n");
		sb.append(Float.toString(waterConsumed) + "\n");
		sb.append("Electricity" + "\n");
		sb.append(Float.toString(powerGenerated) + "\n");
		sb.append(Float.toString(powerConsumed) + "\n");
		sb.append("Jobs" + "\n");
		sb.append(Float.toString(jobs) + "\n");
		sb.append(Float.toString(residents) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Tax income: " + "\n");
		sb.append(Integer.toString(income) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Upkeep expenses: " + "\n");
		sb.append(Integer.toString(expenses) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Cash Flow: " + "\n");
		sb.append("$" + Integer.toString(cashFlow) + "\n");
		sb.append(PLACEHOLDER);
		sb.append("Available Funds:" + "\n");
		sb.append("$" + Integer.toString(getAvailableFunds()) + "\n");

		return sb.toString();
	}

}
