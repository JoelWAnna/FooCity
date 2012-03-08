package terraingenerator.heightfieldgen.util;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to generate height fields of a given size.
 * 
 * @author Nick Aschenbach (nick.aschenbach@gmail.com)
 * @version November 26, 2009
 */
public class HeightFieldGenerator {
	// 2D Field holds all Z values for height field.
	private byte my_field[][];

	// This set holds copies of points that have been given values
	private Set<Point> my_points;

	// The size of the array (set after generate is run).
	private int my_size;

	// The scalar that reduces randomness through recursion.
	private double my_scalar;

	/**
	 * Generate the height field. The result is stored in my_field.
	 * 
	 * @param size
	 *            The size of the array will be 2^size + 1.
	 * @param H
	 *            The degree of randomness between generation.
	 */
	public void generate(final int size, final double H) {
		my_scalar = H;

		my_size = (int) Math.pow(2.0, (double) size) + 1;
		my_field = new byte[my_size][my_size];
		my_points = new HashSet<Point>();

		// Add points to field
		my_points.add(new Point(0, 0));
		my_points.add(new Point(0, my_size - 1));
		my_points.add(new Point(my_size - 1, 0));
		my_points.add(new Point(my_size - 1, my_size - 1));
		buildRecursively(my_size, 1.0);
	}

	// Average values TL, TR, BL, BR of this point.
	private byte aveTriangle(final int i, final int j, final int stride) {
		int iless = i - stride;
		int imore = i + stride;
		int jless = j - stride;
		int jmore = j + stride;
		// Fix iless
		if (iless < 0) {
			iless = iless + my_size;
		}
		if (iless > (my_size - 1)) {
			iless = iless - my_size;
		}
		// Fix imore
		if (imore < 0) {
			imore = imore + my_size;
		}
		if (imore > (my_size - 1)) {
			imore = imore - my_size;
		}
		// Fix jless
		if (jless < 0) {
			jless = jless + my_size;
		}
		if (jless > (my_size - 1)) {
			jless = jless - my_size;
		}
		// Fix jmore
		if (jmore < 0) {
			jmore = jmore + my_size;
		}
		if (jmore > (my_size - 1)) {
			jmore = jmore - my_size;
		}

		int result = (my_field[i][jless] + my_field[imore][j]
				+ my_field[i][jmore] + my_field[iless][j]) / 4;
		return (byte) result;
	}

	// Average values TL, TR, BL, BR of this point.
	private byte aveSquare(final int i, final int j, final int stride) {
		int iless = i - stride;
		int imore = i + stride;
		int jless = j - stride;
		int jmore = j + stride;
		// Fix iless
		if (iless < 0) {
			iless = iless + my_size;
		}
		if (iless > (my_size - 1)) {
			iless = iless - my_size - 1;
		}
		// Fix imore
		if (imore < 0) {
			imore = imore + my_size;
		}
		if (imore > (my_size - 1)) {
			imore = imore - my_size - 1;
		}
		// Fix jless
		if (jless < 0) {
			jless = jless + my_size;
		}
		if (jless > (my_size - 1)) {
			jless = jless - my_size - 1;
		}
		// Fix jmore
		if (jmore < 0) {
			jmore = jmore + my_size;
		}
		if (jmore > (my_size - 1)) {
			jmore = jmore - my_size - 1;
		}

		int result = (my_field[iless][jless] + my_field[imore][jless]
				+ my_field[iless][jmore] + my_field[imore][jmore]) / 4;
		return (byte) result;
	}

	// Build the array and recurse.
	private void buildRecursively(int stride, final double recursion_scalar) {
		if (stride == 0) {
			return;
		}
		// Cut stride length in half
		stride = stride / 2;
		// Square step, add to set
		Set<Point> temp_set = new HashSet<Point>();
		for (Point old_p : my_points) {
			int x_value = old_p.x + stride;
			if (x_value > (my_size - 1)) {
				x_value = x_value - my_size + 1;
			}
			int y_value = old_p.y + stride;
			if (y_value > (my_size - 1)) {
				y_value = y_value - my_size + 1;
			}
			Point p = new Point(x_value, y_value);
			my_field[p.x][p.y] = (byte) (aveSquare(p.x, p.y, stride) + rand(recursion_scalar));
			if (p.x != (my_size - 1) && p.y != (my_size - 1)) {
				temp_set.add(p);
			}
		}
		my_points.addAll(temp_set);

		// Mirror values (wrap edges)
		mirrorValues();

		// Triangle step, add to set
		temp_set = new HashSet<Point>();
		for (Point old_p : my_points) {
			int x_value = old_p.x;
			int y_value = old_p.y + stride;
			if (y_value > (my_size - 1)) {
				y_value = y_value - my_size + 1;
			}
			Point p = new Point(x_value, y_value);
			my_field[p.x][p.y] = (byte) (aveTriangle(p.x, p.y, stride) + rand(recursion_scalar));
			temp_set.add(p);
		}
		my_points.addAll(temp_set);

		// Recurse on all points
		buildRecursively(stride, recursion_scalar * my_scalar);

		// Mirror values (wrap edges)
		mirrorValues();
	}

	// Mirror any values on the border of the array
	private void mirrorValues() {
		for (Point p : my_points) {
			if (p.x == 0) {
				my_field[my_size - 1][p.y] = my_field[p.x][p.y];
			}
			if (p.y == 0) {
				my_field[p.x][my_size - 1] = my_field[p.x][p.y];
			}
		}
	}

	/**
	 * Generate a random number byte between scalar * (-127 to 127)
	 * 
	 * @param scalar
	 *            The scalar between 0.0 and 1.0.
	 * @return A randum number inside the range.
	 */
	public static byte rand(final double scalar) {
		int result = (int) (Math.random() * 254);
		result = result - 127;
		result = (int) ((double) result * scalar);
		return (byte) result;
	}

	// 2D:
	// Left, Right, Mid
	// Compute two points between Left-Mid and Right-Mid
	// 3D:
	// top-right, top-left, bottom-left, bottom-right, mid
	// Compute

	/**
	 * precondition: Must be proceeded by a call to generate(n)
	 * 
	 * @return A reference to the height field.
	 */
	public byte[][] array() {
		return my_field;
	}

	/**
	 * @return The size of the array (on edge).
	 */
	public int size() {
		return my_size;
	}

	public void smooth() {
		for (int x = 0; x < my_size; x++) {
			for (int y = 0; y < my_size; y++) {
				// Average my height with surrounding neighbors
				byte topleft = getFieldValue(x - 1, y - 1); // top left
				byte top = getFieldValue(x, y - 1); // top
				byte topright = getFieldValue(x + 1, y - 1); // top left
				byte left = getFieldValue(x - 1, y);
				byte right = getFieldValue(x + 1, y);
				byte bottomleft = getFieldValue(x - 1, y + 1);
				byte bottom = getFieldValue(x, y + 1);
				byte bottomright = getFieldValue(x + 1, y + 1);
				float average = topleft / 8.0f + top / 8.0f + topright / 8.0f
						+ left / 8.0f + right / 8.0f + bottomleft / 8.0f
						+ bottom / 8.0f + bottomright / 8.0f;
				
				// Correct for overflow
				if (average > Byte.MAX_VALUE) {
					my_field[x][y] = Byte.MAX_VALUE - 1;
				} else if (average < Byte.MIN_VALUE) {
					my_field[x][y] = Byte.MIN_VALUE + 1;
				} else {
					my_field[x][y] = (byte)average;
				}
			}
		}
	}

	/**
	 * Get the value of the field and wrap appropriately.
	 * 
	 * @param the_x
	 *            The x position.
	 * @param the_y
	 *            The y position.
	 * @return The value of the field.
	 */
	public byte getFieldValue(final int the_x, final int the_y) {
		return my_field[(the_x + my_size) % my_size][(the_y + my_size)
				% my_size];
	}

	/**
	 * @return A String representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < my_field.length; i++) {
			for (int j = 0; j < my_field[0].length; j++) {
				sb.append(String.format("%4s", my_field[i][j]));
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
