package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/28/2017.
 * This class stores information that user inputs in a water source report.
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

    /**
     * Getter of report date
     * @return String date report was filed
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for report date
     * @param date date report was submitted
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter of generated report ID
     * @return String unique ID of report
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * Setter of report ID
     * @param reportId new unique ID of report
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    /**
     * Getter for name of the report's reporter
     * @return String name of reporter
     */
    public String getReporterName() {
        return reporterName;
    }

    /**
     * Setter for report's reporter name
     * @param reporterName ID of reporting user
     */
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    /**
     * Getter for name of location of purity report
     * @return String location of water
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location
     * @param location location of water in purity report
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter of water type
     * @return String type of water
     */
    public String getWaterType() {
        return waterType;
    }

    /**
     * Setter of waterType
     * @param waterType string matching type of water
     */
    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }

    /**
     * Getter of WaterCondition
     * @return String condition of the water
     */
    public String getWaterCondition() {
        return waterCondition;
    }

    /**
     * Setter of water condition
     * @param waterCondition String matching desired water type
     */
    public void setWaterCondition(String waterCondition) {
        this.waterCondition = waterCondition;
    }
}
