package edu.hm.am.stausimulator.chart;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.data.RoundData;

public class AVGSpeedChart {
	
	public static JFreeChart draw(LaneData laneData) {
		List<RoundData> rounds = laneData.getAll();
		final XYSeries data = new XYSeries("Velocity");
		
		RoundData rd;
		for (int r = 0; r < rounds.size(); r++) {
			rd = rounds.get(r);
			data.add(r, rd.getAverageVelocity());
		}
		
		JFreeChart chart = ChartFactory.createXYLineChart("Average Velocity", "Round", "AVG Velocity", new XYSeriesCollection(data), PlotOrientation.VERTICAL, false, true, false);
		
		return chart;
	}
	
	public static void write(LaneData data, File file) throws IOException {
		ChartUtilities.saveChartAsPNG(file, draw(data), 1000, 500);
	}
	
}
