package hut.cwp.binderpool.aidl;

import android.os.RemoteException;
import android.util.Log;

import hut.cwp.binderpool.IAdd;

public class AddImpl extends IAdd.Stub {

    private static final String TAG = "AddImpl";

    @Override
    public void add(int a, int b) throws RemoteException {
        Log.d(TAG, "add: " + a + " " + b + " =" + (a + b));
        Log.d(TAG, "add: processId = " + android.os.Process.myPid());
    }
}
