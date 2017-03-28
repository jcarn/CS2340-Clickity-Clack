package com.watro.clickityclack.watro.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Uche Nkadi on 3/27/2017.
 */

public class PurityReport {
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String streetAddress;
    private String waterCondition;
    private String latitude;
    private String longitude;
    private double virusPPM;
    private double contaminantPPM;

    public String getWaterCondition() {
        return waterCondition;
    }

    public void setWaterCondition(String waterCondition) {
        this.waterCondition = waterCondition;
    }

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
     * @param latitude
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
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public double getVirusPPM() {
        return virusPPM;
    }

    public void setVirusPPM(double virusPPM) {
        this.virusPPM = virusPPM;
    }

    public double getContaminantPPM() {
        return contaminantPPM;
    }

    public void setContaminantPPM(double contaminantPPM) {
        this.contaminantPPM = contaminantPPM;
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

    /**
     * Setter of Report's location
     * @return report location
     */
    public LatLng getLocation() {return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));}
}
