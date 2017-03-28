package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/28/2017.
 */

public class PurityModel {
        private String date;
        private String reportId;
        private String workerName;
        private String location;
        private String overallCondition;
        private String virusPPM;
        private String contaminantPPM;

    public PurityModel(String date, String reportId, String workerName, String location, String overallCondition, String virusPPM, String contaminantPPM) {
        this.date = date;
        this.reportId = reportId;
        this.workerName = workerName;
        this.location = location;
        this.overallCondition = overallCondition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
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

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOverallCondition() {
        return overallCondition;
    }

    public void setOverallCondition(String overallCondition) {
        this.overallCondition = overallCondition;
    }

    public String getVirusPPM() {
        return virusPPM;
    }

    public void setVirusPPM(String virusPPM) {
        this.virusPPM = virusPPM;
    }

    public String getContaminantPPM() {
        return contaminantPPM;
    }

    public void setContaminantPPM(String contaminantPPM) {
        this.contaminantPPM = contaminantPPM;
    }
}
