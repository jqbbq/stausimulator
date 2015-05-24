/*
 * 
 */
package edu.hm.am.stausimulator.view.panel;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
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
		setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		init();

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(this.getSize());
		ImagePanel image = new ImagePanel();

		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		scrollPane.setViewportView(image);

		add(scrollPane, "cell 0 0,grow");

		road.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				init();
				image.repaint();

				scrollPane.setPreferredSize(getSize());
				scrollPane.revalidate();
			}
		});

		Simulator.getInstance().addListener(new Runnable() {
			@Override
			public void run() {
				image.repaint();
			}
		});

		Simulator.getInstance().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				image.repaint();
			}
		});

		setVisible(true);
	}

	public void init() {
		lanes = new ArrayList<Lane>();

		int laneY = 5;

		for (edu.hm.am.stausimulator.model.Lane lane : road.getLanes()) {
			lanes.add(new Lane(5, laneY, lane.getCells()));
			laneY += Lane.LANE_HEIGHT + 5;
		}
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = -6511870709177991993L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (Lane lane : lanes) {
				lane.draw(g);
			}
		}
	}
}
