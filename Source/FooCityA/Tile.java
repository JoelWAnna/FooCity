
public class Tile
{
	public int price;
	public int happinessContributed, happinessActual;
	public int jobs;
	public int pollutionContributed, pollutionActual;
	public int crimeContributed, crimeActual;
	public int powerConsumed;
	public int waterConsumed;
	public boolean employed;
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
		setValues();
	}
	protected Tile(int tileType){
		this.tileInt = tileType;
		this.tileChar = MapGridConstants.CHAR_TILES[tileType];
		setValues();
	}
	
	private void setValues(){
		replaceable = true;
		happinessActual = 0;
		crimeActual = 0;
		employed = false;
		switch (tileInt){
		case MapGridConstants.RESIDENTIAL_TILE:
			price = 250;
			happinessContributed = 0;
			jobs = -100;
			pollutionContributed = 0;
			crimeContributed = 1;
			powerConsumed = 30;
			waterConsumed = 15;
			break;
		case MapGridConstants.INDUSTRIAL_TILE:
			price = 450;
			happinessContributed = -5;
			jobs = 75;
			pollutionContributed = 7;
			crimeContributed = 7;
			powerConsumed = 75;
			waterConsumed = 35;
			break;
		case MapGridConstants.COMMERCIAL_TILE:
			price = 350;
			happinessContributed = 2;
			jobs = 35;
			pollutionContributed = 2;
			crimeContributed = 3;
			powerConsumed = 35;
			waterConsumed = 25;
			break;
		case MapGridConstants.DIRT_TILE:
			price = 10;
			happinessContributed = 2;
			jobs = 0;
			pollutionContributed = 0;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.PARK_TILE:
			price = 100;
			happinessContributed = 8;
			jobs = 0;
			pollutionContributed = -3;
			crimeContributed = 0;
			powerConsumed = 10;
			waterConsumed = 5;
			break;
		case MapGridConstants.WATER_TILE:
			replaceable = false;
			price = 0;
			happinessContributed = 10;
			jobs = 0;
			pollutionContributed = 0;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.GRASS_TILE:
			price = 25;
			happinessContributed = 5;
			jobs = 0;
			pollutionContributed = -2;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.FORREST_TILE:
			price = 200;
			happinessContributed = 4;
			jobs = 0;
			pollutionContributed = -5;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.SEWAGE_TILE:
			price = 1000;
			happinessContributed = -8;
			jobs = 20;
			pollutionContributed = 2;
			crimeContributed = 0;
			powerConsumed = 35;
			waterConsumed = -2500;
			break;
		case MapGridConstants.POLICE_TILE:
			price = 1000;
			happinessContributed = 2;
			jobs = 15;
			pollutionContributed = 0;
			crimeContributed = -10;
			powerConsumed = 15;
			waterConsumed = 10;
			break;
		case MapGridConstants.SOLAR_TILE:
			price = 2500;
			happinessContributed = -2;
			jobs = 10;
			pollutionContributed = 0;
			crimeContributed = 0;
			powerConsumed = -1000;
			waterConsumed = 0;
			break;
		case MapGridConstants.GAS_TILE:
			price = 3500;
			happinessContributed = -2;
			jobs = 50;
			pollutionContributed = 3;
			crimeContributed = 0;
			powerConsumed = -5000;
			waterConsumed = 25;
			break;
		case MapGridConstants.COAL_TILE:
			price = 4000;
			happinessContributed = -10;
			jobs = 50;
			pollutionContributed = 10;
			crimeContributed = 0;
			powerConsumed = -5000;
			waterConsumed = 25;
			break;
		case MapGridConstants.WIND_TILE:
			price = 2500;
			happinessContributed = -3;
			jobs = 20;
			pollutionContributed = 0;
			crimeContributed = 0;
			powerConsumed = -1000;
			waterConsumed = 0;
			break;
		case MapGridConstants.BEACH_TILE:
			replaceable = false;
			price = 0;
			happinessContributed = 8;
			jobs = 0;
			pollutionContributed = 0;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		case MapGridConstants.ROAD_TILE:
			price = 40;
			happinessContributed = 0;
			jobs = 0;
			pollutionContributed = 1;
			crimeContributed = 0;
			powerConsumed = 0;
			waterConsumed = 0;
			break;
		}
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