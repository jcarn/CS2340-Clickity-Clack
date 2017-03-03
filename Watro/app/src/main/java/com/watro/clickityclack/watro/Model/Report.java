package com.watro.clickityclack.watro.Model;
import java.util.Date;

/**
 * Created by John on 2/28/2017.
 */

public class Report {


    private Date reportDate;
    private String reportNumber;
    private String reporterID;
    private String streetAddress;

    public enum WaterType {
        BOTTLED("Bottled"), WELL("Well"), STREAM("Stream"), LAKE("Lake"), SPRING("Spring"), OTHER("Other");

        private final String text;

        WaterType(String t) {
            this.text = t;
        }

        private String text() { return text; }

        @Override public String toString(){
            return text;
        }
    }

    public enum WaterCondition {
        WASTE("Waste"), TREATABLE_CLEAR("Clear and Treatable"), TREATABLE_MUDDY("Muddy but Treatable"), POTABLE("Potable");

        private final String text;

        WaterCondition(String t) {
            this.text = t;
        }

        private String text() { return text; }

        @Override public String toString(){
            return text;
        }
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
