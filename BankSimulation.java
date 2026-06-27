import javax.swing.SwingUtilities;

public class BankSimulation {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InputWindow().setVisible(true));
    }
}