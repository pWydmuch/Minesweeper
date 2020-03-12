package pwydmuch.view;

import pwydmuch.model.Draw;
import pwydmuch.model.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class ChangeView extends JFrame implements View, ItemListener {

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private static final String BEGINNER = "Beginner";
    private static final String INTERMEDIATE = "Intermediate";
    private static final String ADVANCED = "Advanced";
    private static final String CUSTOM = "Custom";
    private static final int CUSTOM_OPTION_INDEX = 0;
    private static final int BEGGINER_OPTION_INDEX = 1;
    private static final int INTERMEDIATE_OPTION_INDEX = 2;
    private static final int ADVANCED_OPTION_INDEX = 3;
    private JTextField customRowsInput, customColumnsInput, customMinesInput;
    private JPanel jPan, jPan2, jPan3, jPan4;
    private JCheckBox[] newGameOptions;
    private JButton concelButton, okButton;
    private JFrame frame;
    private JLabel errorLabel;
    private int newGameMinesNumber;
    private int newGameRows;
    private int newGameColumns;


    ChangeView(JFrame frame) {

        okButton = new JButton(OK);
        concelButton = new JButton(CANCEL);
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
        newGameOptions[BEGGINER_OPTION_INDEX] = new JCheckBox(BEGINNER);
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
        for (JCheckBox c : newGameOptions)
            c.addItemListener(this);
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
        customRowsInput.setEditable(false);
        customColumnsInput.setEditable(false);
        customMinesInput.setEditable(false);
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
        concelButton = new JButton(CANCEL);
        okButton.addActionListener(this);
        concelButton.addActionListener(this);
        jPan4.add(okButton);
        jPan4.add(concelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == okButton) {
            if (newGameOptions[CUSTOM_OPTION_INDEX].isSelected()) {
                assignCustomValues();
                if (newGameRows > 24 || newGameRows < 9) {
                    errorLabel.setVisible(true);
                    System.out.println(newGameRows);
                    System.out.println(customRowsInput.getText());
                    errorLabel.setText("Rows out of range");
                } else if (newGameColumns > 30 || newGameColumns < 9) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Columns out of range");
                } else if (newGameMinesNumber < 10 || newGameMinesNumber > 668 || newGameMinesNumber > newGameColumns * newGameRows) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Number of mines out of range");
                } else startNewGame();
            } else {
                startNewGame();
            }
        } else
            this.dispose();
    }

    private void startNewGame(){
        MyButton[][] myButtons = new MyButton[newGameRows][newGameColumns];
        Draw draw = new Draw(newGameMinesNumber, newGameRows, newGameColumns);
        MainView mainView = new MainView(myButtons, draw);
        mainView.go();
//                pwydmuch.Minesweeper.save(sap);
        frame.setVisible(false);
        frame = null;
        this.dispose();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED)
            for (int i = 0; i < newGameOptions.length; i++)
                if (e.getSource() == newGameOptions[i]) {
                    unselectOthersOptions(i);
                    selectOption(i);
                    return;
                }
    }

    private void unselectOthersOptions(int optionIndex) {
        for (int i = 0; i < newGameOptions.length; i++) {
            if (i == optionIndex) continue;
            newGameOptions[i].setSelected(false);
        }
    }

    private void selectOption(int optionIndex) {
        if (optionIndex == CUSTOM_OPTION_INDEX) {
            customRowsInput.setFocusable(true);
            customRowsInput.setEditable(true);
            customColumnsInput.setEditable(true);
            customMinesInput.setEditable(true);

        }
        if (optionIndex == BEGGINER_OPTION_INDEX) {
            customRowsInput.setEditable(false);
            customColumnsInput.setEditable(false);
            customMinesInput.setEditable(false);
            newGameMinesNumber = 10;
            newGameRows = 9;
            newGameColumns = 9;
        }
        if (optionIndex == INTERMEDIATE_OPTION_INDEX) {
            customRowsInput.setEditable(false);
            customColumnsInput.setEditable(false);
            customMinesInput.setEditable(false);
            newGameMinesNumber = 40;
            newGameRows = 16;
            newGameColumns = 16;
        }
        if (optionIndex == ADVANCED_OPTION_INDEX) {
            customRowsInput.setEditable(false);
            customColumnsInput.setEditable(false);
            customMinesInput.setEditable(false);
            newGameMinesNumber = 99;
            newGameRows = 16;
            newGameColumns = 30;
        }
    }

    public void assignCustomValues() {
        try {
            newGameRows = Integer.parseInt(customRowsInput.getText());
            newGameColumns = Integer.parseInt(customColumnsInput.getText());
            newGameMinesNumber = Integer.parseInt(customMinesInput.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
