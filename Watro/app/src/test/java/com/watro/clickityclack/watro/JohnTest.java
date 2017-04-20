package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.SourceModel;

import org.junit.Test;

/**
 * Tests the date of the sourcemodel to ensure invalid dates cannot be added
 */
public class JohnTest {
    @Test(expected = IllegalArgumentException.class)
    public void testDateBad() {
        String badDate = "022317";
        SourceModel sourceMod = new SourceModel("01-01-2017", "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
        sourceMod.setDate(badDate);
    }

    @Test(timeout = 2000)
    public void testDateGood() {
        String goodDate = "02-23-17";
        SourceModel sourceMod = new SourceModel(goodDate, "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
    }
}