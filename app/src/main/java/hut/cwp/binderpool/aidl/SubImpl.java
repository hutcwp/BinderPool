package hut.cwp.binderpool.aidl;

import android.app.ActivityManager;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

import hut.cwp.binderpool.ISub;

public class SubImpl extends ISub.Stub {
    private static final String TAG = "SubImpl";

    @Override
    public void sub(int a, int b) throws RemoteException {
        Log.d(TAG, "sub: " + a + " - " + b + " = " + (a - b));
        Log.d(TAG, "sub: processId = " + android.os.Process.myPid());
    }

    public static String getAppName(Context context) {
        int pid = android.os.Process.myPid(); // Returns the identifier of this process
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

}
