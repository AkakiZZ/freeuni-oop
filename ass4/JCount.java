// JCount.java

/*
 Basic GUI/Threading exercise.
*/

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class JCount extends JPanel {
	private final JLabel current;
	private Thread worker;
	private final JTextField input;
	public JCount() {
		worker = null;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		input = new JTextField("100000000");
		current = new JLabel("0");
		add(input);
		add(current);
		JButton startButton = new JButton("start");
		JButton stopButton = new JButton("stop");

		startButton.addActionListener(e -> {
			int initialCount = Integer.parseInt(input.getText());

			if (worker != null) worker.interrupt();

			worker = new Thread(new countWorker(initialCount));
			worker.start();
		});
		stopButton.addActionListener(e -> {
			if (worker != null) worker.interrupt();
		});
		add(startButton);
		add(stopButton);
		add(Box.createRigidArea(new Dimension(0,40)));
	}

	private static void createAndShowGUI() {
		JFrame mainFrame = new JFrame();
		mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.Y_AXIS));
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
		mainFrame.add(new JCount());
		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public class countWorker implements Runnable {
		private final int maxNumber;

		public countWorker(int maxNumber) {
			this.maxNumber = maxNumber;
		}

		@Override
		public void run() {
			for (int i = 0; i < maxNumber; i++) {
				if (Thread.currentThread().isInterrupted())
					return;

				if (i % 10000 == 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
						return;
					}
					final String param = String.valueOf(i);
					SwingUtilities.invokeLater(() -> current.setText(param));
				}
			}
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(JCount::createAndShowGUI);
	}
}