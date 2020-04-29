package com.multi.dto;

public enum QueryParameter { // DB-column::json-field -> header-парпаметры json-телеграммы
    sGroup("group"),
    sRegion("region"),
    sSite("site"),
    sArea("area"),
    sVersion("version"),
    sPeriod("period"),
    sEventDate("tCreated"),
    iEventType("nEventType"),
    iRecordID("lRecordID"),
    iObjectType("nObjectType"),
    sObjectID("lObjectID"),
    iActive("nActive");

    private String parameter;

    private QueryParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
