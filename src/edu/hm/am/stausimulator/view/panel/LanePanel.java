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
import edu.hm.am.stausimulator.model.Model;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.model.StartingLingerModel;

public class LanePanel extends JPanel {
	
	private static final long	serialVersionUID	= -4734523069383121819L;
	
	private Observer			laneObserver;
	private Observer			roadObserver;
	private Observer			simulatorObserver;
	
	private Road				road;
	
	private int					laneIndex;
	
	public LanePanel(Road road, int laneIndex) {
		this.road = road;
		this.laneIndex = laneIndex;
		
		setLayout(new MigLayout("insets 5", "[grow]", "[25px][grow]"));
		setBackground(Color.WHITE);
		
		JLabel lblStaringProbability = new JLabel("Starting Probability");
		JLabel lblProbability = new JLabel("Probability");
		JLabel lblPrevLane = new JLabel("Previous Lane");
		JLabel lblNextLane = new JLabel("Next Lane");
		
		lblStaringProbability.setForeground((road.getModel() instanceof StartingLingerModel) ? Color.BLACK : Color.GRAY);
		
		JSlider slStartingProbability = new JSlider();
		slStartingProbability.setBackground(Color.WHITE);
		slStartingProbability.setSnapToTicks(true);
		slStartingProbability.setMinorTickSpacing(1);
		slStartingProbability.setMajorTickSpacing(10);
		slStartingProbability.setEnabled(road.getModel() instanceof StartingLingerModel);
		slStartingProbability.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = slStartingProbability.getValue() / 100.0;
				lblStaringProbability.setText("Starting Probability " + value);
				getLane().setStartingProbability(value);
			}
		});
		slStartingProbability.setValue((int) (getLane().getStartingProbability() * 100));
		
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
				getLane().setLingerProbability(value);
			}
		});
		slProbability.setValue((int) (getLane().getLingerProbability() * 100));
		
		JComboBox<String> cbPrevLane = new JComboBox<>();
		// cbPrevLane.setEnabled(false);
		cbPrevLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) cbPrevLane.getSelectedItem();
				Lane prev = null;
				if (selected.startsWith("Lane ")) {
					prev = getLane().getRoad().getLanes().get(new Integer(selected.substring(selected.length() - 1)).intValue() - 1);
				}
				getLane().setPrev(prev);
			}
		});
		
		JComboBox<String> cbNextLane = new JComboBox<>();
		// cbNextLane.setEnabled(false);
		cbNextLane.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String) cbNextLane.getSelectedItem();
				Lane next = null;
				if (selected.startsWith("Lane ")) {
					next = getLane().getRoad().getLanes().get(new Integer(selected.substring(selected.length() - 1)).intValue() - 1);
				}
				getLane().setNext(next);
			}
		});
		
		cbPrevLane.addItem("---");
		cbNextLane.addItem("---");
		
		List<Lane> lanes = getLane().getRoad().getLanes();
		for (int i = 0; i < lanes.size(); i++) {
			cbPrevLane.addItem("Lane " + (i + 1));
			cbNextLane.addItem("Lane " + (i + 1));
		}
		cbPrevLane.setSelectedIndex(lanes.indexOf(getLane()) + 1);
		cbNextLane.setSelectedIndex(lanes.indexOf(getLane()) + 1);
		
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
		tabbedPane.addTab("VDR Diagramm", new VDRChartPanel(road, laneIndex));
		tabbedPane.addTab("VDR2 Diagramm", new VDRChartPanel2(road, laneIndex));
		tabbedPane.addTab("Stages", new StagePanel(road, laneIndex));
		tabbedPane.addTab("Average Velocity", new AVGSpeedChartPanel(road, laneIndex));
		tabbedPane.addTab("Average Flow", new AVGFlowChartPanel(road, laneIndex));
		
		add(settings, "cell 0 0,grow");
		add(tabbedPane, "cell 0 1,grow");
		
		laneObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Changed Prev/Next Lane".equals(arg)) {
					if (getLane().getPrev() != null) {
						cbPrevLane.setSelectedItem("Lane " + (road.getLanes().indexOf(getLane().getPrev()) + 1));
					}
					if (getLane().getNext() != null) {
						cbNextLane.setSelectedItem("Lane " + (road.getLanes().indexOf(getLane().getNext()) + 1));
					}
				}
			}
		};
		
		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Reset".equals(arg)) {
					slProbability.setValue((int) (getLane().getLingerProbability() * 100));
					slStartingProbability.setValue((int) (getLane().getStartingProbability() * 100));
					
				}
				else if ("Changed Model".equals(arg)) {
					Model model = road.getModel();
					lblStaringProbability.setForeground((model instanceof StartingLingerModel) ? Color.BLACK : Color.GRAY);
					slStartingProbability.setEnabled(model instanceof StartingLingerModel);
				}
			}
		};
		
		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Start".equals(arg)) {
					slProbability.setEnabled(false);
					slStartingProbability.setEnabled(false);
					cbPrevLane.setEnabled(false);
					cbNextLane.setEnabled(false);
				}
				else if ("Stop".equals(arg)) {
					slProbability.setEnabled(true);
					slStartingProbability.setEnabled(true);
					cbPrevLane.setEnabled(true);
					cbNextLane.setEnabled(true);
				}
			}
		};
		
		getLane().addObserver(laneObserver);
		road.addObserver(roadObserver);
		Simulator.getInstance().addObserver(simulatorObserver);
	}
	
	private Lane getLane() {
		return road.getLane(laneIndex);
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		
		getLane().deleteObserver(laneObserver);
		road.deleteObserver(roadObserver);
		Simulator.getInstance().deleteObserver(simulatorObserver);
	}
	
}
