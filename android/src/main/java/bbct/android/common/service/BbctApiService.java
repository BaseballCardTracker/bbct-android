package bbct.android.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BbctApiService extends Service {
    public BbctApiService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
