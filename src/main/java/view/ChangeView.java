package view;

import controller.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serializable;


public class ChangeView extends JFrame implements View, ItemListener {

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";
    private static final String BEGINNER = "Beginner";
    private static final String INTERMEDIATE = "Intermediate";
    private static final String ADVANCED = "Advanced";
    private static final String CUSTOM = "Custom";
    private JTextField tf1, tf2, tf3;
    private JPanel jPan, jPan2, jPan3, jPan4;
    private JCheckBox[] cb;
    private JButton jb2, jb;
    private JFrame jf;
    private JLabel jl;
    private int newGameMinesNumber;
    private int newGameHeight;
    private int newGameWidth;


    ChangeView(JFrame jf) {

        jb = new JButton(OK);
        jb2 = new JButton(CANCEL);
        tf1 = new JTextField("", 32);
        tf2 = new JTextField(32);
        tf3 = new JTextField(32);
        jPan4 = new JPanel();
        jPan3 = new JPanel();
        jPan2 = new JPanel();
        jPan = new JPanel();
        jl = new JLabel();
        this.jf = jf;
        cb = new JCheckBox[4];
        cb[1] = new JCheckBox(BEGINNER);
        cb[2] = new JCheckBox(INTERMEDIATE);
        cb[3] = new JCheckBox(ADVANCED);
        cb[0] = new JCheckBox(CUSTOM);

    }

    @Override
    public void showView() {

        setTitle("MainView");
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(330, 250);
        setResizable(false);
        setLocation(jf.getX(), jf.getY() + jf.getHeight() / 4);
        jPan.setLayout(new BorderLayout());
        setPanel2();
        setPanel3();
        setPanel4();
        jPan.add(jPan2, BorderLayout.WEST);
        jPan.add(jPan3, BorderLayout.CENTER);
        jPan.add(jPan4, BorderLayout.SOUTH);
        add(jPan);
        for (JCheckBox c : cb)
            c.addItemListener(this);
    }

    private void setPanel2() {

        jPan2.setLayout(new BoxLayout(jPan2, BoxLayout.Y_AXIS));
        jPan2.add(cb[1]);
        jPan2.add(new JLabel("-10 mines"));
        jPan2.add(new JLabel("-Grid 9x9"));
        jPan2.add(cb[2]);
        jPan2.add(new JLabel("-40 mines"));
        jPan2.add(new JLabel("-Grid 16x16"));
        jPan2.add(cb[3]);
        jPan2.add(new JLabel("-99 mines"));
        jPan2.add(new JLabel("-Grid 16x30"));
    }

    private void setPanel3() {

        jPan3.setLayout(new BoxLayout(jPan3, BoxLayout.Y_AXIS));
        tf1.setPreferredSize(new Dimension(10, 10));
        tf1.setEditable(false);
        tf2.setEditable(false);
        tf3.setEditable(false);
        tf1.setFocusable(true);
        tf3.addActionListener(new CustomGame());
        jl.setVisible(false);
        jPan3.add(cb[0]);
        jPan3.add(new JLabel("Height (9-24)"));
        jPan3.add(tf1);
        jPan3.add(new JLabel("Width (9-30)"));
        jPan3.add(tf2);
        jPan3.add(new JLabel("Mines (10-668)"));
        jPan3.add(tf3);
        jPan3.add(jl);
    }

    private void setPanel4() {

        jb = new JButton(OK);
        jb2 = new JButton(CANCEL);
        jb.addActionListener(this);
        jb2.addActionListener(this);
        jPan4.add(jb);
        jPan4.add(jb2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jb) {
            if (newGameHeight > 24 || newGameHeight < 9) {
                jl.setVisible(true);
                jl.setText("Height out of range");
            } else if (newGameWidth > 30 || newGameWidth < 9) {
                jl.setVisible(true);
                jl.setText("Width out of range");
            } else if (newGameMinesNumber < 10 || newGameMinesNumber > 668 || newGameMinesNumber > newGameWidth * newGameHeight) {
                jl.setVisible(true);
                jl.setText("Number of mines out of range");
            } else {
                MainView sap = new MainView(newGameHeight, newGameWidth, newGameMinesNumber);
                sap.go();
                Minesweeper.save(sap);
                jf.setVisible(false);
                jf = null;
                this.dispose();
            }
        } else
            this.dispose();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED)
            for (int i = 0; i < cb.length; i++)
                if (e.getSource() == cb[i]) {
                    unSelect(i);
                    setNew(i);
                    return;
                }
    }

    private void unSelect(int index) {
        for (int i = 0; i < cb.length; i++) {
            if (i == index) continue;
            cb[i].setSelected(false);
        }
    }

    private void setNew(int index) {
        if (index == 0) {
            tf1.setFocusable(true);
            tf1.setEditable(true);
            tf2.setEditable(true);
            tf3.setEditable(true);

        }
        if (index == 1) {
            tf1.setEditable(false);
            tf2.setEditable(false);
            tf3.setEditable(false);
            newGameMinesNumber = 10;
            newGameHeight = 9;
            newGameWidth = 9;
        }
        if (index == 2) {
            tf1.setEditable(false);
            tf2.setEditable(false);
            tf3.setEditable(false);
            newGameMinesNumber = 40;
            newGameHeight = 16;
            newGameWidth = 16;
        }
        if (index == 3) {
            tf1.setEditable(false);
            tf2.setEditable(false);
            tf3.setEditable(false);
            newGameMinesNumber = 99;
            newGameHeight = 16;
            newGameWidth = 30;
        }
    }
    class CustomGame implements ActionListener, Serializable {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                newGameHeight = Integer.parseInt(tf1.getText());
                newGameWidth = Integer.parseInt(tf2.getText());
                newGameMinesNumber = Integer.parseInt(tf3.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
