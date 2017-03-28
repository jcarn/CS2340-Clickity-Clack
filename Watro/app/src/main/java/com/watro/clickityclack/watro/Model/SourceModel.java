package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/28/2017.
 */

public class SourceModel {
    //date, reportID, reporterName, location, water type, condition
    private String date;
    private String reportId;
    private String reporterName;
    private String location;
    private String waterType;
    private String waterCondition;

    public SourceModel(String date, String reportId, String reporterName, String location, String waterType, String waterCondition) {
        this.date = date;
        this.reportId = reportId;
        this.reporterName = reporterName;
        this.location = location;
        this.waterType = waterType;
        this.waterCondition = waterCondition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    public String getWaterCondition() {
        return waterCondition;
    }

    public void setWaterCondition(String waterCondition) {
        this.waterCondition = waterCondition;
    }
}
