package com.multi.dto;

import java.io.Serializable;
import java.util.HashMap;

public class FileData implements Serializable {
    private int eventType;
    private HashMap<String, Object> jsonHeaderHashMap;
    private HashMap<String, Object> jsonParameterHashMap;

    public FileData() {
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public HashMap<String, Object> getJsonHeaderHashMap() {
        return jsonHeaderHashMap;
    }

    public void setJsonHeaderHashMap(HashMap<String, Object> jsonHeaderHashMap) {
        this.jsonHeaderHashMap = jsonHeaderHashMap;
    }

    public HashMap<String, Object> getJsonParameterHashMap() {
        return jsonParameterHashMap;
    }

    public void setJsonParameterHashMap(HashMap<String, Object> jsonParameterHashMap) {
        this.jsonParameterHashMap = jsonParameterHashMap;
    }
}
