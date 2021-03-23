package com.android.alarmmanager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.WindowManager;
import java.util.Calendar;

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

        dialog=AlertDialog_Simple_Example(context);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        dialog.show();
    }

    public AlertDialog AlertDialog_Simple_Example(Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("鬧鐘提醒");
        builder.setMessage("時間到了，該起床了!!");
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
}
