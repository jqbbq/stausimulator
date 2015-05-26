package edu.hm.am.stausimulator.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Defaults;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.panel.RoadPanel;

public class View {

	// TODO: Remove debug path
	private final JFileChooser fileChooser = new JFileChooser(new File("C:/Users/Luca/Documents/Studium/Angewandte Mathematik/5. Nagel-Schreckenberg/data"));

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

		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		JMenuBar menuBar = new JMenuBar();
		JMenu mnActions = new JMenu("Actions");

		JMenuItem mntmSave = new JMenuItem("Save");
		JMenuItem mntmReset = new JMenuItem("Reset");
		JMenuItem mntmAddRoad = new JMenuItem("Add Road");
		JMenuItem mntmRemoveRoad = new JMenuItem("Delete Road");
		JMenuItem mntmImport = new JMenuItem("Import");
		JMenuItem mntmExport = new JMenuItem("Export");

		JButton btnSingleStep = new JButton();
		btnSingleStep.setToolTipText("Single Step");
		btnSingleStep.setIcon(new ImageIcon(ImageLoader.get("step.png")));

		JButton btnRun = new JButton();
		btnRun.setToolTipText("Play");
		btnRun.setIcon(new ImageIcon(ImageLoader.get("play.png")));

		JSpinner spInterval = new JSpinner();
		spInterval.setModel(new SpinnerNumberModel(Defaults.INTERVAL, 10, 2000, 10));
		spInterval.setToolTipText("Interval");
		spInterval.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				instance.setInterval(((Integer) spInterval.getValue()).intValue());
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0", "[20px,fill][20px,fill][75px,fill]", "[20px]"));
		panel.add(btnSingleStep, "cell 0 0,grow");
		panel.add(btnRun, "cell 1 0,grow");
		panel.add(spInterval, "cell 2 0,grow");

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		mnActions.add(mntmSave);
		mnActions.add(mntmReset);
		mnActions.add(mntmAddRoad);
		mnActions.add(mntmRemoveRoad);
		mnActions.add(mntmImport);
		mnActions.add(mntmExport);

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
		frame.setBounds(10, 10, 1200, 700);
		frame.setResizable(false);
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
				boolean running = instance.isRunning();

				mntmSave.setEnabled(running);
				mntmReset.setEnabled(running);
				mntmAddRoad.setEnabled(running);
				mntmRemoveRoad.setEnabled(running);
				mntmImport.setEnabled(running);
				mntmExport.setEnabled(running);
				spInterval.setEnabled(running);
				btnSingleStep.setEnabled(running);

				btnRun.setToolTipText(running ? "Start" : "Stop");
				btnRun.setIcon(new ImageIcon(ImageLoader.get(running ? "play.png" : "stop.png")));

				if (running) {
					instance.stop();
				} else {
					instance.start();
				}

			}
		});
	}
}
