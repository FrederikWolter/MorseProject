package com.dhbw.MorseProject.gui;

import com.dhbw.MorseProject.translate.Translator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
    private JTextArea textArea_info;
    private JPanel JPanel_border;
    private JTextField textField_info;
    private boolean showingStartRecording = true;
    private boolean showingBeginSend = true;

    public GUI(){
        JFrame frame = new JFrame("Kommunikation via Morsecode - Technikmuseum Kommunikatioinstechnik München");
        frame.add(mainpanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //frame.setResizable(false);

        try {
            frame.setIconImage(ImageIO.read(new File("src/com/dhbw/MorseProject/gui/Morse_Symbolbild.png")));
            // Quelle: "https://w7.pngwing.com/pngs/27/465/png-transparent-morse-code-computer-icons-communication-others-text-code-morse-code.png"
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel_border.setBorder(BorderFactory.createLineBorder(Color.blue, 50));

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

        adjust_splitpane_sizes(sendSplitPane, send_text_textArea, send_morse_textArea);

        adjust_splitpane_sizes(receiveSplitPane, receive_text_textArea, receive_morse_textArea);


        String[][] data = fillTable();
        String[] columnNames = {"Schriftzeichen", "Morse-Code"};

        //DefaultTableCellRenderer centerRendering = new DefaultTableCellRenderer();
        //centerRendering.setHorizontalAlignment(SwingConstants.CENTER);
        //table_alphabet.setDefaultRenderer(String.class, centerRendering);
        //table_alphabet.setDefaultRenderer(Integer.class, centerRendering);

        DefaultTableModel tableModel = (DefaultTableModel) table_alphabet.getModel();
        tableModel.addColumn(columnNames[0]);
        tableModel.addColumn(columnNames[1]);
        for (String[] datum : data) {
            tableModel.addRow(new Object[]{datum[0], datum[1]});
        }

        String info = """
                Der Morsecode (auch Morsealphabet oder Morsezeichen genannt) ist ein gebräuchlicher Code zur telegrafischen Übermittlung von Buchstaben, Ziffern und weiterer Zeichen. Er bestimmt das Zeitschema, nach dem ein diskretes Signal ein- und ausgeschaltet wird.
                
                Der Code kann als Tonsignal, als Funksignal, als elektrischer Puls mit einer Morsetaste über eine Telefonleitung, mechanisch oder optisch (etwa mit blinkendem Licht) übertragen werden – oder auch mit jedem sonstigen Medium, mit dem zwei verschiedene Zustände (wie etwa Ton oder kein Ton) eindeutig und in der zeitlichen Länge variierbar dargestellt werden können. Dieses Übertragungsverfahren nennt man Morsetelegrafie.
                
                Das manchmal bei Notfällen beschriebene Morsen durch Klopfen an metallischen Verbindungen erfüllt diese Forderung daher nur bedingt, ist aber mit einiger Übung aufgrund des charakteristischen Rhythmus von Morsezeichen verständlich. Es ist abgeleitet von den „Klopfern“ aus der Anfangszeit der Telegrafentechnik, bestehend aus einem Elektromagneten mit Anker in einem akustischen Hohlspiegel. Beim Einschalten erzeugte er ein lautes und beim Abschalten ein etwas leiseres Klopfgeräusch. So konnte man den Klang der Morsezeichen schon vor der Erfindung des Lautsprechers selbst in größeren Betriebsräumen hörbar machen.
                
                
                Auszug aus de.wikipedia.org/wiki/Morsecode
                """;
        //Quelle: https://de.wikipedia.org/wiki/Morsecode
        textArea_info.setText(info);

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

    private void adjust_splitpane_sizes(JSplitPane splitPane, JTextArea text_textArea, JTextArea morse_textArea) {
        Dimension textAreas_preferredDimension = new Dimension(splitPane.getWidth() / 2, text_textArea.getPreferredSize().height);

        text_textArea.setPreferredSize(textAreas_preferredDimension);
        text_textArea.setMinimumSize(textAreas_preferredDimension);

        morse_textArea.setPreferredSize(textAreas_preferredDimension);
        morse_textArea.setMinimumSize(textAreas_preferredDimension);

        splitPane.setDividerLocation(splitPane.getWidth()/2);
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
