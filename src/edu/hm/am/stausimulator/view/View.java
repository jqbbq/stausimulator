package edu.hm.am.stausimulator.view;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.panel.CellularAutomataPanel;
import edu.hm.am.stausimulator.view.panel.ChartPanel;
import edu.hm.am.stausimulator.view.panel.SettingsPanel;

public class View {

	private Simulator simulator;

	private JFrame frame;

	private JPanel cellularLivePreviewPanel;
	private JPanel chartLivePreviewPanel;

	private JTabbedPane previewPanel;

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

		previewPanel = new JTabbedPane(JTabbedPane.TOP);

		cellularLivePreviewPanel = new JPanel();
		cellularLivePreviewPanel.setLayout(new ScrollPaneLayout());
		cellularLivePreviewPanel.setLayout(new GridLayout(0, 1, 0, 0));

		for (Road road : simulator.getRoads()) {
			cellularLivePreviewPanel.add(new CellularAutomataPanel(road));
		}

		chartLivePreviewPanel = new JPanel();
		chartLivePreviewPanel.setLayout(new MigLayout("gap 0, insets 0", "[grow,fill][grow,fill]", "[grow,fill]"));

		chartLivePreviewPanel.add(new ChartPanel(simulator.getModel()), "cell 0 0,grow");
		chartLivePreviewPanel.add(new ChartPanel(simulator.getModel()), "cell 1 0,grow");

		previewPanel.addTab("Cellular Live Preview", null, cellularLivePreviewPanel, null);
		previewPanel.addTab("Charts Live Preview", null, chartLivePreviewPanel, null);

		frame = new JFrame();
		frame.setTitle("Nagel-Schreckenberg Stausimulation");
		frame.setBounds(100, 100, 761, 503);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new MigLayout("", "[700px,grow]", "[60px][grow][350px]"));

		frame.add(new SettingsPanel(simulator), "cell 0 0,grow");
		frame.add(previewPanel, "cell 0 1 1 2,grow");

		simulator.getRunner().addRunnable(new Runnable() {
			@Override
			public void run() {
				cellularLivePreviewPanel.repaint();
			}
		});

		simulator.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				frame.repaint();
			}
		});

	}
}
