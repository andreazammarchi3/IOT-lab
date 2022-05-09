package assignment02.SmartCoffeeMachine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SmartCoffeeMachineGUI extends JFrame {
    private JPanel jContentPane = null;
    private JTextField coffeeField = new JTextField();
    private JTextField teaField = new JTextField();
    private JTextField chocolateField = new JTextField();

    public SmartCoffeeMachineGUI() {
        super();
        initialize();
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("SmartCoffeeMachine GUI");
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());

            JPanel panel = new JPanel();

            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            jContentPane.add(panel, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();
            jContentPane.add(buttonsPanel, BorderLayout.SOUTH);
            JButton refillBtn = new JButton("Refill");
            JButton recoverBtn = new JButton("Recover");
            buttonsPanel.add(refillBtn, BorderLayout.WEST);
            buttonsPanel.add(recoverBtn, BorderLayout.EAST);

            JPanel modalityPanel = new JPanel();
            modalityPanel.setLayout(new BoxLayout(modalityPanel, BoxLayout.Y_AXIS));
            panel.add(modalityPanel);

            JPanel availabilityPanel = new JPanel();
            availabilityPanel.setLayout(new BoxLayout(availabilityPanel, BoxLayout.Y_AXIS));
            panel.add(availabilityPanel);

            JPanel selfTestsPanel = new JPanel();
            selfTestsPanel.setLayout(new BoxLayout(selfTestsPanel, BoxLayout.Y_AXIS));
            panel.add(selfTestsPanel);

            modalityPanel.add(new JLabel("Machine state:"));
            JTextField modalityField = new JTextField();
            modalityField.setText("modality");
            modalityField.setEditable(false);
            modalityPanel.add(modalityField);

            availabilityPanel.add(new JLabel("Products availability:"));
            availabilityPanel.add(new JLabel("Coffee:"));
            coffeeField.setText("coffee1");
            coffeeField.setEditable(false);
            availabilityPanel.add(coffeeField);
            availabilityPanel.add(new JLabel("Tea:"));
            teaField.setText("tea2");
            teaField.setEditable(false);
            availabilityPanel.add(teaField);
            availabilityPanel.add(new JLabel("Chocolate:"));
            chocolateField.setText("chocolate3");
            chocolateField.setEditable(false);
            availabilityPanel.add(chocolateField);

            selfTestsPanel.add(new JLabel("Self-tests performed:"));
            JTextField selfTestsField = new JTextField();
            selfTestsField.setText("self-test4");
            selfTestsField.setEditable(false);
            selfTestsPanel.add(selfTestsField);
        }
        return jContentPane;
    }

    public void refreshGUI() {

    }
}
