/*
 * 
 */
package edu.hm.am.stausimulator.view;

import java.awt.Color;
import java.awt.Dimension;
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

	private Simulator instance;

	/** The road. */
	private Road road;

	private List<Lane> lanes;

	public CellularAutomataPanel(Simulator instance, int width, int height) {
		this.instance = instance;
		Dimension d = new Dimension(width, height);
		setMaximumSize(d);
		setMinimumSize(d);
		setSize(d);
		setPreferredSize(d);

		setBackground(Color.WHITE);

		init();

		setVisible(true);
	}

	public void init() {
		road = instance.getRoad();
		lanes = new ArrayList<Lane>();

		int laneY = 2;

		for (edu.hm.am.stausimulator.model.Lane lane : road.getLanes()) {
			lanes.add(new Lane(2, laneY, lane.getCells()));
			laneY += Lane.LANE_HEIGHT + 2;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Lane lane : lanes) {
			lane.draw(g);
		}
	}
}
