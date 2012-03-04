package foocityBackend;

class Tile {
	public int metricsActual[];
	public boolean employed;
	public int variation;
	private char tileChar;
	protected int tileInt;
	protected boolean replaceable;

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

	private void setValues() {
		replaceable = true;
		this.resetMetrics();
		employed = true;
		switch (tileInt) {
			case MapGridConstants.WATER_TILE :
				replaceable = false;
				break;
			case MapGridConstants.BEACH_TILE :
				replaceable = false;
				break;
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

}
