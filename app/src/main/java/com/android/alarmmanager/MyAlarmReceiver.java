package com.android.alarmmanager;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.WindowManager;
import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyAlarmReceiver extends BroadcastReceiver {

    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {

        AlertDialog dialog;

        Log.w("Alan", "broadcast received.");
        mediaPlayer = MediaPlayer.create(context, R.raw.flourish);
        mediaPlayer.start();

        //Toast.makeText(context, "鬧鐘嚮了", Toast.LENGTH_SHORT).show();
        Calendar c = Calendar.getInstance();
        Log.w("Alan","收到:"+c.getTimeInMillis());

        // Alert dialog
        dialog=AlertDialog_Simple_Example(context);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();

        // Notification
        ShowNotification(context);
    }

    public AlertDialog AlertDialog_Simple_Example(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.AlermReminder);
        builder.setMessage(R.string.TimeUp);
        builder.setIcon(R.drawable.kitty033);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.w("Alan", "Positive Button");
                dialog.dismiss();
                mediaPlayer.stop();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

    private void ShowNotification(Context context)
    {
        Intent backToMain = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, backToMain, 0);

        //Step1. 初始化NotificationManager，取得Notification服務
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setTicker("notification on status bar.") // 設置狀態列的顯示的資訊
                .setWhen(System.currentTimeMillis())// 設置時間發生時間
                .setSmallIcon(R.drawable.kitty033) // 設置狀態列裡面的圖示（小圖示）　　
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.kitty033)) // 下拉下拉清單裡面的圖示（大圖示）
                .setContentIntent(pendingIntent)   //設定Notification 點擊返回的Activity
                .setContentTitle(context.getString(R.string.AlermReminder)) // 設置下拉清單裡的標題
                .setContentText(context.getString(R.string.TimeUp));// 設置上下文內容
        mNotificationManager.notify(0, builder.build());

    }
}
