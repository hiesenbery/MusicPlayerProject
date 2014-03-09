package com.hiesenbery.musicplayerproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener, OnCompletionListener,
		OnPreparedListener {

	public static TextView songTitle, duration;
	public static SeekBar seekbar;
	public static ImageView albumArt;
	public static ImageButton playButton, nextButton, previousButton,
			forwardButton, backwardButton;
	private static ArrayList<HashMap<String, String>> songlist = new ArrayList<HashMap<String, String>>();
	private int currentSongIndex;
	private static MusicPlayer mp;
	private SonglistManager songlistManager;
	private static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.playlist:
			startSonglistActivity();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.destroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("index");
			Toast.makeText(this,
					"Request code received!\n" + "Index: " + currentSongIndex,
					Toast.LENGTH_LONG).show();
			mp.playSong(currentSongIndex);

			if (mp.isPlaying()) {
				playButton.setImageDrawable(getResources().getDrawable(
						R.drawable.pause_button));
			}
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						try {
							if (!mp.isPlaying()) {
								break;
							}
							seekbar.setProgress(mp.getCurrentPosition());
							Thread.sleep(27);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}).start();

		} else {
			Toast.makeText(this, "Nothing is received!", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void init() {

		duration = (TextView) findViewById(R.id.duration);
		songTitle = (TextView) findViewById(R.id.textView1);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		albumArt = (ImageView) findViewById(R.id.imageView1);
		backwardButton = (ImageButton) findViewById(R.id.backwardButton);
		previousButton = (ImageButton) findViewById(R.id.previousButton);
		playButton = (ImageButton) findViewById(R.id.playButton);
		nextButton = (ImageButton) findViewById(R.id.nextButton);
		forwardButton = (ImageButton) findViewById(R.id.forwardButton);

		backwardButton.setOnClickListener(this);
		previousButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		forwardButton.setOnClickListener(this);

		albumArt.setImageDrawable(getResources().getDrawable(
				R.drawable.pause_button_art));

		songlistManager = new SonglistManager();
		songlist = songlistManager.getSonglist();
		mp = new MusicPlayer(songlist);
		mp.setOnCompletionListener(this);
		mp.setOnPreparedListener(this);

	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.backwardButton:
			mp.backward();
			break;

		case R.id.previousButton:
			mp.previous();
			break;

		case R.id.playButton:
			mp.play();
			if (mp.isPlaying()) {
				playButton.setImageDrawable(getResources().getDrawable(
						R.drawable.pause_button));
			} else {
				playButton.setImageDrawable(getResources().getDrawable(
						R.drawable.play_button));
			}
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						try {
							if (!mp.isPlaying()) {
								break;
							}
							seekbar.setProgress(mp.getCurrentPosition());
							Thread.sleep(1000 / 60);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}).start();
			
			break;

		case R.id.nextButton:
			mp.next();
			break;

		case R.id.forwardButton:
			mp.forward();
			break;

		}

	}

	/* SeekBar.onProgressChangeListener */

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		Log.d("MainActivity", "Progress= " + progress);
		Log.d("MainActivity", "From= " + String.valueOf(fromUser));

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	/* SeekBar.onProgressChangeListener */

	@Override
	public void onCompletion(MediaPlayer arg0) {
		mp.next();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {

		songTitle.setText(songlist.get(((MusicPlayer) mp).getSongIndex()).get(
				"title"));
		playButton.setImageDrawable(getResources().getDrawable(
				R.drawable.pause_button));
		if (songlistManager.getAlbumArt(((MusicPlayer) mp).getSongPath()) == null) {
			albumArt.setImageDrawable(getResources().getDrawable(
					R.drawable.play_button_art));
		} else {
			albumArt.setImageBitmap(songlistManager
					.getAlbumArt(((MusicPlayer) mp).getSongPath()));
		}
		seekbar.setMax(mp.getDuration());
		duration.setText(converter(mp.getDuration()));
		mp.start();

	}

	public void startSonglistActivity() {
		Intent i = new Intent(getApplicationContext(), SonglistActivity.class);
		startActivityForResult(i, 100);
	}

	public static String converter(int milliseconds) {
		int inSeconds = milliseconds / 1000;

		int minutes = inSeconds / 60;
		int seconds = inSeconds % 60;
		int hours = inSeconds / 3600;

		String hoursText;
		String minutesText;
		String secondsText;

		if (hours == 0) {
			hoursText = "00";
		} else if (hours > 0 && hours < 10) {
			hoursText = "0" + String.valueOf(hours);
		} else {
			hoursText = String.valueOf(hours);
		}

		if (minutes == 0) {
			minutesText = "00";
		} else if (minutes > 0 && minutes < 10) {
			minutesText = "0" + String.valueOf(minutes);
		} else {
			minutesText = String.valueOf(minutes);
		}

		if (seconds == 0) {
			secondsText = "00";
		} else if (seconds > 0 && seconds < 10) {
			secondsText = "0" + String.valueOf(seconds);
		} else {
			secondsText = String.valueOf(seconds);
		}

		if (hours == 0) {
			return minutesText + ":" + secondsText;
		} else {
			return hoursText + ":" + minutesText + ":" + secondsText;
		}

	}

	public static String getCurrentTime() {
		return converter(mp.getCurrentPosition());
	}
	
	public static ArrayList<HashMap<String, String>> getSonglist() {
		return songlist;
	}

	public static synchronized MainActivity getInstace() {

		if (instance == null) {
			instance = new MainActivity();
		}

		return instance;
	}
}
