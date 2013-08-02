package com.fennyfatal.GrooveApi;

public interface GrooveApiAsyncReciever {
	public void recieveClientAsync (Client c);
	public void recievePlaylistAsync (Playlist pl);
	public void recievePlayURL(Song theSong,String url);
}