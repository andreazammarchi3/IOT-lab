package assignment02.SmartCoffeeMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SmartCoffeeMachineGUI extends JFrame {
    private JPanel jContentPane = null;
    private JTextField modalityField;
    private List<JTextField> productFields;
    private JTextField selfTestsField;
    private final SmartCoffeeMachineTracker tracker;
    
    public SmartCoffeeMachineGUI(SmartCoffeeMachineTracker tracker) {
        super();
        this.tracker = tracker;
        initialize();
    }

    private void initialize() {
        this.setSize(400, 300);
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

            /*
            Panels for data view
             */
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
            modalityField = new JTextField();
            modalityField.setText("modality");
            modalityField.setEditable(false);
            modalityPanel.add(modalityField);

            availabilityPanel.add(new JLabel("Products availability:"));
            productFields = new ArrayList<>();
            for (Product product:
                 tracker.getProducts()) {
                JPanel productPanel = createProductPanel(product);
                availabilityPanel.add(productPanel);
            }

            selfTestsPanel.add(new JLabel("Self-tests performed:"));
            selfTestsField = new JTextField();
            selfTestsField.setText(String.valueOf(0));
            selfTestsField.setEditable(false);
            selfTestsPanel.add(selfTestsField);

            /*
            Action buttons
             */
            JPanel buttonsPanel = new JPanel();
            jContentPane.add(buttonsPanel, BorderLayout.SOUTH);
            JButton refillBtn = new JButton("Refill");
            JButton recoverBtn = new JButton("Recover");
            buttonsPanel.add(refillBtn, BorderLayout.WEST);
            buttonsPanel.add(recoverBtn, BorderLayout.EAST);

            refillBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refill();
                }
            });

            recoverBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    recover();
                }
            });
        }
        return jContentPane;
    }

    public void refreshGUI() throws Exception {
        modalityField.setText(tracker.getSerialData("modality"));
        for (int i = 0; i < tracker.getProducts().size(); i++) {
            productFields.get(i).setText(String.valueOf(tracker.getProducts().get(i).getAvailability()));
        }
        selfTestsField.setText(String.valueOf(tracker.getSelfTests()));
    }

    public void refill() {
        for (Product product:
                tracker.getProducts()) {
            product.setAvailability(product.getMaxAvailability());
        }
        tracker.sendSerialData("refill");
    }

    public void recover() {
        tracker.sendSerialData("recover");
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.add(new JLabel(product.getLabel()));
        JTextField productField = new JTextField();
        productField.setText(product.getLabel());
        productField.setEditable(false);
        productFields.add(productField);
        productPanel.add(productField);
        return productPanel;
    }
}
