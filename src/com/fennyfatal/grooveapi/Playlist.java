package com.fennyfatal.GrooveApi;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends ArrayList<Song> {
	
	/**
	 * TODO: Add special song related methods here.
	 */
	private static final long serialVersionUID = 1L;

	public List<String> toStringList()
	{
		ArrayList<String> templist = new ArrayList<String>();
		for (Song s : this)
			templist.add(s.toString());
		return (List<String>)templist;
	}
	
}
