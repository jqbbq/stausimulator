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
import edu.hm.am.stausimulator.data.RoundData;
import edu.hm.am.stausimulator.view.ImageLoader;

public class PositionChart {
	
	private static BufferedImage	ARROW_RIGHT;
	private static BufferedImage	ARROW_UP;
	
	private static boolean			isInited	= false;
	
	public static BufferedImage draw(LaneData data, int stepCount, boolean axis, boolean legend) {
		if (!isInited) {
			ARROW_RIGHT = (BufferedImage) ImageLoader.get("play.png");
			ARROW_RIGHT = Scalr.resize(ARROW_RIGHT, 15, 15);
			ARROW_UP = Scalr.rotate(ARROW_RIGHT, Scalr.Rotation.CW_270);
			isInited = true;
		}
		
		List<RoundData> steps = stepCount > 0 ? data.getLast(stepCount) : data.getAll();
		List<Integer> cells;
		RoundData step;
		
		int width = data.getWidth();
		int height = steps.size();
		
		if (height == 0) {
			height = 1;
		}
		
		int x = 0;
		int y = height - 1;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		// draw background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.BLACK);
		for (int r = 0; r < steps.size(); r++) {
			step = steps.get(r);
			cells = step.getVelocityPerCell();
			
			for (int c = 0; c < cells.size(); c++) {
				if (cells.get(c) != null) {
					g.fillRect(x, y, 1, 1);
				}
				x++;
			}
			y--;
			x = 0;
		}
		
		if (axis) {
			int w = image.getWidth() + 70;
			int h = image.getHeight() + 50;
			int sw;
			
			BufferedImage nImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics nG = nImage.getGraphics();
			
			// draw scale
			nG.setColor(Color.BLACK);
			nG.setFont(new Font("Arial", Font.PLAIN, 20));
			FontMetrics fm = nG.getFontMetrics();
			
			// x-axis
			sw = fm.stringWidth("road");
			nG.drawString("road", w - sw, h - 3);
			nG.drawLine(7, h - 10, w - 10 - sw, h - 10);
			nG.drawImage(ARROW_RIGHT, w - 15 - sw, h - 17, null);
			
			// y-axis
			nG.drawString("time", 3, 20);
			nG.drawLine(7, 30, 7, h - 10);
			nG.drawImage(ARROW_UP, 0, 20, null);
			
			// draw old image
			nG.drawImage(image, 15, 33, null);
			
			image = nImage;
		}
		
		return image;
	}
	
	public static void write(LaneData data, File file) throws IOException {
		ImageIO.write(PositionChart.draw(data, 0, true, true), "png", file);
	}
	
	public static int getHeight(LaneData data, int steps, boolean axis, boolean legend) {
		return axis ? steps + 50 : steps;
	}
	
	public static int getWidth(LaneData data, boolean axis, boolean lengend) {
		return axis ? data.getWidth() + 60 : data.getWidth();
	}
	
}
