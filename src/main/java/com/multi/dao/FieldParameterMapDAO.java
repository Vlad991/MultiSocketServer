package com.multi.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldParameterMapDAO extends Util {

    public String getParameterIndexForEvent(String parameterRealName, int eventType) {
        PreparedStatement ps;
        try {
            ps = getPreparedStatement("select * from ASCOR_EventLogTypes where EventType = ?");
            ps.setInt(1, eventType);
            ResultSet rs = ps.executeQuery();
            rs.next();
            for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                String parameterIndex = rs.getMetaData().getColumnName(i);
                if (rs.getObject(i).equals(parameterRealName)) {
                    return parameterIndex;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean eventTypeExists(int eventType) {
        PreparedStatement ps;
        try {
            ps = getPreparedStatement("select * from ASCOR_EventLogTypes where EventType = ?");
            ps.setInt(1, eventType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
