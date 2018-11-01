package sandhoorahoaldings.lk.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "sandhoorahoaldings.lk.system.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Alarm","Alarm playingg !!!!!!!!!");
//        Intent i = new Intent(context, MyForeGroundService.class);
//        i.putExtra("interval",  intent.getBundleExtra("interval"));
//        i.putExtra("deviceName", intent.getBundleExtra("deviceName"));
//        context.startService(i);
    }
}
