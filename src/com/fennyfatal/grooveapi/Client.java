package com.fennyfatal.grooveapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Client{
	RequestQueue requestQueue;
	String SALT = "gooeyFlubber";
	String VERSION = "20120830";
	String CLIENT_NAME ="mobileshark";
	String session = "";
	String country = "";
	String token = "null";
	int TTL = 120;
	UUID uuid;
	boolean initialized = false;
	
	public Client(Context theContext) throws Exception 
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		this.requestQueue = Volley.newRequestQueue(theContext);
		if (!getSessionandCountry())
			throw new Exception();
		uuid = UUID.randomUUID();
		if (!getCommunicationToken())
			throw new Exception();
	}
	
	JSONObject getPopularSongs (String period)
	{
		if (period.equals("monthly") || period.equals("daily"))
		{
			try {
				return new JSONObject(request("popularGetSongs","{\"type\":\""+period+"\"}",true)[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	String getStreamURLFromSongID(String songID)
	{
		JSONObject streamdata = getStreamKeyFromSongID(songID);
		if (streamdata != null)
		{
			try {
				return "http://"+streamdata.getString("ip")+"/stream.php?streamKey="+streamdata.getString("stream_key");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	JSONObject getStreamKeyFromSongID(String songID)
	{
		
		try {
			return new JSONObject( request("getStreamKeyFromSongIDEx",
				"{"+
				"\"type\":" + "0"+"," +
				"\"prefetch\":false," +
				"\"songID\":"+'"'+24525551+'"'+"," +
				"\"country\":"+ this.country +"," +
				"\"mobile\":false"+
				"}")[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private boolean getCommunicationToken() {
		try {
			String body = request("getCommunicationToken", "{\"secretKey\":\""+Utils.md5(session)+"\"}", true)[1];
			JSONObject values = new JSONObject(body);
			token = values.getString("result");
		return true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private String[] request(String method, String paramaters)
	{
		return request(method, paramaters, true);
	}
	private String[] request(String method, String paramaters, boolean secure){
		String host = "grooveshark.com";
		int port = (!secure) ? 80 : 443;
		String path = "/more.php?" + (method.equals("getCommunicationToken") ? "json" : method);
		
		String Body ="";
		try {
			Body = "{\"header\":"+
			     "{"+
					  "\"token\":"+(this.token.equals("null")? "null" : '"' + genToken(Utils.md5(session),method,this.token) + '"' )+","+
					  "\"uuid\":\""+this.uuid.toString().toUpperCase()+"\","+
					  "\"client\":\""+this.CLIENT_NAME+"\","+
					  "\"session\":\""+this.session+"\","+
					  "\"clientRevision\":\""+this.VERSION+"\"" +
				"}," +
			"\"parameters\":"+
				 paramaters +','+
		    "\"method\":\""+method+"\"" +
			"}";
		} catch (NoSuchAlgorithmException e1) {} catch (UnsupportedEncodingException e1) {} //THESE SHOULD NEVER HAPPEN
		
        String[] headers = {//TODO let's maybe use an http class that doesn't suck instead of manually writing it out?
		"POST " + (secure ? "https" : "http") + "://grooveshark.com" + path + " HTTP/1.1",
		"Content-Type: application/json",
		"Referer: http://grooveshark.com/",
		"Cookie: PHPSESSID=" + this.session,
		"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"User-Agent: Mozilla/5.0 (Windows NT 6.1)",
		"Host: grooveshark.com",
		"Content-Length: "+ Body.length() 
		};
        try {
        	return Utils.getRawUrlRequest(host,headers,Body,port,secure);
		} catch (IOException e) {
			return null;
		}
    }
	
	private String genToken(String sessionHash, String method, String token) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Random rnd = new Random();
        String lastRandomizer = "";
        for (int i = 0; i < 3; i++)
        {
            int value = rnd.nextInt(255);
            lastRandomizer += String.format("%02x", value);
        }
        String clearText = method + ":" + token+ ":" + this.SALT + ":" + lastRandomizer.toLowerCase();
        return (lastRandomizer + Utils.SHA1(clearText)).toLowerCase();
    }

	private boolean getSessionandCountry()
	{
		try{
		String[] response = Utils.getRawUrlRequest("grooveshark.com", "/", 80, false);
		String header = response[0];
		String body = response[1];
		String jsonfinder = "window.gsConfig = ";
		int start = body.indexOf(jsonfinder)+jsonfinder.length();
		int end = body.indexOf(";",start);
		String json = body.substring(start,end);
		JSONObject values = new JSONObject(json);
		this.country = values.getString("country");
		String session = "PHPSESSID=";
		start = header.indexOf(session)+session.length();
		this.session = header.substring(start,start+32);
		return true;
		}
		catch (Exception ex)
		{
		return false;
		}
	}

}
