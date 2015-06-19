package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartPanel;

import edu.hm.am.stausimulator.Runner;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.chart.AVGSpeedChart;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.model.Road;

public class AVGSpeedChartPanel extends Panel {
	
	private static final long	serialVersionUID	= -2497484297331437275L;
	
	private Road				road;
	private int					laneIndex;
	
	private Observer			observer;
	
	// private JFreeChart chart;
	
	public AVGSpeedChartPanel(Road road, int laneIndex) {
		
		this.road = road;
		this.laneIndex = laneIndex;
		
		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		setBackground(Color.WHITE);
		
		ChartPanel panel = new ChartPanel(AVGSpeedChart.draw(getLaneData()));
		
		add(panel, "cell 0 0,grow");
		
		Runner runner = new Runner("chart updater", 500);
		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				panel.setChart(AVGSpeedChart.draw(getLaneData()));
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
					panel.setChart(AVGSpeedChart.draw(getLaneData()));
				}
			}
		};
		
		Simulator.getInstance().addObserver(observer);
	}
	
	@Override
	public void destroy() {
		Simulator.getInstance().deleteObserver(observer);
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private LaneData getLaneData() {
		return road.getLane(laneIndex).getData();
	}
}
