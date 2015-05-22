package edu.hm.am.stausimulator.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Configuration;
import edu.hm.am.stausimulator.Property;
import edu.hm.am.stausimulator.Simulator;

public class View implements ActionListener, ChangeListener {

	private Simulator simulator;

	private JFrame frame;

	private JPanel settingsPanel;
	private JPanel runPanel;
	private JPanel intervalPanel;
	private JPanel densityPanel;
	private JPanel probabilityPanel;
	private JPanel previewPanel;

	private JButton runButton;

	private JSpinner intervalSpinner;

	private JLabel intervalLabel;
	private JLabel densityLabel;
	private JLabel probabilityLabel;

	private JSlider densitySlider;
	private JSlider probabilitySlider;

	private Runner runner;

	private int interval;

	private boolean running;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		simulator = new Simulator();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		intervalLabel = new JLabel("Interval (ms)");
		intervalLabel.setHorizontalAlignment(SwingConstants.CENTER);

		intervalSpinner = new JSpinner();
		intervalSpinner.addChangeListener(this);
		intervalSpinner.setModel(new SpinnerNumberModel(1000, 100, 2000, 100));

		intervalPanel = new JPanel();
		intervalPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		intervalPanel.setLayout(new MigLayout("", "[94px]", "[14px][20px]"));
		intervalPanel.add(intervalLabel, "cell 0 0,alignx center,aligny center");
		intervalPanel.add(intervalSpinner, "cell 0 1,alignx center,aligny center");

		runButton = new JButton("Run");
		runButton.setFont(new Font("Tahoma", Font.PLAIN, 10));
		runButton.addActionListener(this);

		runPanel = new JPanel();
		runPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		runPanel.setLayout(new MigLayout("", "[50px,fill]", "[40px,fill]"));
		runPanel.add(runButton, "cell 0 0,grow");

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
		probabilityPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		probabilityPanel.setLayout(new GridLayout(2, 1, 0, 0));
		probabilityPanel.add(probabilityLabel);
		probabilityPanel.add(probabilitySlider);

		settingsPanel = new JPanel();
		settingsPanel.setBorder(new LineBorder(Color.GRAY));
		settingsPanel.setLayout(new MigLayout("", "[65px][108px][200px][200px]", "[54px]"));

		settingsPanel.add(runPanel, "cell 0 0,alignx left,aligny top");
		settingsPanel.add(intervalPanel, "cell 1 0,alignx left,aligny center");
		settingsPanel.add(densityPanel, "cell 2 0,alignx left,aligny center");
		settingsPanel.add(probabilityPanel, "cell 3 0,alignx left,aligny center");

		previewPanel = new JPanel();
		previewPanel.setBorder(new LineBorder(Color.GRAY));
		previewPanel.add(new CellularAutomataPanel(simulator, 700, 323));

		frame = new JFrame();
		frame.setTitle("Nagel-Schreckenberg Stausimulation");
		frame.setBounds(100, 100, 728, 477);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[700px]", "[60px][350px]"));
		frame.getContentPane().add(settingsPanel, "cell 0 0,grow");
		frame.getContentPane().add(previewPanel, "cell 0 1,grow");

		running = false;
		interval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();

		runner = new Runner("Main");
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		double value = 0;
		if (e.getSource().equals(intervalSpinner)) {
			interval = ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue();
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
			running = runButton.getText().equals("Run");
			runButton.setText(running ? "Stop" : "Run");

			densitySlider.setEnabled(!running);
			probabilitySlider.setEnabled(!running);
		}
	}

	private class Runner implements Runnable {

		private Thread t;
		private String threadName;

		Runner(String name) {
			System.out.println("Creating " + threadName);
			threadName = name;
			t = new Thread(this, threadName);
			t.start();
		}

		@Override
		public void run() {
			System.out.println("Running " + threadName);
			try {
				while (true) {
					if (running) {
						simulator.nextStep();
						frame.repaint();
					}
					Thread.sleep(interval);
				}
			} catch (InterruptedException e) {
				System.out.println("Thread " + threadName + " interrupted.");
			}
			System.out.println("Thread " + threadName + " exiting.");
		}

	}
}
