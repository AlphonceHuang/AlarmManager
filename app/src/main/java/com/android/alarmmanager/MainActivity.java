package com.android.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button b_setting;
    private Button b_cancel;
    private Button b_Alert;
    private Button b_Notify;
    private AlarmManager alarmManager;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_setting = findViewById(R.id.SettingButton);
        b_cancel = findViewById(R.id.CancelButton);
        b_Alert = findViewById(R.id.AlertButton);
        b_Notify = findViewById(R.id.NotifiButton);

        b_setting.setOnClickListener(btn);
        b_cancel.setOnClickListener(btn);
        b_Alert.setOnClickListener(btn);
        b_Notify.setOnClickListener(btn);
    }

    private void StartAlarm()
    {
        Intent intent = new Intent(MainActivity.this, MyAlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this.getApplicationContext(), 12345, intent, 0);
        //pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar currentTime = Calendar.getInstance();
        new TimePickerDialog(MainActivity.this, 0,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //設定當前時間
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(System.currentTimeMillis());
                        // 根據使用者選擇的時間來設定Calendar物件
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        // 設定AlarmManager在Calendar對應的時間啟動Activity
                        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                        Log.w("Alan","設定:"+c.getTimeInMillis());   //這裡的時間是一個unix時間戳
                        Log.w("Alan", c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
                        // 提示鬧鐘設定完畢:
                        Toast.makeText(MainActivity.this, "鬧鐘設定完畢"+ c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE),
                                Toast.LENGTH_SHORT).show();
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true).show();
        b_cancel.setVisibility(View.VISIBLE);
    }

    private final View.OnClickListener btn=new View.OnClickListener() {

        AlertDialog alertDialog;
        AlertDialog.Builder builder;

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.SettingButton:
                    StartAlarm();
                    break;

                case R.id.CancelButton:
                    if (pi != null) {
                        alarmManager.cancel(pi);
                        //b_cancel.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "鬧鐘已取消", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this, "尚未設定鬧鐘", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.AlertButton:
                    builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("時間到了");
                    builder.setMessage("該起床了");
                    builder.setIcon(R.drawable.kitty033);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.w("Alan", "Positive Button");
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;

                case R.id.NotifiButton:
                    //Step1. 初始化NotificationManager，取得Notification服務
                    NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    //Step2. 設定當按下這個通知之後要執行的activity
                    Intent notifyIntent = new Intent(MainActivity.this, MainActivity.class);
                    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent appIntent = PendingIntent.getActivity(MainActivity.this, 0, notifyIntent, 0);

                    //Step3. 透過 Notification.Builder 來建構 notification，
                    //並直接使用其.build() 的方法將設定好屬性的 Builder 轉換
                    //成 notification，最後開始將顯示通知訊息發送至狀態列上。
                    Notification notification
                            = new Notification.Builder(MainActivity.this)
                            .setContentIntent(appIntent)
                            .setSmallIcon(R.drawable.kitty033) // 設置狀態列裡面的圖示（小圖示）　　
                            .setLargeIcon(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.kitty033)) // 下拉下拉清單裡面的圖示（大圖示）
                            .setTicker("notification on status bar.") // 設置狀態列的顯示的資訊
                            .setWhen(System.currentTimeMillis())// 設置時間發生時間
                            .setAutoCancel(false) // 設置通知被使用者點擊後是否清除  //notification.flags = Notification.FLAG_AUTO_CANCEL;
                            .setContentTitle("Notification Title") // 設置下拉清單裡的標題
                            .setContentText("Notification Content")// 設置上下文內容
                            .setOngoing(true)      //true使notification變為ongoing，用戶不能手動清除// notification.flags = Notification.FLAG_ONGOING_EVENT; notification.flags = Notification.FLAG_NO_CLEAR;
                            .setDefaults(Notification.DEFAULT_ALL) //使用所有默認值，比如聲音，震動，閃屏等等
//                 .setDefaults(Notification.DEFAULT_VIBRATE) //使用默認手機震動提示
//                 .setDefaults(Notification.DEFAULT_SOUND) //使用默認聲音提示
//                 .setDefaults(Notification.DEFAULT_LIGHTS) //使用默認閃光提示
//                 .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND) //使用默認閃光提示 與 默認聲音提示

//                 .setVibrate(vibrate) //自訂震動長度
//                 .setSound(uri) //自訂鈴聲
//                 .setLights(0xff00ff00, 300, 1000) //自訂燈光閃爍 (ledARGB, ledOnMS, ledOffMS)

                            .build();

                    // 將此通知放到通知欄的"Ongoing"即"正在運行"組中
                    notification.flags = Notification.FLAG_ONGOING_EVENT;

                    // 表明在點擊了通知欄中的"清除通知"後，此通知不清除，
                    // 經常與FLAG_ONGOING_EVENT一起使用
                    notification.flags = Notification.FLAG_NO_CLEAR;

                    //閃爍燈光
                    notification.flags = Notification.FLAG_SHOW_LIGHTS;

                    // 重複的聲響,直到用戶響應。
                    //notification.flags = Notification.FLAG_INSISTENT;


                    // 把指定ID的通知持久的發送到狀態條上.
                    mNotificationManager.notify(0, notification);

                    // 取消以前顯示的一個指定ID的通知.假如是一個短暫的通知，
                    // 試圖將之隱藏，假如是一個持久的通知，將之從狀態列中移走.
//              mNotificationManager.cancel(0);

                    //取消以前顯示的所有通知.
//              mNotificationManager.cancelAll();
                    break;
            }
        }
    };
}