package service;

import com.example.kidiyaservice.R;
import service.database.DataProvider;
import service.database.DataObserver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.EventCallback;
import android.app.Service;
import android.content.Context;
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
	private Handler mHandler = new Handler();
	private Context context;
	private DataObserver dataObserver;// = new DataObserver(mHandler, context);
    private static Handler initHandler;	// handler on main application thread	
    private LocationSensor locationSensor;
    private AccelerationSensor accelerationSensor;
    private final IBinder binder = new KidiyaBinder();	// interface for clients
	
    /**
     * Class for clients to access Kidiya Service.
     */
    public class KidiyaBinder extends Binder {
        public KidiyaService getService() {
        	context = KidiyaService.this;
        	dataObserver = new DataObserver(mHandler, context);
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
            	getContentResolver().registerContentObserver(DataProvider.CONTENT_URI1,true,dataObserver);
            	getContentResolver().registerContentObserver(DataProvider.CONTENT_URI2,true,dataObserver);
            	startSensing();
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
    	stopSensing();
    	getContentResolver().unregisterContentObserver(dataObserver);
        // stop the main service
        stopForeground(true);

        super.onDestroy();
    }
    
    public synchronized void startKidiya() {
    	Log.v(TAG, "Kidiya service about to be started");
        startService(new Intent(this, KidiyaService.class));
    }
    
    public void startSensing(){
    	locationSensor = LocationSensor.getInstance(context);
    	accelerationSensor = AccelerationSensor.getInstance(context);
    	locationSensor.start();
    	accelerationSensor.start();
    }
    
    public void stopSensing(){
    	locationSensor.stop();
    	accelerationSensor.stop();
    }
}
