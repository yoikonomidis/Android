package com.example.mrdummy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Rester extends AsyncTask<String, String, String> {

	private static final String TAG = "Rester";
	private static InputStream is = null;
	private static int timeoutConnection = 3500;
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Log.v(TAG, "About to send request...");
		dummyRest(params[0], params[1], params[2], params[3]);
		return null;
	}
	
	public void dummyRest(String method, String ip, String port, String function) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		String url = "http://"+ip+":"+port+"/" + function;
		Log.v(TAG, "URL: " + url);
		
		try {
			//add timeout
			if (method == "GET") {
				Log.v(TAG, "Sending GET request...");
	            String paramString = URLEncodedUtils.format(params, "utf-8");
	            url += "?" + paramString;
	            HttpGet httpGet = new HttpGet(url);
	
	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
			}
			else if (method == "POST") {
				Log.v(TAG, "Sending POST request...");
                HttpPost httpPost = new HttpPost(url);
                
                JSONObject json = new JSONObject();

                json.put("id", "41");
                json.put("firstName", "Stelios");
                json.put("lastName", "Kouloglou");
                json.put("managerId", "42");
                StringEntity se = new StringEntity(json.toString());  
                Log.v(TAG,"Data sent: " + json);
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);
                
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            Log.v(TAG, "Response from the server " + sb.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
	}

}
