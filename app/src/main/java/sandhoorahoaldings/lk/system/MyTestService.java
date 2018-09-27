package sandhoorahoaldings.lk.system;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyTestService extends IntentService {
    public MyTestService() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("MyTestService", "Service running");
        new SendLocation().execute("{\"b\":2}");
    }
}
