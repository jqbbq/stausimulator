package edu.hm.am.stausimulator.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.view.ImageLoader;

public class VDRChart {

	private static BufferedImage ARROW_RIGHT;
	private static BufferedImage ARROW_UP;

	private static boolean isInited = false;

	public static BufferedImage draw(LaneData data, int stepCount, boolean axis, boolean legend) {
		if (!isInited) {
			ARROW_RIGHT = (BufferedImage) ImageLoader.get("play.png");
			ARROW_RIGHT = Scalr.resize(ARROW_RIGHT, 15, 15);
			ARROW_UP = Scalr.rotate(ARROW_RIGHT, Scalr.Rotation.CW_270);
			isInited = true;
		}

		List<List<Integer>> steps = stepCount > 0 ? data.getLast(stepCount) : data.getAll();
		List<Integer> step;

		Color[] colors = getColors(data.getMax());

		int width = data.getWidth() * 4;
		int height = steps.size() * 4;

		if (height == 0) {
			height = 1;
		}

		int x = 0;
		int y = height - 1;
		int value;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();

		// draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		for (int r = 0; r < steps.size(); r++) {
			step = steps.get(r);
			for (int c = 0; c < step.size(); c++) {
				if (step.get(c) != null) {
					value = step.get(c).intValue();
					g.setColor(colors[Math.abs(value)]);
					g.fillRect(x, y, 4, 4);

					if (value < 0) {
						g.setColor(Color.BLUE);
						g.fillRect(x + 1, y + 1, 2, 2);
					}
				}
				x += 4;
			}
			y -= 4;
			x = 0;
		}

		if (image.getHeight() >= 4) {
			image = Scalr.resize(image, image.getWidth() / 4, image.getHeight() / 4);
		}

		if (axis) {
			int w = image.getWidth() + 60;
			int h = image.getHeight() + 50;
			int sw;

			BufferedImage nImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics nG = nImage.getGraphics();

			// draw scale
			nG.setColor(Color.BLACK);
			nG.setFont(new Font("Arial", Font.PLAIN, 15));
			FontMetrics fm = nG.getFontMetrics();

			// x-axis
			sw = fm.stringWidth("road");
			nG.drawLine(7, h - 8, w - 12 - sw, h - 8);
			nG.drawImage(ARROW_RIGHT, w - 15 - sw, h - 15, null);
			nG.drawString("road", w - sw, h - 3);

			// y-axis
			nG.drawString("time", 3, 15);
			nG.drawLine(7, 25, 7, h - 8);
			nG.drawImage(ARROW_UP, 0, 18, null);

			// draw old image
			nG.drawImage(image, 15, 33, null);

			image = nImage;
		}

		return image;
	}

	public static void write(LaneData data, File file) throws IOException {
		ImageIO.write(VDRChart.draw(data, 0, true, false), "png", file);
	}

	public static int getHeight(LaneData data, int steps, boolean axis, boolean legend) {
		return axis ? steps + 50 : steps;
	}

	public static int getWidth(LaneData data, boolean axis, boolean lengend) {
		return axis ? data.getWidth() + 60 : data.getWidth();
	}

	private static Color[] getColors(int maxspeed) {
		Color[] colors = new Color[maxspeed + 1];
		for (int i = 0; i <= maxspeed; i++) {
			colors[i] = Color.getHSBColor((float) (0.4 / maxspeed * i), 1.0f, 1.0f);
		}
		return colors;
	}
}
