package com.android.fpad.notification;

/**
 * Created by dimasnurpanca on 2/11/2018.
 */
import android.content.Intent;
import android.util.Log;

import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryList;
import com.android.fpad.ui.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    APIInterface apiInterface;
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String EMPTY = "";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";
    private static final String STORY_ID = "story_id";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        apiInterface = APIClient.getClient().create(APIInterface.class); //retrofit
        Log.d(TAG, "Notif From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.



        //getting the title and the body

    }


    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        NotificationVO notificationVO = new NotificationVO();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();
    }

    private void handleData(Map<String, String> data) {
        final String title = data.get(TITLE);
        final String message = data.get(MESSAGE);
        final String iconUrl = data.get(IMAGE);
        final String action = data.get(ACTION);
        final String actionDestination = data.get(ACTION_DESTINATION);
        final String story_id = data.get(STORY_ID);




        Call<List<StoryList>> call3 = apiInterface.doGetStoryById(story_id);
        call3.enqueue(new Callback<List<StoryList>>() {
            @Override
            public void onResponse(Call<List<StoryList>> call, Response<List<StoryList>> response) {

                List<StoryList> jsonResponse = response.body();
                for (int i = 0; i < jsonResponse.size(); i++) {

                    NotificationVO notificationVO = new NotificationVO();
                    notificationVO.setTitle(title);
                    notificationVO.setMessage(message);
                    notificationVO.setIconUrl(iconUrl);
                    notificationVO.setAction(action);
                    notificationVO.setActionDestination(actionDestination);
                    notificationVO.setStory_id(story_id);
                    notificationVO.setStory_email(jsonResponse.get(i).getEmail());
                    notificationVO.setStory_title(jsonResponse.get(i).getTitle());
                    notificationVO.setStory_description(jsonResponse.get(i).getDescription());
                    notificationVO.setStory_kategori(jsonResponse.get(i).getKategori_id());
                    notificationVO.setStory_content(jsonResponse.get(i).getContent());
                    notificationVO.setStory_status(jsonResponse.get(i).getStatus());
                    notificationVO.setStory_read(jsonResponse.get(i).getRead());
                    notificationVO.setStory_like(jsonResponse.get(i).getLike());
                    notificationVO.setStory_comment(jsonResponse.get(i).getComment());

                    Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    notificationUtils.displayNotification(notificationVO, resultIntent);
                    notificationUtils.playNotificationSound();
                }


            }

            @Override
            public void onFailure(Call<List<StoryList>> call, Throwable t) {

                call.cancel();
            }
        });

    }

}