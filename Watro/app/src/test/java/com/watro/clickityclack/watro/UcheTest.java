package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.SourceModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test (expected = IllegalArgumentException.class)
    public void test_At_Sign_Email() {
        BasicUser person = new BasicUser("John", "Corny", "google@yahoo.com", "12345", "Wonderland", "Basic User");
        //Person was made Suceessfully. Now we are going to attempt to change email to valid value
        person.setEmail("uche@dakid.com");
        //Valid email was set successfully. Now there will be an error because there is no @ sign
        person.setEmail("ucheATdakid.com");
    }
    @Test (expected = IllegalArgumentException.class)
    public void test_dotCom_Email() {
        BasicUser person = new BasicUser("John", "Corny", "google@yahoo.com", "12345", "Wonderland", "Basic User");
        //Person was made Suceessfully. Now we are going to attempt to change email to valid value
        person.setEmail("uche@dakid.com");
        //Valid email was set successfully.Now there will be an error because there is no ".com"
        person.setEmail("uche@dakidcom");
    }
    @Test (expected = IllegalArgumentException.class)
    public void test_atBeforedotCom_Email() {
        BasicUser person = new BasicUser("John", "Corny", "google@yahoo.com", "12345", "Wonderland", "Basic User");
        //Person was made Suceessfully. Now we are going to attempt to change email to valid value
        person.setEmail("uche@dakid.com");
        //Valid email was set successfully. Now there will be an error because @ does not com before .com
        person.setEmail("uche.comdakid@");
    }
    @Test (expected = IllegalArgumentException.class)
    public void test_letterBetween_AtanddotCom_Email() {
        BasicUser person = new BasicUser("John", "Corny", "google@yahoo.com", "12345", "Wonderland", "Basic User");
        //Person was made Suceessfully. Now we are going to attempt to change email to valid value
        person.setEmail("uche@dakid.com");
        //Valid email was set successfully. Now there will be an error because there is no letter between @ and .com
        person.setEmail("uchedakid@.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateBad() {
        String badDate = "022317";
        SourceModel sourceMod = new SourceModel(badDate, "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
    }

    @Test(timeout = 2000)
    public void testDateGood() {
        String goodDate = "02-23-17";
        SourceModel sourceMod = new SourceModel(goodDate, "goodID", "Johny B. Good", "Georgia Tech", "Potable", "Lake");
    }
}