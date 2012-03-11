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

		TileSet = "images/" + "default/";
		for (int i = 0; i < MapGridConstants.LAST_TILE; ++i) {
			String resource_name = TileSet + tileNames[i] + ".png";
			try {
				tiles[i] = ImageIO.read(ClassLoader.getSystemResource(resource_name));
			} catch (IOException e) {
				System.out.println("Error resource '"+resource_name+"' not found");
			}
		}

		m_valid = true;
	}

	public BufferedImage getTile(int i) {
		switch (i) {
			case MapGridConstants.BULLDOZE_TILE:
				return tiles[0];
			default :
				if (!m_valid && (i < 0 || i > tiles.length))
					return null;
		}

		return tiles[i];
	}

	private void initStringNames() {
		tileNames = new String[MapGridConstants.LAST_TILE];
		tileNames[0] = "bulldozer";
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