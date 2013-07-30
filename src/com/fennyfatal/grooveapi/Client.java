package com.fennyfatal.grooveapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.StrictMode;
import android.text.format.Time;

public class Client{
	String SALT = "gooeyFlubber";
	String VERSION = "20120830";
	String CLIENT_NAME ="mobileshark";
	String session = "";
	String country = "";
	String token = "null";
	long TTL = 120000; // Milliseconds
	UUID uuid;
	long time = 0;
	boolean initialized = false;
	
	public Client(Context theContext) throws Exception 
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		if (!getSessionandCountry())
			throw new Exception();
		uuid = UUID.randomUUID();
		if (!getCommunicationToken())
			throw new Exception();
	}
	
	public ArrayList<Song> testFunctions() throws JSONException
	{
		
		JSONArray songs = getPopularSongs("monthly").getJSONArray("Songs");
		ArrayList<Song> Songs = new ArrayList<Song>();
		for (int i = 0; i< songs.length() ; i++)
		{
			Songs.add(Song.songFromJSONObject(songs.getJSONObject(i)));
		}
		return Songs;
	}
	
	
	public String GetPlayURL(Song s)
	{
		return getStreamURLFromSongID(s.getSongID());
	}
	private JSONObject getPopularSongs (String period)
	{
		if (period.equals("monthly") || period.equals("daily"))
		{
			try {
				return new JSONObject(request("popularGetSongs","{\"type\":\""+period+"\"}",true)[1]).getJSONObject("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	private String getStreamURLFromSongID(String songID)
	{
		JSONObject streamdata = getStreamKeyFromSongID(songID);
		if (streamdata != null)
		{
			try {
				return "http://"+streamdata.getString("ip")+"/stream.php?streamKey="+streamdata.getString("streamKey");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private JSONObject getStreamKeyFromSongID(String songID)
	{
		
		try {
			return new JSONObject( request("getStreamKeyFromSongIDEx",
				"{"+
				"\"type\":" + "0"+"," +
				"\"prefetch\":false," +
				"\"songID\":"+'"'+songID+'"'+"," +
				"\"country\":"+ this.country +"," +
				"\"mobile\":false"+
				"}")[1]).getJSONObject("result");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private boolean getCommunicationToken() {
		try {
			this.token = "null";
			String body = request("getCommunicationToken", "{\"secretKey\":\""+Utils.md5(session)+"\"}", true)[1];
			JSONObject values = new JSONObject(body);
			this.token = values.getString("result");
			this.time = Calendar.getInstance().getTimeInMillis()+this.TTL;
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
		if (this.token != "null" && Calendar.getInstance().getTime().after(new Date(this.time)))
			getCommunicationToken();
		String host = "grooveshark.com";
		int port = (!secure) ? 80 : 443;
		String path = "/more.php?" + (method.equals("getCommunicationToken") ? "json" : method);
		
		String Body ="";
		try {
		Body =  "{\"header\":"+
			"{"+
			"\"token\":"+(this.token.equals("null")? "null" : '"' + 
				genToken(Utils.md5(session),method,this.token) + '"' )+","+
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
