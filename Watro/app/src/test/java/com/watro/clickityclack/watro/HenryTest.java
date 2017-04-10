package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.PurityModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by HenrySaba on 4/6/17.
 * Junit test checking contaminantppm format
 */

public class HenryTest {

    private final PurityModel pm = new PurityModel("date", "reportID", "workerName", "location", "overallCondition", "virusPPM", "contaminantPPM");

    @Test(expected = NumberFormatException.class)
    public void testBlankContaminantPPM() throws Exception {
        String contaminantPPM = "";
        boolean check = pm.setContaminantPPMtest(contaminantPPM);
        assertEquals("Blank Field", false, check);
    }

    @Test
    public void testNegativeContaminantPPM() throws Exception {
        String contaminantPPM = "-1234";
        boolean check = pm.setContaminantPPMtest(contaminantPPM);
        assertEquals("Negative number", false, check);
    }

    @Test(expected = NumberFormatException.class)
    public void testNotNumContaminantPPM() throws Exception {
        String contaminantPPM = "hello";
        boolean check = pm.setContaminantPPMtest(contaminantPPM);
        assertEquals("Not a number", false, check);
    }

    @Test
    public void testValidContaminantPPM() throws Exception {
        String contaminantPPM = "3.14";
        boolean check = pm.setContaminantPPMtest(contaminantPPM);
        assertEquals("Valid number", true, check);
    }
}
