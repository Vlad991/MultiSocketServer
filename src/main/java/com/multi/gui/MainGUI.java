package com.multi.gui;

import com.multi.server.Server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainGUI extends JFrame {  // Вся панель GUI
    private JPanel contentPane;  // вся панель без шапки
    private ServerManagePanel serverManagePanel; // панель по управлению сервером
    private ClientsTablePanel clientsTablePanel; // панель клиентов
    private ClientsActionsPanel clientsActionsPanel; // панель действий над клиентами

    private Server server;

    public MainGUI() {
        super("Multi Socket Server");
        this.setBounds(200, 200, 600, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        server = new Server();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new GridBagLayout());
        setContentPane(contentPane);

        serverManagePanel = new ServerManagePanel(server);
        clientsTablePanel = new ClientsTablePanel();
        clientsActionsPanel = new ClientsActionsPanel(clientsTablePanel.getTableBuilder());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        contentPane.add(serverManagePanel, c);
        c.gridx = 0;
        c.gridy = 1;
        contentPane.add(clientsTablePanel, c);
        c.gridx = 0;
        c.gridy = 2;
        contentPane.add(clientsActionsPanel, c);

        Container container = this.getContentPane();
        container.setBounds(200, 200, 1000, 400);
        pack();
        setVisible(true);
    }

    public ClientsTablePanel getClientsTablePanel() {
        return clientsTablePanel;
    }
}
