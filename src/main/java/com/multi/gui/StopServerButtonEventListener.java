package com.multi.gui;

import com.multi.server.Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StopServerButtonEventListener implements ActionListener {
    private Server server;

    public StopServerButtonEventListener(Server server) {
        super();
        this.server = server;
    }

    @Override
    public void actionPerformed(ActionEvent e) { //принажатии кнопки останавливает сервер
        try {
            server.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
