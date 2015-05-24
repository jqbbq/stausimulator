package edu.hm.am.stausimulator.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.panel.RoadPanel;

public class ViewTest {

	private Simulator instance;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ViewTest window = new ViewTest();
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
	public ViewTest() {
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

		JMenuBar menuBar = new JMenuBar();
		JMenu mnActions = new JMenu("Actions");

		JMenuItem mntmReset = new JMenuItem("Reset");
		JMenuItem mntmAddRoad = new JMenuItem("Add Road");
		JMenuItem mntmRemoveRoad = new JMenuItem("Delete Road");
		JMenuItem mntmImport = new JMenuItem("Import");
		JMenuItem mntmExport = new JMenuItem("Export");

		JButton btnSingleStep = new JButton("Single Step");
		JButton btnRun = new JButton("Run");

		JLabel lblInterval = new JLabel("Interval");

		JSpinner spInterval = new JSpinner();
		spInterval.setModel(new SpinnerNumberModel(1000, 100, 2000, 100));
		spInterval.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				instance.setInterval(((Integer) spInterval.getValue()).intValue());
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0", "[75px,fill][75px,fill][50px,fill][75px,fill]", "[25px]"));
		panel.add(btnSingleStep, "cell 0 0");
		panel.add(btnRun, "cell 1 0");
		panel.add(lblInterval, "cell 2 0");
		panel.add(spInterval, "cell 3 0");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		mnActions.add(mntmReset);
		mnActions.add(mntmAddRoad);
		mnActions.add(mntmRemoveRoad);
		mnActions.add(mntmImport);
		mnActions.add(mntmExport);

		mntmAddRoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Road road = new Road(4);
				RoadPanel panel = new RoadPanel(road);
				Simulator.getInstance().addRoad(road);
				tabbedPane.addTab("Road " + (tabbedPane.getTabCount() + 1), panel);
			}
		});
		mntmRemoveRoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.remove(tabbedPane.getSelectedIndex());
			}
		});

		menuBar.add(mnActions);

		for (Road road : instance.getRoads()) {
			tabbedPane.addTab("Road " + road.getId(), new RoadPanel(road));
		}

		frame = new JFrame();
		frame.setTitle("Nagel-Schreckenberg Stausimulation");
		frame.setBounds(100, 100, 1000, 600);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.getContentPane().setLayout(new MigLayout("insets 5", "[1000px]", "[25px][grow]"));
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
				boolean running = instance.isRunning();

				if (running) {
					instance.pause();
				} else {
					instance.start();
				}

				btnRun.setText(running ? "Run" : "Pause");
			}
		});
	}
}
