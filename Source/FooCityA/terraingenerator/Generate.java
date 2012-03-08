package terraingenerator;
import terraingenerator.heightfieldgen.util.HeightFieldGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @author Nick Aschenbach (nick.aschenbach@gmail.com)
 * @version November 26, 2009
 */
public class Generate {
	public static void main(String args[]) {
		for (int i = 0; i < 10; i++) {
			String filename = String.format("%03d", i) + ".txt";
			System.out.println("Generating " + filename + "...");
			write(filename, generate());
		}
	}

	public static String generate() {
		HeightFieldGenerator hg = new HeightFieldGenerator();

		byte field[][];
		do {
			hg.generate(7, 0.5);
			field = hg.array();
		} while (!hasEnoughLand(field, 0.8));
		hg.smooth();
		char map[][] = new char[field.length][field[0].length];

		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++) {
				map[x][y] = convert(field[x][y]);
				if (map[x][y] == 'W') {
					if (map[x + 1][y] == 'G') {
						map[x + 1][y] = 'B';
					}
					if (map[x][y + 1] == 'G') {
						map[x][y + 1] = 'B';
					}
					if (x > 0 && map[x - 1][y] == 'G') {
						map[x - 1][y] = 'B';
					}
					if (y > 0 && map[x][y - 1] == 'G') {
						map[x][y - 1] = 'B';
					}
				}
			}
		}

		// Post process for beaches
		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++) {
				if (map[x][y] == 'W') {
					if (map[x + 1][y] == 'G') {
						map[x + 1][y] = 'B';
					}
					if (map[x][y + 1] == 'G') {
						map[x][y + 1] = 'B';
					}
				}
			}
		}

		// Save to sb
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 128; x++) {
			for (int y = 0; y < 128; y++) {
				sb.append(map[x][y]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Does this array have too much water (i.e. enough land)?
	 * 
	 * @param array
	 *            The input array
	 * @param fract
	 *            The threshold between 0.0 and 1.0 of land.
	 * @return True if the threshold has been exceeded.
	 */
	public static boolean hasEnoughLand(byte[][] array, double fract) {
		int total = 0;
		int count = 0;
		for (int x = 0; x < array[0].length; x++) {
			for (int y = 0; y < array.length; y++) {
				total++;
				if (array[x][y] > 0)
					count++;
			}
		}
		double result = (double) count / (double) total;
		if (result > fract)
			return true;
		else
			return false;
	}

	/**
	 * Convert a byte into an input.
	 * 
	 * @param input
	 *            The input in the range -128 to 127
	 * @return A char
	 */
	public static char convert(byte input) {
		if (input <= 0) {
			return 'W';
		}
		if (input > 22 && input < 35) {
			return 'D';
		}
		if (input > 0 && input < 70) {
			return 'G';
		} else {
			return 'T';
		}
	}

	/**
	 * Write (non-binary) data to a file.
	 * 
	 * @param filename
	 *            The filename.
	 * @param data
	 *            The data to write.
	 */
	public static void write(String filename, String data) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			out.print(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
