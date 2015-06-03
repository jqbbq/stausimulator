package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Simulator;
import edu.hm.am.stausimulator.model.Road;
import edu.hm.am.stausimulator.view.ImageLoader;

public class RoadPanel extends JPanel {

	private static final long serialVersionUID = -6399513557243691428L;

	private JComboBox<Integer> cbLanes;
	private JComboBox<Integer> cbCells;
	private JComboBox<Integer> cbMaxSpeed;

	private JSlider slDensity = new JSlider();

	private JCheckBox cbAllowLaneChange;

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

		JLabel lblStep = new JLabel("Step " + road.getStep());
		JLabel lblLanes = new JLabel("Lanes");
		JLabel lblCells = new JLabel("Cells");
		JLabel lblMaxSpped = new JLabel("Max Speed");
		JLabel lblDensity = new JLabel("Density");
		JLabel lblAllowLaneChange = new JLabel("Allow Lane change?");

		cbLanes = new JComboBox<>();
		cbCells = new JComboBox<>();
		cbMaxSpeed = new JComboBox<>();

		slDensity = new JSlider();

		JButton btnReset = new JButton("Reset Road", new ImageIcon(ImageLoader.get("reset.png")));

		JTabbedPane tabbedPane = new JTabbedPane();

		cbLanes.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5 }));
		cbLanes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbLanes.getSelectedItem();
				road.setLanes(value.intValue());
			}
		});

		cbCells.setModel(new DefaultComboBoxModel<>(new Integer[] { 50, 100, 150, 200, 300, 400, 500, 1000, 1500, 2000 }));
		cbCells.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbCells.getSelectedItem();
				road.setCells(value.intValue());
			}
		});

		cbMaxSpeed.setModel(new DefaultComboBoxModel<>(new Integer[] { 1, 2, 3, 4, 5 }));
		cbMaxSpeed.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer value = (Integer) cbMaxSpeed.getSelectedItem();
				road.setMaxVelocity(value.intValue());
			}
		});

		slDensity.setBackground(Color.WHITE);
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

		cbAllowLaneChange = new JCheckBox();
		cbAllowLaneChange.setSelected(false);
		cbAllowLaneChange.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				road.setAllowLaneChange(cbAllowLaneChange.isSelected());
			}
		});

		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				road.reset();

				lblStep.setText("Road " + road.getStep());

				update();
			}
		});

		JPanel settings = new JPanel();
		settings.setLayout(new MigLayout("insets 0", "[50px][50px][50px][150px][150px]", "[15px][25px][25px]"));
		settings.setBackground(Color.WHITE);
		settings.add(lblStep, "cell 0 0,alignx center");
		settings.add(btnReset, "cell 1 0 2 1,grow");
		settings.add(lblLanes, "cell 0 1,alignx center");
		settings.add(lblCells, "cell 1 1,alignx center");
		settings.add(lblMaxSpped, "cell 2 1,alignx center");
		settings.add(lblDensity, "cell 3 1,alignx center");
		settings.add(lblAllowLaneChange, "cell 4 1,alignx center");
		settings.add(cbLanes, "cell 0 2,growx");
		settings.add(cbCells, "cell 1 2,growx");
		settings.add(cbMaxSpeed, "cell 2 2,growx");
		settings.add(slDensity, "cell 3 2,growx");
		settings.add(cbAllowLaneChange, "cell 4 2,alignx center");

		tabbedPane.addTab("Cellular", null, new CellularAutomataPanel(road), null);

		add(settings, "cell 0 0,grow");
		add(tabbedPane, "cell 0 1,grow");

		roadObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if ("Changed Lanes".equals(arg)) {
					int count = tabbedPane.getTabCount();
					for (int i = 1; i < count; i++) {
						tabbedPane.remove(1);
					}
					for (int i = 0; i < road.getLanes().size(); i++) {
						tabbedPane.addTab("Lane " + (i + 1), null, new LanePanel(road.getLanes().get(i)), null);
					}
				} else if ("Reset".equals(arg)) {

					cbLanes.setEnabled(true);
					cbCells.setEnabled(true);
					cbMaxSpeed.setEnabled(true);
					slDensity.setEnabled(true);
					cbAllowLaneChange.setEnabled(true);

				}
			}
		};

		simulatorObserver = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				cbLanes.setEnabled(false);
				cbCells.setEnabled(false);
				cbMaxSpeed.setEnabled(false);
				slDensity.setEnabled(false);
				cbAllowLaneChange.setEnabled(false);

				if ("Step".equals(arg)) {
					lblStep.setText("Step " + road.getStep());
				}
			}
		};

		road.addObserver(roadObserver);
		Simulator.getInstance().addObserver(simulatorObserver);

		update();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		road.deleteObserver(roadObserver);
		Simulator.getInstance().deleteObserver(simulatorObserver);
	}

	private void update() {
		cbLanes.setSelectedItem(road.getLanes().size());
		cbCells.setSelectedItem(road.getNumberOfCells());
		cbMaxSpeed.setSelectedItem(road.getMaxVelocity());
	}
}
