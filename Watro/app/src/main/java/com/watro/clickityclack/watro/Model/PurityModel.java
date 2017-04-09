package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/28/2017.
 * This class holds information that user inputs in a water purity report.
 */

public class PurityModel {
        private String date;
        private String reportId;
        private String workerName;
        private String location;
        private String overallCondition;
        private String virusPPM;
        private String contaminantPPM;

    /**
     * Constructor for PurityModel class
     * @param date date of report
     * @param reportId id of report
     * @param workerName name of user
     * @param location location of water report
     * @param overallCondition condition of water
     * @param virusPPM amount of viruses in the water
     * @param contaminantPPM amount of contaminants in water
     */
    public PurityModel(String date, String reportId, String workerName, String location, String overallCondition, String virusPPM, String contaminantPPM) {
        this.date = date;
        this.reportId = reportId;
        this.workerName = workerName;
        this.location = location;
        this.overallCondition = overallCondition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }

    /**
     * Getter for date
     * @return String date of report
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for date
     * @param date date of report
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for reportId
     * @return String id of report
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * Setter for reportId
     * @param reportId id of report
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    /**
     * Getter for name of worker submitting report
     * @return String name of worker
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * Setter for workerName
     * @param workerName name of worker who submitted report
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
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
     * Getter for overallCondition
     * @return String overall condition of water
     */
    public String getOverallCondition() {
        return overallCondition;
    }

    /**
     * Setter for overallCondition
     * @param overallCondition condition of water
     */
    public void setOverallCondition(String overallCondition) {
        this.overallCondition = overallCondition;
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
        float x = Float.parseFloat(virusPPM);
        try {
            x = Float.parseFloat(virusPPM);
            if(x < 0) {
                this.virusPPM = "-1";
            } else {
                this.virusPPM = String.format("%f", x);
            }
        } catch (NumberFormatException e) {
            this.virusPPM = "-1";
        }
    }

    /**
     * Setter for virusPPM
     * @param virusPPM number of viruses in water in PPM
     */
    public boolean setVirusPPMtest(String virusPPM) {
        float x = Float.parseFloat(virusPPM);
        try {
            x = Float.parseFloat(virusPPM);
            if(x < 0) {
                this.virusPPM = "-1";
                return false;
            } else {
                this.virusPPM = String.format("%f", x);
                return true;
            }
        } catch (NumberFormatException e) {
            this.virusPPM = "-1";
            return false;
        }

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
        float x = Float.parseFloat(contaminantPPM);
        try {
            x = Float.parseFloat(contaminantPPM);
            if(x < 0) {
                this.contaminantPPM = "-1";
            } else {
                this.contaminantPPM = String.format("%f", x);
            }
        } catch (NumberFormatException e) {
            this.contaminantPPM = "-1";
        }
    }

    /**
     * Setter for contaminantPPM
     * @param contaminantPPM number of contaminants in water in PPM
     */
    public boolean setContaminantPPMtest(String contaminantPPM) {
        float x = Float.parseFloat(contaminantPPM);
        try {
            x = Float.parseFloat(contaminantPPM);
            if(x < 0) {
                this.contaminantPPM = "-1";
                return false;
            } else {
                this.contaminantPPM = String.format("%f", x);
                return true;
            }
        } catch (NumberFormatException e) {
            this.contaminantPPM = "-1";
            return false;
        }
    }
}
