package kidiya.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings {

	private static Settings m_settings;
	private SharedPreferences m_sharedPreferences;
	private SharedPreferences.Editor m_editor;
	
	public enum SETTING_TYPE{
		BOOLEAN,
		INT,
		FLOAT,
		DOUBLE,
		LONG,
		STRING
	}
	
	private Settings(Context context, String applicationName){
		m_sharedPreferences = context.getSharedPreferences(applicationName + "Settings", 0);
		m_editor = m_sharedPreferences.edit();
		initialize();
	}
	
	/**
	 * Returns the existing Settings object if exists, otherwise it creates a new Settings instance
	 * @param context
	 * @param applicationName the application name
	 * @return the Settings instance
	 */
	public static Settings instance(Context context, String applicationName){
		if(m_settings == null)
		{
			m_settings = new Settings(context, applicationName);
		}
		return m_settings;
	}
	
	/**
	 * This function retrieves the value of a setting
	 * @param settingName the name of the setting
	 * @param type the type of the value to be retrieved
	 * @return the value of the setting
	 */
	@SuppressWarnings("unchecked")
	public <T> T value(String settingName, SETTING_TYPE type){
		
		switch(type){
			case BOOLEAN: 
				return (T) (Object) m_sharedPreferences.getBoolean(settingName, false);
			case INT: 
				return (T) ((Object) m_sharedPreferences.getInt(settingName, 0));
			case FLOAT: 
				return (T) ((Object) m_sharedPreferences.getFloat(settingName, 0));
			case DOUBLE: 
				return (T) ((Object) getDouble(m_sharedPreferences, settingName, 0));
			case LONG: 
				return (T) ((Object) m_sharedPreferences.getLong(settingName, 0));
			case STRING: 
				return (T) ((Object) m_sharedPreferences.getString(settingName, ""));
		}
		return null;
	}
	
	/**
	 * This function sets the value of a setting
	 * @param settingName the name of the setting
	 * @param settingValue the value of the setting
	 * @param type the type of the value to be retrieved
	 */
	public <T> void setValue(String settingName, T settingValue, SETTING_TYPE type){
		switch(type){
		case BOOLEAN: 
			m_editor.putBoolean(settingName, Boolean.class.cast(settingValue));
			break;
		case INT: 
			m_editor.putInt(settingName, Integer.class.cast(settingValue));
			break;
		case FLOAT: 
			m_editor.putFloat(settingName, Float.class.cast(settingValue));
			break;
		case DOUBLE: 
			putDouble(m_editor, settingName, Double.class.cast(settingValue));
			break;
		case LONG:  
			m_editor.putLong(settingName, Long.class.cast(settingValue));
			break;
		case STRING: 
			m_editor.putString(settingName, String.class.cast(settingValue));
			break;
		}
		m_editor.commit();
	}
	
	private void putDouble(final Editor edit, final String key, final double value) {
		edit.putLong(key, Double.doubleToRawLongBits(value));
	}

	private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
	}
	
	/**
	 * Initialize any settings
	 */
	private void initialize(){
		//TODO: Initialize with all needed values
	}
}
