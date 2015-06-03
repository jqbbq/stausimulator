package edu.hm.am.stausimulator.view.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import edu.hm.am.stausimulator.model.Car;
import edu.hm.am.stausimulator.model.Vehicle;

public class Lane extends Drawable {

	private List<Cell> cells;

	public Lane(List<?> cells) {
		this(0, 0, cells);
	}

	@SuppressWarnings("unchecked")
	public Lane(int x, int y, List<?> cells) {
		super(Type.RECT, x, y, Cell.CELL_WIDTH * cells.size() + (cells.size() - 1) + 4, Cell.CELL_HEIGHT + 4);

		List<edu.hm.am.stausimulator.model.Cell> c;
		if (!(cells.get(0) instanceof edu.hm.am.stausimulator.model.Cell)) {
			c = new ArrayList<>();

			Integer speed;
			Vehicle vehicle;
			edu.hm.am.stausimulator.model.Cell cell;
			for (int i = 0; i < cells.size(); i++) {
				speed = (Integer) cells.get(i);
				cell = new edu.hm.am.stausimulator.model.Cell();
				if (speed != null) {
					vehicle = new Car();
					vehicle.setSpeed(Math.abs(speed.intValue()));
					if (speed < 0) {
						vehicle.setLingered(true);
					}
					cell.setVehicle(vehicle);
				}
				c.add(cell);
			}

		} else {
			c = (List<edu.hm.am.stausimulator.model.Cell>) cells;
		}

		init(x, y, c);
	}

	@Override
	public int getWidth() {
		return Cell.CELL_WIDTH * cells.size() + (cells.size() - 1) + 4;
	}

	@Override
	public int getHeight() {
		return Cell.CELL_HEIGHT + 4;
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		x += 2;
		for (Cell cell : cells) {
			cell.setX(x);
			x += Cell.CELL_WIDTH + 1;
		}
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		for (Cell cell : cells) {
			cell.setY(y + 2);
		}
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);

		for (Cell cell : cells) {
			cell.draw(g);
		}
	}

	private void init(int x, int y, List<edu.hm.am.stausimulator.model.Cell> cells) {
		setBackgroundColor(Color.GRAY);

		this.cells = new ArrayList<Cell>();

		int cellX = x + 2;
		int cellY = y + 2;

		for (edu.hm.am.stausimulator.model.Cell cell : cells) {
			this.cells.add(new Cell(cellX, cellY, cell));
			cellX += Cell.CELL_WIDTH + 1;
		}
	}
}
