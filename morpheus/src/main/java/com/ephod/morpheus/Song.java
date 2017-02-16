package com.ephod.morpheus;

import android.graphics.Bitmap;

public class Song {

	private long id;
	private String title;
	private String artist;
	private String album;
	private Bitmap albumCover;
	
	public Song(long songID, String songTitle, String songArtist, String songAlbum) {
		  id=songID;
		  title=songTitle;
		  artist=songArtist;
		  album=songAlbum;
		}
	
	
	public long getID(){return id;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}
	public String getAlbum(){return album;}
	public Bitmap getAlbumCover(){return albumCover;}
	
	
	
}
