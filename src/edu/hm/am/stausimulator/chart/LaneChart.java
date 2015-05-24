package edu.hm.am.stausimulator.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.data.LaneData;

public class LaneChart extends JPanel {

	private static final long serialVersionUID = -2497484297331437275L;

	private LaneData data;
	private Dimension dimension;

	public LaneChart(LaneData data, Dimension dimension) {
		this.data = data;
		this.dimension = dimension;

		setPreferredSize(new Dimension(300, data.getHeight() * 3));

		Simulator.getInstance().addListener(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int x = 0;
		int y = 0;

		// draw scale

		List<List<Integer>> steps = data.getLast(100);
		List<Integer> step;

		for (int i = 0; i < steps.size(); i++) {
			step = steps.get(i);
			for (int c = 0; c < step.size(); c++) {
				g.setColor(Color.BLACK);
				if (step.get(c) == null) {
					g.setColor(Color.WHITE);
				}
				g.fillRect(x, y, 3, 3);
				y += 3;
			}
			x += 3;
			y = 0;
		}
	}
}
