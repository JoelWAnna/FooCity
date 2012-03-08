package foocityFrontend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import foocityBackend.MapGridConstants;

class TileLoader {
	private boolean m_valid;

	private String tileNames[];
	private BufferedImage tiles[];
	private String TileSet;
	private BufferedImage road;

	public TileLoader() {
		initStringNames();
		tiles = new BufferedImage[MapGridConstants.LAST_TILE];
		m_valid = true;

		TileSet = "images/" + "SolidColors/";
		for (int i = 1; i < MapGridConstants.LAST_TILE; ++i) {
			try {
				tiles[i] = ImageIO.read(new File(TileSet + tileNames[i]
						+ ".png"));
			} catch (Exception e) {
				System.out.println(TileSet + tileNames[i] + ".png");
				// m_valid = false;
			}
		}

		try {
			road = ImageIO.read(new File("images/road.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_valid = true;
	}

	public BufferedImage getTile(int i) {
		switch (i) {
			case (MapGridConstants.ROAD_TILE) :
				return road;
			default :
				if (!m_valid && (i < 0 || i > tiles.length))
					return null;
		}

		return tiles[i];
	}

	private void initStringNames() {
		tileNames = new String[MapGridConstants.LAST_TILE];
		tileNames[MapGridConstants.WATER_TILE] = "water";
		tileNames[MapGridConstants.BEACH_TILE] = "beach";
		tileNames[MapGridConstants.GRASS_TILE] = "grass";
		tileNames[MapGridConstants.DIRT_TILE] = "dirt";
		tileNames[MapGridConstants.FORREST_TILE] = "forrest";
		tileNames[MapGridConstants.INDUSTRIAL_TILE] = "industrial";
		tileNames[MapGridConstants.COMMERCIAL_TILE] = "commercial";
		tileNames[MapGridConstants.PARK_TILE] = "park";
		tileNames[MapGridConstants.SEWAGE_TILE] = "sewage";
		tileNames[MapGridConstants.POLICE_TILE] = "police";
		tileNames[MapGridConstants.SOLAR_TILE] = "solar";
		tileNames[MapGridConstants.GAS_TILE] = "gas";
		tileNames[MapGridConstants.COAL_TILE] = "coal";
		tileNames[MapGridConstants.WIND_TILE] = "wind";
		tileNames[MapGridConstants.RESIDENTIAL_TILE] = "residential";
		tileNames[MapGridConstants.ROAD_TILE] = "road";
	}

}