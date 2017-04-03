package com.watro.clickityclack.watro.Model;

/**
 * Created by Uche Nkadi on 3/30/2017.
 */

public class UserSingleton {
    private static UserSingleton userInstance;
    private String userType;

    private UserSingleton() {
    }


    public void setUserType(String userType) {
        this.userType = userType;
    }

    public static UserSingleton getInstance() {
        if (userInstance == null) {
            synchronized (UserSingleton.class) {
                userInstance = new UserSingleton();
            }
        }
        return userInstance;
    }
    public boolean exists() {
        return userType != null;
    }


    public String getUserType() {
        return userType;
    }
}
