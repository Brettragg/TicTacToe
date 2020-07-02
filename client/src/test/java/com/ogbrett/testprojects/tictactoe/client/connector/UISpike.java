package com.ogbrett.testprojects.tictactoe.client.connector;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UISpike {

    private JTextField tempTextField;
    private JLabel fahrenheitLabel;
    private final Object lock = new Object();
    private JFrame frame;

    private void createAndShowGUI() {
        tempTextField = new JTextField();
        fahrenheitLabel = new JLabel();
        JButton convertButton = new JButton();
        JLabel celsiusLabel = new JLabel();

        //Configure elements
        fahrenheitLabel.setText("Fahrenheit");
        celsiusLabel.setText("Celsius");
        convertButton.setText("Convert");
        convertButton.addActionListener(actionEvent -> convertButtonActionPerformed());

        //Create and set up the window
        frame = new JFrame("UI Spike");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(tempTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(celsiusLabel)
                                )
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(convertButton)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(fahrenheitLabel)
                                )
                        )
                        .addContainerGap(27, Short.MAX_VALUE)
                )
        );
        layout.linkSize(SwingConstants.HORIZONTAL, convertButton, tempTextField);
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(tempTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(celsiusLabel)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(convertButton)
                                .addComponent(fahrenheitLabel)
                        )
                        .addContainerGap(21, Short.MAX_VALUE)
                )
        );

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized(lock) {
                    frame.setVisible(false);
                    lock.notify();
                }
            }
        });

        //Display the window
        frame.pack();
        frame.setVisible(true);
    }

    private void convertButtonActionPerformed() {
        double tempFahr = (Double.parseDouble(tempTextField.getText())) * 1.8 + 32;
        fahrenheitLabel.setText(tempFahr + " Fahrenheit");
    }

    @Test
    public void uiTest() throws Exception {
        //javax.swing.SwingUtilities.invokeLater(UISpike::createAndShowGUI);
        createAndShowGUI();
        synchronized(lock) {
            while(frame.isVisible()) {
                lock.wait();
            }
        }
    }
}
