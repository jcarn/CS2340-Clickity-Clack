package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.PurityModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by AmyDao on 4/6/17.
 */

public class AmyTest {

    public PurityModel pm = new PurityModel("date", "reportID", "workerName", "location", "overallCondition", "virusPPM", "contaminantPPM");

    @Test(expected = NumberFormatException.class)
    public void testBlankVirusPPM() throws Exception {
        String virusPPM = "";
        boolean check = pm.setVirusPPMtest(virusPPM);
        assertEquals("Blank Field", false, check);
    }

    @Test
    public void testNegativeVirusPPM() throws Exception {
        String virusPPM = "-1234";
        boolean check = pm.setVirusPPMtest(virusPPM);
        assertEquals("Negative number", false, check);
    }

    @Test(expected = NumberFormatException.class)
    public void testNotNumVirusPPM() throws Exception {
        String virusPPM = "hello";
        boolean check = pm.setVirusPPMtest(virusPPM);
        assertEquals("Not a number", false, check);
    }

    @Test
    public void testValidVirusPPM() throws Exception {
        String virusPPM = "3.14";
        boolean check = pm.setVirusPPMtest(virusPPM);
        assertEquals("Valid number", true, check);
    }
}
