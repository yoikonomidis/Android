package kidiya.utils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.content.Context;

public class Tools {
	
	/**
	 * This function checks whether Google Play Services are installed and if not, redirects the user to Google Play Store
	 * @param context
	 * @return true if Google Play Services are installed, false otherwise
	 */
	public static boolean googlePlayServicesExists(Context context){
		final int RQS_GooglePlayServices = 1;
		
		boolean result = false;
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		if (status != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(status, (Activity) context, RQS_GooglePlayServices).show();
		}
		else{
			result = true;
		}
		
		return result;
	}

}
