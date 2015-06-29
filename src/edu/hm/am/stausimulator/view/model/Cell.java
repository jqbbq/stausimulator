package edu.hm.am.stausimulator.view.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

import edu.hm.am.stausimulator.model.Vehicle;
import edu.hm.am.stausimulator.view.ImageLoader;

public class Cell extends Drawable {
	
	public static final int						CELL_WIDTH	= 30;
	public static final int						CELL_HEIGHT	= 30;
	
	private edu.hm.am.stausimulator.model.Cell	model;
	
	private Image								image;
	
	public Cell(int x, int y, edu.hm.am.stausimulator.model.Cell model) {
		super(Type.RECT, x, y, CELL_WIDTH, CELL_HEIGHT);
		this.model = model;
		image = ImageLoader.get("car.png");
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		
		// draw additional model info
		if (!model.isFree()) {
			Vehicle vehicle = model.getVehicle();
			
			int speed = vehicle.getSpeed();
			
			g.setFont(new Font("Arial", Font.PLAIN, 14));
			
			FontMetrics fm = g.getFontMetrics();
			
			int imageWidth = image.getWidth(null);
			int imageHeight = image.getHeight(null);
			
			int x = getX() + 2;
			int y = getY() + fm.getHeight() / 2 + 2;
			
			if (vehicle.hasChangedLane()) {
				g.setColor(new Color(0, 204, 255));
				g.fillRect(getX(), getY(), getWidth(), getHeight());
			}
			
			// draw car
			g.drawImage(image, getX() + ((Cell.CELL_WIDTH - imageWidth) / 2), getY() + (Cell.CELL_HEIGHT - imageHeight) / 2, imageWidth, imageHeight, null);
			
			// draw speed
			g.setColor(Color.BLACK);
			
			if (vehicle.hasLingered()) {
				g.setColor(Color.ORANGE);
				g.fillRect(x, y - 14, 7, 14);
			}
			
			g.setColor(Color.BLACK);
			if (speed == 0) {
				g.setColor(Color.RED);
			}
			
			g.drawString(speed + "", x, y);
			
		}
	}
	
}
