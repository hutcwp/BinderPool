package hut.cwp.binderpool;

import androidx.appcompat.app.AppCompatActivity;
import hut.cwp.binderpool.aidl.AddImpl;
import hut.cwp.binderpool.aidl.BinderPool;
import hut.cwp.binderpool.aidl.SubImpl;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button btnAdd;
    Button btnSub;
    private BinderPool binderPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btn_add);
        btnSub = findViewById(R.id.btn_sub);

        binderPool = BinderPool.getsInstance(this);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IBinder addBinder = binderPool.queryBinder(BinderPool.ADD);

                {
                    if ((addBinder==null)) {
                        Log.d(TAG, "onClick: null");
                    }
                    android.os.IInterface iin = addBinder.queryLocalInterface("hut.cwp.binderpool.IAdd");
                    if (((iin!=null)&&(iin instanceof hut.cwp.binderpool.IAdd))) {
                        Log.d(TAG, "onClick: native binder");
                    }
                    Log.d(TAG, "onClick: binder Proxy");
                }

                IAdd add = AddImpl.asInterface(addBinder);
                try {
                    Log.d(TAG, "btn add: processId = " + android.os.Process.myPid());
                    add.add(12,45);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IBinder subBinder = binderPool.queryBinder(BinderPool.SUB);
                ISub sub = SubImpl.asInterface(subBinder);
                try {
                    Log.d(TAG, "btn sub: processId = " + android.os.Process.myPid());
                    sub.sub(30,10);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
