package com.watro.clickityclack.watro.Model;

/**
 * Created by John on 2/28/2017.
 * This is the old information holder for water source reports.
 */

public class Report {
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String streetAddress;
    private String waterT;
    private String waterC;
    private String latitude;
    private String longitude;

    /**
     * Getter for latitude
     *
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Setter for latitude
     *
     * @param latitude set location lat
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter for longitude
     *
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Setter for longitude
     *
     * @param longitude set location long
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Getter of water type
     * @return String type of water
     */
    public String getWaterType() {
        return waterT;
    }

    /**
     * Setter of waterType
     * @param waterType string matching type of water
     */
    public void setWaterType(String waterType) {
        waterT = waterType;
    }

    /**
     * Getter of WaterCondition
     * @return String condition of the water
     */
    public String getWaterCondition() {
        return waterC;
    }

    /**
     * Setter of water condition
     * @param waterCondition String matching desired water type
     */
    public void setWaterCondition(String waterCondition) {
        waterC = waterCondition;
    }

    /**
     * Getter of report date
     * @return String date report was filed
     */
    public String getReportDate() {
        return reportDate;
    }

    /**
     * Setter for report date
     * @param reportDate date report was submitted
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * Getter of generated report ID
     * @return String unique ID of report
     */
    public String getReportID() {
        return reportID;
    }

    /**
     * Setter of report ID
     * @param reportID new unique ID of report
     */
    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    /**
     * Getter for ID of the report's reporter
     * @return String unique ID of reporter
     */
    public String getReporterID() {
        return reporterID;
    }

    /**
     * Setter for report's reporter ID
     * @param reporterID ID of reporting user
     */
    public void setReporterID(String reporterID) { this.reporterID = reporterID;}

    /**
     * Getter of report's address
     * @return String report's location
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Setter of report's address
     * @param streetAddress address of Report
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }


}
