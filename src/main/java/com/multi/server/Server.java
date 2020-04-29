package com.multi.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.Main;
import com.multi.dao.ClientInfoDAO;
import com.multi.dao.FileDataDAO;
import com.multi.dto.ClientInfo;
import com.multi.dto.ClientStatus;
import com.multi.dto.FileData;
import com.multi.dto.QueryParameter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private ServerSocket server;
    private boolean serverStatus = false; // Показывает работает сервер или нет (start/stop)
    FileDataDAO fileDataDAO = new FileDataDAO();  // соединение (client, client_trash)
    ClientInfoDAO clientInfoDAO = new ClientInfoDAO();  // информация о клиентах (Clients)
    ObjectMapper objectMapper = new ObjectMapper();  // информация о клиентах (Clients)

    public Server() {
    }

    public void start(int port) throws Exception {   // главный метод, запускает сервер
        if (isRunning()) {
            System.out.println("Server is already running!");
            return;
        }
        serverStatus = true;
        server = new ServerSocket(port);
        System.out.println("Server started on port(" + server.getLocalPort() + ")");

        Runnable acceptClientsRunnable = () -> {  // открываем новый поток для подключения клиентов
            try {
                acceptClients();
            } catch (Exception e) {
                System.out.println("Server stopped!");
            }
        };
        new Thread(acceptClientsRunnable).start();
    }

    private void acceptClients() throws Exception { // добавляет клиентов
        while (isRunning()) {
            Socket socket = server.accept();  //главны метод, ждет подключения клиента

            Runnable clientRequestProcessingRunnable = () -> {  // собственный поток каждого клиента
                try {
                    processQueries(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(clientRequestProcessingRunnable).start();
        }
        System.out.println("Server is stopped");
    }

    private void processQueries(Socket socket) throws Exception {  //обработка запросов клиентов
        ClientInfo clientInfo = clientInfoDAO
                .getClientInfoByIpAddress(socket.getInetAddress().toString().replace("/", ""));
        if (clientInfo == null) {  // значит клиент не зарегестрирован
            System.out.println("Client " + socket.getInetAddress().toString().replace("/", "") + " is not registered!");
        } else {  //клиент зареган
            clientInfoDAO.setClientInfoStatus(clientInfo.getIpAddress(), ClientStatus.CONNECTED);
            clientInfo = clientInfoDAO
                    .getClientInfoByIpAddress(socket.getInetAddress().toString().replace("/", ""));
            Main.getMainGUI().getClientsTablePanel().getTableBuilder().updateClientsTable();
            System.out.println("Client accepted: " + socket.toString());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);  //поток вывода данных с сервера на клиент
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));  //поток ввода данных (с клиента)

            while (processQuery(inputStream, printWriter, clientInfo)) {
                if (!isRunning()) break;  //если сервер остановлен запросы больше не обрабатываются
                if (clientInfo.getStatus() == ClientStatus.DISCONNECTED) {
                    clientInfoDAO.setClientInfoStatus(clientInfo.getIpAddress(), ClientStatus.DISCONNECTED);
                    break;
                }
            }

            System.out.println("Client " + socket.toString() + " disconnected!");
            Main.getMainGUI().getClientsTablePanel().getTableBuilder().updateClientsTable();
        }
    }

    private boolean processQuery(BufferedReader inputStream, PrintWriter printWriter, ClientInfo clientInfo) {
        try {
            String jsonFileData;
            jsonFileData = inputStream.readLine(); //проверка, есть ли строка в запросе и получение строки (запроса)
            System.out.println(jsonFileData);
            if (jsonFileData == null) {
                clientInfo.setStatus(ClientStatus.DISCONNECTED);
                return true;
            }
            if (jsonFileData.equals("disconnect") || jsonFileData.equals("-1")) { // прерывается подключение с клиентом (если клиент пишет 'disconnect')
                clientInfo.setStatus(ClientStatus.DISCONNECTED);
                return true;
            }
            ArrayList<HashMap> fileDataList = objectMapper.readValue(jsonFileData, ArrayList.class);

            if (fileDataList.size() == 0) {
                printWriter.println("Json string is incorrect!");
                return true;
            }
            if (fileDataList.get(0).get(QueryParameter.iEventType.getParameter()) == null) {  //проверка есть ли евент
                printWriter.println("Json has no eventType field!");
                return true;
            }
            for (HashMap fileDataElement : fileDataList) {
                FileData fileData = new FileData();
                fileData.setEventType((Integer) fileDataElement.get(QueryParameter.iEventType.getParameter()));  //достаю значение eventType
                HashMap<String, Object> jsonHeaderHashMap = new HashMap<>();
                HashMap<String, Object> jsonParameterHashMap;
                for (QueryParameter parameter : QueryParameter.values()) {
                    Object fileDataElementFieldValue = fileDataElement.get(parameter.getParameter());
                    if (fileDataElementFieldValue instanceof String) {
                        fileDataElementFieldValue = fileDataElementFieldValue.toString().replaceAll("'", "");
                    }
                    jsonHeaderHashMap.put(parameter.toString(), fileDataElementFieldValue);
                    fileDataElement.remove(parameter.getParameter());
                }
                fileDataElement.forEach((key, value) -> {
                    value = value.toString().replaceAll("'", "");
                    fileDataElement.put(key, value);
                });
                jsonParameterHashMap = fileDataElement;
                fileData.setJsonHeaderHashMap(jsonHeaderHashMap);
                fileData.setJsonParameterHashMap(jsonParameterHashMap);
                fileDataDAO.addFileData(fileData);  //записываю обьекты в базу
            }

            printWriter.println("Operation success!");
            return true;
        } catch (Exception ex) {
//            ex.printStackTrace();
            printWriter.println("Not a json format!");
            return true;
        }
    }

    public boolean isRunning() {
        return serverStatus;
    }

    public void stop() throws IOException {  //останавливает сервер server.stop() (gui)
        serverStatus = false;
        List<ClientInfo> clientInfoList = clientInfoDAO.getClientInfoList();
        for (ClientInfo client : clientInfoList) {
            clientInfoDAO.setClientInfoStatus(client.getIpAddress(), ClientStatus.DISCONNECTED);
        }
        if (server != null) {
            server.close();
        }
        Main.getMainGUI().getClientsTablePanel().getTableBuilder().updateClientsTable();
    }
}
