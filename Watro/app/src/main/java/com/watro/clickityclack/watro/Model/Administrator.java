package com.watro.clickityclack.watro.Model;

public class Administrator extends SuperUser {
    /**
     * Constructor for Administrator
     *
     * @param email email of admin
     * @param id firebase generated ID of admin
     */
    protected String homeAddress;
    protected String firstName;
    protected String lastName;
    protected String userType;

    public Administrator(String firstName, String lastName, String email, String id, String homeAddress, String userType) {
        super(email, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.userType = userType;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}