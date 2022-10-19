package org.garden;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GardenServiceGUI {
    JLabel textAreaInfo = new JLabel("Click on the following button or close the application to end the service.");
    Button buttonEnd = new Button("Close");

    public GardenServiceGUI() {
        JFrame mainFrame = new JFrame("GardenService");
        mainFrame.setSize(600, 75);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        mainFrame.getContentPane().add(panel);
        panel.add(textAreaInfo);
        panel.add(buttonEnd);

        buttonEnd.addActionListener(e -> {
            try {
                close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void close() throws Exception {
        GardenService.close();
    }
}
