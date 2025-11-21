package view;

import javax.swing.JOptionPane;

public class OptionPaneUI implements UserInterface {
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}