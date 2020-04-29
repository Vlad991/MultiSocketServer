package com.multi.dao;

import com.multi.dto.DBParameter;
import com.multi.dto.FileData;
import com.multi.dto.QueryParameter;

import java.sql.*;

public class FileDataDAO extends Util {  // таблица в БД "Data" и "Data_other"
    private FieldParameterMapDAO fieldParameterMapDAO = new FieldParameterMapDAO();

    public void addFileData(FileData fileData) { // сохраняет обьект из json в БД
        PreparedStatement ps;
        if (fieldParameterMapDAO.eventTypeExists(fileData.getEventType())) { //проверка на существующий eventType
            try {
                String stringInBrackets = "";
                for (QueryParameter p : QueryParameter.values()) {  // записываем header
                    stringInBrackets += p.toString() + ", ";
                }
                for (DBParameter dbp : DBParameter.values()) {
                    stringInBrackets += dbp.toString() + ", ";
                }
                stringInBrackets = stringInBrackets.substring(0, stringInBrackets.length() - 2);
                ps = getPreparedStatement("insert into Data " +
                        "(" + stringInBrackets + ") " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                int i = 1;
                for (QueryParameter p : QueryParameter.values()) {  // записываем header
                    Object parameterObject = fileData.getJsonHeaderHashMap().get(p.toString());
                    ps.setObject(i, parameterObject);
                    i++;
                }
                for (DBParameter parameter : DBParameter.values()) {// записываем  body
                    String jsonFieldName = fieldParameterMapDAO
                            .getParameterIndexForEvent(parameter.toString(), fileData.getEventType()); //смотрит подкаким параметром в jsonприходит нужный нам параметр
                    if (jsonFieldName == null) {
                        ps.setObject(i, null);
                    } else {
                        Object jsonFieldValue = fileData.getJsonParameterHashMap().get(jsonFieldName);
                        ps.setObject(i, jsonFieldValue);
                    }
                    i++;
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String stringInBrackets = "";
                for (QueryParameter p : QueryParameter.values()) {  // записываем header
                    stringInBrackets += p.toString() + ", ";
                }
                stringInBrackets = stringInBrackets.substring(0, stringInBrackets.length() - 2);
                ps = getPreparedStatement("insert into Data_other " +
                        "(" + stringInBrackets + ", json) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                int i = 1;
                for (QueryParameter p : QueryParameter.values()) {
                    Object parameterObject = fileData.getJsonHeaderHashMap().get(p.toString());
                    ps.setObject(i, parameterObject);
                    i++;
                }
                ps.setObject(i, fileData.getJsonParameterHashMap().toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
