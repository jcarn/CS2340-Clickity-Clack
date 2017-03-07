package com.watro.clickityclack.watro.Model;

/**
 * Created by John on 2/28/2017.
 */

public class Report {
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String streetAddress;
    private WaterType waterType;
    private WaterCondition waterCondition;

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

    public WaterType getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        if (waterType.equals("Bottled")) {
            this.waterType = WaterType.BOTTLED;
        } else if (waterType.equals("Well")) {
            this.waterType = WaterType.WELL;
        } else if (waterType.equals("Stream")) {
            this.waterType = WaterType.STREAM;
        } else if (waterType.equals("Lake")) {
            this.waterType = WaterType.LAKE;
        } else if (waterType.equals("Spring")) {
            this.waterType = WaterType.SPRING;
        } else if (waterType.equals("Other")) {
            this.waterType = WaterType.OTHER;
        }
    }
    
    public WaterCondition getWaterCondition() {
        return waterCondition;
    }

    public void setWaterCondition(String waterCondition) {
        if (waterCondition.equals("Waste")) {
            this.waterCondition = WaterCondition.WASTE;
        } else if (waterCondition.equals("Clear and Treatable")) {
            this.waterCondition = WaterCondition.TREATABLE_CLEAR;
        } else if (waterCondition.equals("Muddy but Treatable")) {
            this.waterCondition = WaterCondition.TREATABLE_MUDDY;
        } else if (waterCondition.equals("Potable")) {
            this.waterCondition = WaterCondition.POTABLE;
        }
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
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
