import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class WebFrame extends JFrame{
    public static final String FILENAME = "links.txt";
    private int numRunningThreads;
    private int numCompletedThreads;
    private final DefaultTableModel model;
    private JProgressBar bar;
    private Thread launcher;
    private final JTextField numThreadField;
    private final JLabel numRunningThreadsLabel;
    private final JLabel numCompletedThreadsLabel;
    private final JLabel timeElapsedLabel;
    private final JButton singleThreadButton;
    private final JButton concurrentThreadButton;
    private final JButton stopButton;

    public WebFrame() {
        super("WebLoader");

        numRunningThreads = 0;

        model = new DefaultTableModel(new String[] { "url", "status"}, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setPreferredSize(new Dimension(600, 300));
        add(scrollpane);

        getFile();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel, BorderLayout.SOUTH);

        singleThreadButton = new JButton("Single Thread Fetch");
        panel.add(singleThreadButton);
        panel.add(Box.createRigidArea(new Dimension(0,10)));
        concurrentThreadButton = new JButton("Concurrent Fetch");
        panel.add(concurrentThreadButton);

        numThreadField = new JTextField("4");
        numThreadField.setMaximumSize(new Dimension(40, 100));
        panel.add(numThreadField);

        numRunningThreadsLabel = new JLabel("Running:0");
        numCompletedThreadsLabel = new JLabel("Completed:0");
        timeElapsedLabel = new JLabel("Elapsed:");
        panel.add(numRunningThreadsLabel);
        panel.add(numCompletedThreadsLabel);
        panel.add(timeElapsedLabel);

        bar = new JProgressBar(0, model.getRowCount());
        panel.add(bar);

        stopButton = new JButton("Stop");
        panel.add(stopButton);

        stopButton.setEnabled(false);

        addActionListeners();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void getFile() {
        model.setRowCount(0);
        try (BufferedReader input = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = input.readLine()) != null) {
                Object[] row = new Object[2];
                row[0] = line;
                row[1] = "";
                model.addRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addActionListeners() {
        singleThreadButton.addActionListener(e -> {
            numRunningThreads = 0;
            numCompletedThreads = 0;
            getFile();
            launcher = new Launcher(1);
            launcher.start();
        });

        concurrentThreadButton.addActionListener(e -> {
            numRunningThreads = 0;
            numCompletedThreads = 0;
            getFile();
            launcher = new Launcher(Integer.parseInt(numThreadField.getText()));
            launcher.start();
        });

        stopButton.addActionListener(e -> {
            launcher.interrupt();
        });
    }

    private class Launcher extends Thread {
        private final Semaphore semaphore;
        public Launcher(int numThread) {
            semaphore = new Semaphore(numThread);
        }

        @Override
        public void run() {
            singleThreadButton.setEnabled(false);
            concurrentThreadButton.setEnabled(false);
            stopButton.setEnabled(true);

            List<Thread> threads = new ArrayList<>();
            increaseRunning();
            long startTime = System.currentTimeMillis();
            for (int row = 0; row < model.getRowCount(); row++) {
                WebWorker worker = new WebWorker(model.getValueAt(row, 0).toString(), WebFrame.this, semaphore, row);
                worker.start();
                threads.add(worker);
            }

            for (Thread worker : threads) {
                try {
                    worker.join();
                } catch (InterruptedException e) {
                    for (Thread worker1 : threads) {
                        worker1.interrupt();
                    }
                    break;
                }
            }
            long endTime = System.currentTimeMillis();
            timeElapsedLabel.setText("Elapsed: " + (endTime - startTime) + "ms");
            decreaseRunning();
            stopButton.setEnabled(false);
            singleThreadButton.setEnabled(true);
            concurrentThreadButton.setEnabled(true);
        }
    }

    public synchronized void increaseRunning() {
        numRunningThreads++;
        SwingUtilities.invokeLater(() -> numRunningThreadsLabel.setText("Running: "+ numRunningThreads));
    }

    public synchronized void decreaseRunning() {
        numRunningThreads--;
        SwingUtilities.invokeLater(() -> numRunningThreadsLabel.setText("Running: "+ numRunningThreads));
    }

    public synchronized void updateRow(int row, String status) {
        numCompletedThreads++;
        SwingUtilities.invokeLater(() -> {
            numCompletedThreadsLabel.setText("Completed:" + numCompletedThreads);
            bar.setValue(numCompletedThreads);
            model.setValueAt(status, row, 1);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WebFrame::new);
    }
}
