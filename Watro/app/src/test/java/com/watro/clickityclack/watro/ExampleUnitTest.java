package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.SourceModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDate() {
        String badDate = "022317";
        SourceModel sourceMod = new SourceModel(badDate, "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
    }
}