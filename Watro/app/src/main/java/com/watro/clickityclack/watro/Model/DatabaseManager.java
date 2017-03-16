package com.watro.clickityclack.watro.Model;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by henrysaba on 3/8/17.
 */

public class DatabaseManager {
    private FirebaseAuth firebaseAuth;

    public DatabaseManager() {
        firebaseAuth = FirebaseAuth.getInstance();


    }
}
