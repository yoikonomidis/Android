package com.example.kidiyaservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import service.KidiyaService;
import service.KidiyaService.KidiyaBinder;

/*
 * High-level API for Kidiya Service.
 */
public class KidiyaAPI {
    private final ServiceConnection mServiceConnection;
    private KidiyaService mKidiyaService;
    private final Context mContext;
    private static final String TAG = "Kidiya API";
    private boolean mServiceBound = false;
    
    /**
     * Service connection to handle connection with the Kidiya service.
     */
    private class KidiyaServiceConn implements ServiceConnection {
        private final ServiceConnection mServiceConnection;

        public KidiyaServiceConn(ServiceConnection serviceConnection) {
            mServiceConnection = serviceConnection;
        }

        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.v(TAG, "Bound to Kidiya API...");

            mKidiyaService = ((KidiyaBinder) binder).getService();
            mServiceBound = true;

            if (mServiceConnection != null) {
                mServiceConnection.onServiceConnected(className, binder);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG, "Kidiya API disconnected...");

            mKidiyaService = null;
            mServiceBound = false;

            if (mServiceConnection != null) {
                mServiceConnection.onServiceDisconnected(className);
            }
        }
    }

    public KidiyaAPI(Context context) {
        this(context, null);
    }

    public KidiyaAPI(Context context, ServiceConnection serviceConnection) {
        mServiceConnection = new KidiyaServiceConn(serviceConnection);
        mContext = context;
        bindToKidiyaService();
    }
    
    public void bindToKidiyaService() {
        if (!mServiceBound) {
            final Intent serviceIntent = new Intent(mContext, KidiyaService.class);
            boolean bindResult = mContext.bindService(serviceIntent, mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            // already bound
        }
    }
    
    public void unbindFromKidiyaService() {
        if (true == mServiceBound && null != mServiceConnection) {
            mContext.unbindService(mServiceConnection);
        } else {
            // already unbound
        }
        mKidiyaService = null;
        mServiceBound = false;
    }
}
