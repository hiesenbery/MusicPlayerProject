package com.hiesenbery.musicplayerproject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import android.graphics.Bitmap;
import android.os.Environment;

public class SonglistManager {

	private ArrayList<HashMap<String, String>> songlist = new ArrayList<HashMap<String, String>>();

	public SonglistManager() {

		scanDirectory(new File(Environment.getExternalStorageDirectory().getPath()));
		addSongMetadata();
		
	}
	
	public void scanDirectory(File directory) {
		
		File filelist[] = directory.listFiles();
		
		if (filelist != null) {
			for (int i = 0; i < filelist.length; i++) {
				if (filelist[i].isDirectory()) {
					scanDirectory(filelist[i]);
				} else {
					if (filelist[i].getName().endsWith(".MP3")
							|| filelist[i].getName().endsWith(".mp3")) {
						
						HashMap<String, String> song = new HashMap<String, String>();

						song.put("path", filelist[i].getPath());
						songlist.add(song);
					}

				}
			}
			
		}
		
	}
	
	public void addSongMetadata() {

		String artist = null;
		String name = null;
		String title;
		String album = null;
		
		ID3v1 tagv1;
		ID3v2 tagv2;

		for (int i = 0; i < songlist.size(); i++) {

			try {
				
				Mp3File file = new Mp3File(songlist.get(i).get("path"));
				
				if(file.hasId3v1Tag()) {
					
					tagv1 = file.getId3v1Tag();
					name = tagv1.getTitle();
					artist = tagv1.getArtist();
					album = tagv1.getAlbum();
					
				} else if(file.hasId3v2Tag()) {
					tagv2 = file.getId3v2Tag();
					
					name = tagv2.getTitle();
					artist = tagv2.getArtist();
					album = tagv2.getAlbum();
				}
				
				title = artist + " - " + name;
				songlist.get(i).put("title", title);
				songlist.get(i).put("album", album);
				
			} catch (UnsupportedTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public Bitmap getAlbumArt(String path) {

		Bitmap songImage = null;

		return songImage;
	}

	public ArrayList<HashMap<String, String>> getSonglist() {
		return songlist;
	}
	
	public class FileExtensionFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return filename.endsWith(".mp3") || filename.endsWith(".MP3");
		}

	}

}
