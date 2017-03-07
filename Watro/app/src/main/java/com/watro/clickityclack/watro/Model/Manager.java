package com.watro.clickityclack.watro.Model;

public class Manager extends Worker {

    /**
     * Constructor for Manager class
     * @param firstName first name of user
     * @param lastName surname of user
     * @param email email of user
     * @param id firebase generated user ID
     * @param homeAddress home address of user
     * @param userType user's permissions in the app, i.e. worker, manager, admin, etc.
     */
    public Manager(String firstName, String lastName, String email, String id, String homeAddress, String userType) {
        super(firstName, lastName, email, id, homeAddress, userType);
    }
}