package edu.hm.am.stausimulator.view;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;

import edu.hm.am.stausimulator.Defaults;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.chart.FlowFundamentalChart;
import edu.hm.am.stausimulator.chart.SpeedFundamentalChart;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.panel.RoadPanel;

public class View {
	
	// TODO: Remove debug path
	private JFileChooser	fileChooser;
	
	private JFrame			frame;
	
	private JMenu			mnActions;
	
	private JMenuItem		mntmSave;
	private JMenuItem		mntmReset;
	private JMenuItem		mntmAddRoad;
	private JMenuItem		mntmRemoveRoad;
	private JMenuItem		mntmImport;
	private JMenuItem		mntmExport;
	private JMenuItem		mntmFlowFD;
	private JMenuItem		mntmSpeedFD;
	
	private JButton			btnSingleStep;
	private JButton			btnRun;
	
	private JSpinner		spSteps;
	private JSpinner		spInterval;
	
	private Simulator		instance;
	
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
		
		instance = Simulator.getInstance();
		
		fileChooser = new JFileChooser(new File("C:/Users/Luca/Documents/Studium/Angewandte Mathematik/5. Nagel-Schreckenberg/data"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		JMenuBar menuBar = new JMenuBar();
		mnActions = new JMenu("Actions");
		
		mntmSave = new JMenuItem("Save");
		mntmReset = new JMenuItem("Reset");
		mntmAddRoad = new JMenuItem("Add Road");
		mntmRemoveRoad = new JMenuItem("Delete Road");
		mntmImport = new JMenuItem("Import");
		mntmExport = new JMenuItem("Export");
		mntmFlowFD = new JMenuItem("Flow FD");
		mntmSpeedFD = new JMenuItem("Speed FD");
		
		btnSingleStep = new JButton();
		btnSingleStep.setToolTipText("Single Step");
		btnSingleStep.setIcon(new ImageIcon(ImageLoader.get("step.png")));
		
		btnRun = new JButton();
		btnRun.setToolTipText("Play");
		btnRun.setIcon(new ImageIcon(ImageLoader.get("play.png")));
		
		spSteps = new JSpinner();
		spSteps.setModel(new SpinnerNumberModel(Defaults.ROUNDS, 0, 1000, 100));
		spSteps.setToolTipText("Steps");
		
		spInterval = new JSpinner();
		spInterval.setModel(new SpinnerNumberModel(Defaults.INTERVAL, 10, 2000, 10));
		spInterval.setToolTipText("Interval");
		spInterval.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				instance.setInterval(((Integer) spInterval.getValue()).intValue());
			}
		});
		
		JLabel lblSteps = new JLabel("Steps");
		JLabel lblInterval = new JLabel("Interval");
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0", "[20px,fill][20px,fill][25px,fill][75px,fill][25px,fill][75px,fill]", "[20px]"));
		panel.add(btnSingleStep, "cell 0 0,grow");
		panel.add(btnRun, "cell 1 0,grow");
		panel.add(lblSteps, "cell 2 0,grow");
		panel.add(spSteps, "cell 3 0,grow");
		panel.add(lblInterval, "cell 4 0,grow");
		panel.add(spInterval, "cell 5 0,grow");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		mnActions.add(mntmSave);
		mnActions.add(mntmReset);
		mnActions.add(mntmAddRoad);
		mnActions.add(mntmRemoveRoad);
		mnActions.add(mntmImport);
		mnActions.add(mntmExport);
		mnActions.add(mntmFlowFD);
		mnActions.add(mntmSpeedFD);
		
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int returnVal = fileChooser.showOpenDialog(frame);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File directory = fileChooser.getSelectedFile();
					instance.save(directory);
					JOptionPane.showMessageDialog(frame, "Saved");
				}
			}
		});
		mntmAddRoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Road road = new Road();
				RoadPanel panel = new RoadPanel(road);
				instance.addRoad(road);
				tabbedPane.addTab("Road " + (tabbedPane.getTabCount() + 1), panel);
			}
		});
		mntmRemoveRoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.removeRoad(tabbedPane.getSelectedIndex());
				tabbedPane.remove(tabbedPane.getSelectedIndex());
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					tabbedPane.setTitleAt(i, "Road " + (i + 1));
				}
			}
		});
		
		mntmFlowFD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Flow Fundamental Diagramm");
				JProgressBar progressbar = new JProgressBar(JProgressBar.HORIZONTAL);
				
				JLabel status = new JLabel();
				
				frame.setLayout(new GridLayout(2, 1));
				progressbar.setIndeterminate(true);
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						JFreeChart chart = FlowFundamentalChart.draw(status);
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						
						Frame f = new ChartFrame("Flow Fundamental Diagramm", chart);
						f.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent we) {
								chart.getXYPlot().setDataset(null);
								f.removeAll();
								f.dispose();
								
								System.gc();
							}
						});
						f.pack();
						f.setVisible(true);
					}
				});
				
				frame.add(status);
				frame.add(progressbar);
				
				frame.pack();
				frame.setVisible(true);
				
				thread.start();
			}
		});
		
		mntmSpeedFD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame("Speed Fundamental Diagramm");
				JProgressBar progressbar = new JProgressBar(JProgressBar.HORIZONTAL);
				
				JLabel status = new JLabel();
				
				frame.setLayout(new GridLayout(2, 1));
				progressbar.setIndeterminate(true);
				
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						JFreeChart chart = SpeedFundamentalChart.draw(status);
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
						
						Frame f = new ChartFrame("Speed Fundamental Diagramm", chart);
						f.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosing(WindowEvent we) {
								f.dispose();
							}
						});
						f.pack();
						f.setVisible(true);
					}
				});
				
				frame.add(status);
				frame.add(progressbar);
				
				frame.pack();
				frame.setVisible(true);
				
				thread.start();
			}
		});
		
		menuBar.add(mnActions);
		
		for (int i = 0; i < instance.getRoads().size(); i++) {
			tabbedPane.addTab("Road " + (i + 1), new RoadPanel(instance.getRoads().get(i)));
		}
		
		frame = new JFrame();
		frame.setTitle("Nagel-Schreckenberg Stausimulation");
		frame.setBounds(10, 10, 1200, 700);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().setLayout(new MigLayout("insets 5", "[grow]", "[25px][grow]"));
		frame.getContentPane().add(panel, "cell 0 0");
		frame.getContentPane().add(tabbedPane, "cell 0 1,grow");
		
		btnSingleStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				instance.nextStep();
			}
		});
		
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (instance.isRunning()) {
					instance.stop();
				}
				else {
					instance.start(((Integer) spSteps.getValue()).intValue());
				}
				updateUI();
			}
		});
		
		instance.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				updateUI();
			}
		});
	}
	
	private void updateUI() {
		boolean stopped = !instance.isRunning();
		mntmSave.setEnabled(stopped);
		mntmReset.setEnabled(stopped);
		mntmAddRoad.setEnabled(stopped);
		mntmRemoveRoad.setEnabled(stopped);
		mntmImport.setEnabled(stopped);
		mntmExport.setEnabled(stopped);
		spSteps.setEnabled(stopped);
		spInterval.setEnabled(stopped);
		btnSingleStep.setEnabled(stopped);
		
		btnRun.setToolTipText(stopped ? "Start" : "Stop");
		btnRun.setIcon(new ImageIcon(ImageLoader.get(stopped ? "play.png" : "stop.png")));
	}
}
