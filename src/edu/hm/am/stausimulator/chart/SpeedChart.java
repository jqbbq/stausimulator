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

public class SpeedChart {
	
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
		
		Color[] colors = getColors(data.getMax());
		
		int width = data.getWidth();
		int height = steps.size();
		
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
			cells = step.getVelocityPerCell();
			
			for (int c = 0; c < cells.size(); c++) {
				if (cells.get(c) != null) {
					value = cells.get(c).intValue();
					g.setColor(colors[Math.abs(value)]);
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
			
			if (legend) {
				w = image.getWidth();
				h = image.getHeight() + (int) (Math.ceil((colors.length * 30.0) / w) * 20);
				nImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				nG = nImage.createGraphics();
				nG.setFont(new Font("Arial", Font.PLAIN, 10));
				fm = nG.getFontMetrics();
				
				x = 12;
				y = image.getHeight();
				sw = fm.stringWidth(" = 0");
				
				// draw old image
				nG.drawImage(image, 0, 0, null);
				
				for (int i = 0; i < colors.length; i++) {
					if (x + sw + 20 > w) {
						x = 12;
						y += 20;
					}
					// fill color rect
					nG.setColor(colors[i]);
					nG.fillRect(x, y + 4, 5, 5);
					
					x += 5;
					
					// draw string
					nG.setColor(Color.BLACK);
					nG.drawString(" = " + i, x, y + 10);
					
					x += sw + 5;
				}
				
				image = nImage;
			}
		}
		
		return image;
	}
	
	public static void write(LaneData data, File file) throws IOException {
		ImageIO.write(SpeedChart.draw(data, 0, true, true), "png", file);
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
			colors[i] = Color.getHSBColor((float) (0.3 / maxspeed * i), 1.0f, 0.8f);
		}
		return colors;
	}
	
	public static void main(String[] args) {
		System.out.println(getColors(1).length);
		System.out.println(getColors(1)[0]);
		System.out.println(getColors(1)[1]);
	}
}
