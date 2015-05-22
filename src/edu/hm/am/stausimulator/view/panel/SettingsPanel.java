package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Configuration;
import edu.hm.am.stausimulator.Property;
import edu.hm.am.stausimulator.Runner;
import edu.hm.am.stausimulator.Simulator;

public class SettingsPanel extends JPanel implements ActionListener, ChangeListener {

	private static final long serialVersionUID = 2638822354647768192L;

	private JPanel runPanel;
	private JPanel roadsLanesPanel;
	private JPanel intervalPanel;
	private JPanel densityPanel;
	private JPanel probabilityPanel;

	private JLabel runLabel;
	private JLabel roadLabel;
	private JLabel laneLabel;
	private JLabel intervalLabel;
	private JLabel densityLabel;
	private JLabel probabilityLabel;

	private JComboBox<Integer> roadsSelectbox;
	private JComboBox<Integer> lanesSelectbox;

	private JSlider densitySlider;
	private JSlider probabilitySlider;

	private JButton runButton;
	private JSpinner intervalSpinner;

	private Simulator simulator;
	private Runner runner;

	public SettingsPanel(Simulator simulator) {
		this.simulator = simulator;
		runner = simulator.getRunner();

		runLabel = new JLabel();
		runLabel.setText("Run " + simulator.getStep());

		runButton = new JButton("Run");
		runButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		runButton.addActionListener(this);

		runPanel = new JPanel();
		runPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		runPanel.setLayout(new MigLayout("", "[50px,fill]", "[25px,fill][25px,fill]"));

		runPanel.add(runLabel, "cell 0 0");
		runPanel.add(runButton, "cell 0 1,grow");

		intervalLabel = new JLabel("Interval (ms)");
		intervalLabel.setHorizontalAlignment(SwingConstants.CENTER);

		intervalSpinner = new JSpinner();
		intervalSpinner.addChangeListener(this);
		intervalSpinner.setModel(new SpinnerNumberModel(1000, 100, 2000, 100));

		intervalPanel = new JPanel();
		intervalPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		intervalPanel.setLayout(new MigLayout("", "[50px]", "[25px][25px]"));
		intervalPanel.add(intervalLabel, "cell 0 0,alignx center,aligny center");
		intervalPanel.add(intervalSpinner, "cell 0 1,alignx center,aligny center");

		roadLabel = new JLabel("Stra\u00DFen");
		roadLabel.setHorizontalAlignment(SwingConstants.CENTER);

		laneLabel = new JLabel("Spuren");
		laneLabel.setHorizontalAlignment(SwingConstants.CENTER);

		Integer[] roads = new Integer[3];
		Integer[] lanes = new Integer[6];
		for (int i = 0; i < 3; i++) {
			roads[i] = i + 1;
		}
		for (int i = 0; i < 6; i++) {
			lanes[i] = i + 1;
		}

		roadsSelectbox = new JComboBox<>(roads);
		roadsSelectbox.setSelectedIndex(Configuration.getProperty(Property.ROADS).intValue() - 1);
		roadsSelectbox.addActionListener(this);

		lanesSelectbox = new JComboBox<>(lanes);
		lanesSelectbox.setSelectedIndex(Configuration.getProperty(Property.LANES).intValue() - 1);
		lanesSelectbox.addActionListener(this);

		roadsLanesPanel = new JPanel();
		roadsLanesPanel.setLayout(new MigLayout("", "[50px,grow][50px,grow]", "[25px][25px]"));
		roadsLanesPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));

		roadsLanesPanel.add(roadsSelectbox, "cell 0 1,grow");
		roadsLanesPanel.add(roadLabel, "cell 0 0,alignx center");
		roadsLanesPanel.add(laneLabel, "cell 1 0,alignx center");
		roadsLanesPanel.add(lanesSelectbox, "cell 1 1,grow");

		densityLabel = new JLabel();
		densityLabel.setHorizontalAlignment(SwingConstants.CENTER);

		densitySlider = new JSlider();
		densitySlider.setMinorTickSpacing(1);
		densitySlider.setMajorTickSpacing(10);
		densitySlider.setSnapToTicks(true);
		densitySlider.addChangeListener(this);
		densitySlider.setValue((int) (Configuration.getProperty(Property.DENSITY).doubleValue() * 100));

		densityPanel = new JPanel();
		densityPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		densityPanel.setLayout(new GridLayout(2, 1, 0, 0));
		densityPanel.add(densityLabel);
		densityPanel.add(densitySlider);

		probabilityLabel = new JLabel();
		probabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);

		probabilitySlider = new JSlider();
		probabilitySlider.addChangeListener(this);
		probabilitySlider.setSnapToTicks(true);
		probabilitySlider.setMinorTickSpacing(1);
		probabilitySlider.setMajorTickSpacing(10);
		probabilitySlider.setValue((int) (Configuration.getProperty(Property.PROBABILITY).doubleValue() * 100));

		probabilityPanel = new JPanel();
		probabilityPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		probabilityPanel.setLayout(new GridLayout(2, 1, 0, 0));
		probabilityPanel.add(probabilityLabel);
		probabilityPanel.add(probabilitySlider);

		setBorder(new LineBorder(Color.GRAY));
		setLayout(new MigLayout("gap 0,insets 0", "[50px][50px][100px][150px][150px]", "[grow,fill]"));

		add(runPanel, "cell 0 0");
		add(intervalPanel, "cell 1 0");
		add(roadsLanesPanel, "cell 2 0");
		add(densityPanel, "cell 3 0,alignx left,aligny center");
		add(probabilityPanel, "cell 4 0,alignx left,aligny center");

		runner.addRunnable(new Runnable() {
			@Override
			public void run() {
				runLabel.setText("Run " + simulator.getStep());
			}
		});
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		double value = 0;

		if (e.getSource().equals(intervalSpinner)) {

			runner.setInterval(((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue());

		} else if (e.getSource().equals(densitySlider)) {

			value = densitySlider.getValue() / 100.0;
			Configuration.putProperty(Property.DENSITY, value);
			densityLabel.setText("Dichte (" + value + ")");

		} else if (e.getSource().equals(probabilitySlider)) {

			value = probabilitySlider.getValue() / 100.0;
			Configuration.putProperty(Property.PROBABILITY, value);
			probabilityLabel.setText("Trödelfaktor (" + value + ")");

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(runButton)) {

			boolean paused = runButton.getText().equals("Run");
			runButton.setText(paused ? "Stop" : "Run");

			roadsSelectbox.setEditable(!paused);
			lanesSelectbox.setEditable(!paused);
			densitySlider.setEnabled(!paused);

			if (paused) {
				runner.resume();
			} else {
				runner.pause();
			}

		}/*
		 * else if (e.getSource().equals(roadsSelectbox)) {
		 * 
		 * Configuration.putProperty(Property.ROADS, ((Number)
		 * roadsSelectbox.getSelectedItem()).intValue()); simulator.reset();
		 * 
		 * } else if (e.getSource().equals(lanesSelectbox)) {
		 * 
		 * Configuration.putProperty(Property.LANES, ((Number)
		 * lanesSelectbox.getSelectedItem()).intValue()); simulator.reset();
		 * 
		 * }
		 * 
		 * simulator.notifyObservers(Simulator.RESET);
		 */
	}

}
