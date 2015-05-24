package edu.hm.am.stausimulator.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import edu.hm.am.stausimulator.data.RoadData;

public class ChartPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4249332653340429468L;

	private RoadData data;

	private JComboBox<Integer> laneSelectbox;
	private JComboBox<Integer> chartSelectbox;

	public ChartPanel(RoadData data) {
		this.data = data;

		setLayout(new MigLayout("gap 0", "[grow]", "[50px,fill][grow]"));
		setBackground(Color.WHITE);

		JPanel settingsPanel = new JPanel();
		JPanel previewPanel = new JPanel();

		JLabel laneLabel = new JLabel();
		JLabel chartLabel = new JLabel();

		laneSelectbox = new JComboBox<>();
		chartSelectbox = new JComboBox<>();

		laneLabel.setText("Lane");
		laneLabel.setLabelFor(laneSelectbox);

		chartLabel.setText("Chart");
		chartLabel.setLabelFor(chartSelectbox);

		laneSelectbox.addActionListener(this);
		chartSelectbox.addActionListener(this);

		settingsPanel.setLayout(new MigLayout("", "[grow][grow]", "[25px][25px]"));
		settingsPanel.add(laneLabel, "cell 0 0,grow");
		settingsPanel.add(chartLabel, "cell 1 0,grow");
		settingsPanel.add(laneSelectbox, "cell 0 1,grow");
		settingsPanel.add(chartSelectbox, "cell 1 1,grow");

		this.add(settingsPanel, "cell 0 0,grow");
		this.add(previewPanel, "cell 0 1,grow");
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
