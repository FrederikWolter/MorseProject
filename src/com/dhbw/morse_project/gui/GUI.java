package com.dhbw.morse_project.gui;

import com.dhbw.morse_project.receive.Decoder;
import com.dhbw.morse_project.send.Encoder;
import com.dhbw.morse_project.send.Melody;
import com.dhbw.morse_project.send.events.IEncoderFinishedListener;
import com.dhbw.morse_project.translate.Translator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import static javax.swing.JOptionPane.showMessageDialog;

//todo process GUI warnings
// todo Hardcoded string literal in a UI form
// todo Missing mnemonic: Empfangen beginnen, Melodie, Senden beginnen, Text zurücksetzen, Text zurücksetzen, Übersetzen
// todo No label for component

/**
 * Class responsible for user interactions.
 * [ID: F-GUI-*, NF-GUI-* ]
 *
 * @author Mark Mühlenberg, Kai Grübener supported by Frederik Wolter, Lucas Schaffer, (Daniel Czeschner - Decoder integration)
 */
@SuppressWarnings("DanglingJavadoc")
public class GUI {
    /**
     * Object to synchronize the GUI with the {@link Decoder}, to pull the latest detected Morse-Signals.
     *
     * @see Decoder#getLastSignal()
     */
    private final Object GUI_DECODER_SYNCHRONIZE_Object;
    private final Decoder DECODER;
    private final Map<textArea, textArea_focusState> textAreaFocusMap = new HashMap<>();

    private JTabbedPane tabbedPane1;
    private JPanel toSend;
    private JPanel toReceive;
    private JPanel toInfo;
    private javax.swing.JPanel main_panel;
    private JTextArea receive_text_textArea;
    private JTextArea receive_morse_textArea;
    private JButton startRecordingButton;
    private JSlider receive_sensitivity_slider;
    private JButton beginSendingButton;
    private JTextArea send_text_textArea;
    private JTextArea send_morse_textArea;
    private JSlider frequenz_slider;
    private JComboBox<String> comboBoxMelody;
    private JButton send_translate_button;
    private JButton send_clear_button;
    private JButton receive_clear_button;
    private JSplitPane receiveSplitPane;
    private JSplitPane sendSplitPane;
    private JTable table_alphabet;
    private JTextArea textArea_info;
    private JPanel JPanel_border;
    private JTextField textField_info;              // todo never used variables
    private boolean showingStartRecording = true;
    private boolean showingBeginSend = true;
    private Thread ui_update_thread;

    private enum textArea_focusState {
        FOCUS_GAINED,
        FOCUS_LOST,
        FOCUS_LOST_NEWEST,
        NONE
    }

    private enum textArea {
        MORSE,
        TEXT
    }

    /**
     * Constructor for class {@link GUI}
     *
     * @see JFrame
     * @see #setupJFrame()
     * @see #prepareInfoPanel()
     * @see #prepareMelodySelectionComboBox()
     * @see #configureListeners()
     */
    public GUI() {
        JFrame frame = setupJFrame();  // todo frame never used

        GUI_DECODER_SYNCHRONIZE_Object = new Object();
        DECODER = new Decoder(this);

        prepareInfoPanel();

        prepareMelodySelectionComboBox();

        configureListeners();
    }

    /**
     * Configuring and adding all Action and Event listeners:
     *
     * @see #startRecordingButton
     * @see #beginSendingButton
     * @see IEncoderFinishedListener
     * @see #send_clear_button
     * @see #receive_clear_button
     * @see #startPlaying()
     * @see #stopPlaying()
     */
    private void configureListeners() {
        /**
         * Action-Listener to start the recording of audio when startRecordingButton is pressed
         */
        startRecordingButton.addActionListener(e -> {
            if (!showingStartRecording) {
                stopRecording(e);

            } else {
                startRecording(e);
            }
        });

        /**
         * Action-Listener to start and stop sending the given morse-code via audio when beginSendingButton is pressed
         * @see #startPlaying()
         * @see #stopPlaying()
         */
        beginSendingButton.addActionListener(e -> {
            if (!showingBeginSend) {
                stopPlaying();
            } else {
                startPlaying();
            }
        });

        Encoder.getInstance().addFinishedEventListener(this::stopPlayingChangeVariables);

        /**
         * Action-Listeners to clear textAreas after clicking the button
         */
        send_clear_button.addActionListener(e -> clear_textAreas(send_text_textArea, send_morse_textArea));
        receive_clear_button.addActionListener(e -> clear_textAreas(receive_text_textArea, receive_morse_textArea));

        prepareTranslationButton();
    }

    /**
     * Translating the contents of the last selected {@link TextArea} to the other {@link TextArea} in send tab
     * when {@link #send_translate_button} is pressed
     *
     * @see #send_translate_button
     * @see #translateSendTextAreas(Map)
     * @see #send_text_textArea
     * @see #send_morse_textArea
     * @see #textAreaFocusMap
     * @see #textAreaFocusLost(Map, textArea, textArea)
     */
    private void prepareTranslationButton() {
        prepareListenForTextAreaFocusChange();

        send_translate_button.addActionListener(e -> translateSendTextAreas(textAreaFocusMap));
    }

    /**
     * Listening for focus change in the
     * {@link TextArea} {@link #send_text_textArea} and {@link #send_morse_textArea}
     * to record which of the text areas last had focus.
     *
     * @see #send_text_textArea
     * @see #send_morse_textArea
     * @see #textAreaFocusMap
     * @see #textAreaFocusLost(Map, textArea, textArea)
     * @see #prepareTranslationButton()
     * @see #translateSendTextAreas(Map)
     */
    private void prepareListenForTextAreaFocusChange() {
        send_morse_textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textAreaFocusMap.put(textArea.MORSE, textArea_focusState.FOCUS_GAINED);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textAreaFocusLost(textAreaFocusMap, textArea.MORSE, textArea.TEXT);
            }
        });
        send_text_textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textAreaFocusMap.put(textArea.TEXT, textArea_focusState.FOCUS_GAINED);
            }

            @Override
            public void focusLost(FocusEvent e) {
                textAreaFocusLost(textAreaFocusMap, textArea.TEXT, textArea.MORSE);
            }
        });
    }

    /**
     * Settings for the {@link #comboBoxMelody} to select given melodies or frequencies.
     *
     * @see #startPlaying()
     * @see Melody
     * @see Encoder
     */
    private void prepareMelodySelectionComboBox() {

        for (int i = 0; i < Melody.getMelodyList().size(); i++) {
            comboBoxMelody.addItem(Melody.getMelodyList().get(i).getName());
        }
        comboBoxMelody.addActionListener(
                e ->
                        frequenz_slider.setEnabled(Objects.requireNonNull(comboBoxMelody.getSelectedItem()).toString().equals("Fest")));
    }

    /**
     * Filling text and table in Information-Tab with content and setting appearance
     *
     * @see #getTableData()
     * @see #table_alphabet
     * @see #textArea_info
     */
    private void prepareInfoPanel() {
        String[][] data = getTableData();
        String[] columnNames = {"Schriftzeichen", "Morse-Code"};
        DefaultTableModel tableModel = (DefaultTableModel) table_alphabet.getModel();
        tableModel.addColumn(columnNames[0]);
        tableModel.addColumn(columnNames[1]);
        for (String[] datum : data) {
            tableModel.addRow(new Object[]{datum[0], datum[1]});
        }
        DefaultTableCellRenderer centerRendering = new DefaultTableCellRenderer();
        centerRendering.setHorizontalAlignment(JLabel.CENTER);
        table_alphabet.getColumnModel().getColumn(0).setCellRenderer(centerRendering);
        table_alphabet.getColumnModel().getColumn(1).setCellRenderer(centerRendering);

        String info = getInfoText();

        textArea_info.setText(info);
    }

    /**
     * Settings for frame and application appearance
     *
     * @see JFrame
     */
    private JFrame setupJFrame() {
        JFrame frame = new JFrame("Kommunikation via Morsecode - Technikmuseum Kommunikationstechnik München");
        frame.add(main_panel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //frame.setResizable(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {  //overwriting the standard behaviour when closing the window
                try {
                    stopPlaying();
                    stopRecording(null);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    showMessageDialog(null, "Es ist ein Fehler aufgetreten, bitte melden Sie sich beim Personal.\nERROR: could not stop recording", "Error", JOptionPane.ERROR_MESSAGE);
                }
                frame.dispose();
                System.exit(0);
            }
        });

        try {
            frame.setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResource("morse-code.png")))); // see https://stackoverflow.com/a/45580/13777031
            // source: https://cdn-icons-png.flaticon.com/512/260/260301.png
        } catch (IOException e) {
            e.printStackTrace();
        }

        //main_panel.setBorder(BorderFactory.createLineBorder(Color.blue, 50));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        int effectiveMaxHeight = (screenSize.height - taskBarSize);

        setWindowDimensions(frame, screenSize, effectiveMaxHeight);

        adjust_split_pane_sizes(sendSplitPane, send_text_textArea, send_morse_textArea);

        adjust_split_pane_sizes(receiveSplitPane, receive_text_textArea, receive_morse_textArea);

        return frame;
    }

    /**
     * Setting the dimensions and position of the given {@link JFrame}
     *
     * @param frame              mainframe
     * @param screenSize         size of screen
     * @param effectiveMaxHeight factor for scaling
     */
    private void setWindowDimensions(JFrame frame, Dimension screenSize, int effectiveMaxHeight) {
        double scale = 0.5;
        double modifier = (int) (1.0 / scale);
        Dimension windowSize = new Dimension((int) (((double) screenSize.width) / modifier), (int) (((double) effectiveMaxHeight) / modifier));
        frame.setMinimumSize(windowSize);
        main_panel.setMinimumSize(windowSize);
        main_panel.setMaximumSize(new Dimension(500, 500));
        main_panel.setSize(500, 500);

        tabbedPane1.setFont(new Font("Arial", Font.BOLD, 16));

        double multiplier = 1 + 1.0 / modifier;
        frame.setLocation(screenSize.width - (int) (frame.getWidth() * multiplier), effectiveMaxHeight - (int) (frame.getHeight() * multiplier));
    }

    /**
     * @return {@link String} with info text
     */
    @SuppressWarnings("SameReturnValue")
    private String getInfoText() {
        return """
                Der Morsecode (auch Morsealphabet oder Morsezeichen genannt) ist ein gebräuchlicher Code zur telegrafischen Übermittlung von Buchstaben, Ziffern und weiterer Zeichen. Er bestimmt das Zeitschema, nach dem ein diskretes Signal ein- und ausgeschaltet wird.
                                
                Der Code kann als Tonsignal, als Funksignal, als elektrischer Puls mit einer Morsetaste über eine Telefonleitung, mechanisch oder optisch (etwa mit blinkendem Licht) übertragen werden – oder auch mit jedem sonstigen Medium, mit dem zwei verschiedene Zustände (wie etwa Ton oder kein Ton) eindeutig und in der zeitlichen Länge variierbar dargestellt werden können. Dieses Übertragungsverfahren nennt man Morsetelegrafie.
                                
                Das manchmal bei Notfällen beschriebene Morsen durch Klopfen an metallischen Verbindungen erfüllt diese Forderung daher nur bedingt, ist aber mit einiger Übung aufgrund des charakteristischen Rhythmus von Morsezeichen verständlich. Es ist abgeleitet von den „Klopfern“ aus der Anfangszeit der Telegrafentechnik, bestehend aus einem Elektromagneten mit Anker in einem akustischen Hohlspiegel. Beim Einschalten erzeugte er ein lautes und beim Abschalten ein etwas leiseres Klopfgeräusch. So konnte man den Klang der Morsezeichen schon vor der Erfindung des Lautsprechers selbst in größeren Betriebsräumen hörbar machen.
                                
                                
                Auszug aus de.wikipedia.org/wiki/Morsecode""";

        //Quelle: https://de.wikipedia.org/wiki/Morsecode
    }

    /**
     * Trying to stop the recording of the {@link Decoder} or displaying an error message if failed
     *
     * @param e ActionEvent
     */
    @SuppressWarnings("unused")
    private void stopRecording(ActionEvent e) {
        boolean success = DECODER.stopRecording();

        if (success) {
            startRecordingButton.setText("Empfangen beginnen");
            showingStartRecording = !showingStartRecording;
        } else {
            showMessageDialog(null, "Es ist ein Fehler aufgetreten, bitte melden Sie sich beim Personal.\nERROR: fatal exception while 'stop recording'", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Trying to start the recording of the {@link Decoder} or displaying an error message if failed
     *
     * @param e event which is triggered
     */
    @SuppressWarnings("unused")
    private void startRecording(ActionEvent e) {
        clear_textAreas(receive_text_textArea, receive_morse_textArea); //Clearing text areas beforehand

        Runnable ui_update_runnable = getUiUpdateRunnable();

        ui_update_thread = new Thread(ui_update_runnable);

        boolean success = DECODER.startRecording();
        if (success) {
            ui_update_thread.start();
            startRecordingButton.setText("Empfangen beenden");
            showingStartRecording = !showingStartRecording;
        } else {
            showMessageDialog(null, "Es ist ein Fehler aufgetreten, bitte melden Sie sich beim Personal.\nIst ein Mikrofon korrekt angeschlossen?\nERROR: fatal exception while starting recoding.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return a {@link Runnable} to automatically update the {@link #receive_morse_textArea} and translate the contents
     * to the {@link #receive_text_textArea} via the {@link Translator} when the {@link Decoder} generated a new Signal
     */
    private Runnable getUiUpdateRunnable() {
        return () -> {
            do {
                try {
                    synchronized (GUI_DECODER_SYNCHRONIZE_Object) {
                        GUI_DECODER_SYNCHRONIZE_Object.wait();
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                receive_morse_textArea.append(DECODER.getLastSignal());
                String currentMorseTranslation = Translator.morseToText(receive_morse_textArea.getText());
                receive_text_textArea.setText(currentMorseTranslation);
            } while (!showingStartRecording);
        };
    }

    /**
     * Trying to start playing the morse code from the {@link #send_morse_textArea} and displaying an error message
     * if failed.
     * <p>
     * If a melody is selected, the morse code is played with that melody, otherwise, when "Fest" is selected, the
     * Morse Code is played with a constant frequency from the {@link #frequenz_slider}
     *
     * @see Encoder
     */
    private void startPlaying() {
        translateSendTextAreas(textAreaFocusMap);
        String morse = send_morse_textArea.getText();
        Melody sendMelody = getMelody();
        if (sendMelody != null) {
            try {
                if (Objects.equals(morse, "")) {
                    showMessageDialog(null, "Bitte geben Sie nur Text ein, der übersetzt werden kann (Siehe Informationen)", "Falsche Eingabe", JOptionPane.WARNING_MESSAGE);
                } else {
                    Encoder.getInstance().send(morse, sendMelody);
                    beginSendingButton.setText("Senden beenden");
                    showingBeginSend = !showingBeginSend;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                showMessageDialog(null, "Es ist ein Fehler aufgetreten, bitte melden Sie sich beim Personal.\nERROR: while starting play signal:\n" + Arrays.toString(e.getStackTrace()), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return the selected {@link Melody}. If a melody is selected, then that {@link Melody} is returned,
     * otherwise, when "Fest" is selected, a new {@link Melody} with a constant frequency from the
     * {@link #frequenz_slider} is generated and returned.
     */
    private Melody getMelody() {
        Melody sendMelody = null;

        if (comboBoxMelody.getSelectedItem() != "Fest") {
            for (Melody melody : Melody.getMelodyList()) {
                // todo Method invocation 'toString' may produce 'NullPointerException'
                if (melody.getName().equals(comboBoxMelody.getSelectedItem().toString())) {
                    sendMelody = melody;
                }
            }
        } else {
            sendMelody = new Melody("Fest", new int[]{frequenz_slider.getValue()});
        }
        return sendMelody;
    }

    /**
     * Trying to stop the {@link Encoder} from playing the currently playing Morse Code.
     * If Failed, display an error message
     */
    private void stopPlaying() {
        try {
            Encoder.getInstance().stopPlaying();
            stopPlayingChangeVariables();
        } catch (InterruptedException e) {
            e.printStackTrace();
            showMessageDialog(null, "Es ist ein Fehler aufgetreten, bitte melden Sie sich beim Personal.\nERROR: while stop playing:\n" + Arrays.toString(e.getStackTrace()), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Changing the relevant variables for the GUI to indicate that the {@link Encoder} stopped playing
     */
    private void stopPlayingChangeVariables() {
        beginSendingButton.setText("Senden beginnen");
        showingBeginSend = !showingBeginSend;
    }

    /**
     * Updating the {@link textArea_focusState} of the {@param caller} and {@param non_caller} in the given
     * {@param textAreaFocusMap} to indicate, which {@link TextArea} last lost focus.
     *
     * @param textAreaFocusMap indicating who last had focus
     * @param caller           to be updated
     * @param non_caller       to be updated
     * @see #prepareListenForTextAreaFocusChange()
     */
    private void textAreaFocusLost(Map<GUI.textArea, GUI.textArea_focusState> textAreaFocusMap, textArea caller, textArea non_caller) {
        if (!textAreaFocusMap.getOrDefault(caller, textArea_focusState.NONE).equals(textArea_focusState.NONE)) { //can only lose focus if it has gained focus before
            if (textAreaFocusMap.getOrDefault(non_caller, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_LOST_NEWEST)
                    || textAreaFocusMap.getOrDefault(non_caller, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_LOST)) {
                textAreaFocusMap.put(non_caller, textArea_focusState.FOCUS_LOST);
                textAreaFocusMap.put(caller, textArea_focusState.FOCUS_LOST_NEWEST);
            } else if (textAreaFocusMap.getOrDefault(non_caller, textArea_focusState.NONE).equals(textArea_focusState.NONE)) {
                textAreaFocusMap.put(caller, textArea_focusState.FOCUS_LOST_NEWEST);
            } else {
                textAreaFocusMap.put(caller, textArea_focusState.FOCUS_LOST_NEWEST);
            }
        }
    }

    /**
     * Translating the Contents of the {@link TextArea} via the {@link Translator} of the {@link TextArea} that last
     * had focus, as indicated by the {@param textAreaFocusMap}.
     * If that method of detection fails, the detection falls back to translating to the {@link TextArea} which has
     * an empty body.
     * If none could be detected, display an error message
     *
     * @param textAreaFocusMap indicating who last had focus
     * @see #prepareListenForTextAreaFocusChange()
     * @see #textAreaFocusLost(Map, textArea, textArea)
     * @see #textAreaFocusMap
     */
    private void translateSendTextAreas(Map<GUI.textArea, GUI.textArea_focusState> textAreaFocusMap) {
        if (textAreaFocusMap.getOrDefault(textArea.TEXT, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_LOST_NEWEST)
                || textAreaFocusMap.getOrDefault(textArea.TEXT, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_GAINED)) {
            translateTextAreaTextToMorse();
        } else if (textAreaFocusMap.getOrDefault(textArea.MORSE, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_LOST_NEWEST)
                || textAreaFocusMap.getOrDefault(textArea.MORSE, textArea_focusState.NONE).equals(textArea_focusState.FOCUS_GAINED)) {
            translateMorseTextAreaToText();
        } else {
            if (send_morse_textArea.getText().equals("") && !send_text_textArea.getText().equals("")) {
                translateTextAreaTextToMorse();
            } else if (!send_morse_textArea.getText().equals("") && send_text_textArea.getText().equals("")) {
                translateMorseTextAreaToText();
            } else {
                System.out.println("No way to decide what to translate to which");
                showMessageDialog(null, "Bitte wählen Sie ein Texteingabefeld", "Texteingabe nicht erkannt", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Trying to translate the Morse Code, contained in {@link #send_morse_textArea} into the {@link #send_text_textArea} as Text.
     * If failed, display an error message
     *
     * @see Translator
     */
    private void translateMorseTextAreaToText() {
        String textTranslation = Translator.morseToText(send_morse_textArea.getText());
        // todo Condition 'textTranslation == null' is always 'false'
        if (textTranslation == null) {
            showMessageDialog(null, "Bitte geben Sie nur Text ein, der übersetzt werden kann (Siehe Informationen)", "Falsche Eingabe", JOptionPane.WARNING_MESSAGE);
            send_text_textArea.setText("");
            System.out.println("morseToText_translationError");
        } else {
            send_text_textArea.setText(textTranslation);
        }
    }

    /**
     * Trying to translate the Text, contained in {@link #send_text_textArea} into the {@link #send_morse_textArea} as Morse Code.
     * If failed, display an error message
     *
     * @see Translator
     */
    private void translateTextAreaTextToMorse() {
        String morseTranslation = Translator.textToMorse(send_text_textArea.getText());
        if (morseTranslation == null) {
            showMessageDialog(null, "Bitte geben Sie nur Text ein, der übersetzt werden kann (Siehe Informationen)", "Falsche Eingabe", JOptionPane.WARNING_MESSAGE);
            send_morse_textArea.setText("");
            System.out.println("textToMorse_translationError");
        } else {
            send_morse_textArea.setText(morseTranslation);
        }
    }

    /**
     * Clearing the body of all provided {@link TextArea}s
     *
     * @param textAreas to be cleared
     * @see TextArea
     */
    private void clear_textAreas(JTextArea... textAreas) {
        for (JTextArea textArea :
                textAreas) {
            textArea.setText("");
        }
    }

    /**
     * Adjusting the sizes of the provided {@link TextArea}s to each take up 50% of the given {@param splitPane}
     * and setting the resize weight of the given {@param splitPane} to 50% so that the divider stays in the middle.
     *
     * @param splitPane      between two textAreas
     * @param text_textArea  textArea for text
     * @param morse_textArea textArea for morse
     */
    private void adjust_split_pane_sizes(JSplitPane splitPane, JTextArea text_textArea, JTextArea morse_textArea) {
        Dimension textAreas_preferredDimension = new Dimension(splitPane.getWidth() / 2, text_textArea.getPreferredSize().height);
        morse_textArea.setPreferredSize(textAreas_preferredDimension);
        morse_textArea.setMinimumSize(textAreas_preferredDimension);
        text_textArea.setPreferredSize(textAreas_preferredDimension);
        text_textArea.setMinimumSize(textAreas_preferredDimension);
        splitPane.setDividerLocation(splitPane.getWidth() / 2);
        splitPane.setResizeWeight(0.5);
    }

    /**
     * @return 2-Dimensional {@link String} Array with Morse Code and Translations to fill the {@link #table_alphabet}
     * @see Translator
     */
    private String[][] getTableData() {
        ArrayList<Character> allCharacters = new ArrayList<>(Translator.getCharToMorse().keySet());
        String[][] data = new String[42][2];
        int counter = 0;
        for (Character allCharacter : allCharacters) {
            data[counter][0] = "" + allCharacter;
            data[counter][1] = Translator.toMorse(allCharacter);
            counter++;
        }
        return data;
    }

    /**
     * Getter to get the {@link #GUI_DECODER_SYNCHRONIZE_Object}-Object which the {@link Decoder} and the {@link #ui_update_thread} are using to synchronize.
     *
     * @return The {@link #GUI_DECODER_SYNCHRONIZE_Object}-Object.
     */
    public Object getGuiDecoderSynchronizeObject() {
        return GUI_DECODER_SYNCHRONIZE_Object;
    }

    /**
     * @return the noise threshold, currently selected in {@link #receive_sensitivity_slider}
     * @see Encoder
     * @see #startRecording(ActionEvent)
     */
    public int getNoiseThreshold() {
        return 100 - receive_sensitivity_slider.getValue();
    }
}