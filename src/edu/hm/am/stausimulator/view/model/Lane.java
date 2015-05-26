package edu.hm.am.stausimulator.view.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Lane extends Drawable {

	private final List<Cell> cells;

	public Lane(int x, int y, List<edu.hm.am.stausimulator.model.Cell> cells) {
		super(Type.RECT, x, y, Cell.CELL_WIDTH * cells.size() + (cells.size() - 1) + 4, Cell.CELL_HEIGHT + 4);

		setBackgroundColor(Color.GRAY);

		this.cells = new ArrayList<Cell>();

		int cellX = x + 2;
		int cellY = y + 2;

		for (edu.hm.am.stausimulator.model.Cell cell : cells) {
			this.cells.add(new Cell(cellX, cellY, cell));
			cellX += Cell.CELL_WIDTH + 1;
		}
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
	public void draw(Graphics g) {
		super.draw(g);

		for (Cell cell : cells) {
			cell.draw(g);
		}
	}
}
