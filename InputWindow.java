import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InputWindow extends JFrame {

    private final JTextField customersField = new JTextField("100", 8);
    private final JTextField arrivalLowerField = new JTextField("1", 8);
    private final JTextField arrivalUpperField = new JTextField("8", 8);
    private final JTextField serviceLowerField = new JTextField("1", 8);
    private final JTextField serviceUpperField = new JTextField("6", 8);
    private final JLabel statusLabel = new JLabel(" ");

    public InputWindow() {
        super("Bank Queue Simulation - Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        addRow(form, gbc, 0, "Number of customers:", customersField);
        addRow(form, gbc, 1, "Inter-arrival time lower bound (min):", arrivalLowerField);
        addRow(form, gbc, 2, "Inter-arrival time upper bound (min):", arrivalUpperField);
        addRow(form, gbc, 3, "Service time lower bound (min):", serviceLowerField);
        addRow(form, gbc, 4, "Service time upper bound (min):", serviceUpperField);

        JButton runButton = new JButton("Run Simulation");
        runButton.addActionListener(this::onRun);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runButton);

        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(form, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void onRun(ActionEvent e) {
        try {
            int n = Integer.parseInt(customersField.getText().trim());
            double aLower = Double.parseDouble(arrivalLowerField.getText().trim());
            double aUpper = Double.parseDouble(arrivalUpperField.getText().trim());
            double sLower = Double.parseDouble(serviceLowerField.getText().trim());
            double sUpper = Double.parseDouble(serviceUpperField.getText().trim());

            if (n <= 0) {
                showError("Number of customers must be greater than 0.");
                return;
            }
            if (aLower >= aUpper) {
                showError("Inter-arrival lower bound must be less than the upper bound.");
                return;
            }
            if (sLower >= sUpper) {
                showError("Service time lower bound must be less than the upper bound.");
                return;
            }

            statusLabel.setText(" ");
            QueueProcess process = new QueueProcess(n, aLower, aUpper, sLower, sUpper);
            process.run();
            SimulationStatistics stats = new SimulationStatistics(process);

            new OutputWindow(process, stats).setVisible(true);

        } catch (NumberFormatException ex) {
            showError("Please enter valid numbers in every field.");
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
    }
}