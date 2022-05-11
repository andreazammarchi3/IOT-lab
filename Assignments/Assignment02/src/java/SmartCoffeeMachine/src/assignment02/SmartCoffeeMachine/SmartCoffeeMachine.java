package assignment02.SmartCoffeeMachine;

import javax.swing.*;

/**
 * Main app
 */
public class SmartCoffeeMachine {
    public static void main(String[] args) throws Exception {

        /*
        Start the tracker
         */
        SmartCoffeeMachineTracker tracker = new SmartCoffeeMachineTracker();

        /*
        Start the GUI
         */
        SmartCoffeeMachineGUI frame = new SmartCoffeeMachineGUI(tracker);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
