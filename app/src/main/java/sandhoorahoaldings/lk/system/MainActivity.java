package sandhoorahoaldings.lk.system;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.DialogFragment;
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
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity { // implements View.OnClickListener{

    TextView textView ;
    EditText deviceName ;
    EditText interval;
    EditText year;
    EditText month;
    EditText day;
    EditText hour;
    EditText minute;

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

        Button alarmSet = findViewById(R.id.alarmSet);
        alarmSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

//        alarmOn.setEnabled(false);
//        alarmOff.setEnabled(false);
        alarmSet.setEnabled(false);


        this.textView = findViewById(R.id.textView);
        this.deviceName = findViewById(R.id.deviceNameTextField);
        this.interval = findViewById(R.id.intervalTextField);
        this.year = findViewById(R.id.yearTextField);
        this.month = findViewById(R.id.monthTextField);
        this.day = findViewById(R.id.dateTextField);
        this.hour = findViewById(R.id.hourTextField);
        this.minute = findViewById(R.id.minuteTextField);


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
                Log.e("ALARM TIMER", "ALARM SET");
                Long next_alarm = alarminfo.getTriggerTime();
                Log.e("ALARM TIMER", next_alarm.toString());
                this.textView.setText("ALARM AT : " + next_alarm.toString());
            } else {
                this.textView.setText("Alarm off");
            }
        } else {
            this.textView.setText("Unsupported Android version");
        }
    }

    public void scheduleAlarm() {
        String interval = this.interval.getText().toString();
        String device = this.deviceName.getText().toString();
        String year = this.year.getText().toString();
        String month = this.month.getText().toString();
        String day = this.day.getText().toString();
        String hour = this.hour.getText().toString();
        String minute = this.minute.getText().toString();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(Integer.parseInt( year),Integer.parseInt(month),Integer.parseInt(day),Integer.parseInt(hour),Integer.parseInt(minute));

        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("deviceName", device);
        intent.putExtra("interval", interval);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every every half hour from this point onwards
        //long firstMillis = System.currentTimeMillis(); // alarm is set right away
        long firstMillis = cal.getTimeInMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// if api level is above 21
            alarm.setExact(AlarmManager.RTC_WAKEUP, firstMillis, pIntent);

            Long next_alarm = alarm.getNextAlarmClock().getTriggerTime();
            Log.e("ALARM", "ALARM SET TO "+next_alarm.toString());
            this.textView.setText("Alarm set to "+next_alarm.toString());
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
        this.textView.setText("Alarm is off");
    }

    public void setAlarm() {
        Intent intent = new Intent(MainActivity.this, TimePicker.class);
        startActivity(intent);
        Log.e("ala","sad");
    }
}
