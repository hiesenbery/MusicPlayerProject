package com.hiesenbery.musicplayerproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaPlayer;

public class MusicPlayer extends MediaPlayer {
	
	private static ArrayList<HashMap<String, String>> songlist = new ArrayList<HashMap<String, String>>();
	
	private int songIndex;
	private int interval = 5000;
	private String path;
	
	private boolean isFirstTimePlaying = true;
	
	public MusicPlayer(ArrayList<HashMap<String, String>> list) {
		songlist = list;
	}
	
	/* Call only when playbutton is pressed by user */
	public void play() {
		
		if(isPlaying()) {
			pauseSong();
		} else {
			
			if(isFirstTimePlaying) {
				
				isFirstTimePlaying = false;
				
				try {
					songIndex = 0;
					path = songlist.get(songIndex).get("path");
					setDataSource(path);
					prepare();
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				resumeSong();
			}
			
		}

	}
	
	/* Call from songlist or within next() and previous() method */
	public void playSong(int index) {
		
		isFirstTimePlaying = false;
		
		try {
			stop();
			reset();
			songIndex = index;
			path = songlist.get(songIndex).get("path");
			setDataSource(path);
			prepare();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void pauseSong() {
		pause();
	}
	
	public void resumeSong() {
		start();
	}
	
	public void next() {
		if(songIndex >= songlist.size() - 1) {
			songIndex = 0;
		} else {
			songIndex += 1;
		}
		playSong(songIndex);
	}
	
	public void previous() {
		
		if(songIndex <= 0) {
			songIndex = 0;
		} else {
			songIndex -= 1;
		}
		playSong(songIndex);
	}
	
	public void forward() {
		seekTo(getCurrentPosition() + interval);
	}
	
	public void backward() {
		seekTo(getCurrentPosition() - interval);
	}
	
	public void destroy() {
		stop();
		release();
	}

	public int getSongIndex() {
		return songIndex;
	}
	
	public String getSongPath() {
		return path;
	}
	
}
