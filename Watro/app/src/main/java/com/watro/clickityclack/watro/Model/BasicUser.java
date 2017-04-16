package com.watro.clickityclack.watro.Model;

import android.content.res.Resources;

import com.watro.clickityclack.watro.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BasicUser extends SuperUser {

    private String homeAddress;
    private String firstName;
    private String lastName;
    private String userType;

    /**
     * Default Constructor
     */
    public BasicUser() {
        super();
    }
    /**
     * Constructor for the basic user
     * @param firstName first name of user
     * @param lastName surname of user
     * @param email email of user
     * @param id firebase generated user ID
     * @param homeAddress home address of user
     * @param userType user's permissions in the app, i.e. worker, manager, admin, etc.
     */
    public BasicUser(String firstName, String lastName, String email, String id, String homeAddress, String userType) {
        super(email, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        try {
            setUserType(userType);
        } catch (Exception e){
            setUserType("User");
        }
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
     * Getter for homeAddress
     * @return String address of user
     */
    public String getHomeAddress() {
        return homeAddress;
    }

    /**
     * Setter for homeAddress
     * @param address new home address of user
     */
    public void setHomeAddress(String address) {
        this.homeAddress = address;
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
        switch (userType) {
            case "User":
            case "Worker":
            case "Manager":
            case "Administrator":
                this.userType = userType;
                break;
            default:
                throw new IllegalArgumentException("User type must be one of " + R.array.userTypes);
        }
    }
}