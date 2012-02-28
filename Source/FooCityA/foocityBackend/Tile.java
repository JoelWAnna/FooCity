package foocityBackend;

class Tile
{
	/*public String description;
	public int price;
	public int monthlyCost;
	//public int metricsContributed[];*/
	public int metricsActual[];
	/*public int jobs;
	public int powerConsumed;
	public int waterConsumed;*/
	public boolean employed;
	public int variation;
	private char tileChar;
	protected int tileInt;
	protected boolean replaceable;


	
	protected Tile(char tileChar)
	{
		for (int i = 0; i < MapGridConstants.CHAR_TILES.length; i++) {
			if (tileChar == MapGridConstants.CHAR_TILES[i])
				tileInt = i;
		}
		
		this.tileChar = tileChar;
		metricsActual = new int[3];
		//metricsContributed = new int[3];
		setValues();
	}
	
	// TODO: return this to protected, currently needed for FooCityGUI to get tile price/description
	public Tile(int tileType){
		this.tileInt = tileType;
		this.tileChar = MapGridConstants.CHAR_TILES[tileType];
		metricsActual = new int[3];
	//	metricsContributed = new int[3];
		setValues();
	}
	
	public void resetMetrics(){

		metricsActual[MapGridConstants.METRIC_POLLUTION] = 0;
		metricsActual[MapGridConstants.METRIC_CRIME] = 0;
		metricsActual[MapGridConstants.METRIC_HAPPINESS] = 0;
	}
	
	private void setValues(){
		replaceable = true;
		this.resetMetrics();
		//monthlyCost = 0;
		employed = true;
		switch (tileInt){
	/*	case MapGridConstants.RESIDENTIAL_TILE:
			description = "Provides housing for residents.";
			price = 250;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
			jobs = -100;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 1;
			powerConsumed = 30;
			waterConsumed = 15;
			monthlyCost = -10;
			break;
		case MapGridConstants.INDUSTRIAL_TILE:
			description = "Provides more jobs per dollar for residents than Commercial, but causes more crime, pollution, and unhappiness.";
			price = 450;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -5;
			jobs = 75;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 7;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 7;
			powerConsumed = 75;
			waterConsumed = 35;
			monthlyCost = -20;
			break;
		case MapGridConstants.COMMERCIAL_TILE:
			description = "Provides jobs for residents.  More expensive per job than Industrial, but causes less crime and pollution.";
			price = 350;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 2;
			jobs = 35;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 2;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 3;
			powerConsumed = 35;
			waterConsumed = 25;
			monthlyCost = -15;
			break;
		case MapGridConstants.DIRT_TILE:
			description = "Undeveloped dirt.  When a tile is bulldozed, it reverts to this.";
			price = 10;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 2;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.PARK_TILE:
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
*/		case MapGridConstants.WATER_TILE:
//			description = "";
			replaceable = false;
/*			price = 0;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 10;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.GRASS_TILE:
			description = "Provides a moderate increase in happiness and slightly lowers pollution.";
			price = 25;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 5;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = -2;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.FORREST_TILE:
			description = "Provides a small happiness increase, but greatly reduces pollution.";
			price = 200;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 4;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = -5;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.SEWAGE_TILE:
			description = "Provides water for the city.  Greatly lowers happiness (Nobody wants to live near a sewage treatment plant!).";
			price = 1000;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -8;
			jobs = 20;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 2;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 35;
			waterConsumed = -2500;
			monthlyCost = 50;
			break;
		case MapGridConstants.POLICE_TILE:
			description = "Lowers crime in the area.";
			price = 1000;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 2;
			jobs = 15;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = -10;
			powerConsumed = 15;
			waterConsumed = 10;
			monthlyCost = 50;
			break;
		case MapGridConstants.SOLAR_TILE:
			description = "An expensive, but clean source of electricity.";
			price = 2500;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -2;
			jobs = 10;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = -1000;
			waterConsumed = 0;
			monthlyCost = 125;
			break;
		case MapGridConstants.GAS_TILE:
			description = "Somewhat clean source of power and a moderate cost.";
			price = 3500;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -2;
			jobs = 50;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 3;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = -5000;
			waterConsumed = 25;
			monthlyCost = 175;
			break;
		case MapGridConstants.COAL_TILE:
			description = "A cheap source of electricity, but produces horrible smog.";
			price = 4000;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -10;
			jobs = 50;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 10;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = -5000;
			waterConsumed = 25;
			monthlyCost = 200;
			break;
		case MapGridConstants.WIND_TILE:
			description = "A clean source of power, but residents don't like the sight of them.";
			price = 2500;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = -3;
			jobs = 20;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = -1000;
			waterConsumed = 0;
			monthlyCost = 125;
			break;
*/		case MapGridConstants.BEACH_TILE:
			replaceable = false;
/*			price = 0;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 8;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 0;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.ROAD_TILE:
			description = "Required for residents to get to their jobs.";
			price = 20;
			metricsContributed[MapGridConstants.METRIC_HAPPINESS] = 0;
			jobs = 0;
			metricsContributed[MapGridConstants.METRIC_POLLUTION] = 1;
			metricsContributed[MapGridConstants.METRIC_CRIME] = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			monthlyCost = 5;
			break;
*/		}
	}

	public boolean isReplaceable()
	{
		return replaceable;
	}

	public char getTileChar()
	{
		return tileChar;
	}
	
	public int getTileInt()
	{
		return tileInt;
	}

}

