package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Lane;
import edu.hm.am.stausimulator.model.Road;

public class RoadPanel extends JPanel {

	private static final long serialVersionUID = -6399513557243691428L;

	private JComboBox<Integer> cbLanes = new JComboBox<>();
	private JComboBox<Integer> cbCells = new JComboBox<>();
	private JComboBox<Integer> cbMaxSpeed = new JComboBox<>();

	private JSlider slDensity = new JSlider();
	private JSlider slStartingProbability = new JSlider();
	private JSlider slProbability = new JSlider();

	private Observer roadObserver;
	private Observer simulatorObserver;

	private Road road;

	/**
	 * Create the panel.
	 */
	public RoadPanel(Road road) {
		this.road = road;

		setBackground(Color.WHITE);
		setLayout(new MigLayout("insets 5", "[grow]", "[50px][grow]"));

		JLabel lblLanes = new JLabel("Lanes");
		JLabel lblCells = new JLabel("Cells");
		JLabel lblMaxSpped = new JLabel("Max Speed");
		JLabel lblDensity = new JLabel("Density");
		JLabel lblStaringProbability = new JLabel("Starting Probability");
		JLabel lblProbability = new JLabel("Probability");

		cbLanes.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5 }));
		cbLanes.setSelectedItem(new Integer(road.getLanes().size()));
		cbLanes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbLanes.getSelectedItem();
				road.setLanes(value.intValue());
			}
		});

		cbCells.setModel(new DefaultComboBoxModel<>(new Integer[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 300, 500, 1000 }));
		cbCells.setSelectedItem(new Integer(road.getCells()));
		cbCells.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbCells.getSelectedItem();
				road.setCells(value.intValue());
			}
		});

		cbMaxSpeed.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5 }));
		cbMaxSpeed.setSelectedItem(new Integer(road.getMaxSpeed()));
		cbMaxSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbMaxSpeed.getSelectedItem();
				road.setMaxSpeed(value.intValue());
			}
		});

		slDensity.setSnapToTicks(true);
		slDensity.setMinorTickSpacing(1);
		slDensity.setMajorTickSpacing(10);
		slDensity.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slDensity.getValue() / 100.0;
				lblDensity.setText("Density " + value);
				road.setDensity(value);
			}
		});
		slDensity.setValue((int) (road.getDensity() * 100));

		slStartingProbability.setSnapToTicks(true);
		slStartingProbability.setMinorTickSpacing(1);
		slStartingProbability.setMajorTickSpacing(10);
		slStartingProbability.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slStartingProbability.getValue() / 100.0;
				lblStaringProbability.setText("Starting Probability " + value);
				road.setStartingProbability(value);
			}
		});
		slStartingProbability.setValue((int) (road.getStartingProbability() * 100));

		slProbability.setSnapToTicks(true);
		slProbability.setMinorTickSpacing(1);
		slProbability.setMajorTickSpacing(10);
		slProbability.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slProbability.getValue() / 100.0;
				lblProbability.setText("Probability " + value);
				road.setProbability(value);
			}
		});
		slProbability.setValue((int) (road.getProbability() * 100));

		JPanel settings = new JPanel();
		settings.setLayout(new MigLayout("insets 0", "[50px][50px][50px][150px][150px][150px]", "[25px][25px]"));
		settings.add(cbLanes, "cell 0 1,growx");
		settings.add(lblDensity, "cell 3 0,alignx center,aligny center");
		settings.add(lblProbability, "cell 5 0,alignx center");
		settings.add(lblStaringProbability, "cell 4 0,alignx center,aligny center");
		settings.add(lblLanes, "cell 0 0,alignx center");
		settings.add(lblMaxSpped, "cell 2 0");
		settings.add(lblCells, "cell 1 0,alignx center");
		settings.add(cbCells, "cell 1 1,growx");
		settings.add(cbMaxSpeed, "cell 2 1,growx");
		settings.add(slDensity, "flowx,cell 3 1,alignx left,aligny top");
		settings.add(slStartingProbability, "cell 4 1,alignx left,aligny top");
		settings.add(slProbability, "cell 5 1");

		JPanel cellularTab = new JPanel();
		cellularTab.setBackground(Color.WHITE);
		cellularTab.setLayout(new MigLayout("", "[grow]", "[grow]"));
		cellularTab.add(new CellularAutomataPanel(road), "cell 0 0,grow");

		JTabbedPane vdrTabbedPane = new JTabbedPane();
		List<Lane> lanes = road.getLanes();
		for (int i = 1; i <= lanes.size(); i++) {
			vdrTabbedPane.addTab("Lane " + i, new VDRChartPanel(lanes.get(i - 1)));
		}

		JPanel vdrTab = new JPanel();
		vdrTab.setBackground(Color.WHITE);
		vdrTab.setLayout(new MigLayout("", "[grow]", "[grow]"));
		vdrTab.add(vdrTabbedPane, "cell 0 0,grow");

		JPanel chartsTab = new JPanel();
		chartsTab.setLayout(new MigLayout("gap 0,insets 0", "[grow][grow][grow]", "[grow][grow]"));
		chartsTab.setBackground(Color.WHITE);

		chartsTab.add(new ChartPanel(null), "cell 0 0,grow");
		chartsTab.add(new ChartPanel(null), "cell 1 0,grow");
		chartsTab.add(new ChartPanel(null), "cell 2 0,grow");
		chartsTab.add(new ChartPanel(null), "cell 0 1,grow");
		chartsTab.add(new ChartPanel(null), "cell 1 1,grow");
		chartsTab.add(new ChartPanel(null), "cell 2 1,grow");

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Cellular", null, cellularTab, null);
		tabbedPane.addTab("VDR Chart", null, vdrTab, null);
		tabbedPane.addTab("Charts", null, chartsTab, null);

		add(settings, "cell 0 0,grow");
		add(tabbedPane, "cell 0 1,grow");

		roadObserver = new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				vdrTabbedPane.removeAll();
				List<Lane> lanes = road.getLanes();
				for (int i = 1; i <= lanes.size(); i++) {
					vdrTabbedPane.addTab("Lane " + i, new VDRChartPanel(lanes.get(i - 1)));
				}
			}
		};

		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					cbLanes.setEnabled(false);
					cbCells.setEnabled(false);
					cbMaxSpeed.setEnabled(false);
					slDensity.setEnabled(false);
				} else if ("Pause".equals(arg) || "Stop".equals(arg)) {
					cbLanes.setEnabled(true);
					cbCells.setEnabled(true);
					cbMaxSpeed.setEnabled(true);
					slDensity.setEnabled(true);
				}
			}
		};

		road.addObserver(roadObserver);
		Simulator.getInstance().addObserver(simulatorObserver);
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		road.deleteObserver(roadObserver);
		Simulator.getInstance().deleteObserver(simulatorObserver);
	}
}
