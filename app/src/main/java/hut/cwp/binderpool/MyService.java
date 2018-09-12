package hut.cwp.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import hut.cwp.binderpool.aidl.BinderPool;

public class MyService extends Service {

    private Binder mBinderPool = new BinderPool.BinderPoolImpl();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

}
