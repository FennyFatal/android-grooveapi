package com.fennyfatal.GrooveApi;

import android.content.Context;
import android.os.AsyncTask;

/*
 * This needs major refactoring. Input should be sane, and the like.
 *  
 */
public class AsyncClient implements GrooveApiAsyncReciever
{
	boolean initialized;
	Client myClient = null;
	GrooveApiAsyncReciever reciever;
	public AsyncClient(GrooveApiAsyncReciever reciever) {
		this.reciever = reciever;
	}
	public AsyncClient() {
		
	}
	public boolean hasClient()
	{
		return (myClient != null);
	}
	public void getNewClassAsync()
	{
		new AsyncClientInit().execute(reciever,this);
	}
	public void getNewClassAsync(GrooveApiAsyncReciever reciever)
	{
		this.reciever = reciever;
		new AsyncClientInit().execute(reciever,this);
	}
	public void doSearchAsync(String query)
	{
		new AsyncClientDoSearch().execute(myClient,query,this);
	}
	public void getPopular()
	{
		new AsyncClientGetPopular().execute(myClient,this);
	}
	public int getSongUrl(Song s)
	{
		new AsyncClientGetPlayURL().execute(myClient,s,this);
		return Integer.parseInt(s.getSongID());
	}
	
	public void getNewClassAsync(GrooveApiAsyncReciever reciever, Context theContext)
	{
		this.reciever = reciever;
		new AsyncClientInit().execute(theContext,this);
	}
	public void doSearchAsync(GrooveApiAsyncReciever reciever, String query, Client theClient)
	{
		new AsyncClientDoSearch().execute(theClient,query,reciever);
	}
	public void getPopular(GrooveApiAsyncReciever reciever, Client theClient)
	{
		new AsyncClientGetPopular().execute(theClient,reciever);
	}
	public int getSongUrl(GrooveApiAsyncReciever reciever, Song s , Client theClient)
	{
		new AsyncClientGetPlayURL().execute(theClient,s,reciever);
		return Integer.parseInt(s.getSongID());
	}
	class AsyncClientGetPopular extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object playlist = ((Client)arg[0]).getPopularSongs();
	    	Object[] retval = {playlist,arg[0],arg[1]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Playlist playlist = (Playlist)result[0];
	        ((GrooveApiAsyncReciever)result[2]).recievePlaylistAsync(playlist);
	    }
	}
	class AsyncClientGetPlayURL extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object string = ((Client)arg[0]).GetPlayURL((Song) arg[1]);
	    	Object[] retval = {string,arg[1],arg[2]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	        ((GrooveApiAsyncReciever)result[2]).recievePlayURL(((Song)result[1]), (String) result[0]);
	    }
	}
	class AsyncClientDoSearch extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object playlist = ((Client)arg[0]).doSearch((String)arg[1]);
	    	Object[] retval = {playlist,arg[0],arg[2]};
	    	return retval;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Playlist playlist = (Playlist)result[0];
	        ((GrooveApiAsyncReciever)result[2]).recievePlaylistAsync(playlist);
	    }
	}
	class AsyncClientInit extends AsyncTask<Object, Integer, Object[]> {	

		@Override
	    protected Object[] doInBackground(Object... arg) {
	    	Object milk = null;
	    	try
	    	{
	    	milk = new Client((Context) arg[0]);
	    	} catch (Exception ex) {}
	    	Object[] bob = {milk, arg[1]};
	        return  bob;
	    }
		@Override
	    protected void onPostExecute(Object[] result) {
	    	Client client = (Client)result[0];
	        ((GrooveApiAsyncReciever)result[1]).recieveClientAsync(client);
	    }
	}
	@Override
	public void recieveClientAsync(Client c) {
		myClient = c;
		if (reciever != null)
			reciever.recieveClientAsync(c);
	}
	@Override
	public void recievePlaylistAsync(Playlist pl) {
		if (reciever != null)
			reciever.recievePlaylistAsync(pl);
	}
	@Override
	public void recievePlayURL(Song theSong, String url) {
		if (reciever != null)
			reciever.recievePlayURL(theSong, url);
	}
}