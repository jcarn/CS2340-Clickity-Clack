package com.watro.clickityclack.watro.Model;

public class Administrator extends SuperUser {
    /**
     * Constructor for Administrator
     *
     * @param email email of admin
     * @param id firebase generated ID of admin
     */
    public Administrator(String email, String id) {
        super(email, id);
    }
}