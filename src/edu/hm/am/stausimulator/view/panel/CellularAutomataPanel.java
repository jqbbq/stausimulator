/*
 * 
 */
package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.Dimension;
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
	private static final long	serialVersionUID	= 1L;
	
	/** The road. */
	private Road				road;
	
	private List<Lane>			lanes;
	
	private JScrollPane			scrollPane;
	private ImagePanel			image;
	
	private Observer			roadObserver;
	
	private Observer			simulatorObserver;
	
	public CellularAutomataPanel(Road road) {
		this.road = road;
		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		setBackground(Color.WHITE);
		
		init();
		
		image = new ImagePanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(this.getSize());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(image);
		
		add(scrollPane, "cell 0 0,grow");
		
		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				init();
				image.repaint();
				scrollPane.revalidate();
			}
		};
		
		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				image.repaint();
			}
		};
		
		road.addObserver(roadObserver);
		Simulator.getInstance().addObserver(simulatorObserver);
		
	}
	
	public void init() {
		lanes = new ArrayList<Lane>();
		
		int laneY = 5;
		
		Lane l;
		for (edu.hm.am.stausimulator.model.Lane lane : road.getLanes()) {
			l = new Lane(5, laneY, lane.getCells());
			lanes.add(l);
			laneY += l.getHeight() + 5;
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		road.deleteObserver(roadObserver);
		Simulator.getInstance().deleteObserver(simulatorObserver);
	}
	
	private class ImagePanel extends JPanel {
		
		private static final long	serialVersionUID	= -6511870709177991993L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			if (lanes.size() > 0) {
				Dimension size = new Dimension(lanes.get(0).getWidth() + 10, lanes.get(0).getHeight() * lanes.size() + 10);
				setSize(size);
				setPreferredSize(size);
				setMinimumSize(size);
				setMaximumSize(size);
				
				for (int l = 0; l < lanes.size(); l++) {
					lanes.get(l).draw(g);
				}
			}
		}
	}
}
