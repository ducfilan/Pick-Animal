package com.example.pickanimal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainMenu extends Activity {
	public final static String EXTRA_TYPE = "com.example.bubblegame.MainMenu.TYPE";
	public final static String EXTRA_DIFFICULT = "com.example.bubblegame.MainMenu.DIFFICULT";
	public final static String EXTRA_SOUND = "com.example.bubblegame.MainMenu.SOUND";
	public final static String EXTRA_RESTART = "com.example.bubblegame.MainMenu.RESTART";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		final Spinner spinner = (Spinner) findViewById(R.id.difficultSelect);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(MainMenu.this, R.array.difficult, R.layout.ghost_text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		OnTouchListener gameMode = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						Intent sendGameType = new Intent(MainMenu.this, MainActivity.class);
						if (v.getId() == R.id.countMode) {
							sendGameType.putExtra(EXTRA_TYPE, "count");
						} else {
							sendGameType.putExtra(EXTRA_TYPE, "time");
						}
						sendGameType.putExtra(EXTRA_DIFFICULT, spinner.getSelectedItem().toString());
						finish();
						startActivity(sendGameType);
						break;
					default :
						break;
				}
				return false;
			}
		};
		findViewById(R.id.countMode).setOnTouchListener(gameMode);
		findViewById(R.id.timeMode).setOnTouchListener(gameMode);

		Intent a = getIntent();
		boolean restart = a.getBooleanExtra(MainActivity.EXTRA_RESTART, false);
		String mode = a.getStringExtra(MainActivity.EXTRA_TYPE);
		String difficult = a.getStringExtra(MainActivity.EXTRA_DIFFICULT);
		boolean soundOn = a.getBooleanExtra(MainActivity.EXTRA_SOUND, true);
		if (restart) {
			Intent sendGameType = new Intent(this, MainActivity.class);
			sendGameType.putExtra(EXTRA_TYPE, mode);
			sendGameType.putExtra(EXTRA_DIFFICULT, difficult);
			sendGameType.putExtra(EXTRA_SOUND, soundOn);
			sendGameType.putExtra(EXTRA_RESTART, true);

			finish();
			startActivity(sendGameType);
		}
		Button exit = (Button) findViewById(R.id.quit);
		exit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN :
						finish();
						break;
					default :
						break;
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				 /*
				  * This ID represents the Home or Up button. In the case of this activity, the Up button is shown.
				  * Use NavUtils to allow users to navigate up one level in the application structure. For more details, 
				  * see the Navigation pattern on Android Design: http://developer.android.com/design/patterns/navigation.html#up-vs-back
				  */
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
