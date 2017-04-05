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

    /**
     * Constructor for the Administrator user
     * @param firstName first name of user
     * @param lastName surname of user
     * @param email email of user
     * @param id firebase generated user ID
     * @param homeAddress home address of user
     * @param userType user's permissions in the app, i.e. worker, manager, admin, etc.
     */
    public Administrator(String firstName, String lastName, String email, String id, String homeAddress, String userType) {
        super(email, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.userType = userType;
    }

    /**
     * Getter for homeAddress
     * @return String address of user
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * Setter for homeAddress
     * @param homeAddress new home address of user
     */
    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    /**
     * Getter for firstName
     * @return String first name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter for firstName
     * @param firstName new first name of user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter for lastName
     * @return String surname of user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter for lastName
     * @param lastName new last name of user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter for userType
     * @return String type of user
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Setter for userType
     * @param userType user's new type
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }
}