package com.fennyfatal.GrooveApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Playlist extends ArrayList<Song> implements Parcelable {
	
	/**
	 * TODO: Add special song related methods here.
	 */
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	    public Playlist createFromParcel(Parcel parcel) {
	        return new Playlist(parcel);
	    }

	    public Playlist[] newArray(int size) {
	        return new Playlist[size];
	    }
	};
	private static final long serialVersionUID = 1L;
	public Playlist()
	{
		super();
	}
	public Playlist(Parcel parcel) {
		super();
		int count = parcel.readInt();
		for (int i =0;i<count;i++)
		{
			this.add((Song)parcel.readParcelable(getClass().getClassLoader()));
		}
	}

	public List<String> toStringList()
	{
		ArrayList<String> templist = new ArrayList<String>();
		for (Song s : this)
			templist.add(s.toString());
		return (List<String>)templist;
	}

	@Override
	public boolean add(Song string) {
		// TODO Auto-generated method stub
		return super.add(string);
	}

	@Override
	public void add(int index, Song string) {
		// TODO Auto-generated method stub
		super.add(index, string);
	}

	@Override
	public boolean addAll(Collection<? extends Song> collection) {
		// TODO Auto-generated method stub
		return super.addAll(collection);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Song> collection) {
		// TODO Auto-generated method stub
		return super.addAll(index, collection);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
	}

	@Override
	public Playlist clone() {
		// TODO Auto-generated method stub
		return (Playlist) super.clone();
	}

	public boolean contains(String string) {
		// TODO Auto-generated method stub
		return super.contains(string);
	}

	@Override
	public void ensureCapacity(int minimumCapacity) {
		// TODO Auto-generated method stub
		super.ensureCapacity(minimumCapacity);
	}

	public boolean equals(String o) {
		// TODO Auto-generated method stub
		return super.equals(o);
	}

	@Override
	public Song get(int index) {
		// TODO Auto-generated method stub
		return super.get(index);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	public int indexOf(String string) {
		// TODO Auto-generated method stub
		return super.indexOf(string);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return super.isEmpty();
	}

	@Override
	public Iterator<Song> iterator() {
		// TODO Auto-generated method stub
		return super.iterator();
	}

	public int lastIndexOf(String string) {
		// TODO Auto-generated method stub
		return super.lastIndexOf(string);
	}

	@Override
	public Song remove(int index) {
		// TODO Auto-generated method stub
		return super.remove(index);
	}

	public boolean remove(String string) {
		// TODO Auto-generated method stub
		return super.remove(string);
	}

	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		super.removeRange(fromIndex, toIndex);
	}

	@Override
	public Song set(int index, Song string) {
		// TODO Auto-generated method stub
		return super.set(index, string);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return super.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] contents) {
		// TODO Auto-generated method stub
		return super.toArray(contents);
	}

	@Override
	public void trimToSize() {
		// TODO Auto-generated method stub
		super.trimToSize();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.size());
		for (Song s : this)
		{
			dest.writeParcelable(s, flags);
		}
	}
	
}
