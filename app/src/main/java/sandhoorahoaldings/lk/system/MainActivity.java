package sandhoorahoaldings.lk.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity { // implements View.OnClickListener{

    TextView textView ;
    EditText deviceName ;
    EditText interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("System Service");

        Button startServiceButton = findViewById(R.id.start_foreground_service_button);
        startServiceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startForeGroundService();
            }
        });

        Button stopServiceButton = findViewById(R.id.stop_foreground_service_button);
        stopServiceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopForeGroundService();
            }
        });

//        startServiceButton.setEnabled(false);
//        stopServiceButton.setEnabled(false);

        Button alarmOn = findViewById(R.id.alarmOn);
        alarmOn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleAlarm();
            }
        });

        Button alarmOff = findViewById(R.id.alarmOff);
        alarmOff.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        alarmOn.setEnabled(false);
        alarmOff.setEnabled(false);


        this.textView = findViewById(R.id.textView);
        this.deviceName = findViewById(R.id.deviceNameTextField);
        this.interval = findViewById(R.id.intervalTextField);


        ActivityCompat.requestPermissions(this, new String[]{"android.permission.FOREGROUND_SERVICE",
                "android.permission.INTERNET",
                "android.permission.ACCESS_FINE_LOCATION",
                "android.permission.ACCESS_NETWORK_STATE"}, 123);

        initializeForeGroundService();


//        initializeAlarm();
    }


    public void initializeForeGroundService() {
//
//        Log.e("MANINACTIVITY", "Sending Location");
//        new SendLocation().execute("a","a","{\"a\":\"15\"}");
//        Log.e("MANINACTIVITY", "Location Sent");
    }

    public void startForeGroundService() {
        String device = this.deviceName.getText().toString();
        String interval = this.interval.getText().toString();
        Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
        intent.putExtra("deviceName", device);
        intent.putExtra("interval", interval);
        intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
//                Intent intent = new Intent(MainActivity.this, MyOtherForeGroundService.class);
//                intent.setAction(MyOtherForeGroundService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);
    }

    public void stopForeGroundService() {
        Intent intent = new Intent(MainActivity.this, MyForeGroundService.class);
        intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
//                Intent intent = new Intent(MainActivity.this, MyOtherForeGroundService.class);
//                intent.setAction(MyOtherForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
        startService(intent);
    }

    public void initializeAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlarmManager.AlarmClockInfo alarminfo = alarm.getNextAlarmClock();

            if(alarminfo != null) {
                Log.e("ALARM TIMER", "running");
                Long next_alarm = alarminfo.getTriggerTime();
                Log.e("ALARM TIMER", next_alarm.toString());
                this.textView.setText(next_alarm.toString());
            } else {
                this.textView.setText("Alarm off");
            }
        } else {
            this.textView.setText("Unsupported Android version");
        }
    }

    public void scheduleAlarm() {
        Long interval = Long.parseLong(this.interval.getText().toString());
        if(interval == null || interval == 0L) {
            this.interval.setText("5", TextView.BufferType.EDITABLE);
            interval = 5L;
        }
        interval = interval * 10 * 1000L;

        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("interval", interval);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis() + interval; // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// if api level is above 21
            alarm.setExact(AlarmManager.RTC_WAKEUP, firstMillis, pIntent);

            Long next_alarm = alarm.getNextAlarmClock().getTriggerTime();
            Log.e("ALARM TIMER", next_alarm.toString());
            this.textView.setText(next_alarm.toString());
        }
        else {
            this.textView.setText("Unsupported Android version");
        }



    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
        this.textView.setText("Alarn is off");
    }
}
