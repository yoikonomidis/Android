package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

/*
 * Kidiya Service implementation. Initiates sensing and transmission of 
 * the required data.
 */
public class KidiyaService extends Service {
	private static final String TAG = "Kidiya Service";
	
    private static Handler initHandler;	// handler on main application thread	
    private final IBinder binder = new KidiyaBinder();	// interface for clients
	
    /**
     * Class for clients to access Kidiya Service.
     */
    public class KidiyaBinder extends Binder {
        public KidiyaService getService() {
            return KidiyaService.this;
        }
    }
	
	/**
	 * Does nothing except pop out a log message. The service is really started 
	 * in onStartCommand otherwise it would also start when an activity binds to
	 * it.
	 */
	@Override
    public void onCreate() {
		Log.v(TAG, "Kidiya service is being created");
		Transceiver.instance();
    }
	
    /**
	 * Starts the Kidiya service.
	 *
	 * @param intent
	 * 		The Intent supplied to startService(Intent).
	 * 
	 * @param flags
	 * 		Additional data about this start request.
	 * 
	 * @param startId
	 * 		A unique integer representing this specific request to start.
	 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.v(TAG, "Kidiya service is being started");
		
        if (null == initHandler) {
            HandlerThread startThread = new HandlerThread("Start thread");
            startThread.start();
            initHandler = new Handler(startThread.getLooper());
        }
        initHandler.post(new Runnable() {

            @Override
            public void run() {
            	Log.i(TAG, "Start sensing and transmission");
            	// TODO: Create startSensing() and startTransmission().
            }
        });
        return START_STICKY;
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "Some component is binding to Kidiya service");
		// TODO: Change KidiyaBinder() to instantiate the proper binder.
		
		return binder;
	}
	
    /**
     * Stops data transmission and sensing.
     */
    @Override
    public void onDestroy() {
    	Log.v(TAG, "Kidiya service is being destroyed");
    	// TODO: Create stopSensing() and stopTransmission().
    	
        // stop the main service
        stopForeground(true);

        super.onDestroy();
    }
}
