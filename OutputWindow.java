import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class OutputWindow extends JFrame {

    private static final String[] COLUMNS = {
            "Customer #", "Inter-Arrival Time", "Arrival Time", "Service Time",
            "Service Start", "Service End", "Waiting Time", "Time in System",
            "No. in Queue", "No. in System"
    };

    private final QueueProcess process;

    public OutputWindow(QueueProcess process, SimulationStatistics stats) {
        super("Bank Queue Simulation - Output (" + process.customerList.size() + " customers)");
        this.process = process;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTable table = buildTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 400));

        JPanel statsPanel = buildStatsPanel(stats);

        JButton saveButton = new JButton("Save Table as CSV");
        saveButton.addActionListener(e -> saveAsCsv());
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        pack();
        setLocationByPlatform(true);
    }

    private JTable buildTable() {
        DefaultTableModel model = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        var customers = process.customerList;
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            model.addRow(new Object[]{
                    i + 1,
                    fmt(c.interArrivalTime),
                    fmt(c.arrivalTime),
                    fmt(c.serviceTime),
                    fmt(c.serviceStart),
                    fmt(c.serviceEnd),
                    fmt(c.waitingTime),
                    fmt(c.getTimeInSystem()),
                    c.numberInQueue,
                    c.numberInSystem
            });
        }

        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        return table;
    }

    private JPanel buildStatsPanel(SimulationStatistics stats) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 20, 4));
        panel.setBorder(BorderFactory.createTitledBorder("Queue Statistics"));

        panel.add(statLabel("Average inter-arrival time:", fmt(stats.avgInterArrivalTime) + " min"));
        panel.add(statLabel("Average service time:", fmt(stats.avgServiceTime) + " min"));
        panel.add(statLabel("Average waiting time (queue):", fmt(stats.avgWaitingTime) + " min"));
        panel.add(statLabel("Average time in system:", fmt(stats.avgTimeInSystem) + " min"));
        panel.add(statLabel("Average number in queue:", fmt(stats.avgNumberInQueue)));
        panel.add(statLabel("Average number in system:", fmt(stats.avgNumberInSystem)));
        panel.add(statLabel("Maximum queue length:", String.valueOf(stats.maxQueueLength)));
        panel.add(statLabel("Probability a customer waits:", fmt(stats.probabilityOfWaiting * 100) + " %"));
        panel.add(statLabel("Server utilisation:", fmt(stats.serverUtilization * 100) + " %"));
        panel.add(statLabel("Total server idle time:", fmt(stats.totalIdleTime) + " min"));
        panel.add(statLabel("Total simulation time:", fmt(stats.totalSimulationTime) + " min"));

        return panel;
    }

    private JPanel statLabel(String name, String value) {
        JPanel row = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(name);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD));
        row.add(nameLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.EAST);
        return row;
    }

    private void saveAsCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new java.io.File("bank_simulation_results.csv"));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        java.io.File file = chooser.getSelectedFile();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(String.join(",", COLUMNS));
            var customers = process.customerList;
            for (int i = 0; i < customers.size(); i++) {
                Customer c = customers.get(i);
                writer.printf(Locale.US, "%d,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%d%n",
                        i + 1, c.interArrivalTime, c.arrivalTime, c.serviceTime,
                        c.serviceStart, c.serviceEnd, c.waitingTime, c.getTimeInSystem(),
                        c.numberInQueue, c.numberInSystem);
            }
            JOptionPane.showMessageDialog(this, "Saved to " + file.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save file: " + ex.getMessage(),
                    "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String fmt(double value) {
        return String.format(Locale.US, "%.2f", value);
    }
}