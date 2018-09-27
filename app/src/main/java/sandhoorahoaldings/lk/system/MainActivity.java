package sandhoorahoaldings.lk.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import sandhoorahoaldings.lk.system.R;

public class MainActivity extends AppCompatActivity{ // implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("dev2qa.com - Android Foreground Service Example.");

        Button startServiceButton = (Button)findViewById(R.id.start_foreground_service_button);
        startServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
                intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
                startService(intent);
            }
        });

        Button stopServiceButton = (Button)findViewById(R.id.stop_foreground_service_button);
        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
                intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
                startService(intent);
            }
        });


        Button alarmOn = (Button)findViewById(R.id.alarmOn);
        alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
            }
        });

        Button alarmOff = (Button)findViewById(R.id.alarmOff);
        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        ActivityCompat.requestPermissions(this, new String[] {"android.permission.FOREGROUND_SERVICE"}, 123);
        ActivityCompat.requestPermissions(this, new String[] {"android.permission.INTERNET"}, 124);
        ActivityCompat.requestPermissions(this, new String[] {"android.permission.ACCESS_FINE_LOCATION"}, 125);
        ActivityCompat.requestPermissions(this, new String[] {"android.permission.ACCESS_NETWORK_STATE"}, 126);
    }


    public void scheduleAlarm() {
        Long interval = 1*30*1000L;
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                interval, pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
