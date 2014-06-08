package service;

import service.database.DataProvider;
import service.database.DataObserver;
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
	private Handler m_Handler = new Handler();
	private Context m_context;
	private DataObserver m_dataObserver;// = new DataObserver(mHandler, context);
    private static Handler m_initHandler;	// handler on main application thread	
    private LocationSensor m_locationSensor;
    private final IBinder m_binder = new KidiyaBinder();	// interface for clients
    private Intent m_intent;
	
    /**
     * Class for clients to access Kidiya Service.
     */
    public class KidiyaBinder extends Binder {
        public KidiyaService getService() {
        	m_context = KidiyaService.this;
        	m_dataObserver = new DataObserver(m_Handler, m_context);
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
    	
        if (null == m_initHandler) {
            HandlerThread startThread = new HandlerThread("Start thread");
            startThread.start();
            m_initHandler = new Handler(startThread.getLooper());
        }
        m_initHandler.post(new Runnable() {

            @Override
            public void run() {
            	Log.i(TAG, "Start sensing and transmission");
            	getContentResolver().registerContentObserver(DataProvider.CONTENT_URI,true,m_dataObserver);
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
		
		return m_binder;
	}
	
    /**
     * Stops data transmission and sensing.
     */
    @Override
    public void onDestroy() {
    	Log.v(TAG, "Kidiya service is being destroyed");
    	// TODO: Create stopSensing() and stopTransmission().
    	stopSensing();
    	getContentResolver().unregisterContentObserver(m_dataObserver);
        // stop the main service
        stopForeground(true);

        super.onDestroy();
    }
    
    public synchronized void startKidiya() {
    	Log.v(TAG, "Kidiya service about to be started");
    	m_intent = new Intent(this, KidiyaService.class);
        startService(m_intent);
    }
    
    public synchronized void stopKidiya(){
    	Log.v(TAG, "Kidiya service about to be terminated");
    	
    	if(m_intent != null)
    		stopService(m_intent);
    }
    
    public void startSensing(){
    	m_locationSensor = LocationSensor.getInstance(m_context);
    	m_locationSensor.start();
    }
    
    public void stopSensing(){
    	m_locationSensor.stop();
    }
}
