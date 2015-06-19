package edu.hm.am.stausimulator.chart;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.hm.am.stausimulator.data.RoundData;
import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.Road;

public class FlowFundamentalChart {
	
	private static final int	ROUNDS			= 100;
	private static final int	CELLS			= 200;
	private static final double	DENSITY_START	= 0.001;
	private static final double	DENSITY_STEP	= 0.003;
	private static final double	DENSITY_END		= 1;
	
	public static JFreeChart draw(JLabel statusLabel) {
		
		List<List<RoundData>> detV1 = new ArrayList<>();
		List<List<RoundData>> probV1 = new ArrayList<>();
		List<List<RoundData>> detV5 = new ArrayList<>();
		List<List<RoundData>> probV5 = new ArrayList<>();
		
		double density = DENSITY_START;
		
		for (; density <= DENSITY_END; density += DENSITY_STEP) {
			statusLabel.setText("Generating Density " + density);
			detV1.add(generateData(ROUNDS, CELLS, 1, density, 0));
			probV1.add(generateData(ROUNDS, CELLS, 1, density, 0.15));
			detV5.add(generateData(ROUNDS, CELLS, 5, density, 0));
			probV5.add(generateData(ROUNDS, CELLS, 5, density, 0.15));
		}
		
		final XYSeries xyDetV1 = new XYSeries("det v1");
		final XYSeries xyProbV1 = new XYSeries("prob 0.15 v1");
		final XYSeries xyDetV5 = new XYSeries("det v5");
		final XYSeries xyProbV5 = new XYSeries("prob 0.15 v5");
		
		statusLabel.setText("Start parsing\n");
		parseData(detV1, xyDetV1);
		parseData(probV1, xyProbV1);
		parseData(detV5, xyDetV5);
		parseData(probV5, xyProbV5);
		
		detV1 = null;
		probV1 = null;
		detV5 = null;
		probV5 = null;
		
		XYSeriesCollection collection = new XYSeriesCollection();
		collection.addSeries(xyDetV1);
		collection.addSeries(xyProbV1);
		collection.addSeries(xyDetV5);
		collection.addSeries(xyProbV5);
		
		JFreeChart chart = ChartFactory.createScatterPlot("flow per density", "density", "flow", collection, PlotOrientation.VERTICAL, true, true, false);
		
		XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
		renderer.setBaseShape(new Ellipse2D.Double(-1, -1, 2, 2));
		
		renderer.setSeriesShape(0, new Ellipse2D.Double(-1, -1, 2, 2));
		renderer.setSeriesPaint(0, Color.GRAY);
		
		renderer.setSeriesShape(1, new Ellipse2D.Double(-1, -1, 2, 2));
		renderer.setSeriesPaint(1, Color.BLUE);
		
		renderer.setSeriesShape(2, new Ellipse2D.Double(-1, -1, 2, 2));
		renderer.setSeriesPaint(2, Color.RED);
		
		renderer.setSeriesShape(3, new Ellipse2D.Double(-1, -1, 2, 2));
		renderer.setSeriesPaint(3, new Color(0, 100, 0));
		
		((XYPlot) chart.getPlot()).setRenderer(renderer);
		
		return chart;
	}
	
	private static List<RoundData> generateData(int rounds, int cells, int maxVelocity, double density, double lingerProbabilty) {
		
		Road road = new Road(1, cells, maxVelocity, density);
		Lane lane = road.getLanes().get(0);
		lane.setLingerProbability(lingerProbabilty);
		lane.setStartingProbability(lingerProbabilty);
		lane.setNext(lane);
		lane.setPrev(lane);
		
		for (int i = 0; i < rounds; i++) {
			road.nextStep();
		}
		
		List<RoundData> data = lane.getData().getAll();
		road.destory();
		return data;
	}
	
	private static void parseData(List<List<RoundData>> data, XYSeries series) {
		
		List<RoundData> rounds;
		
		double density = DENSITY_START;
		double velocitySum = 0;
		
		for (int i = 0; i < data.size(); i++) { // go over every density
			rounds = data.get(i);
			velocitySum = 0;
			
			for (RoundData round : rounds) { // go over every round
				velocitySum += round.getAverageVelocity();
			}
			
			series.add(density, new Double(velocitySum / rounds.size() * density));
			density += DENSITY_STEP;
		}
	}
	
}