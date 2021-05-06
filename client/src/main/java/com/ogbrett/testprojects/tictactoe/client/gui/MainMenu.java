package com.ogbrett.testprojects.tictactoe.client.gui;

import com.ogbrett.testprojects.tictactoe.client.connector.ClientConnector;
import com.ogbrett.testprojects.tictactoe.client.connector.JMSClientConnector;
import com.ogbrett.testprojects.tictactoe.core.beans.responses.TTTStateResponse;

import javax.swing.*;

public class MainMenu {
    private JTextField loginTextField;
    private JTextField serverAddressTextField;
    private JFrame connectingFrame;
    private JLabel connectingLabel;
    private boolean stopThread;
    private Object threadLock;

    private Thread connectingThread;

    private void createAndShowGUI() {
        loginTextField = new JTextField();
        serverAddressTextField = new JTextField();
        JLabel loginLabel = new JLabel("Login");
        JLabel serverAddressLabel = new JLabel("Server address");
        JButton connectButton = new JButton("Connect");

        //Configure elements
        connectButton.addActionListener(actionEvent -> connectButtonActionPerformed());

        //Initialize main frame
        JFrame mainFrame = new JFrame("Main menu");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout mainLayout = new GroupLayout(mainFrame.getContentPane());
        mainLayout.setAutoCreateGaps(true);
        mainLayout.setAutoCreateContainerGaps(true);
        mainFrame.getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
            mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(loginLabel)
                    .addComponent(serverAddressLabel)
                    .addComponent(connectButton)
                )
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(serverAddressTextField, 100, 150, 200)
                    .addComponent(loginTextField, 100, 150, 200)
                )
        );
        mainLayout.setVerticalGroup(
            mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(loginLabel)
                    .addComponent(loginTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(serverAddressLabel)
                    .addComponent(serverAddressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                )
                .addComponent(connectButton)
        );

        connectingLabel = new JLabel("Connecting...");
        JButton connectingCancelButton = new JButton("Cancel");
        connectingCancelButton.addActionListener(actionEvent -> cancelConnectingButtonActionPerformed());
        //Initialize connecting frame
        connectingFrame = new JFrame("Connecting");
        connectingFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        connectingFrame.setSize(500, 300);
        GroupLayout connectingLayout = new GroupLayout(connectingFrame.getContentPane());
        connectingLayout.setAutoCreateGaps(true);
        connectingLayout.setAutoCreateContainerGaps(true);
        connectingFrame.getContentPane().setLayout(connectingLayout);
        connectingLayout.setVerticalGroup(
            connectingLayout.createSequentialGroup()
                .addComponent(connectingLabel)
                .addComponent(connectingCancelButton)
        );
        connectingLayout.setHorizontalGroup(
            connectingLayout.createParallelGroup()
                .addComponent(connectingLabel)
                .addComponent(connectingCancelButton)
        );

        //Display the window
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private void connectButtonActionPerformed() {
        try (ClientConnector clientConnector = new JMSClientConnector(serverAddressTextField.getText(), loginTextField.getText())) {
            System.out.println(clientConnector.getState());
            connectingThread = new Thread(() -> {
                while (!stopThread) {
                    try {
                        if (clientConnector.getState() == TTTStateResponse.PlayerState.WAITING_FOR_SECOND_PLAYER) {
                            connectingLabel.setText("Waiting for second player");
                        } else {
                            // TODO: 4/15/2021 start the game
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // TODO: 4/15/2021 catch
                    }
                    try {
                        Thread.sleep(500); //fixme busy waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();// TODO: 4/15/2021 catch
                    }
                }
            });
            connectingThread.start();
            connectingFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelConnectingButtonActionPerformed() {
        // TODO: 4/3/2021 disconnect from server
        // TODO: 4/15/2021 join thread
        connectingFrame.dispose();
    }

    public void show() {
//        SwingUtilities.invokeLater(this::createAndShowGUI);
        createAndShowGUI();
    }
}
