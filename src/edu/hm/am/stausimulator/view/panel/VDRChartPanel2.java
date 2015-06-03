package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.chart.VDRChart2;
import edu.hm.am.stausimulator.data.LaneData;
import edu.hm.am.stausimulator.model.Lane;

public class VDRChartPanel2 extends JPanel {

	private static final long serialVersionUID = -2497484297331437275L;

	private LaneData data;

	private Observer observer;

	private JScrollPane scrollPane;

	public VDRChartPanel2(Lane lane) {

		data = lane.getData();

		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		setBackground(Color.WHITE);

		JPanel image = new ImagePanel();

		scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(this.getSize());
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportView(new ImagePanel());

		add(scrollPane, "cell 0 0,grow");

		observer = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					// runner.start();
				} else if ("Stop".equals(arg)) {
					// runner.stop();
				} else if ("Step".equals(arg)) {
					image.repaint();
					scrollPane.revalidate();
				}
			}
		};
		Simulator.getInstance().addObserver(observer);

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		// runner.stop();
		Simulator.getInstance().deleteObserver(observer);
	}

	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = -6511870709177991993L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);

			BufferedImage image = VDRChart2.draw(data, (int) scrollPane.getVisibleRect().getHeight(), false, false);
			Dimension dimension = new Dimension(image.getWidth(), image.getHeight());

			g.drawImage(image, 0, 0, null);

			setSize(dimension);
			setMinimumSize(dimension);
			setMaximumSize(dimension);
			setPreferredSize(dimension);

		}

	}
}