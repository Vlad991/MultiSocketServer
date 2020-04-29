package com.multi.gui;

import com.multi.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ServerStatusIndicatorListener implements MouseListener {
    private Server server;
    private JLabel serverStatusIndicator;

    public ServerStatusIndicatorListener(Server server, JLabel serverStatusIndicator) {
        super();
        this.server = server;
        this.serverStatusIndicator = serverStatusIndicator;
    }

    @Override
    public void mouseClicked(MouseEvent e) { // при нажэатии start or stop проверяет запущен ли сервер, исссответственно ставит цвет идикатору
        if (server.isRunning()) {
            serverStatusIndicator.setBackground(Color.GREEN);
            serverStatusIndicator.setText("Running!");
        } else {
            serverStatusIndicator.setBackground(Color.RED);
            serverStatusIndicator.setText("Not Running..");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (server.isRunning()) {
            serverStatusIndicator.setBackground(Color.GREEN);
            serverStatusIndicator.setText("Running!");
        } else {
            serverStatusIndicator.setBackground(Color.RED);
            serverStatusIndicator.setText("Not Running..");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (server.isRunning()) {
            serverStatusIndicator.setBackground(Color.GREEN);
            serverStatusIndicator.setText("Running!");
        } else {
            serverStatusIndicator.setBackground(Color.RED);
            serverStatusIndicator.setText("Not Running..");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
