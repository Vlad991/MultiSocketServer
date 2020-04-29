package com.multi.gui;

import com.multi.server.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartServerButtonEventListener implements ActionListener { //кнопка старта сервера
    private Server server;
    private JTextField serverPortField;

    public StartServerButtonEventListener(Server server, JTextField serverPortField) {
        super();
        this.server = server;
        this.serverPortField = serverPortField;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //метод выполняется при нажатии кнопки
        try {
            server.start(Integer.parseInt(serverPortField.getText())); // сервер стартует
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
