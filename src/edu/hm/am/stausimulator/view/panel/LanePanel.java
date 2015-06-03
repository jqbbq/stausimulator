package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

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

public class LanePanel extends JPanel {

	private static final long serialVersionUID = -4734523069383121819L;

	private Observer laneObserver;
	private Observer roadObserver;
	private Observer simulatorObserver;

	private Lane lane;
	private Road road;

	public LanePanel(Lane lane) {
		this.lane = lane;
		road = lane.getRoad();

		setLayout(new MigLayout("insets 5", "[grow]", "[25px][grow]"));
		setBackground(Color.WHITE);

		JLabel lblStaringProbability = new JLabel("Starting Probability");
		JLabel lblProbability = new JLabel("Probability");
		JLabel lblPrevLane = new JLabel("Previous Lane");
		JLabel lblNextLane = new JLabel("Next Lane");

		JSlider slStartingProbability = new JSlider();
		slStartingProbability.setBackground(Color.WHITE);
		slStartingProbability.setSnapToTicks(true);
		slStartingProbability.setMinorTickSpacing(1);
		slStartingProbability.setMajorTickSpacing(10);
		slStartingProbability.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slStartingProbability.getValue() / 100.0;
				lblStaringProbability.setText("Starting Probability " + value);
				lane.setStartingProbability(value);
			}
		});
		slStartingProbability.setValue((int) (lane.getStartingProbability() * 100));

		JSlider slProbability = new JSlider();
		slProbability.setBackground(Color.WHITE);
		slProbability.setSnapToTicks(true);
		slProbability.setMinorTickSpacing(1);
		slProbability.setMajorTickSpacing(10);
		slProbability.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slProbability.getValue() / 100.0;
				lblProbability.setText("Probability " + value);
				lane.setLingerProbability(value);
			}
		});
		slProbability.setValue((int) (lane.getLingerProbability() * 100));

		JComboBox<String> cbPrevLane = new JComboBox<>();
		cbPrevLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) cbPrevLane.getSelectedItem();
				Lane prev = null;
				if (selected.startsWith("Lane ")) {
					prev = lane.getRoad().getLanes().get(new Integer(selected.substring(selected.length() - 1)).intValue() - 1);
				}
				lane.setPrev(prev);
			}
		});

		JComboBox<String> cbNextLane = new JComboBox<>();
		cbNextLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) cbNextLane.getSelectedItem();
				Lane next = null;
				if (selected.startsWith("Lane ")) {
					next = lane.getRoad().getLanes().get(new Integer(selected.substring(selected.length() - 1)).intValue() - 1);
				}
				lane.setNext(next);
			}
		});

		cbPrevLane.addItem("---");
		cbNextLane.addItem("---");

		List<Lane> lanes = lane.getRoad().getLanes();
		for (int i = 0; i < lanes.size(); i++) {
			cbPrevLane.addItem("Lane " + (i + 1));
			cbNextLane.addItem("Lane " + (i + 1));
		}
		cbPrevLane.setSelectedIndex(lanes.indexOf(lane) + 1);
		cbNextLane.setSelectedIndex(lanes.indexOf(lane) + 1);

		JPanel settings = new JPanel();
		settings.setLayout(new MigLayout("insets 0", "[150px][150px][100px][100px]", "[25px][25px]"));
		settings.setBackground(Color.WHITE);
		settings.add(lblStaringProbability, "cell 0 0, alignx center");
		settings.add(lblProbability, "cell 1 0,alignx center");
		settings.add(lblPrevLane, "cell 2 0,alignx center");
		settings.add(lblNextLane, "cell 3 0,alignx center");
		settings.add(slStartingProbability, "cell 0 1");
		settings.add(slProbability, "cell 1 1");
		settings.add(cbPrevLane, "cell 2 1,grow");
		settings.add(cbNextLane, "cell 3 1,grow");

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("VDR Diagramm", new VDRChartPanel(lane));
		tabbedPane.addTab("VDR2 Diagramm", new VDRChartPanel2(lane));
		tabbedPane.addTab("Stages", new StagePanel(lane));

		add(settings, "cell 0 0,grow");
		add(tabbedPane, "cell 0 1,grow");

		laneObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if (lane.getPrev() != null) {
					cbPrevLane.setSelectedItem("Lane " + (lanes.indexOf(lane.getPrev()) + 1));
				}
				if (lane.getNext() != null) {
					cbNextLane.setSelectedItem("Lane " + (lanes.indexOf(lane.getNext()) + 1));
				}
			}
		};

		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				cbPrevLane.setEnabled(!road.isAllowLaneChange());
				cbNextLane.setEnabled(!road.isAllowLaneChange());
			}
		};

		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					slProbability.setEnabled(false);
					slStartingProbability.setEnabled(false);
					cbNextLane.setEnabled(false);
				} else if ("Stop".equals(arg)) {
					slProbability.setEnabled(true);
					slStartingProbability.setEnabled(true);
					cbNextLane.setEnabled(true);
				}
			}
		};

		lane.addObserver(laneObserver);
		road.addObserver(roadObserver);
		Simulator.getInstance().addObserver(simulatorObserver);
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();

		lane.deleteObserver(laneObserver);
		road.deleteObserver(roadObserver);
		Simulator.getInstance().deleteObserver(simulatorObserver);
	}

}
