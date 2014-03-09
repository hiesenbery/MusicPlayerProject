package com.hiesenbery.musicplayerproject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SonglistActivity extends ListActivity {

	ArrayList<HashMap<String, String>> songlist = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_songlist);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.songlist, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case R.id.back:
			finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void init() {

		songlist = MainActivity.getSonglist();

		for(int i = 0;i < songlist.size();i++) {
			
			Log.d("Songlist Activity", "Song path: " + songlist.get(i).get("path"));
			Log.d("Songlist Activity", "Song title: " + songlist.get(i).get("title"));
			Log.d("Songlist Activity", "Song album: " + songlist.get(i).get("album"));
			Log.d("Songlist Activity", "\n");
			
		}
		
		ListView lsView = getListView();
		ListAdapter adapter = new SimpleAdapter(this, songlist,
				R.layout.songlist_item, new String[] { "title", "album" },
				new int[] { R.id.songTitle, R.id.songAlbum });
		setListAdapter(adapter);
		lsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				Log.d("Songlist Activity", "Position= " + position);
				i.putExtra("index", position);
				setResult(100, i);
				finish();
			}

		});
	}

}
