package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import edu.hm.am.stausimulator.view.model.Lane;

public class StagePanel extends Panel {
	
	/**
	 * 
	 */
	private static final long					serialVersionUID	= -5497259938578733439L;
	
	private JScrollPane							scrollPane;
	
	private Observer							roadObserver;
	private Observer							simulatorObserver;
	
	private edu.hm.am.stausimulator.model.Road	road;
	
	private int									laneIndex;
	
	private Panel								panel;
	
	private List<String>						labels;
	private List<Lane>							stages;
	
	private Simulator							instance;
	
	public StagePanel(edu.hm.am.stausimulator.model.Road road, int laneIndex) {
		this.road = road;
		this.laneIndex = laneIndex;
		
		labels = new ArrayList<>();
		stages = new ArrayList<Lane>();
		
		labels.add("Stage 0 - Original");
		labels.add("Stage 1 - Accelerate");
		// labels.add("Stage 1.5 - Lane Change");
		labels.add("Stage 2 - Brake");
		labels.add("Stage 3 - Linger");
		labels.add("Stage 4 - Move");
		
		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		setBackground(Color.WHITE);
		
		panel = new Panel();
		
		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(this.getSize());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(panel);
		
		add(scrollPane, "cell 0 0,grow");
		
		instance = Simulator.getInstance();
		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (!instance.isRunning() && arg != null && arg instanceof String) {
					if (((String) arg).startsWith("Stage")) {
						if (((String) arg).startsWith("Stage 0")) {
							stages = new ArrayList<Lane>();
						}
						stages.add(new Lane(getLane().export()));
					}
					else if (((String) arg).startsWith("Step")) {
						panel.repaint();
						scrollPane.revalidate();
					}
					else if ("Reset".equals(arg)) {
						stages = null;
					}
					
				}
			}
		};
		
		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					stages = null;
					panel.repaint();
					scrollPane.revalidate();
				}
			}
		};
		
		road.addObserver(roadObserver);
		instance.addObserver(simulatorObserver);
	}
	
	@Override
	public void destroy() {
		road.deleteObserver(roadObserver);
		instance.deleteObserver(simulatorObserver);
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private edu.hm.am.stausimulator.model.Lane getLane() {
		return road.getLane(laneIndex);
	}
	
	private class Panel extends JPanel {
		
		private static final long	serialVersionUID	= -3515344269392271823L;
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			
			if (stages != null) {
				int x = 5;
				int y = 5;
				
				Lane lane;
				
				for (int s = 0; s < stages.size(); s++) {
					
					y += 10;
					
					// draw label
					g.setColor(Color.BLACK);
					g.setFont(new Font("Arial", Font.PLAIN, 10));
					g.drawString(labels.get(s), x, y);
					
					y += 2;
					
					lane = stages.get(s);
					if (s == 0) {
						Dimension size = new Dimension(lane.getWidth() + 10, lane.getHeight() * stages.size() + (stages.size() + 1) * 5);
						setSize(size);
						setPreferredSize(size);
						setMinimumSize(size);
						setMaximumSize(size);
					}
					lane.setX(x);
					lane.setY(y);
					
					lane.draw(g);
					
					y += lane.getHeight() + 5;
				}
			}
		}
	}
}
