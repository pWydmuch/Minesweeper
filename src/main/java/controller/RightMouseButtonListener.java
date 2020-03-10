//package controller;
//
//import model.MyButton;
//import view.MainView;
//import view.SuccessView;
//
//import javax.swing.*;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.io.Serializable;
//
//import static model.MyButton.flag;
//import static model.MyButton.questionMark;
//
//class RightMouseButton implements MouseListener, Serializable {
//
//    @Override public void mouseClicked(MouseEvent e) {
//
//        if (SwingUtilities.isRightMouseButton(e)) {
//            for (MyButton[] button : buttons) {
//                for (MyButton aButton : button) {
//                    if (e.getSource() == aButton) {
//                        aButton.incrementClickNumber();
//                        if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == FLAG_STATE) {
//                            aButton.setIcon(flag);
//                            flagsNumber--;
//                            minesLeftLabel.setText(String.valueOf(flagsNumber));
//                            aButton.removeMouseListener(leftMouseButton);
//                            return;
//                        }
//                        if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == QUESTION_STATE) {
//                            aButton.setIcon(questionMark);
//                            flagsNumber++;
//                            minesLeftLabel.setText(String.valueOf(flagsNumber));
//                            aButton.addMouseListener(leftMouseButton);
//                            return;
//                        }
//                        if (aButton.getClicksNumber() % NUMBER_OF_BUTTON_STATES == EMPTY_STATE) {
//                            aButton.setIcon(null);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        if(draw.isSuccess(buttons, flag)&& flagsNumber ==0) {
//            timer.stop();
//            timer.setDelay(Integer.MAX_VALUE);
//            new SuccessView(MainView.this).showView();
//
//        }
//    }
//
//    @Override public void mouseReleased(MouseEvent e) {}
//    @Override public void mouseEntered(MouseEvent e) {}
//    @Override public void mouseExited(MouseEvent e) {}
//    @Override public void mousePressed(MouseEvent e) {}
//
//}