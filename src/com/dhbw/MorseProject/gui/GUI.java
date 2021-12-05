package com.dhbw.MorseProject.gui;

import javax.swing.*;
import java.awt.*;
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
    private JButton beginSendingButton;
    private JTextArea send_text_textArea;
    private JTextArea send_morse_textArea;
    private JSlider frequenz_slider;
    private JComboBox comboBox1;
    private JButton send_translate_button;
    private JButton send_clear_button;
    private JButton receive_clear_button;

    public GUI(){
        JFrame frame = new JFrame("GUI-Test");
        frame.add(mainpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        int effectiveMaxHeight = (screenSize.height-taskBarSize);

        Dimension windowSize = new Dimension(screenSize.width/2, effectiveMaxHeight/2);
        frame.setMinimumSize(windowSize);

        frame.setLocation(screenSize.width - (int) (frame.getWidth()*1.5), effectiveMaxHeight - (int) (frame.getHeight()*1.5) );

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
