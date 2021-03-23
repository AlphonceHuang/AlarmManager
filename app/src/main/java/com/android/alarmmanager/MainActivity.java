package com.android.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private AlarmManager alarmManager;
    private PendingIntent pi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_setting = findViewById(R.id.SettingButton);
        b_cancel = findViewById(R.id.CancelButton);
        b_Alert = findViewById(R.id.AlertButton);

        b_setting.setOnClickListener(btn);
        b_cancel.setOnClickListener(btn);
        b_Alert.setOnClickListener(btn);
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
            }
        }
    };
}