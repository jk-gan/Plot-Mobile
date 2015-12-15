package com.example.jkgan.pmot.service;
/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jkgan.pmot.MainActivity;
import com.example.jkgan.pmot.MyApplication;
import com.example.jkgan.pmot.PromotionActivity;
import com.example.jkgan.pmot.R;
import com.example.jkgan.pmot.Shops.Promotion;
import com.google.android.gms.gcm.GcmListenerService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    static int numMessages = 0;
    static String notificationTitle = "";
    final int notifyID = 1;
    NotificationCompat.Builder notificationBuilder = null;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");
        String title = data.getString("title");
        String promotion_id = data.getString("promotion_id");


        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message, title, promotion_id);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(final String message, final String title, String id) {

        String url = MyApplication.getApiUrl() + "/promotions/" + id + "?token=" + MyApplication.getUser().getToken();
        final Promotion[] promotion = {null};

        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    JSONObject jsnObj2 = new JSONObject(response.body().string());

                    promotion[0] = new Promotion(jsnObj2.optString("pName"), jsnObj2.optString("description"), jsnObj2.optString("id"), jsnObj2.getJSONObject("image").getJSONObject("medium").optString("url"), jsnObj2.getJSONObject("image").getJSONObject("small").optString("url"), jsnObj2.optString("term_and_condition"), jsnObj2.optString("name"),jsnObj2.optString("address"),jsnObj2.optString("sId"));

                    Intent intent = new Intent(getApplicationContext(), PromotionActivity.class);
                    intent.putExtra("NAME", promotion[0].getName());
                    intent.putExtra("SHOP_ID", promotion[0].getId());
                    intent.putExtra("IMAGE", promotion[0].getImage());
                    intent.putExtra("DESCRIPTION", promotion[0].getDescription());
                    intent.putExtra("TNC", promotion[0].getTnc());
                    intent.putExtra("SHOP_NAME", promotion[0].getShop().getName());
                    intent.putExtra("ADDRESS", promotion[0].getShop().getAddress());
                    intent.putExtra("SUBSCRIBED", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    if(numMessages >= 1) {
                        notificationTitle += (", " + title);

                        notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle((numMessages + 1) + " new promotions")
                                .setContentText(notificationTitle)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                        notificationBuilder.setContentText(message)
                                .setNumber(++numMessages);

                    } else {
                        notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                        notificationTitle = title;
                        numMessages++;
//            notificationBuilder.setContentText(message)
//                    .setNumber(++numMessages);
                    }

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    // On génère un nombre aléatoire pour pouvoir afficher plusieurs notifications
                    notificationManager.notify(notifyID, notificationBuilder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//        }).start();
//    }
}