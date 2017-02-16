package com.ephod.morpheus;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaMetadataRetriever;
import android.os.Environment;

public class SongsManager {

	final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath(); //new String("/sdcard/");
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
	
	
	public SongsManager(){
		
	}
	
	public ArrayList<HashMap<String, String>> getSongs(){
		File home = new File(MEDIA_PATH);
		
		 if (home.listFiles(new FileExtensionFilter()).length > 0) {
	            for (File file : home.listFiles(new FileExtensionFilter())) {
	                HashMap<String, String> song = new HashMap<String, String>();
	                metaRetriever.setDataSource(file.getPath());
	                song.put("songTitle", metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
	                song.put("songPath", metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
	 
	                // Adding each song to SongList
	                songsList.add(song);
	            }
	        }
	        // return songs list array
	  return songsList;
	}
	
	
	class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".MP4") || name.endsWith(".mp4") || name.endsWith(".wma") || name.endsWith(".WMA") || name.endsWith(".OGG") || name.endsWith(".ogg"));
        }
    }
	
	
	/*String[] proj = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE };
String selection = MediaStore.Audio.Media.DATA +" NOT LIKE '%/music_folder/%'";
        musiccursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj, selection, null, null);

music_column_index=musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
music_column_index = musiccursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);*/
	
}


