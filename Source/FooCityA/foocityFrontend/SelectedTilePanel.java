package foocityFrontend;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import foocityBackend.MapGridConstants;
import foocityBackend.TileMetrics;
import java.awt.FlowLayout;


class SelectedTilePanel extends JPanel {
	private FooCityGUIInterface _interface;
	private Point selectedTile;
	private JLabel tile_coordinates;
	private JLabel tile_happiness;
	private JLabel tile_pollution;
	private JLabel tile_crime;
	private JLabel tile_monthlyCost;
	private JLabel tile_jobs;
	private JLabel tile_power;
	private JLabel tile_water;
	private JPanel graphicPanel;
	private JLabel label;
	SelectedTilePanel(FooCityGUIInterface i) {
		super();
		if (i != null)
		{
			this._interface = i;
		}
		setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		graphicPanel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension (32, 32);
			}
			public Dimension getMinimumSize() {
				return new Dimension (32, 32);
			}
			@Override
			public void paint(Graphics g) {
				final TileLoader t = new TileLoader();
				BufferedImage tile = null;
				if (selectedTile != null)
				{
					int tileType = _interface.getCityManager().getTileInt(selectedTile.x, selectedTile.y);
					tile = t.getTile(tileType).getSubimage(0, 0, FooCityGUIConstants.TILE_WIDTH, FooCityGUIConstants.TILE_HEIGHT);

				}
				
				if (tile != null) {
					g.drawImage(tile, 0, 0, null);
				}
				else {
					/*g.clearRect(0, 32, 0, 32);
					tile = t.getTile(-1).getSubimage(0, 0, FooCityGUIConstants.TILE_WIDTH, FooCityGUIConstants.TILE_HEIGHT);
					if (tile != null){
						g.drawImage(tile, 0, 0, null);
					}*/
				}
			}
		};
		//Box info_panel = Box.createHorizontalBox();
		this.add(graphicPanel);
		Box TextBox = Box.createHorizontalBox();

		JPanel Text = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		tile_coordinates = new JLabel ();

		TextBox.add(tile_coordinates);
		tile_crime = new JLabel ();
		Text.add(tile_crime);
		tile_pollution = new JLabel ();
		Text.add(tile_pollution);
		tile_happiness = new JLabel ();
		Text.add(tile_happiness);
		tile_jobs = new JLabel ();
		Text.add(tile_jobs);
		tile_power = new JLabel ();
		Text.add(tile_power);
		tile_water = new JLabel ();
		Text.add(tile_water);
		tile_monthlyCost = new JLabel ();
		Text.add(tile_monthlyCost);
		TextBox.add(Text);
		this.add(TextBox);
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, FooCityGUIConstants.STATUSBAR_HEIGHT);
	}
	@Override
	public Dimension getMaximumSize() {
		return new Dimension(200, FooCityGUIConstants.STATUSBAR_HEIGHT);
	}
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(200, FooCityGUIConstants.STATUSBAR_HEIGHT);
	}
	
	public void clearSelectedTile() {
		updateSelectedTile(-1,-1);
	}
	public void updateSelectedTile(int x, int y) {
		if (x >= 0 && y >= 0) {
			selectedTile = new Point(x, y);
		}
		else {
			selectedTile = null;
		}
	
		updateTileText();

	}
	private void updateTileText() {
		if (selectedTile != null) {
			int x = selectedTile.x;
			int y = selectedTile.y;
			int crime = _interface.getCityManager().getTileMetrics(x, y, MapGridConstants.METRIC_CRIME);
			int happiness = _interface.getCityManager().getTileMetrics(x, y, MapGridConstants.METRIC_HAPPINESS);
			int pollution = _interface.getCityManager().getTileMetrics(x, y, MapGridConstants.METRIC_POLLUTION);
			tile_coordinates.setText(
					String.format("Selected Tile: (%1$3d,%2$3d)", x, y));
			tile_crime.setText(
					String.format("Crime: %1$3d", crime));
			tile_pollution.setText(
					String.format("Pollution: %1$3d", pollution));
			tile_happiness.setText(
					String.format("Happiness: %1$3d", happiness));
	
			TileMetrics tm = TileMetrics.GetTileMetrics(_interface.getCityManager().getTileInt(x, y));
			if (tm != null) {
				tile_monthlyCost.setText("Monthly Cost: " + tm.getMonthlyCost());
				int jobs = tm.getJobs();
				if (jobs > 0)
					tile_jobs.setText(
							String.format("Jobs: %1$3d", jobs));
				else
					tile_jobs.setText(
							String.format("Residents: %1$3d", -jobs));
				
				int power = tm.getPowerConsumed();
				if (power < 0)
					tile_power.setText(
							String.format("Power+: %1$3d", power));
				else
					tile_power.setText(
							String.format("Power-: %1$3d", -power));
				int water = tm.getWaterConsumed();
				if (water < 0)
					tile_water.setText(
							String.format("Water+: %1$3d", water));
				else
					tile_water.setText(
							String.format("Water-: %1$3d", -water));
			}
		} else {
			tile_coordinates.setText("");
			tile_crime.setText("");
			tile_pollution.setText("");
			tile_happiness.setText("");
	
			tile_monthlyCost.setText("");
			tile_jobs.setText("");
			tile_power.setText("");
			tile_water.setText("");
		}
		repaint();
		
	}
}