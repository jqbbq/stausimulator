package edu.hm.am.stausimulator.view.model;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Drawable {

	enum Type {
		RECT, OVAL
	}

	private final Type type;

	private Color borderColor;
	private Color backgroundColor;

	private int x;
	private int y;

	private int width;
	private int height;

	private boolean border;

	public Drawable(Type type) {
		this(type, 0, 0, 0, 0);
	}

	public Drawable(Type type, int x, int y) {
		this(type, x, y, 0, 0);
	}

	public Drawable(Type type, int x, int y, int width, int height) {
		border = false;
		borderColor = Color.BLACK;
		backgroundColor = Color.WHITE;

		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public Type getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean getBorder() {
		return border;
	}

	public void setBorder(boolean border) {
		this.border = border;
	}

	public void draw(Graphics g) {
		if (backgroundColor != null) {
			g.setColor(backgroundColor);

			switch (type) {
			case RECT:
				g.fillRect(x, y, width, height);
				break;

			case OVAL:
				g.fillOval(x, y, width, height);
				break;
			}
		}

		if (border && borderColor != null) {
			g.setColor(borderColor);
			switch (type) {
			case RECT:
				g.drawRect(x, y, width - 1, height - 1);
				break;

			case OVAL:
				g.drawOval(x, y, width - 1, height - 1);
				break;
			}
		}
	}
}
