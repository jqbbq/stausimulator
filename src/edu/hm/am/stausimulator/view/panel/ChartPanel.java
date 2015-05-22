package edu.hm.am.stausimulator.view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.Run;
import edu.hm.am.stausimulator.model.Road;

public class ChartPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4249332653340429468L;

	private JComboBox<Integer> roadSelectbox;
	private JComboBox<Integer> laneSelectbox;
	private JComboBox<Integer> chartSelectbox;

	private Run model;

	public ChartPanel(Run run) {
		model = run;

		setLayout(new MigLayout("gap 0", "[grow]", "[50px,fill][grow]"));

		JPanel settingsPanel = new JPanel();
		JPanel previewPanel = new JPanel();

		JLabel roadLabel = new JLabel();
		JLabel laneLabel = new JLabel();
		JLabel chartLabel = new JLabel();

		Integer[] values = new Integer[run.getRoads().size()];
		for (int i = 0; i < run.getRoads().size(); i++) {
			values[i] = i + 1;
		}

		roadSelectbox = new JComboBox<>(values);
		laneSelectbox = new JComboBox<>();
		chartSelectbox = new JComboBox<>();

		roadLabel.setText("Stra\u00DFe");
		roadLabel.setLabelFor(roadSelectbox);

		laneLabel.setText("Spur");
		laneLabel.setLabelFor(laneSelectbox);

		chartLabel.setText("Chart");
		chartLabel.setLabelFor(chartSelectbox);

		roadSelectbox.addActionListener(this);
		laneSelectbox.addActionListener(this);
		chartSelectbox.addActionListener(this);

		settingsPanel.setLayout(new MigLayout("insets 0", "[118px][118px][118px]", "[25px][25px]"));
		settingsPanel.add(roadLabel, "cell 0 0,grow");
		settingsPanel.add(laneLabel, "cell 1 0,grow");
		settingsPanel.add(chartLabel, "cell 2 0,grow");
		settingsPanel.add(roadSelectbox, "cell 0 1,grow");
		settingsPanel.add(laneSelectbox, "cell 1 1,grow");
		settingsPanel.add(chartSelectbox, "cell 2 1,grow");

		this.add(settingsPanel, "cell 0 0,grow");
		this.add(previewPanel, "cell 0 1,grow");

		roadSelectbox.setSelectedIndex(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(roadSelectbox)) {
			Road road = model.getRoad(roadSelectbox.getSelectedIndex());
			laneSelectbox.removeAllItems();
			for (int i = 0; i < road.getLanes().size(); i++) {
				laneSelectbox.addItem(new Integer(i + 1));
			}

		}

	}

}
