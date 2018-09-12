package hut.cwp.binderpool.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

import hut.cwp.binderpool.IPool;
import hut.cwp.binderpool.MyService;

public class BinderPool {
    private static final String TAG = "BinderPool";
    public static final int ADD = 0;
    public static final int SUB = 1;

    private Context mContext;
    private IPool mBinderPool;
    private static volatile BinderPool sInstance;
//    private CountDownLatch mConnectBinderPoolCountDownLatch;

    private BinderPool(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPool getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPool.class) {
                if (sInstance == null) {
                    sInstance = new BinderPool(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectBinderPoolService() {
//        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, MyService.class);
        mContext.bindService(service, mBindPoolServiceConnection, Context.BIND_AUTO_CREATE);
//        try {
//            mConnectBinderPoolCountDownLatch.await();
//            Log.d(TAG, "connectBinderPoolService: ");
//        } catch (InterruptedException e) {
//            Log.e(TAG, "connectBinderPoolService: ", e);
//            e.printStackTrace();
//        }
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "queryBinder: ", e);
        }
        return binder;
    }

    private ServiceConnection mBindPoolServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBinderPool = IPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                Log.e(TAG, "onServiceConnected: ", e);
            }
//            mConnectBinderPoolCountDownLatch.countDown();
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    private IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.w(TAG, "binderDied: ");
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    public static class BinderPoolImpl extends IPool.Stub {
        @Override
        public IBinder queryBinder(int code) throws RemoteException {
            IBinder binder = null;
            switch (code) {
                case 0:
                    binder = new AddImpl();
                    break;
                case 1:
                    binder = new SubImpl();
                    break;
                default:
                    break;
            }
            return binder;
        }
    }

}
