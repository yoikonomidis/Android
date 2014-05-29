package kidiya.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tools {
	
	public static JSONArray convertArrayListToJSONArray(ArrayList<String> listToConvert, String name){
    	JSONObject jsonObject = new JSONObject();
		try {
			JSONArray jsonArray = new JSONArray();
			for(int i=0; i<listToConvert.size(); i++){
				jsonArray.put(listToConvert.get(i).toString());
			}
			
			jsonObject.put(name, jsonArray);
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		JSONArray jsonArray = new JSONArray();
		jsonArray.put(jsonObject);
		
		return jsonArray;
    }

}
