package com.watro.clickityclack.watro.Model;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by John on 2/28/2017.
 */

public class Report {
    private String reportDate;
    private String reportID;
    private String reporterID;
    private String streetAddress;
    private String waterT;
    private String waterC;
    private WaterType waterType;
    private WaterCondition waterCondition;
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
     * Possible types of water
     */
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

    /**
     * Possible conditions of water
     */
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

    /**
     * Getter of water type enum
     * @return enum type of water
     */
    public String getWaterType() {
        return waterT.toString();
    }

    /**
     * Setter of waterType enum
     * @param waterType string matching type of water
     */
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
        } else {
            return;
        }
        waterT = waterType;
    }

    /**
     * Getter of WaterCondtion Enum
     * @return enum condition of the water
     */
    public String getWaterCondition() {
        return waterCondition.toString();
    }

    /**
     * Setter of water condition enum
     * @param waterCondition String matching desired water type
     */
    public void setWaterCondition(String waterCondition) {
        if (waterCondition.equals("Waste")) {
            this.waterCondition = WaterCondition.WASTE;
        } else if (waterCondition.equals("Clear and Treatable")) {
            this.waterCondition = WaterCondition.TREATABLE_CLEAR;
        } else if (waterCondition.equals("Muddy but Treatable")) {
            this.waterCondition = WaterCondition.TREATABLE_MUDDY;
        } else if (waterCondition.equals("Potable")) {
            this.waterCondition = WaterCondition.POTABLE;
        } else {
            return;
        }
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

    /**
     * Setter of Report's location
     * @return report location
     */
    public LatLng getLocation() {return new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));}

}
