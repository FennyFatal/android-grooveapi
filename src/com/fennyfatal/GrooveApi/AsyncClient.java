package com.fennyfatal.GrooveApi;

import java.util.ArrayList;

import com.fennyfatal.GrooveApi.Client.GrooveApiAsyncReceiver;

import android.content.Context;
import android.os.AsyncTask;

/*
 * Here we have asynchronicity. 
 *  
 */
public class AsyncClient implements GrooveApiAsyncReceiver
{
	boolean executingQuery;
	Client myClient = null;
	Context lastContext;
	ArrayList<GrooveApiAsyncReceiver> receivers;
	
	/*
	 * Constructors
	 */
	
	public AsyncClient(Context receiver)
	{
		this(receiver, true);
	}
	
	public AsyncClient(Context receiver, boolean initializeClient) {
		lastContext = (Context) receiver;
		this.receivers = new ArrayList<Client.GrooveApiAsyncReceiver>();
		if (receiver instanceof GrooveApiAsyncReceiver)
			receivers.add((GrooveApiAsyncReceiver) receiver);
		if (initializeClient)
			initializeClientAsync();
	}
	
	public AsyncClient(Context aContext, GrooveApiAsyncReceiver receiver)
	{
		this(aContext,receiver,true);
	}
	
	public AsyncClient(Context aContext, GrooveApiAsyncReceiver receiver, boolean initializeClient) {
		lastContext = aContext;
		this.receivers = new ArrayList<Client.GrooveApiAsyncReceiver>();
		this.receivers.add(receiver);
		if (initializeClient)
			initializeClientAsync();
	}
	
	public boolean registerGrooveApiAsyncReceiver(GrooveApiAsyncReceiver receiver)
	{
		return receivers.add(receiver);
	}
	
	
	public boolean removeGrooveApiAsyncReceiver(GrooveApiAsyncReceiver receiver)
	{
		return receivers.remove(receiver);
	}
	/*
	 * This method will only return true when the asynchronous client creation has completed.
	 */
	public boolean isExecutingQuery()
	{
		return executingQuery;
	}
	
	/*
	 * WARNING: this method will return null if init has not completed.
	 */
	public Client getClient()
	{
		return myClient;
	}
	/*
	 * This method creates a new local client and sends THE SAME INSTANCE to all of the registered receivers.
	 */
	public void initializeClientAsync()
	{
		myClient = null;
		executingQuery = true;
		new AsyncClientInit().execute(lastContext,this);
	}
	
	/*
	 * This method replaces the local client  
	 */
	public void initializeClientAsync(Context theContext)
	{
		myClient = null;
		executingQuery = true;
		new AsyncClientInit().execute(theContext,this);
	}
	
	/*
	 * This method sends a search query using the local client, and sends it to all of the receivers.
	 */
	public void doSearchAsync(String query)
	{
		executingQuery = true;
		new AsyncClientDoSearch().execute(myClient,query,this);
	}

	/*
	 * This method sends a search query using the local client, and sends it to all of the receivers.
	 */
	public void getPopular(String period)
	{
		executingQuery = true;
		new AsyncClientGetPopular().execute(myClient,this, period);
	}
	/*
	 * This method sends a search query using the local client, and sends it to all of the receivers.
	 */
	public int getSongUrl(Song s)
	{
		executingQuery = true;
		new AsyncClientGetPlayURL().execute(myClient,s,this);
		return Integer.parseInt(s.getSongID());
	}
	
	/*STATIC Methods And Classes below here*/
	
	/*
	 * This method sends a search query using supplied context, and sends it to the supplied receiver.
	 */
	public static void getNewClassAsync(GrooveApiAsyncReceiver receiver, Context theContext)
	{
		new AsyncClientInit().execute(theContext,receiver);
	}
	/*
	 * This method sends a search query using supplied context, and client and sends it to the supplied receiver.
	 */
	public static void doSearchAsync(GrooveApiAsyncReceiver receiver, String query, Client theClient)
	{
		new AsyncClientDoSearch().execute(theClient,query,receiver);
	}
	
	/*
	 * This method sends a popular query using supplied context, and client and sends it to the supplied receiver.
	 */
	public static void getPopular(GrooveApiAsyncReceiver receiver, Client theClient, String period)
	{
		new AsyncClientGetPopular().execute(theClient,receiver, period);
	}
	
	/*
	 * This method requests a song for a given song using the supplied client, and the result is sent to the supplied receiver.
	 * This method returns the unique song ID for call tracking purposes.
	 */
	public static int getSongUrl(GrooveApiAsyncReceiver receiver, Song s , Client theClient)
	{
		new AsyncClientGetPlayURL().execute(theClient,s,receiver);
		return Integer.parseInt(s.getSongID());
	}
	
	private static class AsyncClientGetPopular extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object playlist = ((Client)arg[0]).getPopularSongs((String)arg[2]);
	    	Object[] retval = {playlist,arg[0],arg[1]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Playlist playlist = (Playlist)result[0];
	        ((GrooveApiAsyncReceiver)result[2]).recievePlaylistAsync(playlist);
	    }
	}
	private static class AsyncClientGetPlayURL extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object string = ((Client)arg[0]).GetPlayURL((Song) arg[1]);
	    	Object[] retval = {string,arg[1],arg[2]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	        ((GrooveApiAsyncReceiver)result[2]).recievePlayURL(((Song)result[1]), (String) result[0]);
	    }
	}
	private static class AsyncClientDoSearch extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object playlist = ((Client)arg[0]).doSearch((String)arg[1]);
	    	Object[] retval = {playlist,arg[0],arg[2]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Playlist playlist = (Playlist)result[0];
	        ((GrooveApiAsyncReceiver)result[2]).recievePlaylistAsync(playlist);
	    }
	}
	private static class AsyncClientInit extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object client = null;
	    	try
	    	{
	    	client = new Client((Context) arg[0]);
	    	} catch (Exception ex) {}
	    	Object[] retval = {client, arg[1]};
	        return  retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Client client = (Client)result[0];
	        ((GrooveApiAsyncReceiver)result[1]).recieveClientAsync(client);
	    }
	}
	@Override
	public void recieveClientAsync(Client c) {
		myClient = c;
		for (GrooveApiAsyncReceiver rec : receivers) 
		if (rec != null)
			rec.recieveClientAsync(c);
		executingQuery = false;
	}
	@Override
	public void recievePlaylistAsync(Playlist pl) {
		for (GrooveApiAsyncReceiver rec : receivers) 
		if (rec != null)
			rec.recievePlaylistAsync(pl);
		executingQuery = false;
	}
	@Override
	public void recievePlayURL(Song theSong, String url) {
		for (GrooveApiAsyncReceiver rec : receivers) 
		if (rec != null)
			rec.recievePlayURL(theSong, url);
		executingQuery = false;
	}
}