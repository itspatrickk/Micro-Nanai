package com.pioneer.microhmo.util;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pioneer.microhmo.AccountActivity;
import com.pioneer.microhmo.DatabaseHelper;
import com.pioneer.microhmo.MainActivity;
import com.pioneer.microhmo.SmsService;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";
    private static final String CHANNEL_ID = "UserStatusChannel";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String messageBody = smsMessage.getMessageBody();
                        Log.d(TAG, "Received SMS: " + messageBody);

                        // Check if the message contains "INACTIVE" or "RESIGN"
                        if (messageBody.contains("INACTIVE") || messageBody.contains("RESIGN")) {


                            // Save the status in SharedPreferences
                            SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
                            SharedPreferencesUtility.saveStatus(sharedPreferences, "INACTIVE");

                            String name = SharedPreferencesUtility.getAgentName(sharedPreferences);
                            // Show notification
                            showNotification(context, "Account Status Changed", "Hello "+name.toUpperCase()+" your account has been marked as INACTIVE or RESIGNED.");
                            SharedPreferencesUtility.clear(sharedPreferences);



                            // Start SmsService with the message content
                            Intent serviceIntent = new Intent(context, SmsService.class);
                            serviceIntent.putExtra("messageBody", messageBody);
                            ContextCompat.startForegroundService(context, serviceIntent);

                        }
                    }
                }
            }
        }
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "User Status Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for user status changes.");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}



//public class SmsReceiver extends BroadcastReceiver {
//    private static final String TAG = "SmsReceiver";
//    DatabaseHelper databaseHelper;
//
//    private static final String CHANNEL_ID = "UserStatusChannel";
//    private static final int NOTIFICATION_ID = 1001;
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                Object[] pdus = (Object[]) bundle.get("pdus");
//                if (pdus != null) {
//                    for (Object pdu : pdus) {
//                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
//                        String messageBody = smsMessage.getMessageBody();
//
//                        if (messageBody.contains("INACTIVE") || messageBody.contains("RESIGN")) {
//                            Log.d(TAG, "Received SMS with keyword INACTIVE or RESIGN: " + messageBody);
//                            SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFERENCE_ID, Context.MODE_PRIVATE);
//                            SharedPreferencesUtility.saveStatus(sharedPreferences , "INACTIVE");
//                            showNotification(context , "Account Status Changed",
//                                    "Your account has been marked as INACTIVE or RESIGNED.");
//                            Intent serviceIntent = new Intent(context, SmsService.class);
//                            serviceIntent.putExtra("messageBody", messageBody);
//                            context.startForegroundService(serviceIntent);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//
//
//    private void showNotification(Context context, String title, String message) {
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        String CHANNEL_ID = "UserStatusChannel";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "User Status Notifications",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            channel.setDescription("Notifications for user status changes.");
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(android.R.drawable.ic_dialog_alert) // Use app icon here
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        notificationManager.notify(1002, builder.build());
//    }
//
//
//
//}
