package gui.dataProject;

import java.awt.Color;
import java.nio.charset.Charset;
import java.util.ArrayList;

import util.Configuration;

import com.csvreader.CsvReader;

public class MetaLoadColors {

	private static MetaLoadColors instance = null;

	private static ArrayList<Color> colors = null;

	private static String colorsPath = Configuration.getInstance().getProperty(
			"colors");
	//private static String colorsPath =	FileControl.getInstance().getOPCATDirectory()
	//		+ FileControl.fileSeparator + "colors.ops" ; 

	private MetaLoadColors() {

	}

	public static MetaLoadColors getInstance() {
		if (instance == null) {
			instance = new MetaLoadColors();
		}
		return instance;
	}

	public ArrayList<Color> load() {
		if (colors != null) {
			return colors;
		} else {
			colors = new ArrayList<Color>();
		}

		try {

			CsvReader coloesFile = new CsvReader(colorsPath, ',', Charset
					.defaultCharset());

			coloesFile.readHeaders();
			String[] headers = coloesFile.getHeaders();
			int redIndex = -1;
			int greenIndex = -1;
			int blueIndex = -1;

			if (headers.length <= 3) {
				for (int i = 0; i < headers.length; i++) {
					if (headers[i].equalsIgnoreCase("red")) {
						redIndex = i;
					} else if (headers[i].equalsIgnoreCase("green")) {
						greenIndex = i;
					} else if (headers[i].equalsIgnoreCase("blue")) {
						blueIndex = i;
					}
				}

				if ((redIndex == -1) || (greenIndex == -1) || (blueIndex == -1)) {
					loadDefaultColors();
				} else {
					while (coloesFile.readRecord()) {
						String red = coloesFile.get(redIndex);
						String blue = coloesFile.get(blueIndex);
						String green = coloesFile.get(greenIndex);
						colors.add(new Color(Integer.valueOf(red).intValue(),
								Integer.valueOf(green).intValue(), Integer
										.valueOf(blue).intValue()));
					}
				}

			} else {
				loadDefaultColors();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			loadDefaultColors();
		}

		return colors;
	}

	private void loadDefaultColors() {
		colors = new ArrayList<Color>();
		colors.clear();
		colors.add(new Color(51, 102, 255));
		colors.add(new Color(102, 51, 255));
		colors.add(new Color(153, 51, 255));
		colors.add(new Color(51, 204, 255));
		colors.add(new Color(204, 51, 255));
		colors.add(new Color(51, 153, 255));
		colors.add(new Color(255, 51, 255));
		colors.add(new Color(51, 255, 255));
		colors.add(new Color(255, 51, 204));
		colors.add(new Color(51, 51, 255));
		colors.add(new Color(255, 51, 153));
		colors.add(new Color(255, 51, 102));
		colors.add(new Color(153, 255, 51));
		colors.add(new Color(255, 51, 51));
		colors.add(new Color(102, 255, 51));
		colors.add(new Color(255, 102, 51));
		colors.add(new Color(51, 255, 51));
		colors.add(new Color(255, 153, 51));
		colors.add(new Color(51, 255, 102));
		colors.add(new Color(255, 204, 51));
		colors.add(new Color(51, 255, 153));
		colors.add(new Color(255, 255, 51));
		colors.add(new Color(51, 255, 204));
		colors.add(new Color(204, 255, 51));
		colors.add(new Color(51, 255, 255));
	}
}
