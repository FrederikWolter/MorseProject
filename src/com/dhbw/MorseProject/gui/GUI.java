package com.dhbw.MorseProject.gui;

import com.dhbw.MorseProject.translate.Translator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private final Translator translator = new Translator();
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
    private JSplitPane receiveSplitPane;
    private JSplitPane sendSplitPane;
    private JTable table_alphabet;
    private boolean showingStartRecording = true;
    private boolean showingBeginSend = true;

    public GUI(){
        JFrame frame = new JFrame("Morse-Kommunikation - Technikmuseum Kommunikatioinstechnik München");
        frame.add(mainpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        int effectiveMaxHeight = (screenSize.height-taskBarSize);

        double scale = 0.5;
        double modifier = (int) (1.0/scale);
        Dimension windowSize = new Dimension((int)(((double) screenSize.width)/modifier), (int)(((double)effectiveMaxHeight)/modifier));
        frame.setMinimumSize(windowSize);


        double multiplier = 1 + 1.0/modifier;
        frame.setLocation(screenSize.width - (int) (frame.getWidth()*multiplier), effectiveMaxHeight - (int) (frame.getHeight()*multiplier) );

        Dimension send_text_textArea_preferredDimension = new Dimension(sendSplitPane.getWidth()/2 ,send_text_textArea.getPreferredSize().height);
        send_text_textArea.setPreferredSize(send_text_textArea_preferredDimension);
        send_text_textArea.setMinimumSize(send_text_textArea_preferredDimension);

        Dimension recieve_text_textArea_preferredDimension = new Dimension(receiveSplitPane.getWidth()/2 ,receive_text_textArea.getPreferredSize().height);
        receive_text_textArea.setPreferredSize(recieve_text_textArea_preferredDimension);
        receive_text_textArea.setMinimumSize(recieve_text_textArea_preferredDimension);

        receiveSplitPane.setDividerLocation(receiveSplitPane.getWidth()/2);
        sendSplitPane.setDividerLocation(sendSplitPane.getWidth()/2);

        String[][] data = fillTable();
        String[] columnNames = {"Schriftzeichen", "Morse-Code"};

        DefaultTableModel tableModel = (DefaultTableModel) table_alphabet.getModel();
        tableModel.addColumn(columnNames[0]);
        tableModel.addColumn(columnNames[1]);
        for (int i = 0; i < data.length; i++) {
            tableModel.addRow(new Object[]{data[i][0], data[i][1]});
        }

        startRecordingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //TODO link receive module

                if (!showingStartRecording){
                    startRecordingButton.setText("Start Recording");
                } else{
                    startRecordingButton.setText("Stop Recording");
                }
                showingStartRecording = !showingStartRecording;
            }
        });

        beginSendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO link receive module

                if (!showingBeginSend){
                    beginSendingButton.setText("Senden beginnen");
                } else{
                    beginSendingButton.setText("Senden beenden");
                }
                showingBeginSend = !showingBeginSend;
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

    public String[][] fillTable(){
        String[][] data = new String[42][2];
        int counter = 0;

        for (int i = 65; i <= 90; i++){
            data[counter][0] = ""+(char)i;
            data[counter][1] = Translator.toMorse((char)i);
            counter++;
        }
        for (int i = 48; i<= 57; i++){
            data[counter][0] = ""+(char)i;
            data[counter][1] = Translator.toMorse((char)i);
            counter++;
        }
        data[counter][0] = ".";
        data[counter][1] = Translator.toMorse('.');
        counter++;
        data[counter][0] = ",";
        data[counter][1] = Translator.toMorse(',');
        counter++;
        data[counter][0] = "'";
        data[counter][1] = Translator.toMorse('\'');
        counter++;
        data[counter][0] = ":";
        data[counter][1] = Translator.toMorse(':');
        counter++;
        data[counter][0] = "-";
        data[counter][1] = Translator.toMorse('-');
        counter++;
        data[counter][0] = " ";
        data[counter][1] = Translator.toMorse(' ');
        return data;
    }
}
