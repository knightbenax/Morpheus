package com.ephod.morpheus;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ephod.morpheus.MorpheusService.MusicBinder;
import com.ephod.morpheus.util.SystemUiHider;
import com.ephod.morpheus.utils.TunnelPlayerWorkaround;
import com.ephod.morpheus.visualizer.VisualizerView;
import com.ephod.morpheus.visualizer.renderer.DotsRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

//import com.

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class NowPlayingActivity extends AppCompatActivity implements OnSeekCompleteListener, CircularSeekBar.OnCircularSeekBarChangeListener {
	WakeLock wakeLock;
	MediaMetadataRetriever metaRetriever;
    MediaMetadataRetriever album_artRetriever;
    public VisualizerView mVisualizerView;

	byte[] art;
    byte[] single_art;
	ImageView album_art;
    TextView album, artist, genre, song;
    LinearLayout playlistguy, libraryguy;
    ListView library;
    Button playbutton;
    RelativeLayout parent;
    int width, height;
    String filename = Environment.getExternalStorageDirectory().getPath() + "/Eargasm/01 Heaven.mp3";
    boolean isPrepared = false;
	
    Bitmap pausebg;
	public BitmapDrawable pausebgdrawable;
	Bitmap playbg;
	BitmapDrawable playbgdrawable;


    int px = 0, pxx = 0;
    int mediaPos;
	int mediaMax;
	//private SlidingMenu slidingMenu;
	
	public Handler handler = new Handler();

		
	public ArrayList<Song> songList;
	private MorpheusService musicSrv;
	private Intent playIntent;
	private boolean musicBound = false;
	View rootView;

    private CircularSeekBar roundSeekBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_now_playing);
		//setBehindContentView(R.layout.musiclibary);
		
		rootView = getWindow().getDecorView();
		
		pausebg = BitmapFactory.decodeResource(getResources(), R.drawable.pausebutton);
		pausebgdrawable = new BitmapDrawable(pausebg);
		
		playbg = BitmapFactory.decodeResource(getResources(), R.drawable.playbutton);
		playbgdrawable = new BitmapDrawable(playbg);
		
		stuffToSetUp();
		//hideBar();


		
	    //setVolumeControlStream(AudioManager.STREAM_MUSIC);
	    //PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
	    //wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Morpheus");
	    //createSlidingMenu();
	    songList = new ArrayList<Song>();
	    getSongList();

	    //arrange them alphabetically
	    Collections.sort(songList, new Comparator<Song>(){
	    	  public int compare(Song a, Song b){
	    	    return a.getTitle().compareTo(b.getTitle());
	    	  }
	    });	    
	    
	    //setListHolderShadow();
	    //setListHolderShadowII();
	    //ListAdapter adapter = new SimpleAdapter(this, songsList, R.layout.album_item, new String[] { "songTitle" }, new int[] { R.id.listablumname });
        //library.setAdapter(adapter);
        
        SongAdapter songAdt = new SongAdapter(this, songList);
        //library.setAdapter(songAdt);
        
        /*library.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				// TODO Auto-generated method stub
				songPicked(view, position);
			}
        	
        	
        });*/

        createBackground();
		settingStatusBarTransparent();
		//initialize();
	}

	private void settingStatusBarTransparent() {

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			Window w = getWindow(); // in Activity's onCreate() for instance
			//w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}

		//Apparently, Android for some weird reason ignores all xml values for styles when you set only one programmatically
		//Screw you, Google!
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
			//getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
			getWindow().setStatusBarColor(Color.TRANSPARENT);
			//setStatusBarTranslucent(true);
		}
	}

    private void initTunnelPlayerWorkaround() {
        // Read "tunnel.decode" system property to determine
        // the workaround is needed
        if (TunnelPlayerWorkaround.isTunnelDecodeEnabled(this)) {
            musicSrv.mSilentPlayer = TunnelPlayerWorkaround.createSilentMediaPlayer(this);
        }
    }


    // Methods for adding renderers to visualizer
    public void addDotsGraphRenderers()
    {
        Paint paint = new Paint();
        //paint.setStrokeWidth(50f);
        paint.setAntiAlias(true);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setAlpha(30);
        DotsRenderer barGraphRendererBottom = new DotsRenderer(40, paint, false);
        mVisualizerView.addRenderer(barGraphRendererBottom);
    }

    private void createBackground(){
        Bitmap finalBg_don_dada = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas drawBagger = new Canvas(finalBg_don_dada);
        Paint p = new Paint();

        p.setStyle(Paint.Style.FILL);
		//p.setAlpha(45);
        p.setColor(Color.BLACK);
        //p.setAlpha(45);
        float horizontal = (float)(width)/(px) + 1;
        float vertical = (float)(height)/(px) + 1;
        int length = (int)((horizontal) * (vertical));
        int y = 0;

        Log.e("X", String.valueOf(length));
        int x = 0, z = 0;
        int pq = 0, py = 0;
        album_artRetriever = new MediaMetadataRetriever();
        while(y < length){

            Random r = new Random();
            int i1 = r.nextInt((songList.size() - 1)); //r.nextInt((songList.size() - 1) - 0) + 0

            Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songList.get(i1).getID());
            album_artRetriever.setDataSource(this, trackUri); //Environment.getExternalStorageDirectory().getPath() +

            if(album_artRetriever.getEmbeddedPicture() != null){
                single_art = album_artRetriever.getEmbeddedPicture();
                Bitmap curImage = BitmapFactory.decodeByteArray(single_art, 0, single_art.length);
                if(curImage != null){
                    int dstWidth = px, dstHeight = px;


                    Bitmap m = Bitmap.createScaledBitmap(curImage, dstWidth, dstHeight, false);

                    x = pq * px;


                    if(pq >= horizontal){
                        x = 0;
                        pq = 0;
                        py++;
                    } else {
                        z = py * px;
                    }

                    if(py >= vertical){
                      //z = 0;
                       py = 0;
                    }

                    pq++;

                    //Log.e("PK", String.valueOf(x));
                    //Log.e("PO", String.valueOf(z));
                    drawBagger.drawBitmap(m, x, z, p);
                    //Log.e("UY", String.valueOf(y));
                    y++;
                }
            }
        }

        BitmapDrawable bitmapDrawable = new BitmapDrawable(finalBg_don_dada);
        parent.setBackgroundDrawable(bitmapDrawable);
    }
	
	@Override
	protected void onStart() {
	  super.onStart();
	  if(playIntent == null){
		  playIntent = new Intent(this, MorpheusService.class);
		  bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
		  startService(playIntent);
		  //Log.e("Crap", "Stuff");
	  }
	  
	}
	
	
	public void songPicked(View view, int songposition){
		//musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
		musicSrv.setSong(songposition);
		getSongDetails(songList.get(songposition).getID());
		playbutton.setBackground(pausebgdrawable);
		handler.postDelayed(moveSeekBarThread, 100);
		musicSrv.playSong();
	}
	
	
	@Override
	protected void onDestroy() {
		stopService(playIntent);
	  	musicSrv = null;
	  	super.onDestroy();
	}
	
	
	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection(){
	 
	  @Override
	  public void onServiceConnected(ComponentName name, IBinder service) {
	    MusicBinder binder = (MusicBinder)service;
	    //get service
	    musicSrv = binder.getService();
	    //pass list
	    musicSrv.setList(songList);
	    musicSrv.mother = NowPlayingActivity.this;
	    initialize();
	    getSongDetails(songList.get(musicSrv.songPosn).getID());
	    musicBound = true;
	  }
	 
	  @Override
	  public void onServiceDisconnected(ComponentName name) {
	    musicBound = false;
	  }
	};
	
	public void getSongList() {
		  //retrieve song info
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		
		if(musicCursor!=null && musicCursor.moveToFirst()){
			  //get columns
			  //if (MediaStore.Audio.Media.IS_ALARM.){
				  int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
				  int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
				  int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
				  int albumColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM);
				  //add songs to list
				  do {
					  long thisId = musicCursor.getLong(idColumn);
					  String thisTitle = musicCursor.getString(titleColumn);
					  String thisArtist = musicCursor.getString(artistColumn);
					  String thisAlbum = musicCursor.getString(albumColumn);
					  songList.add(new Song(thisId, thisTitle, thisArtist, thisAlbum));
				  }
			  //}
			  
			  while (musicCursor.moveToNext());
		}
	}
	
	
	/*private void createSlidingMenu(){
		slidingMenu = getSlidingMenu();//new SlidingMenu(this);
		
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setSecondaryMenu(R.layout.playlist);
        slidingMenu.setTouchmodeMarginThreshold(200);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN) ;


		//slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.85f);
        //slidingMenu.setSecondaryShadowDrawable(R.drawable.slidingmenu_shadow_right);
        //slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //slidingMenu.setMenu(R.layout.playlist);
        
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);
	} */
	
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}

    @Override
    protected void onResume()
    {
        super.onResume();
        initTunnelPlayerWorkaround();
        //init();
    }
	 
	@Override
	public void onPause(){
	    super.onPause();
        if(musicSrv != null){
            if(musicSrv.player != null){
                if(musicSrv.player.isPlaying()){
                    //Pause();
                    //isTuning = false;
                }
                if(isFinishing()){
                    //track.dispose();
                    finish();
                }
            } else{
                if(isFinishing()){
                    finish();
                }
            }
        }

	}
	
	public void initialize(){
		mediaPos = musicSrv.player.getCurrentPosition();
		mediaMax = musicSrv.player.getDuration();

		roundSeekBar.setMax(mediaMax); // Set the Maximum range of the
        roundSeekBar.setProgress(mediaPos);// set current progress to song's

		handler.removeCallbacks(moveSeekBarThread);
		handler.postDelayed(moveSeekBarThread, 100); 
		
		//mp.setOnCompletionListener(this);
		//mp.setOnPreparedListener(this);
		musicSrv.player.setOnSeekCompleteListener((OnSeekCompleteListener)this);
        roundSeekBar.setProgress(0);
        roundSeekBar.setOnSeekBarChangeListener(this);
	}
	
	public void Play(){
		musicSrv.playSong();
		playbutton.setBackground(pausebgdrawable);
		handler.postDelayed(moveSeekBarThread, 100); 
	}
	
	public void Pause(){
		musicSrv.player.pause();
		playbutton.setBackground(playbgdrawable);
	}

    @Override
    public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
        // TODO Insert your code here
        if(fromUser){
            musicSrv.player.seekTo(progress);
            roundSeekBar.setProgress(progress);
        }
    }

    @Override
    public void onStopTrackingTouch(CircularSeekBar circularSeekBar){

    }

    @Override
    public void onStartTrackingTouch(CircularSeekBar circularSeekBar){

    }
	
	public void PlayNext(View v){
		
		musicSrv.playNext();
		handler.postDelayed(moveSeekBarThread, 100);
		playbutton.setBackground(pausebgdrawable);
		getSongDetails(songList.get(musicSrv.songPosn).getID());
	}
	
	public void PlayPrevious(View v){
		
		musicSrv.playPrev();
		handler.postDelayed(moveSeekBarThread, 100);
		playbutton.setBackground(pausebgdrawable);
		getSongDetails(songList.get(musicSrv.songPosn).getID());
	}
	
	
	public void PlayPause(View v){
		if (musicSrv.player.isPlaying()){
			Pause();
		} else {
			Play();
			//musicSrv.playSong();
		}		
	}
	
	
	public Runnable moveSeekBarThread = new Runnable() {
	    public void run() {
	           if(musicSrv.player.isPlaying()){
	        	   	//hideBar();
	            	int mediaPos_new = musicSrv.player.getCurrentPosition();
	            	int mediaMax_new = musicSrv.player.getDuration();
	            	roundSeekBar.setMax(mediaMax_new);
                    roundSeekBar.setProgress(mediaPos_new);

	            	handler.postDelayed(this, 100); //Looping the thread after 0.1 second
	                                            // seconds
	           }

	    }
	};
	
	
	public void hideBar(){
		if (rootView.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LOW_PROFILE){
			rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}
	
	
	public void stuffToSetUp(){
		
		//WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    	//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    	
    	//layoutParams.screenBrightness = 1f;
    	//getWindow().setAttributes(layoutParams);
        mVisualizerView = (VisualizerView) findViewById(R.id.visualizerView);
    	album_art = (ImageView) findViewById(R.id.albumart);
        album = (TextView) findViewById(R.id.albumname);
        artist = (TextView) findViewById(R.id.artistname);
        song = (TextView) findViewById(R.id.songname);
        parent = (RelativeLayout) findViewById(R.id.parentholder);
        library = (ListView) findViewById(R.id.library);
        playbutton = (Button) findViewById(R.id.playbutton);
        libraryguy = (LinearLayout)findViewById(R.id.libraryholder);
        roundSeekBar = (CircularSeekBar)findViewById(R.id.holoCircularProgressBar);

        Point outSize =  new Point();
        getWindowManager().getDefaultDisplay().getSize(outSize);
		height = outSize.y;
		width = outSize.x;

        Resources r = getResources();
        px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
        pxx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 255, r.getDisplayMetrics());
        //genre = (TextView) findViewById(R.id.genre);		
	}
	
	public void showAbout(View v){
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	@SuppressWarnings("deprecation")
	public void setListHolderShadow(){
		playlistguy = (LinearLayout)findViewById(R.id.listholder);
		//int width;
		int[] color = new int[]{Color.parseColor("#111111"), Color.parseColor("#000000")};
		//float[] colorpositions = new float[]{0.0f, 0.2f, 0.4f};
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		/*create the gradient*/
		//LinearGradient grad = new LinearGradient(0, 0, 0, canvas.getHeight(), color, null, TileMode.CLAMP);
		LinearGradient grad = new LinearGradient(0, 0, 9, 0, color, null, TileMode.CLAMP);
		
		/*draw your gradient to the top of your bitmap*/
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setAlpha(255);
		p.setShader(grad);
		
		//canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
		canvas.drawRect(0, 0, 9, height, p);
				
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		playlistguy.setBackgroundDrawable(bitmapDrawable);
	}
	
	public void setListHolderShadowII(){

		//int width;
		int[] color = new int[]{Color.parseColor("#000000"), Color.parseColor("#111111")};
		//float[] colorpositions = new float[]{0.0f, 0.2f, 0.4f};
		int newwidth = (width);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		/*create the gradient*/
		//LinearGradient grad = new LinearGradient(0, 0, 0, canvas.getHeight(), color, null, TileMode.CLAMP);
		LinearGradient grad = new LinearGradient((newwidth - 9), 0, newwidth, 0, color, null, TileMode.CLAMP);
		
		/*draw your gradient to the top of your bitmap*/
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setAlpha(255);
		p.setShader(grad);
		
		//canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
		canvas.drawRect((newwidth - 9), 0, newwidth, height, p);
				
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		libraryguy.setBackgroundDrawable(bitmapDrawable);
	}
	
	//@SuppressWarnings("deprecation")
	public void changeBackgroundImage(Bitmap image){
		Drawable[] layers = new Drawable[3];
		Bitmap m;// = image;
		
		int mWidth = image.getWidth();
		int mHeight = image.getHeight();
		
		float ratio = mHeight/mWidth;
		
		int dstWidth, dstHeight;
		
		dstHeight = height;
		//dstWidth = width;
		//dstHeight = (int) (dstWidth * ratio);
		dstWidth = (int) (dstHeight * ratio);
		
		m = setEffect(this, image);
		m = Bitmap.createScaledBitmap(m, dstWidth, dstHeight, true);
		
		GradientDrawable blackbg = new GradientDrawable();
		blackbg.setShape(GradientDrawable.RECTANGLE);
		blackbg.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		blackbg.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
		blackbg.setColors(new int[]{Color.parseColor("#000000"), Color.parseColor("#000000")});
		layers[0] = blackbg;
		
		BitmapDrawable bitmapDrawable = new BitmapDrawable(m);
		bitmapDrawable.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		layers[1] = bitmapDrawable;
		
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.RECTANGLE);
		gd.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		gd.setOrientation(GradientDrawable.Orientation.BOTTOM_TOP);
		gd.setColors(new int[]{Color.parseColor("#80000000"), Color.parseColor("#ff000000")});
		layers[2] = gd;
		
		LayerDrawable bck = new LayerDrawable(layers);
		
		/*LayerDrawable layers = (LayerDrawable)this.getResources().getDrawable(R.drawable.nowplayingbg);
		BitmapDrawable bg = (BitmapDrawable) (layers.findDrawableByLayerId(R.id.appbackgroundimage));
		bg = new BitmapDrawable(this.getResources(), image);*/
		//bg.
		parent.invalidate();
		parent.setBackgroundColor(Color.BLACK);
		parent.setBackgroundDrawable(bck);
		//parent.setBackgroundColor(Color.BLUE);
		
	}
	
	
	
	
	public static Bitmap setEffect(Context context, Bitmap bmp){
		int[] color = new int[]{Color.parseColor("#ff000000"), Color.parseColor("#80000000"), Color.parseColor("#00000000")};
		//float[] colorpositions = new float[]{0.0f, 0.2f, 0.4f};
		
		Bitmap bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(bitmap);
		/*create the gradient*/
		//LinearGradient grad = new LinearGradient(0, 0, 0, canvas.getHeight(), color, null, TileMode.CLAMP);
		LinearGradient grad = new LinearGradient(0, 0, 0, 100, color, null, TileMode.CLAMP);
		
		/*draw your gradient to the top of your bitmap*/
		Paint p = new Paint();
		p.setStyle(Style.FILL);
		p.setAlpha(255);
		p.setShader(grad);
		
		//canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
		canvas.drawRect(0, 0, canvas.getWidth(), 100, p);
		
		return bitmap;		
	}
	
	public Bitmap addVinyl(Bitmap bmp, Resources r){
		Bitmap vinyl = BitmapFactory.decodeResource(r, R.drawable.vinylwide);
		Bitmap cs = Bitmap.createBitmap(251, 180, Bitmap.Config.ARGB_8888);
		
		Bitmap newBmp = Bitmap.createScaledBitmap(bmp, 180, 180, true);
		Bitmap newVinyl = Bitmap.createScaledBitmap(vinyl, 71, 180, true);
		
		Canvas comboImage = new Canvas(cs);
		
		
		comboImage.drawBitmap(newBmp, 0f, 0f, null);
		comboImage.drawBitmap(newVinyl, 180, 0f, null);
		
		return cs;
	} 
	
	public void getSongDetails(long songId){
		metaRetriever = new MediaMetadataRetriever();
		Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId);
		metaRetriever.setDataSource(this, trackUri); //Environment.getExternalStorageDirectory().getPath() +
		
		try{
			art = metaRetriever.getEmbeddedPicture();
			Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
						
			song.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
			album.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
			artist.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

			if(song.getText().toString().equals("")){
				song.setText("Unknown Song");
			}

			if(album.getText().toString().equals("")){
				album.setText("Unknown Album");
			}

			if(artist.getText().toString().equals("")){
				artist.setText("Unknown Artist");
			}

			
			//changeBackgroundImage(songImage); //change backgroundimagefirst
			//songImage = addVinyl(songImage, getResources());
			songImage = Bitmap.createScaledBitmap(songImage, pxx, pxx, true); //167, 120,
			album_art.invalidate();
			album_art.setImageBitmap(songImage);
            roundSeekBar.image = songImage;
            roundSeekBar.invalidate();
			
		} catch (Exception e){
			song.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
			album.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
			artist.setText(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

			if(song.getText().toString().equals("")){
				song.setText("Unknown Song");
			}

			if(album.getText().toString().equals("")){
				album.setText("Unknown Album");
			}

			if(artist.getText().toString().equals("")){
				artist.setText("Unknown Artist");
			}

			Bitmap songImage = BitmapFactory.decodeResource(getResources(), R.drawable.albumartnone);
			
			//changeBackgroundImage(songImage); //change backgroundimagefirst
			//songImage = addVinyl(songImage, getResources());
			songImage = Bitmap.createScaledBitmap(songImage, pxx, pxx, true); //167, 120,
			album_art.invalidate();
			album_art.setImageBitmap(songImage);
            roundSeekBar.image = songImage;
            roundSeekBar.invalidate();
			
			//album_art.setBackgroundColor(Color.GRAY);
			//album.setText(e.getMessage());
			//artist.setText("Unknown Artist");
		}
	}


	@Override
	public void onSeekComplete(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}
}
