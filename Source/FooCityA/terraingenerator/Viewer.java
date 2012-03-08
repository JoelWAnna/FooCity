package terraingenerator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.html.HTMLEditorKit;
/**
 * @author Nick Aschenbach (nick.aschenbach@gmail.com)
 * @version November 26, 2009
 */
public class Viewer extends JFrame {
	private static final long serialVersionUID = 7082022890958365430L;
	public static final String HEADER = "<html><style type=\"text/css\">body {" + "font-family: Monospaced;"
			+ "font-size: 10; } </style><body><code>";
	public static final String FOOTER = "</code></body></html>";
	private JTextPane m_text;
	private String m_data;
	private String m_data_html;

	public static void main(String args[]) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Nimbus LAF not available");
		}
		new Viewer();
	}

	public Viewer() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(1000, 800));
		init();
		center_frame();
		this.setTitle("Terrain Viewer / Generator");
		this.setVisible(true);
	}

	private void load(String filename) {
		m_data = readfile(filename);
		setPage();
	}

	private void setPage() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				m_data_html = HEADER + convert2(m_data) + FOOTER;
				m_text.setText(m_data_html);
				m_text.setCaretPosition(0);
				System.out.println(m_data_html.length());
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	private void init() {
		m_text = new JTextPane();
		m_text.setEditorKit(new HTMLEditorKit());
		m_text.setEditable(false);

		load("000.txt");

		JScrollPane scrollPane = new JScrollPane(m_text);
		getContentPane().add(scrollPane);

		JMenuBar menuBar = new JMenuBar();
		scrollPane.setColumnHeaderView(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmLoad = new JMenuItem("Load...");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new TextFilter());
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showOpenDialog(fc);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					load(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		mnFile.add(mntmLoad);

		JMenuItem mntmSave = new JMenuItem("Save...");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new TextFilter());
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showSaveDialog(fc);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String filename = fc.getSelectedFile().getAbsolutePath();
					if (!filename.contains(".txt")) {
						filename = filename + ".txt";
					}
					Generate.write(filename, m_data);
				}
			}
		});
		mnFile.add(mntmSave);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem mntmGenerate = new JMenuItem("Generate");
		mntmGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				m_data = Generate.generate();
				setPage();
			}
		});
		mnFile.add(mntmGenerate);
		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenuItem copy_menu_item = new JMenuItem("Copy Text");
		copy_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(m_data), null);
			}
		});
		mnEdit.add(copy_menu_item);

		JMenuItem copy_html_menu_item = new JMenuItem("Copy HTML");
		copy_html_menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(m_data_html), null);
			}
		});
		mnEdit.add(copy_html_menu_item);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Terrain Viewer / Generator\n\n(c) 2012 Nick Aschenbach");
			}
		});
		mnHelp.add(mntmAbout);
	}

	// Convert MAP to HTML
	private String convert(String file) {
		file = file.replace("\n", "<br>\n");
		file = file.replace("W", "<font color=\"#49b3ea\">W</font>"); // Water
		file = file.replace("G", "<font color=\"#00FF00\">G</font>"); // Grass
		file = file.replace("B", "<font color=\"#eaac49\">B</font>"); // Beach
		file = file.replace("D", "<font color=\"#992222\">D</font>"); // Dirt
		file = file.replace("T", "<font color=\"#00AA00\">T</font>"); // Trees

		return file;
	}

	// Convert MAP to HTML
	private String convert2(String file) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < file.length(); i++) {
			switch (file.charAt(i)) {
			case 'W':
				sb.append("<font color=\"#49b3ea\">W</font>");
				break;
			case 'G':
				sb.append("<font color=\"#00FF00\">G</font>");
				break;
			case 'B':
				sb.append("<font color=\"#eaac49\">B</font>");
				break;
			case 'D':
				sb.append("<font color=\"#992222\">D</font>");
				break;
			case 'T':
				sb.append("<font color=\"#00AA00\">T</font>");
				break;
			case '\n':
				sb.append("<br>\n");
				break;
			}
			// Give the UI a chance to run
			// if ((i % 250) == 0) {
			// try {
			// Thread.sleep(1);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
			// }
		}

		return sb.toString();
	}

	private String readfile(String filename) {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String str;
			while ((str = in.readLine()) != null) {
				sb.append(str);
				sb.append("\n");
			}
			in.close();
		} catch (IOException e) {
			System.err.println("Error reading " + filename);
		}

		return sb.toString();
	}

	private void center_frame() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		this.setLocation(screenWidth / 2 - this.getWidth() / 2, screenHeight / 2 - this.getHeight() / 2);
	}
}
