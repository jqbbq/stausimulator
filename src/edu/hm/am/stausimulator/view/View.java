package edu.hm.am.stausimulator.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneLayout;
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
import edu.hm.am.stausimulator.model.Road;

public class View implements ActionListener, ChangeListener {

	private Simulator simulator;

	private JFrame frame;

	private JPanel settingsPanel;
	private JPanel runPanel;
	private JPanel intervalPanel;
	private JPanel densityPanel;
	private JPanel probabilityPanel;
	private JPanel cellularLivePreviewPanel;

	private JLabel intervalLabel;
	private JLabel densityLabel;
	private JLabel probabilityLabel;
	
	private JSlider densitySlider;
	private JSlider probabilitySlider;
	
	private JTabbedPane previewPanel;
	
	private JPanel chartLivePreviewPanel;
	private JPanel chartPanel;

	private JButton runButton;
	private JSpinner intervalSpinner;
	private JComboBox<Integer> chartPreviewLaneSelectbox;
	
	private Runner runner;
	private JLabel runLabel;
	private JPanel chart1Panel;
	private JPanel chart2Panel;
	private JPanel chart1SettingsPanel;
	private JPanel chart1PreviewPanel;
	private JPanel chart2SettingsPanel;
	private JPanel chart2PreviewPanel;
	
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
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
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

		runLabel = new JLabel();
		runLabel.setText("Run "+simulator.getStep());
		
		runPanel = new JPanel();
		runPanel.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		runPanel.setLayout(new MigLayout("", "[50px,fill]", "[40px,fill][]"));
		
		runPanel.add(runButton, "cell 0 0,grow");
		runPanel.add(runLabel, "cell 0 1");

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
		

		previewPanel = new JTabbedPane(JTabbedPane.TOP);

		cellularLivePreviewPanel = new JPanel();
		cellularLivePreviewPanel.setLayout(new ScrollPaneLayout());
		cellularLivePreviewPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		for(Road road:simulator.getRoads()){
			cellularLivePreviewPanel.add(new CellularAutomataPanel(road));
		}
		
		chartLivePreviewPanel = new JPanel();
		chartLivePreviewPanel.setLayout(new MigLayout("gap 0 0", "[grow,fill][grow,fill]", "[grow,fill]"));
		
		chart1Panel = new JPanel();
		chartLivePreviewPanel.add(chart1Panel, "cell 0 0,grow");
		chart1Panel.setLayout(new MigLayout("", "[grow]", "[25px,fill][grow]"));
		
		chart1SettingsPanel = new JPanel();
		chart1Panel.add(chart1SettingsPanel, "cell 0 0,grow");
		
		chart1PreviewPanel = new JPanel();
		chart1Panel.add(chart1PreviewPanel, "cell 0 1,grow");
		
		chart2Panel = new JPanel();
		chartLivePreviewPanel.add(chart2Panel, "cell 1 0,grow");
		chart2Panel.setLayout(new MigLayout("", "[grow]", "[25px,fill][grow]"));
		
		chart2SettingsPanel = new JPanel();
		chart2Panel.add(chart2SettingsPanel, "cell 0 0,grow");
		
		chart2PreviewPanel = new JPanel();
		chart2Panel.add(chart2PreviewPanel, "cell 0 1,grow");

		previewPanel.addTab("Cellular Live Preview", null, cellularLivePreviewPanel, null);
		previewPanel.addTab("Charts Live Preview", null, chartLivePreviewPanel, null);
		
		frame = new JFrame();
		frame.setTitle("Nagel-Schreckenberg Stausimulation");
		frame.setBounds(100, 100, 761, 503);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new MigLayout("", "[700px,grow]", "[60px][grow][350px]"));
		frame.getContentPane().add(settingsPanel, "cell 0 0,grow");
		frame.getContentPane().add(previewPanel, "cell 0 1 1 2,grow");
		
		
		runner = new Runner("Main", ((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue(), new Runnable() {
			@Override
			public void run() {
				simulator.nextStep();
				runLabel.setText("Run "+simulator.getStep());
				frame.repaint();
			}
		});
		runner.start();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		double value = 0;
		
		if (e.getSource().equals(intervalSpinner)) {
			
			runner.setInterval(((SpinnerNumberModel) intervalSpinner.getModel()).getNumber().intValue());
			
		}
		else if (e.getSource().equals(densitySlider)) {
			
			value = densitySlider.getValue() / 100.0;
			Configuration.putProperty(Property.DENSITY, value);
			densityLabel.setText("Dichte (" + value + ")");
			
		}
		else if (e.getSource().equals(probabilitySlider)) {
			
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

			densitySlider.setEnabled(!paused);
			// probabilitySlider.setEnabled(!paused);

			if (paused) {
				runner.resume();
			}
			else {
				runner.pause();
			}
		}
	}
}
