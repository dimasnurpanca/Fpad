package com.android.fpad.notification;

/**
 * Created by dimasnurpanca on 2/11/2018.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "fpad";

    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();
        //FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}