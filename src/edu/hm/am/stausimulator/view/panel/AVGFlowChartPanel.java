package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartPanel;

import edu.hm.am.stausimulator.Runner;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.chart.AVGFlowChart;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.model.Road;

public class AVGFlowChartPanel extends JPanel {
	
	private static final long	serialVersionUID	= -2497484297331437275L;
	
	private Road				road;
	
	private int					laneIndex;
	
	private Observer			observer;
	
	public AVGFlowChartPanel(Road road, int laneIndex) {
		
		this.road = road;
		this.laneIndex = laneIndex;
		
		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		setBackground(Color.WHITE);
		
		ChartPanel panel = new ChartPanel(AVGFlowChart.draw(getLaneData()));
		
		add(panel, "cell 0 0,grow");
		
		Runner runner = new Runner("chart updater", 500);
		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				panel.setChart(AVGFlowChart.draw(getLaneData()));
			}
		});
		
		observer = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					runner.start();
				}
				else if ("Stop".equals(arg)) {
					runner.stop();
				}
				else if ("Step".equals(arg) && !runner.isRunning()) {
					panel.setChart(AVGFlowChart.draw(getLaneData()));
				}
			}
		};
		
		Simulator.getInstance().addObserver(observer);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		Simulator.getInstance().deleteObserver(observer);
	}
	
	private LaneData getLaneData() {
		return road.getLane(laneIndex).getData();
	}
}
