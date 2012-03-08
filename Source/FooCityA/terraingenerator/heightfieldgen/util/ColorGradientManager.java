package terraingenerator.heightfieldgen.util;

import java.awt.Color;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class helps in organizing color gradient Color arrays. Each array has
 * 256 values, which can be used for various graphical user interface
 * applications.
 * 
 * @author Nick Aschenbach (nick.aschenbach@gmail.com)
 * @version November 29, 2009
 */
public class ColorGradientManager {
	private Map<String, Color[]> my_gradients;
	private Color[] my_selection;

	/**
	 * Initialize the ColorGradientManager by loading references to all color
	 * gradients into the manager.
	 */
	public ColorGradientManager() {
		my_gradients = new TreeMap<String, Color[]>();

		// Add ColorGradients1
		my_gradients.put("Abstract 1", ColorGradients1.abstract1);
		my_gradients.put("Abstract 2", ColorGradients1.abstract2);
		my_gradients.put("Abstract 3", ColorGradients1.abstract3);
		my_gradients.put("Aneurism", ColorGradients1.aneurism);
		my_gradients.put("Blinds", ColorGradients1.blinds);
		my_gradients.put("Blue Green", ColorGradients1.blue_green);
		my_gradients.put("Toxic Wasteland", ColorGradients1.toxic_wasteland);
		my_gradients.put("Brushed Aluminum", ColorGradients1.brushed_aluminum);
		my_gradients.put("Burnt Paper", ColorGradients1.burnt_paper);
		my_gradients.put("Carribean Blues", ColorGradients1.carribean_blues);
		my_gradients.put("Compact Disc", ColorGradients1.cd);
		my_gradients.put("Cold Steel 1", ColorGradients1.cold_steel1);
		my_gradients.put("Cold Steel 2", ColorGradients1.cold_steel2);

		// Add ColorGradients2
		my_gradients.put("Land Sea", ColorGradients2.land_sea);
		my_gradients.put("Land Sky", ColorGradients2.land_sky);
		my_gradients.put("Incandescent", ColorGradients2.incandescent);
		my_gradients.put("Horizon", ColorGradients2.horizon);
		my_gradients.put("Greens", ColorGradients2.greens);
		my_gradients.put("Golden", ColorGradients2.golden);
		my_gradients.put("Horizon", ColorGradients2.horizon);
		my_gradients.put("German Flag", ColorGradients2.german_flag);
		my_gradients.put("Full Saturation", ColorGradients2.full_saturation);
		my_gradients.put("French Flag", ColorGradients2.french_flag);
		my_gradients.put("Flare Glow 1", ColorGradients2.flare_glow1);
		my_gradients.put("Flare Glow 2", ColorGradients2.flare_glow2);
		my_gradients.put("Deep Sea", ColorGradients2.deep_sea);
		my_gradients.put("Dark", ColorGradients2.dark);

		// Add ColorGradients3
		my_gradients.put("HSV Hues", ColorGradients3.HSV_hues);
		my_gradients.put("Blue Sky", ColorGradients3.blue_sky);
		my_gradients.put("Yellow Orange", ColorGradients3.yellow_orange);
		my_gradients.put("Tropical", ColorGradients3.tropical);
		my_gradients.put("Sunrise", ColorGradients3.sunrise);
		my_gradients.put("Skyline", ColorGradients3.skyline);
		my_gradients.put("Romanian Flag", ColorGradients3.romanian_flag);
		my_gradients.put("Purple", ColorGradients3.purples);
		my_gradients.put("Pastels", ColorGradients3.pastels);
		my_gradients.put("Mexican Flag", ColorGradients3.mexican_flag);
		my_gradients.put("Metallic", ColorGradients3.metallic);
		my_gradients.put("Greyscale", ColorGradients3.greyscale);

		// Select initial color
		my_selection = my_gradients.get("Land Sea");
	}

	/**
	 * @return A reference to the currently selected item.
	 */
	public Color[] gradient() {
		return my_selection;
	}

	/**
	 * @return A reference to the key of all colors.
	 */
	public Set getAllGradients() {
		return my_gradients.keySet();
	}

	/**
	 * Set the current active color gradient.
	 * 
	 * @param the_gradient
	 *            The name of the new color gradient.
	 */
	public void select(final String the_gradient) {
		if (my_gradients.containsKey(the_gradient)) {
			my_selection = my_gradients.get(the_gradient);
		} else {
			throw new IllegalArgumentException();
		}
	}
}
