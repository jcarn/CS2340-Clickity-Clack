package com.watro.clickityclack.watro;

import com.watro.clickityclack.watro.Model.BasicUser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by danielchung on 4/10/17.
 */

public class DanielTest {
    @Test
    public void testCorrectValues() throws IllegalArgumentException {
        BasicUser person = new BasicUser("Daniel", "Chung", "google@yahoo.com", "1", "The CULC", "User");
        person.setUserType("User");
        assertEquals(person.getUserType(), "User");
        person.setUserType("Manager");
        assertEquals(person.getUserType(), "Manager");
        person.setUserType("Worker");
        assertEquals(person.getUserType(), "Worker");
        person.setUserType("Administrator");
        assertEquals(person.getUserType(), "Administrator");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectValue() throws IllegalArgumentException {
        BasicUser person = new BasicUser("Daniel", "Chung", "google@yahoo.com", "1", "The CULC", "User");
        person.setUserType("Adminiworker");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() throws IllegalArgumentException {
        BasicUser person = new BasicUser("Daniel", "Chung", "google@yahoo.com", "1", "The CULC", "User");
        person.setUserType(null);
    }

}
