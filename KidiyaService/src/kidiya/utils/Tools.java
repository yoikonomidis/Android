package kidiya.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Tools {

	public static Editor putDouble(final Editor edit, final String key, final double value) {
		return edit.putLong(key, Double.doubleToRawLongBits(value));
	}

	public static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
	}

}
