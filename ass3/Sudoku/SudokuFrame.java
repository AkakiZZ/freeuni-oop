import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SudokuFrame extends JFrame {

	 private JTextArea sourceTextArea;
	 private JTextArea solutionTextArea;

	 private JButton checkButton;
	 private JCheckBox autoCheckBox;
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
		// YOUR CODE HERE
		
		// Could do this:
		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		BorderLayout bl = new BorderLayout(4,4);
		setLayout(bl);

		addGuiElements();

		sourceTextArea.setBorder(new TitledBorder("Puzzle"));
		solutionTextArea.setBorder(new TitledBorder("solution"));
		solutionTextArea.setEditable(false);
		checkButton.setText("Check");
		autoCheckBox.setSelected(true);

		sourceTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				printSolution();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				printSolution();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				printSolution();
			}
		});

		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printSolution();
			}
		});

		pack();
	}

	private void addGuiElements() {
		sourceTextArea = new JTextArea(15, 20);
		solutionTextArea = new JTextArea(15, 20);

		Box menuBox = Box.createHorizontalBox();

		checkButton = new JButton();

		autoCheckBox = new JCheckBox();

		menuBox.add(checkButton);
		menuBox.add(autoCheckBox);
		menuBox.add(new JLabel("Auto Check"));

		add(menuBox, BorderLayout.SOUTH);
		add(sourceTextArea, BorderLayout.CENTER);
		add(solutionTextArea, BorderLayout.EAST);
	}

	private void printSolution() {
		StringBuilder solution = new StringBuilder();
		try {
			Sudoku sudoku = new Sudoku(Sudoku.textToGrid(sourceTextArea.getText()));
			int numSolutions = sudoku.solve();
			if (numSolutions > 0) {
				solution.append(sudoku.getSolutionText());
				solution.append("Solutions: ").append(numSolutions).append('\n');
				solution.append("elapsed: ").append(sudoku.getElapsed()).append("ms\n");
			}
		} catch (Exception e) {
			solution.append("Parsing problem");
		}
		solutionTextArea.setText(solution.toString());
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
