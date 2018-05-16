package com.quarto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Final extends JDialog {
    private JPanel contentPane;
    private JTextArea resultTextArea;


    public Final(String result) {
        resultTextArea.setText(result);
        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args,String result) {
        Final aFinal = new Final(result);
        aFinal.pack();
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - aFinal.getWidth()) / 2;
        final int y = (screenSize.height - aFinal.getHeight()) / 2;
        aFinal.setLocation(x,y);
        aFinal.setVisible(true);
        aFinal.setTitle("Quarto Game");
        System.exit(0);
    }
}
