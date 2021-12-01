package com.dhbw.MorseProject.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JTabbedPane tabbedPane1;
    private JPanel toSend;
    private JPanel toReceive;
    private JPanel toInfo;
    private javax.swing.JPanel mainpanel;
    private JTextField halloTestTextField;
    private JTextArea receive_text_textArea;
    private JTextArea receive_morse_textArea;
    private JButton startRecordingButton;
    private JSlider receive_sensitivity_slider;
    private JComboBox comboBox_melody;
    private JTextField textField_input;
    private JButton button_send;
    private JTextArea textArea_TEST;

    public GUI(){
        JFrame frame = new JFrame("GUI-Test");
        frame.add(mainpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        startRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO link receive module
            }
        });
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