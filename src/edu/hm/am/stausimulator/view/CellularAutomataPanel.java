/*
 * 
 */
package edu.hm.am.stausimulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.model.Lane;

// TODO: Auto-generated Javadoc
/**
 * The Class CellularAutomataPanel.
 */
public class CellularAutomataPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The road. */
	private Road road;

	private List<Lane> lanes;

	public CellularAutomataPanel(Road road) {
		this.road = road;

		init();

		setVisible(true);
	}

	public void init() {
		lanes = new ArrayList<Lane>();

		int laneY = 12;

		for (edu.hm.am.stausimulator.model.Lane lane : road.getLanes()) {
			lanes.add(new Lane(2, laneY, lane.getCells()));
			laneY += Lane.LANE_HEIGHT + 2;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.drawString("Road "+road.getId(), 2, 10);
		for (Lane lane : lanes) {
			lane.draw(g);
		}
	}
}
