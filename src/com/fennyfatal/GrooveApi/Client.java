package com.fennyfatal.GrooveApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Client{
	public interface GrooveApiAsyncReceiver {
		public void recieveClientAsync (Client c);
		public void recievePlaylistAsync (Playlist pl);
		public void recievePlayURL(Song theSong,String url);
		public void recieveShareURL(Song theSong,String url);
	}

	public static class RequestArgs
	{
		public static String LastARG (String arg)
		{
			int lastloc = arg.lastIndexOf(',');
			if (lastloc != -1)
				return arg.substring(0, arg.lastIndexOf(','));
			else
				return arg;
		}
		public static String SongID (String songID)
		{
			return "\"songID\":"+'"'+songID+'"' 
					+',';
		}
		public static String Type (String type)
		{
			return "\"type\":" + '"' + type + '"' 
					+',';
		}
		public static String Query (String query)
		{
			return "\"query\":" + '"' + query +'"' 
					+',';
		}
		public static String Country (String country)
		{
			return "\"country\":"+ country
					+',';
		}
		public static String Mobile (boolean mobile)
		{
			return "\"mobile\":" + mobile
					+',';
		}
		public static String Prefetch (boolean prefetch)
		{
			return "\"prefetch\":" + prefetch
					+',';
		}
		public static String Token (String token)
		{
			if (token == null)
				return "\"token\":null"	
					+',';
			else
				return "\"token\":\""+ token + '"'
					+',';
		}
		public static String Uuid (String uuid)
		{
			return "\"uuid\":\""+uuid+'"'
					+',';
		}
		public static String Client (String client)
		{
			return "\"client\":\""+ client+'"'
					+',';
		}
		public static String Session (String session)
		{
			return "\"session\":\""+session+'"'
					+',';
		}
		public static String ClientRevision (String clientRevision)
		{ 
			return "\"clientRevision\":\""+clientRevision+'"'
					+',';
		}
		public static String SecretKey (String secretKey)
		{ 
			return "\"secretKey\":\""+secretKey+"\",";
		}
	}
	String SALT_JSQUEUE = "nuggetsOfBaller";
	String VERSION_JSQUEUE = "20130520";
	String CLIENT_NAME_JSQUEUE ="jsqueue";
	String SALT = "gooeyFlubber";
	String VERSION = "20120830";
	String CLIENT_NAME ="mobileshark";
	String session = "null";
	String country = "null";
	String token = "null";
	long TTL = 120000; // Milliseconds
	UUID uuid = null;
	long time = 0;
	boolean initialized = false;
	
	//For when we ever go internally async.
	
	public boolean isInitialized() { 
		return initialized;
	}
	
	/*
	 * The Constructor:D
	 * Just instantiate stuff here. 
	 */
	
	public Client(Context theContext) throws Exception 
	{
		this(theContext, false);
	}
	
	public Client(Context theContext, boolean useCountryHack) throws Exception 
	{
		/*
		 * This is so we can do all of these things from the main thread, it is recommended to make calls from worker threads.
		 * This will likely be removed from the final release. Please do not rely on it if you use this in your projects. 
		 */
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);
		
		//Set up needed information for the instance.
		if (!getSessionandCountry(useCountryHack))
			throw new Exception(); //TODO Put some information in here.
		uuid = UUID.randomUUID();
		if (!getCommunicationToken())
			throw new Exception(); //TODO Put some information in here.
		
	}

	/*
	 * Public method that takes the results from getResultsFromSearch and returns a playlist. 
	 * oddly enough the internal song array is also called "result" here.
	 * It also has a slightly different field layout from the popular songs results.
	 */
	public Playlist getPopularSongs(String period)
	{
		JSONArray songs;
	try {
		songs = getPopularSongsInt(period).getJSONArray("Songs");
		Playlist Songs = new Playlist();
		for (int i = 0; i< songs.length() ; i++)
		{
			Songs.add(Song.songFromJSONObject(songs.getJSONObject(i)));
		}
		return Songs;
	} catch (JSONException e) {
		// TODO Should we just re-throw this?
		e.printStackTrace();
	}
	return null;
	}
	
	
	/*
	 * Public method that takes the results from getResultsFromSearch and returns a playlist. 
	 * oddly enough the internal song array is also called "result" here.
	 * It also has a slightly different field layout from the popular songs results.
	 */
	
	public Playlist doSearch(String query)
	{
		JSONArray songs;
	try { 
		songs = getSearchResults(query).getJSONArray("result");
		Playlist Songs = new Playlist();
		for (int i = 0; i< songs.length() ; i++)
		{
			Songs.add(Song.songFromJSONObject(songs.getJSONObject(i)));
		}
		return Songs;
	} catch (JSONException e) {
		// TODO Should we just re-throw this?
		e.printStackTrace();
	}
	return null;
	}


	/*
	 * Overloaded internal method for getResultsFromSearch.
	 * returns the Result object from the json response.
	 */
	private JSONObject getSearchResults(String query)
	{
		return getSearchResults("Songs", query);
	}
	
	/*
	 * Internal method that holds the param definition for getResultsFromSearch.
	 * returns the Result object from the json response.
	 */
	private JSONObject getSearchResults(String type, String query)
	{
		try {
			return new JSONObject(
			request("getResultsFromSearch", 
					'{' +
					RequestArgs.Type(type) +
					RequestArgs.LastARG(RequestArgs.Query(query)) +
					'}')[1]).getJSONObject("result");
			
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return null;
	}
	
	public String GetPlayURL(Song s)
	{
		return getStreamURLFromSongID(s.getSongID());
	}
	/*
	 * Internal method that holds the param definition for popularGetSongs.
	 * returns the Result object from the json response.
	 */
	private JSONObject getPopularSongsInt (String period)
	{
		if (period.equals("monthly") || period.equals("daily"))
		{
			try {
				return new JSONObject(request("popularGetSongs",
						'{' +
						RequestArgs.LastARG(RequestArgs.Type(period)) + 
						'}',true)[1]).getJSONObject("result");
			} catch (JSONException e) {
				// TODO Should we just re-throw this?
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
				// TODO Should we just re-throw this?
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/*
	 * Internal method that holds the param definition for getStreamKeyFromSongID.
	 * returns the Result object from the json response.
	 */
	
	private JSONObject getStreamKeyFromSongID(String songID)
	{
		try {
			return new JSONObject( request("getStreamKeyFromSongIDEx",
				"{"+
				RequestArgs.Type("0") +
				RequestArgs.Prefetch(false) +
				RequestArgs.SongID(songID) +
				RequestArgs.Country(country) + 
				RequestArgs.LastARG(RequestArgs.Mobile(false)) +
				"}")[1]).getJSONObject("result");
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Internal method that holds the param definition for getSongFromToken.
	 * returns the Result object from the json response.
	 */
	private JSONObject getTokenForSong(String songID)
	{
		try {
			return new JSONObject( request("getTokenForSong",
				"{"+
				RequestArgs.SongID(songID) +
				RequestArgs.LastARG(RequestArgs.Country(this.country)) +
				'}')[1]);
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return null;
	}
	
	private JSONObject getDetailsForBroadcast(String songID)
	{
		try {
			String result = request("getDetailsForBroadcast",
					'{'+
					RequestArgs.LastARG(RequestArgs.SongID(songID)) +
					'}',true, true)[1];
			return new JSONObject( result ).getJSONObject("result");
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return null;
	}
	
	public String getShareURL(Song s)
	{
		JSONObject streamdata = getDetailsForBroadcast(s.SongID);
		if (streamdata != null)
		{
			try {
				return streamdata.getString("tinySongURL");
			} catch (JSONException e) {
				// TODO Should we just re-throw this?
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public Playlist getPlaylistFromToken(String token)
	{
		JSONObject songs;
	try {
		Playlist Songs = new Playlist();
		Songs.add(Song.songFromJSONObject(getSongFromToken(token)));
		return Songs;
	} catch (JSONException e) {
		// TODO Should we just re-throw this?
		e.printStackTrace();
	}
	return null;
	}
	
	private JSONObject getSongFromToken(String token)
	{
		try {
			return new JSONObject( request("getSongFromToken",
				"{"+
				RequestArgs.Token(token) +
				RequestArgs.LastARG(RequestArgs.Country(this.country)) +
				'}')[1]).getJSONObject("result");
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * Here we get the communication token. 
	 * This seems to last a random amount of time, despite what the header seems to say about expiration.
	 * We are reupping on it every ~2 min or so for now (configurable in the const values for the time being)
	 * TODO Add an argument for the TTL in the constructor?
	 */
	private boolean getCommunicationToken() {
		try {
			this.token = "null";
			String body = request("getCommunicationToken", 
					'{' + 
					RequestArgs.LastARG(RequestArgs.SecretKey(Utils.md5(session))) +
					'}', true)[1];
			JSONObject values = new JSONObject(body);
			this.token = values.getString("result");
			this.time = Calendar.getInstance().getTimeInMillis()+this.TTL;
		return true;
		} catch (JSONException e) {
			// TODO Should we just re-throw this?
			e.printStackTrace();
		}
		return false;
	}
	
	private String[] request(String method, String paramaters)
	{
		return request(method, paramaters, true, false);
	}
	
	private String[] request(String method, String paramaters,boolean secure)
	{
		return request(method, paramaters, secure, false);
	}
	
	/*
	 * This method is the main request builder of the application.
	 * The whole thing is working RAW primarily due to my dissatisfaction with 
	 * HTTP layers on this platform.
	 * I am open to any suggestions.
	 */
	private String[] request(String method, String paramaters, boolean secure, boolean isJSQUEUE){
		if (this.token != "null" && Calendar.getInstance().getTime().after(new Date(this.time)))
			getCommunicationToken();
		String host = "grooveshark.com";
		int port = (!secure) ? 80 : 443;
		String path = "/more.php?" + (method.equals("getCommunicationToken") ? "json" : method);
		
		String Body ="";
		try {
		Body =  "{\"header\":"+
			"{"+
			RequestArgs.Token(this.token.equals("null")? null : genToken(method,this.token,isJSQUEUE)) +
			RequestArgs.Uuid(this.uuid.toString().toUpperCase()) +
			RequestArgs.Client(isJSQUEUE ? this.CLIENT_NAME_JSQUEUE : this.CLIENT_NAME) +
			RequestArgs.Session(this.session) +
			RequestArgs.LastARG(RequestArgs.ClientRevision(isJSQUEUE ? this.VERSION_JSQUEUE : this.VERSION)) +
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
	
	/*
	 * Generate a token based on the SALT, the method we are calling, and the session token. 
	 * we also prepend a random series of 3 hex bytes we use for salt to the final hash. 
	 */
	private String genToken(String method, String token,boolean isJSQUEUE) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Random rnd = new Random();
        String lastRandomizer = "";
        for (int i = 0; i < 3; i++)
        {
            int value = rnd.nextInt(255);
            lastRandomizer += String.format("%02x", value);
        }
        String clearText = method + ":" + 
        token+      //Why bother passing this as an argument?
        ":" +
        (isJSQUEUE? this.SALT_JSQUEUE : this.SALT) + //or maybe we should pass this as an argument? Would probably be better for portability, and possible refactoring.
        ":" + lastRandomizer.toLowerCase();
        return (lastRandomizer + Utils.SHA1(clearText)).toLowerCase();
	}

	/*
	 * Get the Session header value, and store it. We also parse the country string rather than using a canned one.
	 * If the country code becomes too much of a moving target we may have to either use regex, or static values.
	 */
	private boolean getSessionandCountry()
	{
		return getSessionandCountry(false);
	}
	
	private boolean getSessionandCountry(boolean useCountryHack)
	{
		try{
		long utmb = Calendar.getInstance().getTimeInMillis() / 1000;
		String[] response = Utils.getRawUrlRequest("grooveshark.com", "/preload.php?" + (utmb + 1) +"&getCommunicationToken=1&hash=", 80, false);
		String header = response[0];
		String body = response[1];
		String jsonfinder = "window.tokenData = {";
		int start = (body.indexOf(jsonfinder)+jsonfinder.length()) - 1;
		if (!useCountryHack && (start > (jsonfinder.length() - 2))) 
		{
			try
			{
			int end = body.indexOf("};",start) +1;
			String json = body.substring(start,end);
			JSONObject values = new JSONObject(json);
			this.country = values.getJSONObject("getGSConfig").getString("country");
			} catch (Exception ex) { useCountryHack = true;}
		}
		else 
		{
			this.country = "{\"CC1\":\"0\", \"CC2\":\"0\", \"CC3\":\"0\", \"CC4\":\"0\", \"ID\":\"1\"}"; 
		}
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
