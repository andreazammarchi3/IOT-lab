package assignment02.SmartCoffeeMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI class, extends a JFrame from javax.swing
 */
public class SmartCoffeeMachineGUI extends JFrame {

    /**
     * Content panel
     */
    private JPanel jContentPane = null;

    /**
     * Text field containing the actual modality of the machine
     */
    private JTextField modalityField;

    /**
     * List of text fields containing the availability for each product (Coffee, Tea, Chocolate)
     */
    private List<JTextField> productFields;

    /**
     * Text field containing the number of self tests performed by the machine
     */
    private JTextField selfTestsField;

    /**
     * Global tracker containing updated values from the machine
     */
    private final SmartCoffeeMachineTracker tracker;
    
    public SmartCoffeeMachineGUI(SmartCoffeeMachineTracker tracker) throws Exception {
        super();
        this.tracker = tracker;
        initialize();
    }

    private void initialize() throws Exception {
        this.setSize(400, 300);
        this.setContentPane(getJContentPane());
        this.setTitle("SmartCoffeeMachine GUI");
    }

    /**
     * Create the content panel and its children layouts
     */
    private JPanel getJContentPane() throws Exception {
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
            JButton refreshBtn = new JButton("Refresh");
            JButton recoverBtn = new JButton("Recover");
            buttonsPanel.add(refillBtn, BorderLayout.WEST);
            buttonsPanel.add(refreshBtn, BorderLayout.CENTER);
            buttonsPanel.add(recoverBtn, BorderLayout.EAST);

            refillBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refill();
                }
            });

            refreshBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        refreshGUI();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            recoverBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    recover();
                }
            });

            refreshGUI();
        }
        return jContentPane;
    }

    /**
     * Refresh GUI with new values
     */
    public void refreshGUI() throws Exception {
        tracker.update();
        modalityField.setText(tracker.getModality());
        for (int i = 0; i < tracker.getProducts().size(); i++) {
            productFields.get(i).setText(String.valueOf(tracker.getProducts().get(i).getAvailability()));
        }
        selfTestsField.setText(String.valueOf(tracker.getSelfTests()));
    }

    /**
     * Refill the machine with the starting products availabilities
     */
    public void refill() {
        for (Product product:
                tracker.getProducts()) {
            product.setAvailability(product.getMaxAvailability());
        }
        tracker.sendSerialData("refill");
    }

    /**
     * Recover the machine if a self test failed.
     */
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
