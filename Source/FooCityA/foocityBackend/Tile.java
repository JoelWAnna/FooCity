package foocityBackend;

public class Tile {
	public int metricsActual[];
	public boolean employed;
	private int variation;

	private char tileChar;
	protected int tileInt;
	protected boolean replaceable;
	private boolean has_variations;

	protected Tile(char tileChar) {
		for (int i = 0; i < MapGridConstants.CHAR_TILES.length; i++) {
			if (tileChar == MapGridConstants.CHAR_TILES[i])
				tileInt = i;
		}

		this.tileChar = tileChar;
		metricsActual = new int[3];
		setValues();
	}

	// TODO: return this to protected, currently needed for FooCityGUI to get
	// tile price/description
	protected Tile(int tileType) {
		this.tileInt = tileType;
		this.tileChar = MapGridConstants.CHAR_TILES[tileType];
		metricsActual = new int[3];
		setValues();
	}

	public void resetMetrics() {

		metricsActual[MapGridConstants.METRIC_POLLUTION] = 0;
		metricsActual[MapGridConstants.METRIC_CRIME] = 0;
		metricsActual[MapGridConstants.METRIC_HAPPINESS] = 0;
	}
	
	private static boolean hasPermanentVariations(int tileType) {
		return tileType != MapGridConstants.ROAD_TILE;
	}

	public static boolean isNaturalTile(int tileType) {
		if (tileType >= MapGridConstants.WATER_TILE && tileType <= MapGridConstants.FORREST_TILE)
			return true;
		return false;
	}

	private void setValues() {
		replaceable = true;
		this.resetMetrics();
		employed = true;
		has_variations = false;
		switch (tileInt) {
			case MapGridConstants.ROAD_TILE :
				has_variations = true;
				break;
			case MapGridConstants.WATER_TILE :
				replaceable = false;
				break;
			case MapGridConstants.BEACH_TILE :
				replaceable = false;
				break;
		}
		
		if (isNaturalTile(tileInt) || tileInt == MapGridConstants.RESIDENTIAL_TILE) {
			has_variations = true;
			variation = ((int)(Math.random()*16));
		}
	}

	public boolean isReplaceable() {
		return replaceable;
	}

	public char getTileChar() {
		return tileChar;
	}

	public int getTileInt() {
		return tileInt;
	}

	public boolean hasVariations() {
		return has_variations;
	}
	
	public void setVariation(int variation) {
		if (!hasPermanentVariations(tileInt))
			this.variation = variation;
	}

	public int getVariation() {
		return this.variation;
	}

}
