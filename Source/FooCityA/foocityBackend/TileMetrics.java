package foocityBackend;

public class TileMetrics {
	private String description;
	private int price;
	private int monthlyCost;
	private int metricsContributed[];
	private int jobs;
	private int powerConsumed;
	private int waterConsumed;
	private int tile_type;

	private static TileMetrics tiles[];

	public static TileMetrics GetTileMetrics(int tileType) {
		if (tiles == null)
			init();
		if (MapGridConstants.WATER_TILE <= tileType
				&& tileType < MapGridConstants.LAST_TILE)
			return tiles[tileType];
		if (MapGridConstants.BULLDOZE_TILE == tileType)
			return tiles[0];
		return null;
	}

	static void init() {
		if (tiles == null) {
			FooLogger.errorLog("Creating Tile Metrics");
			tiles = new TileMetrics[MapGridConstants.LAST_TILE];
			tiles[0] = new TileMetrics(MapGridConstants.BULLDOZE_TILE);
			for (int i = MapGridConstants.WATER_TILE; i < MapGridConstants.LAST_TILE; ++i) {
				tiles[i] = new TileMetrics(i);
			}
		}
	}

	public boolean hasBusinessTax() {
		switch (tile_type)
		{
		case MapGridConstants.COMMERCIAL_TILE:
		case MapGridConstants.INDUSTRIAL_TILE:
			return true;
		default:
			return false;
		}
	}

	public boolean hasIncomeTax() {
		switch (tile_type)
		{
		case MapGridConstants.RESIDENTIAL_TILE:
			return true;
		default:
			return false;
		}
	}
	
	public boolean hasOccupationTax() {
		switch (tile_type)
		{
		case MapGridConstants.RESIDENTIAL_TILE:
			return true;
		default:
			return false;
		}
	}
	
	public boolean hasPropertyTax() {
		switch (tile_type)
		{
		case MapGridConstants.COMMERCIAL_TILE:
		case MapGridConstants.INDUSTRIAL_TILE:
		case MapGridConstants.SOLAR_TILE:
		case MapGridConstants.COAL_TILE:
		case MapGridConstants.GAS_TILE:
		case MapGridConstants.SEWAGE_TILE:
		case MapGridConstants.WIND_TILE:
		case MapGridConstants.RESIDENTIAL_TILE:
		case MapGridConstants.POLICE_TILE:
			return true;
		default:
			return false;
		}
	}
	
	public boolean hasSalesTax() {
		switch (tile_type)
		{
		case MapGridConstants.COMMERCIAL_TILE:
			return true;
		default:
			return false;
		}
	}
	
	private TileMetrics(int tileType) {
		tile_type = tileType;
		metricsContributed = new int[MapGridConstants.METRIC_LAST];
		switch (tileType) {
			case MapGridConstants.BULLDOZE_TILE :
				description = "Bulldozer";
				price = 10;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				monthlyCost = 0;
				break;
			case MapGridConstants.RESIDENTIAL_TILE :
				description = "Provides housing for residents.";
				price = 250;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
				jobs = -100;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 1;
				powerConsumed = 30;
				waterConsumed = 15;
				monthlyCost = -20;
				break;
			case MapGridConstants.INDUSTRIAL_TILE :
				description = "Provides more jobs per dollar for residents than Commercial, but causes more crime, pollution, and unhappiness.";
				price = 450;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -5;
				jobs = 75;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 7;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 7;
				powerConsumed = 75;
				waterConsumed = 35;
				monthlyCost = -40;
				break;
			case MapGridConstants.COMMERCIAL_TILE :
				description = "Provides jobs for residents.  More expensive per job than Industrial, but causes less crime and pollution.";
				price = 350;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 2;
				jobs = 35;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 2;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 3;
				powerConsumed = 35;
				waterConsumed = 25;
				monthlyCost = -30;
				break;
			case MapGridConstants.DIRT_TILE :
				description = "Undeveloped dirt.  When a tile is bulldozed, it reverts to this.";
				price = 10;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				break;
			case MapGridConstants.PARK_TILE :
				description = "Greatly increases happiness and reduces pollution";
				price = 100;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 8;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = -3;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 10;
				waterConsumed = 5;
				monthlyCost = 5;
				break;
			case MapGridConstants.WATER_TILE :
				description = "";
				price = 0;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 10;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				break;
			case MapGridConstants.GRASS_TILE :
				description = "Provides a moderate increase in happiness and slightly lowers pollution.";
				price = 25;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 5;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = -2;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				break;
			case MapGridConstants.FORREST_TILE :
				description = "Provides a small happiness increase, but greatly reduces pollution.";
				price = 200;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 4;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = -5;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				break;
			case MapGridConstants.SEWAGE_TILE :
				description = "Provides water for the city.  Greatly lowers happiness (Nobody wants to live near a sewage treatment plant!).";
				price = 1000;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -8;
				jobs = 20;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 2;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 35;
				waterConsumed = -2500;
				monthlyCost = 40;
				break;
			case MapGridConstants.POLICE_TILE :
				description = "Lowers crime in the area.";
				price = 1000;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 2;
				jobs = 15;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = -10;
				powerConsumed = 15;
				waterConsumed = 10;
				monthlyCost = 40;
				break;
			case MapGridConstants.SOLAR_TILE :
				description = "An expensive, but clean source of electricity.";
				price = 2500;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -2;
				jobs = 10;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = -1000;
				waterConsumed = 0;
				monthlyCost = 100;
				break;
			case MapGridConstants.GAS_TILE :
				description = "Somewhat clean source of power and a moderate cost.";
				price = 3500;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -4;
				jobs = 50;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 3;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = -5000;
				waterConsumed = 25;
				monthlyCost = 150;
				break;
			case MapGridConstants.COAL_TILE :
				description = "A cheap source of electricity, but produces horrible smog.";
				price = 4000;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -10;
				jobs = 50;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 10;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = -5000;
				waterConsumed = 25;
				monthlyCost = 150;
				break;
			case MapGridConstants.WIND_TILE :
				description = "A clean source of power, but residents don't like the sight of them.";
				price = 2500;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -3;
				jobs = 20;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = -1000;
				waterConsumed = 0;
				monthlyCost = 100;
				break;
			case MapGridConstants.BEACH_TILE :
				price = 0;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 8;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				break;
			case MapGridConstants.ROAD_TILE :
				description = "Required for residents to get to their jobs.";
				price = 20;
				metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
				jobs = 0;
				metricsContributed[MapGridConstants.METRIC_POLLUTION] = 1;
				metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
				powerConsumed = 0;
				waterConsumed = 0;
				monthlyCost = 3;
				break;
		}
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @return the monthlyCost
	 */
	public int getMonthlyCost() {
		return monthlyCost;
	}

	/**
	 * @return the metricsContributed
	 */
	public int[] getMetricsContributed() {
		return metricsContributed;
	}

	/**
	 * @return the jobs
	 */
	public int getJobs() {
		return jobs;
	}

	/**
	 * @return the powerConsumed
	 */
	public int getPowerConsumed() {
		return powerConsumed;
	}

	/**
	 * @return the waterConsumed
	 */
	public int getWaterConsumed() {
		return waterConsumed;
	}

}