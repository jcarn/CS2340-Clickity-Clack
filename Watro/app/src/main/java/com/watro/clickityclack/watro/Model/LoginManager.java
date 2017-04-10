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
     * @param activity current activity
     * @param email email of user
     * @param password password of user
     */
//    public void loginWithFirebase(Activity activity, String email, String password) throws Exception {
    public void loginWithFirebase(Activity activity, String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
//                            throw new Exception("");
                        } else {

                        }
                    }
                });

//        final boolean[] loginSuccess = {true};
//        final String[] exceptionMessage = new String[1];
//        final boolean[] signInAttemptCompleted = {false};
//
//        firebaseAuth.signInWithEmailAndPassword(email, password).
//                addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                signInAttemptCompleted[0] = true;
//
//                if (!task.isSuccessful()) {
//                    loginSuccess[0] = false;
//
//                    exceptionMessage[0] = task.getException().getMessage();
//                }
//            }
//        });
//
//        if (!loginSuccess[0] && signInAttemptCompleted[0]) {
//            throw new Exception(exceptionMessage[0]);
//        }
//
//        if (loginSuccess[0] && signInAttemptCompleted[0]) {
//            return true;
//        }
//
//        return false;
    }

}
