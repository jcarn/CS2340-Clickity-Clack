package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/27/2017.
 * This is the previous information holder for the purity report.
 */

public class PurityReport {
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String streetAddress;
    private String waterCondition;
    private String virusPPM;
    private String contaminantPPM;

    public String getWaterCondition() {
        return waterCondition;
    }

    public void setWaterCondition(String waterCondition) {
        this.waterCondition = waterCondition;
    }

//    /**
//     * Getter for latitude
//     *
//     * @return latitude
//     */
//    public String getLatitude() {
//        return latitude;
//    }

    /**
     * Setter for latitude
     *
     * @param latitude Set location latitude
     */
    public void setLatitude(String latitude) {
        String latitude1 = latitude;
    }

//    /**
//     * Getter for longitude
//     *
//     * @return longitude
//     */
//    public String getLongitude() {
//        return longitude;
//    }

    /**
     * Setter for longitude
     *
     * @param longitude Set location longitude
     */
    public void setLongitude(String longitude) {
        String longitude1 = longitude;
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
     * Getter for virusPPM
     * @return String number of viruses in water in PPM
     */
    public String getVirusPPM() {
        return virusPPM;
    }

    /**
     * Setter for virusPPM
     * @param virusPPM number of viruses in water in PPM
     */
    public void setVirusPPM(String virusPPM) {
        this.virusPPM = virusPPM;
    }

    /**
     * Getter for contaminantPPM
     * @return String number of contaminants in water in PPM
     */
    public String getContaminantPPM() {
        return contaminantPPM;
    }

    /**
     * Setter for contaminantPPM
     * @param contaminantPPM number of contaminants in water in PPM
     */
    public void setContaminantPPM(String contaminantPPM) {
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

}
