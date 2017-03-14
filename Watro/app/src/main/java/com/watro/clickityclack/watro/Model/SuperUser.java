package com.watro.clickityclack.watro.Model;

public abstract class SuperUser {
    
    protected String email;
    protected String id;

    /**
     * Constructor for general user class from which all others inherit
     * @param email email of user in form: name@host.ext
     * @param id firebase generated ID of user
     */
    public SuperUser(String email, String id) {
        this.email = email;
        this.id = id;
    }

    /**
     * Getter of user's email
     * @return String email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of email
     * @param email new email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter of id
     * @return String id of user
     */
    public String getId() {
        return id;
    }
}