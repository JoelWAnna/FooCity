package terraingenerator;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * @author Nick Aschenbach (nick.aschenbach@gmail.com)
 * @version November 26, 2009
 */
public class TextFilter extends FileFilter {
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			if (extension.equals("txt")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	// The description of this filter
	public String getDescription() {
		return "Map files";
	}
}
