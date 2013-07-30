package com.fennyfatal.GrooveApi;

import org.json.JSONException;
import org.json.JSONObject;

public class Song {
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
		try	{
		return new Song(Jsong.getString("SongName"),Jsong.getString("SongID"),Jsong.getString("EstimateDuration"),null,Jsong.getString("IsLowBitrateAvailable"),Jsong.getString("IsVerified"),Jsong.getString("PopularityIndex"),Jsong.getString("ArtistName"),Jsong.getString("ArtistID"),Jsong.getString("AlbumName"),Jsong.getString("AlbumID"),Jsong.getString("Year"),Jsong.getString("CoverArtFilename"),Jsong.getString("TrackNum"),null,null);
		} catch (Exception ex) {} // Try to load from search first, and the SongName value will failout first, allowing us to save cycles testing it.
		return new Song(Jsong.getString("Name"),Jsong.getString("SongID"),Jsong.getString("EstimateDuration"),Jsong.getString("Flags"),Jsong.getString("IsLowBitrateAvailable"),Jsong.getString("IsVerified"),Jsong.getString("Popularity"),Jsong.getString("ArtistName"),Jsong.getString("ArtistID"),Jsong.getString("AlbumName"),Jsong.getString("AlbumID"),Jsong.getString("Year"),Jsong.getString("CoverArtFilename"),Jsong.getString("TrackNum"),Jsong.getString("AvgDailyWeight"),Jsong.getString("NumPlaysMonth")); 
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
}
