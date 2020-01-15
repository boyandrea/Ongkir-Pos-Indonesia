package com.doubled.ongkirposindonesia.helper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.doubled.ongkirposindonesia.R;
import com.doubled.ongkirposindonesia.activity.NewsActivity;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Irfan Septiadi Putra on 25/02/2016.
 */
public class MyGcmListenerService extends GcmListenerService {

    private Context mContext = this;
    private DatabaseHandler dbHandler;

    @Override
    public void onMessageReceived(String from, Bundle amplop) {
        super.onMessageReceived(from, amplop);
        String pesan = amplop.getString("message");
        Intent intent = new Intent(this, NewsActivity.class);
        dbHandler = new DatabaseHandler(this);
        try{
            JSONObject jsonObject = new JSONObject(pesan);
            String message = jsonObject.getString("message");
            String title = jsonObject.getString("title");
            dbHandler.insertNews(message,String.valueOf(System.currentTimeMillis()));
            showNotificationMessage(title,message,intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void showNotificationMessage(String title, String message,Intent intent) {

        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;

        if (isAppIsInBackground(mContext)) {
            // notification icon
            int icon = R.mipmap.notif;
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            mContext,
                            0,
                            intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(inboxStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(100, notification);
        } else {
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
        }
    }



    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}
