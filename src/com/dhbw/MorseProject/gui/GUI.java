package com.dhbw.MorseProject.gui;

import javax.swing.*;

public class GUI {
    private JTabbedPane tabbedPane1;
    private JPanel toSend;
    private JPanel toReceive;
    private JPanel toInfo;
    private javax.swing.JPanel mainpanel;
    private JTextField halloTestTextField;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton startRecordingButton;
    private JTextField textfield_input;
    private JButton button_send;

    public GUI(){
        JFrame frame = new JFrame("GUI-Test");
        frame.add(mainpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI();
            }
        });
    }

}
