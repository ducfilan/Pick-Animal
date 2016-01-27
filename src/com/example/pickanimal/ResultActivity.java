package com.example.pickanimal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ResultActivity extends Activity {
	public final static String EXTRA_TYPE = "com.example.bubblegame.ResultActivity.TYPE";
	public final static String EXTRA_DIFFICULT = "com.example.bubblegame.ResultActivity.DIFFICULT";
	public final static String EXTRA_SOUND = "com.example.bubblegame.ResultActivity.SOUND";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent a = getIntent();
		final String mode = a.getStringExtra(MainActivity.EXTRA_TYPE);
		final String difficult = a.getStringExtra(MainActivity.EXTRA_DIFFICULT);
		int time = Integer.parseInt(a.getStringExtra(MainActivity.EXTRA_TIME));
		final boolean soundOn = a.getBooleanExtra(MainActivity.EXTRA_SOUND, true);
		int bonus = Integer.parseInt(a.getStringExtra(MainActivity.EXTRA_BONUS));
		int[] number = a.getIntArrayExtra(MainActivity.EXTRA_RESULT);
		Button restart = (Button) findViewById(R.id.restartGame);
		Button menu = (Button) findViewById(R.id.Menu);

		((TextView) findViewById(R.id.dogNum)).setText("X " + number[0]);
		((TextView) findViewById(R.id.bearNum)).setText("X " + number[1]);
		((TextView) findViewById(R.id.catNum)).setText("X " + number[2]);
		((TextView) findViewById(R.id.cowNum)).setText("X " + number[3]);
		((TextView) findViewById(R.id.dolphinNum)).setText("X " + number[4]);
		((TextView) findViewById(R.id.eagleNum)).setText("X " + number[5]);
		((TextView) findViewById(R.id.elephantNum)).setText("X " + number[6]);
		((TextView) findViewById(R.id.tigerNum)).setText("X " + number[7]);
		((TextView) findViewById(R.id.frogNum)).setText("X " + number[8]);
		((TextView) findViewById(R.id.pigNum)).setText("X " + number[9]);
		((TextView) findViewById(R.id.wolfNum)).setText("X " + number[10]);
		((TextView) findViewById(R.id.horseNum)).setText("X " + number[11]);
		((TextView) findViewById(R.id.bonus)).setText(getString(R.string.Bonus) + bonus);
		Point size = new Point();
		WindowManager w = getWindowManager();
		w.getDefaultDisplay().getSize(size);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		final int screenWidth = displayMetrics.widthPixels;
		((MyImageView) findViewById(R.id.dogAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.bearAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.catAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.cowAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.dolphinAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.eagleAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.elephantAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.tigerAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.frogAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.pigAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.wolfAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.horseAnimal)).getLayoutParams().height = screenWidth / 8;
		((MyImageView) findViewById(R.id.dogAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.bearAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.catAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.cowAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.dolphinAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.eagleAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.elephantAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.tigerAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.frogAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.pigAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.wolfAnimal)).getLayoutParams().width = screenWidth / 8;
		((MyImageView) findViewById(R.id.horseAnimal)).getLayoutParams().width = screenWidth / 8;

		int total = bonus;
		for (int i = 0; i < 12; i++)
			total += number[i];
		((TextView) findViewById(R.id.total)).setText(getString(R.string.total) + total);
		if (mode.equalsIgnoreCase("count")) {
			if (time < MainActivity.mustTouch * 3) {
				((TextView) findViewById(R.id.Comment)).setText(getString(R.string.fast));
			} else {
				((TextView) findViewById(R.id.Comment)).setText(getString(R.string.slow));
			}
		} else {
			if (total > MainActivity.length / 3) {
				((TextView) findViewById(R.id.Comment)).setText(getString(R.string.fast));
			} else {
				((TextView) findViewById(R.id.Comment)).setText(getString(R.string.slow));
			}
		}
		menu.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
					case MotionEvent.ACTION_DOWN :
						Intent mainMenu = new Intent(ResultActivity.this, MainMenu.class);
						finish();
						startActivity(mainMenu);
						break;
					default :
						break;
				}
				return true;
			}
		});

		restart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				switch (arg1.getAction()) {
					case MotionEvent.ACTION_DOWN :
						Intent restartGame = new Intent(ResultActivity.this, MainActivity.class);
						restartGame.putExtra(EXTRA_TYPE, mode);
						restartGame.putExtra(EXTRA_DIFFICULT, difficult);
						restartGame.putExtra(EXTRA_SOUND, soundOn);
						finish();
						startActivity(restartGame);
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
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home :
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
