package com.ephod.morpheus;

import java.util.ArrayList;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.ephod.morpheus.visualizer.VisualizerView;

public class MorpheusService extends Service implements OnPreparedListener, OnErrorListener, OnCompletionListener {

	//media player
	public MediaPlayer player;
    public MediaPlayer mSilentPlayer;  /* to avoid tunnel player issue */

	//song list
	private ArrayList<Song> songs;
	//current position
	public int songPosn;
	public NowPlayingActivity mother;
	private String songTitle="";
	private static final int NOTIFY_ID=1;
    boolean visualizerLinked = false;
    boolean playedBefore = false;
	
	@Override
	public void onCreate(){
	  //create the service
		//create the service
		super.onCreate();
		//initialize position
		songPosn = 0;
		//create player
		player = new MediaPlayer();
		initMusicPlayer();
		
		//Log.e("Crap", "Stuff");
	}
	
	public void setList(ArrayList<Song> theSongs){
		  songs = theSongs;
	}
	
	private final IBinder musicBind = new MusicBinder();
	
	public class MusicBinder extends Binder {
		MorpheusService getService() {
		    return MorpheusService.this;
		}
	}
	
	public void initMusicPlayer(){
		  //set player properties
		player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return musicBind;
	}
	
	@Override
	public boolean onUnbind(Intent intent){
	  player.stop();
	  player.release();
	  return false;
	}
	
	public void playPrev(){
		  songPosn--;
		  if(songPosn < 0) songPosn=songs.size()-1;
		  mother.getSongDetails(mother.songList.get(songPosn).getID());
		  mother.playbutton.setBackground(mother.pausebgdrawable);
		  mother.handler.postDelayed(mother.moveSeekBarThread, 100);
		  playSong();
		}
	
	public void playNext(){
		  songPosn++;
		  if(songPosn >= songs.size()) songPosn=0;
		  mother.getSongDetails(mother.songList.get(songPosn).getID());
		  mother.playbutton.setBackground(mother.pausebgdrawable);
		  mother.handler.postDelayed(mother.moveSeekBarThread, 100);
		  playSong();
		}
	
	public void playSong(){
		//play a song
		player.reset();
		Song playSong = songs.get(songPosn);
		songTitle = playSong.getTitle();
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong);
		try{
		  player.setDataSource(getApplicationContext(), trackUri);
		}
		catch(Exception e){
		  Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync();
        playedBefore = true;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if(player.getCurrentPosition() > 0){
		    mp.reset();
		    if(playedBefore == true){
                playNext();
            }

		  }		
	}
	
	@Override
	public void onDestroy() {
	  stopForeground(true);
	}

	@Override
	public boolean onError(MediaPlayer mp, int arg1, int arg2) {
		// TODO Auto-generated method stub
		mp.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mp.start();

        if(visualizerLinked == false){
            mother.mVisualizerView.link(mp);
            mother.addDotsGraphRenderers();
            visualizerLinked = true;
        }

		Intent notIntent = new Intent(this, NowPlayingActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
		  notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		Notification.Builder builder = new Notification.Builder(this);
		 
		builder.setContentIntent(pendInt)
		  .setSmallIcon(R.drawable.ic_launcher)
		  .setTicker(songTitle)
		  .setOngoing(true)
		  .setContentTitle("Playing")

		  .setContentText(songTitle);
		
		Notification not = builder.build();
		 
		startForeground(NOTIFY_ID, not);
	}
	
	
	public void setSong(int songIndex){
		  songPosn = songIndex;
	}

}
