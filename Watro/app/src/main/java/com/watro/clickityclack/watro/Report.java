package com.watro.clickityclack.watro;
import java.util.Date;

/**
 * Created by John on 2/28/2017.
 */

public class Report {


    private Date reportDate;
    private String reportNumber;
    private String reporterID;
    private String streetAddress;
    private enum waterType {
        BOTTLED, WELL, STREAM, LAKE, SPRING, OTHER
    }

    private enum waterCondition {
        WASTE, TREATABLE_CLEAR, TREATABLE_MUDDY, POTABLE
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getReporterID() {
        return reporterID;
    }

    public void setReporterID(String reporterID) {
        this.reporterID = reporterID;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

}
