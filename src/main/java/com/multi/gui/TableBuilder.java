package com.multi.gui;

import com.multi.dao.ClientInfoDAO;
import com.multi.dto.ClientInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TableBuilder { //обновляет таблицу клиентов
    private ClientInfoDAO clientInfoDAO;
    private JTable clientsTable;
    private DefaultTableModel model;

    public TableBuilder(JTable clientsTable, DefaultTableModel model) {
        this.clientInfoDAO = new ClientInfoDAO();
        this.model = model;
        this.clientsTable = clientsTable;
        clientsTable.setModel(model);
    }

    public void updateClientsTable() {
        List<ClientInfo> clientInfoList = clientInfoDAO.getClientInfoList(); //достаем список клиентов
        model.setRowCount(0); //очищает таблицу
        for (ClientInfo clientInfo : clientInfoList) { // заполняем таблицу клиентов
            model.addRow(new Object[]{ clientInfo.getIpAddress(), clientInfo.getName(), clientInfo.getStatus()}); //todo
        }
    }
}
