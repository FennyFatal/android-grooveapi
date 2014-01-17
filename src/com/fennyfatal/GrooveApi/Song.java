package com.fennyfatal.GrooveApi;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
	String Name;
	String SongID; 
	String EstimateDuration; 
	String Flags; 
	String IsLowBitrateAvailable; 
	String IsVerified; 
	String Popularity; 
	String ArtistName; 
	String ArtistID; 
	String AlbumName; 
	String AlbumID; 
	String Year; 
	String CoverArtFilename; 
	String TrackNum; 
	String AvgDailyWeight; 
	String NumPlaysMonth;
	
	@Override
	public String toString() {
		return ArtistName+" - "+Name; 
	}

	public Song(String name, String songID, String estimateDuration, String flags,
			String isLowBitrateAvailable, String isVerified, String popularity,
			String artistName, String artistID, String albumName, String albumID,
			String year, String coverArtFilename, String trackNum,
			String avgDailyWeight, String numPlaysMonth) {
		Name = name;
		SongID = songID;
		EstimateDuration = estimateDuration;
		Flags = flags;
		IsLowBitrateAvailable = isLowBitrateAvailable;
		IsVerified = isVerified;
		Popularity = popularity;
		ArtistName = artistName;
		ArtistID = artistID;
		AlbumName = albumName;
		AlbumID = albumID;
		Year = year;
		CoverArtFilename = coverArtFilename;
		TrackNum = trackNum;
		AvgDailyWeight = avgDailyWeight;
		NumPlaysMonth = numPlaysMonth;
	}

	public static Song songFromJSONObject(JSONObject Jsong) throws JSONException
	{
		return new Song(Jsong.optString("SongName",Jsong.optString("Name",null)),
				Jsong.optString("SongID",null),
				Jsong.optString("EstimateDuration",null),
				Jsong.optString("Flags",null),
				Jsong.optString("IsLowBitrateAvailable",null),
				Jsong.optString("IsVerified",null),
				Jsong.optString("PopularityIndex",null),
				Jsong.optString("ArtistName",null),
				Jsong.optString("ArtistID",null),
				Jsong.optString("AlbumName",null),
				Jsong.optString("AlbumID",null),
				Jsong.optString("Year",null),
				Jsong.optString("CoverArtFilename",null),
				Jsong.optString("TrackNum",null),
				Jsong.optString("AvgDailyWeight",Jsong.optString("Weight",null)),
				Jsong.optString("NumPlaysMonth",Jsong.optString("NumPlays",null)));	
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSongID() {
		return SongID;
	}
	public void setSongID(String songID) {
		SongID = songID;
	}
	public String getEstimateDuration() {
		return EstimateDuration;
	}
	public void setEstimateDuration(String estimateDuration) {
		EstimateDuration = estimateDuration;
	}
	public String getFlags() {
		return Flags;
	}
	public void setFlags(String flags) {
		Flags = flags;
	}
	public String getIsLowBitrateAvailable() {
		return IsLowBitrateAvailable;
	}
	public void setIsLowBitrateAvailable(String isLowBitrateAvailable) {
		IsLowBitrateAvailable = isLowBitrateAvailable;
	}
	public String getIsVerified() {
		return IsVerified;
	}
	public void setIsVerified(String isVerified) {
		IsVerified = isVerified;
	}
	public String getPopularity() {
		return Popularity;
	}
	public void setPopularity(String popularity) {
		Popularity = popularity;
	}
	public String getArtistName() {
		return ArtistName;
	}
	public void setArtistName(String artistName) {
		ArtistName = artistName;
	}
	public String getArtistID() {
		return ArtistID;
	}
	public void setArtistID(String artistID) {
		ArtistID = artistID;
	}
	public String getAlbumName() {
		return AlbumName;
	}
	public void setAlbumName(String albumName) {
		AlbumName = albumName;
	}
	public String getAlbumID() {
		return AlbumID;
	}
	public void setAlbumID(String albumID) {
		AlbumID = albumID;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getCoverArtFilename() {
		return CoverArtFilename;
	}
	public void setCoverArtFilename(String coverArtFilename) {
		CoverArtFilename = coverArtFilename;
	}
	public String getTrackNum() {
		return TrackNum;
	}
	public void setTrackNum(String trackNum) {
		TrackNum = trackNum;
	}
	public String getAvgDailyWeight() {
		return AvgDailyWeight;
	}
	public void setAvgDailyWeight(String avgDailyWeight) {
		AvgDailyWeight = avgDailyWeight;
	}
	public String getNumPlaysMonth() {
		return NumPlaysMonth;
	}
	public void setNumPlaysMonth(String numPlaysMonth) {
		NumPlaysMonth = numPlaysMonth;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	 public static final Parcelable.Creator<Song> CREATOR =
			   new Parcelable.Creator<Song>(){

			    @Override
			    public Song createFromParcel(Parcel source) {
			     return new Song(source);
			    }

			    @Override
			    public Song[] newArray(int size) {
			     return new Song[size];
			    }
		};
			 
	
	private Song(Parcel in)
	{
		Name = in.readString();
		SongID = in.readString();
		EstimateDuration = in.readString();
		Flags = in.readString();
		IsLowBitrateAvailable = in.readString();
		IsVerified = in.readString();
		Popularity = in.readString();
		ArtistName = in.readString();
		ArtistID = in.readString();
		AlbumName = in.readString();
		AlbumID = in.readString();
		Year = in.readString();
		CoverArtFilename = in.readString();
		TrackNum = in.readString();
		AvgDailyWeight = in.readString();
		NumPlaysMonth = in.readString();	
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {	
		out.writeString(Name);
		out.writeString(SongID);
		out.writeString(EstimateDuration);
		out.writeString(Flags);
		out.writeString(IsLowBitrateAvailable);
		out.writeString(IsVerified);
		out.writeString(Popularity);
		out.writeString(ArtistName);
		out.writeString(ArtistID);
		out.writeString(AlbumName);
		out.writeString(AlbumID);
		out.writeString(Year);
		out.writeString(CoverArtFilename);
		out.writeString(TrackNum);
		out.writeString(AvgDailyWeight);
		out.writeString(NumPlaysMonth);

	}
}
