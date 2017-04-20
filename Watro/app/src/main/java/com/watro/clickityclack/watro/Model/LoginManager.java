package com.watro.clickityclack.watro.Model;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by henrysaba on 3/8/17.
 * This class checks user input to give users access to the app
 */

class LoginManager {
    private final FirebaseAuth firebaseAuth;

    /**
     * Default Constructor
     */
    public LoginManager() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Logs into firebase
     *
     * @param activity current activity
     * @param email    email of user
     * @param password password of user
     */
    public void loginWithFirebase(Activity activity, String email, String password) throws Exception {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
//                            throw new Exception("");
                } else {

                }
            }
        });
    }
}

