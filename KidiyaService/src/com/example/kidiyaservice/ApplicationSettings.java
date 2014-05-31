package com.example.kidiyaservice;

public class ApplicationSettings {

	private static ApplicationSettings m_instance;
	private int m_vehicleId;
	private String m_busLine;
	
	private ApplicationSettings(){
	}
	
	/**
	 * Returns the existing ApplicationSettings object if exists, otherwise it creates a new ApplicationSettings instance
	 * @return the Settings instance
	 */
	public static ApplicationSettings instance(){
		if(m_instance == null)
		{
			m_instance = new ApplicationSettings();
		}
		return m_instance;
	}

	public void setVehicleId(int vehicleId){
		m_vehicleId = vehicleId;
	}
	
	public int vehicleId(){
		return m_vehicleId;
	}
	
	public void setBusLine(String busLine){
		m_busLine = busLine;
	}
	
	public String busLine(){
		return m_busLine;
	}
}
