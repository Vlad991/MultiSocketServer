package com.multi.dto;

import java.io.Serializable;

public class ClientInfo implements Serializable {
    private String ipAddress;
    private String name;
    private ClientStatus status; // может быть только CONNECTED или DISCONNECTED


    public ClientInfo() {
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }
}
