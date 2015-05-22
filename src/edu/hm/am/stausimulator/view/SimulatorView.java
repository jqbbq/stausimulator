/*
 * 
 */
package edu.hm.am.stausimulator.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import edu.hm.am.stausimulator.Simulator;

// TODO: Auto-generated Javadoc
/**
 * The Class SimulatorView.
 */
public class SimulatorView {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant WINDOW_WIDTH. */
	private static final int WINDOW_WIDTH = 1000;

	/** The Constant WINDOW_HEIGHT. */
	private static final int WINDOW_HEIGHT = 500;

	private final Simulator simulator;

	/** The window. */
	private JFrame window;

	/**
	 * Instantiates a new simulator view.
	 */
	public SimulatorView(Simulator instance) {
		simulator = instance;

		window = new JFrame("Nagel-Schreckenberg Stausimulator");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setLayout(new GridLayout(2, 1));

		window.add(new CellularAutomataPanel(instance, WINDOW_WIDTH, WINDOW_HEIGHT));

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == updateButton) {
					instance.nextStep();
					window.repaint();
				}
			}
		});

		window.add(updateButton);

		window.setVisible(true);

	}

}
