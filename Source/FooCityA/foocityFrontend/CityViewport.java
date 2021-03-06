package foocityFrontend;
// Project FooCity-group2
// CS300
// Developers: Joel Anna and David Wiza
//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

import foocityBackend.FooCityManager;
import foocityBackend.MapGridConstants;
import foocityBackend.Tile;

class CityViewport extends JPanel {
	FooCityGUIInterface Interface;
	private TileLoader tileLoader;
	private Point cursor;
	private Point selected_tile;
	private Dimension map_area;
	private Dimension preferred_size;
	private boolean showgrid;

	public CityViewport(FooCityGUIInterface i) {
		super();
		Interface = i;
		tileLoader = new TileLoader();
		cursor = null;
		map_area = new Dimension(MapGridConstants.MAP_WIDTH,
				MapGridConstants.MAP_HEIGHT);
		preferred_size = new Dimension((int) map_area.getWidth()
				* FooCityGUIConstants.TILE_WIDTH, (int) map_area.getHeight()
				* FooCityGUIConstants.TILE_HEIGHT);

		if (Interface != null) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					placeTile(e.getPoint(), true);
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					setMousePoint(null);
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent arg0) {
					setMousePoint(arg0.getPoint());
				}
				
				@Override
				public void mouseDragged(MouseEvent arg0) {
					int tileType = Interface.getCityManager().getPlacingTile();
					placeTile(arg0.getPoint(), (Tile.isNaturalTile(tileType) || tileType == MapGridConstants.ROAD_TILE));
				}
			});
		}
	}

	protected void placeTile(Point point, boolean place) {
	selected_tile = point;
	selected_tile.x /= FooCityGUIConstants.TILE_WIDTH;
	selected_tile.y /= FooCityGUIConstants.TILE_HEIGHT;
	// System.out.print(p + " " + x + " " + y);
	if (place && Interface.getCityManager().getPlacingTile() != 0)
		Interface.getCityManager().placeTile(selected_tile);
	Interface.updateSelectedTile(selected_tile);
	Interface.updateDisplay();
		
	}

	public Dimension getPreferredSize() {
		return (Dimension) preferred_size.clone();
	}

	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		if (Interface == null || tileLoader == null)
			return;
		Graphics2D g = (Graphics2D) g1;
		FooCityManager city_manager = Interface.getCityManager();
		if (city_manager == null)
			return;

		if (!city_manager.MapGridLoaded()) {
			selected_tile = null;
			return;
		}

		map_area = city_manager.getMapArea();
		if (map_area == null)
			return;
		preferred_size = new Dimension((int) map_area.getWidth()
				* FooCityGUIConstants.TILE_WIDTH, (int) map_area.getHeight()
				* FooCityGUIConstants.TILE_HEIGHT);

		Rectangle r = this.getVisibleRect();
		g.clearRect(r.x, r.y, r.width, r.height);

		// System.out.print("visiblerect = " + r +"\n");
		g.setColor(Color.BLACK);
		for (int y = 0; y < map_area.getHeight(); ++y) {
			int yCoord = y * FooCityGUIConstants.TILE_HEIGHT;
			// render to the entire area plus 1 tile extra to eliminate the
			// tearing when rapidly scrolling
			if ((yCoord < r.y - FooCityGUIConstants.TILE_HEIGHT)
					|| (yCoord >= r.y + r.height
							+ FooCityGUIConstants.TILE_HEIGHT))
				continue;
			for (int x = 0; x < map_area.getWidth(); ++x) {
				int xCoord = x * FooCityGUIConstants.TILE_WIDTH;
				// render to the entire area plus 1 tile extra to eliminate the
				// tearing when rapidly scrolling
				if ((xCoord < r.x - FooCityGUIConstants.TILE_WIDTH)
						|| (xCoord >= r.x + r.width
								+ FooCityGUIConstants.TILE_WIDTH))
					continue;
				BufferedImage bI = null;
				int tileType = city_manager.getTileInt(x, y);
				int variation = city_manager.getTileVariation(x, y);
				try {
					bI = tileLoader.getTile(tileType).getSubimage(
							variation * FooCityGUIConstants.TILE_WIDTH, 0,
							FooCityGUIConstants.TILE_WIDTH,
							FooCityGUIConstants.TILE_HEIGHT);
				} catch (RasterFormatException e) {
					try {
						bI = tileLoader.getTile(tileType).getSubimage(0, 0,
								FooCityGUIConstants.TILE_WIDTH,
								FooCityGUIConstants.TILE_HEIGHT);
					} catch (RasterFormatException e1) {
						bI = null;
					}
				}
				if (bI != null)
					g.drawImage(bI, xCoord, yCoord, null);

				if (showgrid)
					g.drawLine(xCoord, yCoord, xCoord, yCoord + FooCityGUIConstants.TILE_HEIGHT);
			}
			if (showgrid)
				g.drawLine(r.x, yCoord, r.x + (int) map_area.getWidth()	* FooCityGUIConstants.TILE_WIDTH, yCoord);

			if (selected_tile != null) {
				g.setColor(Color.WHITE);
				g.drawRect(selected_tile.x * FooCityGUIConstants.TILE_WIDTH,
						selected_tile.y * FooCityGUIConstants.TILE_HEIGHT,
						FooCityGUIConstants.TILE_WIDTH,
						FooCityGUIConstants.TILE_HEIGHT);
				g.setColor(Color.BLACK);
			}
		}

		int mouse_tile = city_manager.getPlacingTile();
		if (cursor != null && mouse_tile != 0) {
			BufferedImage mImage = null;
			try {
				mImage = tileLoader.getTile(mouse_tile);
				if (mImage != null)
					mImage = mImage.getSubimage(0, 0,
						FooCityGUIConstants.TILE_WIDTH,
						FooCityGUIConstants.TILE_HEIGHT);
			} catch (RasterFormatException e) {
				mImage = null;
			}

			if (mImage != null) {
				final float[] scales = {1f, 1f, 1f, 0.5f};
				final float[] offsets = new float[4];
				final RescaleOp rop = new RescaleOp(scales, offsets, null);
				g.drawImage(mImage, rop, cursor.x
						& ~(FooCityGUIConstants.TILE_WIDTH - 1),
						(cursor.y & ~(FooCityGUIConstants.TILE_HEIGHT - 1)));
			}
		}

	}

	private void setMousePoint(Point point) {
		this.cursor = point;
		repaint();
	}

	public void enableGrid(boolean enable) {
		this.showgrid = enable;
	}
}

class MiniMapPanel extends JPanel {
	private int viewMode;
	FooCityGUIInterface Interface;
	private Dimension map_area;
	private int scale;

	public MiniMapPanel(FooCityGUIInterface i, int _scale) {
		super();
		Interface = i;
		scale = _scale;
		if (scale < 1 || scale > 10)
			scale = 2;
		if (Interface != null) {
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					Point point = new Point(arg0.getX() / scale, arg0.getY()
							/ scale);
					Interface.updateDisplayCenter(point);
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent arg0) {
					Point point = new Point(arg0.getX() / scale, arg0.getY()
							/ scale);
					Interface.updateDisplayCenter(point);
				}
			});
		}
	}

	public void setViewMode(int newMode) {
		this.viewMode = newMode;
		this.repaint();
	}

	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		if (Interface == null)
			return;
		FooCityManager city_manager = Interface.getCityManager();

		if (city_manager == null)
			return;
		map_area = city_manager.getMapArea();
		if (map_area == null)
			return;
		if (!city_manager.MapGridLoaded())
			return;
		if (viewMode == 0) { // Normal view
			for (int y = 0; y < map_area.getHeight(); y++) {
				for (int x = 0; x < map_area.getWidth(); x++) {
					switch (city_manager.getTileInt(x, y)) {
						case (MapGridConstants.BEACH_TILE) :
							g1.setColor(new Color(255, 225, 0)); // A yellowish
																	// sandy
																	// color
							break;
						case (MapGridConstants.WATER_TILE) :
							g1.setColor(new Color(0, 0, 180)); // A deep ocean
																// blue
							break;
						case (MapGridConstants.GRASS_TILE) :
							g1.setColor(new Color(0, 180, 0)); // A somewhat
																// dark green
							break;
						case (MapGridConstants.COAL_TILE) :
							g1.setColor(new Color(50, 50, 50)); // Very dark
																// grey
							break;
						case (MapGridConstants.COMMERCIAL_TILE) :
							g1.setColor(new Color(0, 0, 255)); // Vivid blue
							break;
						case (MapGridConstants.DIRT_TILE) :
							g1.setColor(new Color(140, 110, 0)); // Brown
							break;
						case (MapGridConstants.FORREST_TILE) :
							g1.setColor(new Color(0, 75, 0)); // Deep forest
																// green
							break;
						case (MapGridConstants.INDUSTRIAL_TILE) : // Dirty
																	// yellow
							g1.setColor(new Color(225, 225, 0));
							break;
						case (MapGridConstants.GAS_TILE) :
							g1.setColor(new Color(96, 112, 204)); // Pale blue
							break;
						case (MapGridConstants.PARK_TILE) :
							g1.setColor(new Color(100, 255, 100)); // Pale green
							break;
						case (MapGridConstants.POLICE_TILE) :
							g1.setColor(new Color(0, 0, 200)); // Deep blue
							break;
						case (MapGridConstants.RESIDENTIAL_TILE) :
							g1.setColor(new Color(0, 255, 0)); // Bright green
							break;
						case (MapGridConstants.SEWAGE_TILE) :
							g1.setColor(new Color(110, 72, 20)); // Dark murkey
																	// brown
							break;
						case (MapGridConstants.SOLAR_TILE) :
							g1.setColor(new Color(255, 244, 128)); // Bright
																	// yellow
							break;
						case (MapGridConstants.WIND_TILE) :
							g1.setColor(new Color(0, 200, 255)); // Sky blue
							break;
						case (MapGridConstants.ROAD_TILE) :
							g1.setColor(new Color(139, 136, 136)); // dark grey
							break;
						default :
							g1.setColor(Color.green);
							System.err.print("Unknown tile drawn on minimap\n");
							break;
					}
					g1.fillRect(x * this.scale, y * this.scale, this.scale,
							this.scale);
				}
			}
		} else if (viewMode == 1) { // Pollution
			for (int y = 0; y < map_area.getHeight(); y++) {
				for (int x = 0; x < map_area.getWidth(); x++) {
					int c = city_manager.getTileMetrics(x, y,
							MapGridConstants.METRIC_POLLUTION) * 10;
					if (c > 255)
						c = 255;
					if (c < 0)
						c = 0;
					g1.setColor(new Color(c, c, c));
					g1.fillRect(x * this.scale, y * this.scale, this.scale,
							this.scale);
				}
			}
		} else if (viewMode == 2) { // Crime
			for (int y = 0; y < map_area.getHeight(); y++) {
				for (int x = 0; x < map_area.getWidth(); x++) {
					int c = city_manager.getTileMetrics(x, y,
							MapGridConstants.METRIC_CRIME) * 10;
					if (c > 255)
						c = 255;
					if (c < 0)
						c = 0;
					g1.setColor(new Color(c, c, c));
					g1.fillRect(x * this.scale, y * this.scale, this.scale,
							this.scale);
				}
			}
		} else if (viewMode == 3) { // Happiness
			for (int y = 0; y < map_area.getHeight(); y++) {
				for (int x = 0; x < map_area.getWidth(); x++) {
					int c = city_manager.getTileMetrics(x, y,
							MapGridConstants.METRIC_HAPPINESS);
					if (c > 255)
						c = 255;
					if (c < 0)
						c = 0;
					g1.setColor(new Color(c, c, c));
					g1.fillRect(x * this.scale, y * this.scale, this.scale,
							this.scale);
				}
			}
		}
		// Draw the frame around our current view
		Rectangle r = Interface.getViewRect();
		Point p = Interface.getViewPoint();
		if (r != null && p != null) {
			// System.out.print("visiblerect = " + r +"\n");
			r.x = this.scale * p.x / FooCityGUIConstants.TILE_WIDTH;
			r.y = this.scale * p.y / FooCityGUIConstants.TILE_HEIGHT;
			r.width = this.scale * r.width / FooCityGUIConstants.TILE_WIDTH;
			r.height = this.scale * r.height / FooCityGUIConstants.TILE_HEIGHT;
			g1.setColor(Color.black);
			g1.drawRect(r.x, r.y, r.width, r.height);
		}
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(scale * 128, scale * 128);
	}

}