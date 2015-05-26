package edu.hm.am.stausimulator.view.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Runner;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.chart.VDRChart;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.model.Lane;

public class VDRChartPanel extends JPanel {

	private static final long serialVersionUID = -2497484297331437275L;

	private Lane lane;
	private LaneData data;

	private Runner runner;
	private Observer observer;

	private JScrollPane scrollPane;

	public VDRChartPanel(Lane lane) {

		this.lane = lane;
		data = lane.getData();

		setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));

		JPanel image = new ImagePanel();

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(this.getSize());
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(new ImagePanel());

		add(scrollPane, "cell 0 0,grow");

		runner = new Runner("Line Chart", 500);
		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				image.repaint();
				scrollPane.revalidate();
			}
		});

		observer = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					runner.start();
				} else if ("Stop".equals(arg)) {
					runner.stop();
				} else if ("Step".equals(arg)) {
					repaint();
				}
			}
		};
		Simulator.getInstance().addObserver(observer);

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		runner.stop();
		Simulator.getInstance().deleteObserver(observer);
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = -6511870709177991993L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			BufferedImage image = VDRChart.draw(data, (int) scrollPane.getVisibleRect().getHeight(), false, false);
			Dimension dimension = new Dimension(image.getWidth(), image.getHeight());

			g.drawImage(image, 0, 0, null);

			setSize(dimension);
			setMinimumSize(dimension);
			setMaximumSize(dimension);
			setPreferredSize(dimension);

		}

	}
}
