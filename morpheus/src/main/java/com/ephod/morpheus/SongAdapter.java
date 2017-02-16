package com.ephod.morpheus;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongAdapter extends BaseAdapter {

	private ArrayList<Song> songs;
	private LayoutInflater songInf;
	Context y;
	Song currSong;
	ViewHolder holder;
	public ImageLoader imageLoader;
	
	public SongAdapter(Context c, ArrayList<Song> theSongs){
		  songs=theSongs;
		  songInf=LayoutInflater.from(c);
		  y = c;
		  //imageLoader = new ImageLoader(c);
	}
	
	@Override
	public int getCount() {
	  return songs.size();
	}
	 
	  @Override
	  public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return null;
	  }
	  
	  
	  
	  @Override
	  public View getView (int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    if(rowView == null){
	    	rowView = songInf.inflate(R.layout.album_item, parent, false);
			holder = new ViewHolder();
			//get title and artist views
		    holder.mName = (CustomTextView)rowView.findViewById(R.id.listablumname);
		    holder.mArtist = (CustomTextViewBold)rowView.findViewById(R.id.listablumartist);
		    holder.cover = (ImageView)rowView.findViewById(R.id.listalbumcover);
			rowView.setTag(holder);
		} //else {
			//holder.loader.cancel();
		//}
		
		//fill data
		holder = (ViewHolder)rowView.getTag();
		currSong = songs.get(position);
	    //get title and artist strings
	    holder.mName.setText(currSong.getTitle());    
	    if(currSong.getArtist().equals("<unknown>")){
	    	holder.mArtist.setText("Unknown");
	    } else {
	    	holder.mArtist.setText(currSong.getArtist());
	    }
	    
	    //holder.loader = new ImageLoader();
        //holder.loader.execute();
	    
	    /*long thisId = currSong.getID();
	    Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
	    
	    MediaMetadataRetriever mR = new MediaMetadataRetriever();
		mR.setDataSource(y, trackUri);
		
		if(mR.getEmbeddedPicture() != null){
			byte[] songByte;
			Bitmap songCover;
			songByte = mR.getEmbeddedPicture();
			songCover = BitmapFactory.decodeByteArray(songByte, 0, songByte.length);
			Bitmap newCover = Bitmap.createScaledBitmap(songCover, 80, 80, false);
			holder.cover.setImageBitmap(newCover);
		} else {
			Bitmap newCover = BitmapFactory.decodeResource(y.getResources(), R.drawable.albumartnone);
			holder.cover.setImageBitmap(newCover);
		}
	    
		mR.release();*/
	    imageLoader = new ImageLoader(y);
	    imageLoader.DisplayImage("", holder.cover, songs.get(position));
		return rowView;
	  }
	  
	  
	  private class ViewHolder {
	    	CustomTextView mName;
	    	CustomTextViewBold mArtist;
	    	ImageLoader loader;
	    	ImageView cover;
	    	int position;
	    }
	 
	  @Override
	  public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return 0;
	  }
	  
	  
	  
	 
	
	 /* class ImageLoader extends AsyncTask {
		    private boolean cancel = false;

		    private Bitmap bitmap;

		    public void cancel() { cancel = true; }

		  

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				 try {
					wait(100);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("e", e.getMessage());
				}
			     if(!cancel) {
			    	long thisId = currSong.getID();
			  	    Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);
			  	    
			  	    MediaMetadataRetriever mR = new MediaMetadataRetriever();
			  		mR.setDataSource(y, trackUri);
			  		
			  		if(mR.getEmbeddedPicture() != null){
			  			byte[] songByte;
			  			Bitmap songCover;
			  			songByte = mR.getEmbeddedPicture();
			  			songCover = BitmapFactory.decodeByteArray(songByte, 0, songByte.length);
			  			Bitmap newCover = Bitmap.createScaledBitmap(songCover, 80, 80, false);
			  			holder.cover.setImageBitmap(newCover);
			  		} else {
			  			Bitmap newCover = BitmapFactory.decodeResource(y.getResources(), R.drawable.albumartnone);
			  			holder.cover.setImageBitmap(newCover);
			  		}
			     }
				return null;
			}
		}*/
	  
	
}	
