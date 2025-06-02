package pwydmuch.view;

import pwydmuch.domain.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.stream.IntStream;


public class ChangeView extends JFrame implements View, ItemListener {

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private static final String BEGINNER = "Beginner";
    private static final String INTERMEDIATE = "Intermediate";
    private static final String ADVANCED = "Advanced";
    private static final String CUSTOM = "Custom";
    private static final int CUSTOM_OPTION_INDEX = 0;
    private static final int BEGINNER_OPTION_INDEX = 1;
    private static final int INTERMEDIATE_OPTION_INDEX = 2;
    private static final int ADVANCED_OPTION_INDEX = 3;
    private final JTextField customRowsInput, customColumnsInput, customMinesInput;
    private final JPanel jPan, jPan2, jPan3, jPan4;
    private final JCheckBox[] newGameOptions;
    private JButton cancelButton, okButton;
    private JFrame frame;
    private final JLabel errorLabel;
    private int newGameMinesNumber;
    private int newGameRows;
    private int newGameColumns;


    ChangeView(JFrame frame) {
        okButton = new JButton(OK);
        cancelButton = new JButton(CANCEL);
        customRowsInput = new JTextField("", 32);
        customColumnsInput = new JTextField(32);
        customMinesInput = new JTextField(32);
        jPan4 = new JPanel();
        jPan3 = new JPanel();
        jPan2 = new JPanel();
        jPan = new JPanel();
        errorLabel = new JLabel();
        this.frame = frame;
        newGameOptions = new JCheckBox[4];
        newGameOptions[CUSTOM_OPTION_INDEX] = new JCheckBox(CUSTOM);
        newGameOptions[BEGINNER_OPTION_INDEX] = new JCheckBox(BEGINNER);
        newGameOptions[INTERMEDIATE_OPTION_INDEX] = new JCheckBox(INTERMEDIATE);
        newGameOptions[ADVANCED_OPTION_INDEX] = new JCheckBox(ADVANCED);
    }

    @Override
    public void showView() {
        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(330, 250);
        setResizable(false);
        setLocation(frame.getX(), frame.getY() + frame.getHeight() / 4);
        jPan.setLayout(new BorderLayout());
        setPanel2();
        setPanel3();
        setPanel4();
        jPan.add(jPan2, BorderLayout.WEST);
        jPan.add(jPan3, BorderLayout.CENTER);
        jPan.add(jPan4, BorderLayout.SOUTH);
        add(jPan);
        for (var c : newGameOptions) c.addItemListener(this);
    }

    private void setPanel2() {
        jPan2.setLayout(new BoxLayout(jPan2, BoxLayout.Y_AXIS));
        jPan2.add(newGameOptions[1]);
        jPan2.add(new JLabel("-10 mines"));
        jPan2.add(new JLabel("-Grid 9x9"));
        jPan2.add(newGameOptions[2]);
        jPan2.add(new JLabel("-40 mines"));
        jPan2.add(new JLabel("-Grid 16x16"));
        jPan2.add(newGameOptions[3]);
        jPan2.add(new JLabel("-99 mines"));
        jPan2.add(new JLabel("-Grid 16x30"));
    }

    private void setPanel3() {
        jPan3.setLayout(new BoxLayout(jPan3, BoxLayout.Y_AXIS));
        customRowsInput.setPreferredSize(new Dimension(10, 10));
        setEditableInputsTo(false);
        customRowsInput.setFocusable(true);
        errorLabel.setVisible(false);
        jPan3.add(newGameOptions[0]);
        jPan3.add(new JLabel("Rows (9-24)"));
        jPan3.add(customRowsInput);
        jPan3.add(new JLabel("Columns (9-30)"));
        jPan3.add(customColumnsInput);
        jPan3.add(new JLabel("Mines (10-668)"));
        jPan3.add(customMinesInput);
        jPan3.add(errorLabel);
    }

    private void setPanel4() {
        okButton = new JButton(OK);
        cancelButton = new JButton(CANCEL);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        jPan4.add(okButton);
        jPan4.add(cancelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            if (newGameOptions[CUSTOM_OPTION_INDEX].isSelected()) {
                runWithCustomOptions();
            } else {
                startNewGame();
            }
        } else
            this.dispose();
    }

    private void runWithCustomOptions() {
        if (assignCustomValues()) {
            checkIfInputsInRange();
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("You must provide a valid value");
        }
    }

    private void checkIfInputsInRange() {
        if (newGameRows > 24 || newGameRows < 9) {
            errorLabel.setVisible(true);
            errorLabel.setText("Rows out of range");
        } else if (newGameColumns > 30 || newGameColumns < 9) {
            errorLabel.setVisible(true);
            errorLabel.setText("Columns out of range");
        } else if (isNumberOfMinesIncorrect()) {
            errorLabel.setVisible(true);
            errorLabel.setText("Number of mines out of range");
        } else startNewGame();
    }

    private boolean isNumberOfMinesIncorrect() {
        return newGameMinesNumber < 10 || newGameMinesNumber > 668
                || newGameMinesNumber > newGameColumns * newGameRows;
    }

    private void startNewGame() {
        var board = new Board(newGameRows, newGameColumns, newGameMinesNumber);
        var mainView = new MainView(board);
//                pwydmuch.Minesweeper.save(sap);
        frame.setVisible(false);
        frame = null;
        this.dispose();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED)
            for (var i = 0; i < newGameOptions.length; i++)
                if (e.getSource() == newGameOptions[i]) {
                    unselectOthersOptions(i);
                    selectOption(i);
                    return;
                }
    }

    private void unselectOthersOptions(int optionIndex) {
        IntStream.range(0, newGameOptions.length)
                .filter(i -> i != optionIndex)
                .forEach(i -> newGameOptions[i].setSelected(false));
    }

    private void selectOption(int optionIndex) {
        switch (optionIndex) {
            case CUSTOM_OPTION_INDEX -> {
                customRowsInput.setFocusable(true);
                setEditableInputsTo(true);
            }
            case BEGINNER_OPTION_INDEX -> {
                setEditableInputsTo(false);
                newGameMinesNumber = 10;
                newGameRows = 9;
                newGameColumns = 9;
            }
            case INTERMEDIATE_OPTION_INDEX -> {
                setEditableInputsTo(false);
                newGameMinesNumber = 40;
                newGameRows = 16;
                newGameColumns = 16;
            }
            case ADVANCED_OPTION_INDEX -> {
                setEditableInputsTo(false);
                newGameMinesNumber = 99;
                newGameRows = 16;
                newGameColumns = 30;
            }
        }
    }

    private void setEditableInputsTo(boolean option) {
        customRowsInput.setEditable(option);
        customColumnsInput.setEditable(option);
        customMinesInput.setEditable(option);
    }

    public boolean assignCustomValues() {
        try {
            newGameRows = Integer.parseInt(customRowsInput.getText());
            newGameColumns = Integer.parseInt(customColumnsInput.getText());
            newGameMinesNumber = Integer.parseInt(customMinesInput.getText());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

}
