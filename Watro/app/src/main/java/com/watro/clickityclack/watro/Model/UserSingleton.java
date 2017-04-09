package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/30/2017.
 * This is a singleton used to store user types.
 */

public class UserSingleton {
    private static UserSingleton userInstance;
    private String userType;

    /**
     * Default Constructor
     */
    private UserSingleton() {
    }

    /**
     * Setter for userType
     * @param userType user's new type
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Creates a singleton for users
     * @return a singleton for user types
     */
    public static UserSingleton getInstance() {
        if (userInstance == null) {
            synchronized (UserSingleton.class) {
                userInstance = new UserSingleton();
            }
        }
        return userInstance;
    }

    /**
     * Boolean of whether or not user type is null
     * @return boolean for user type
     */
    public boolean exists() {
        return userType != null;
    }

    /**
     * Getter for userType
     * @return String type of user
     */
    public String getUserType() {
        return userType;
    }
}
