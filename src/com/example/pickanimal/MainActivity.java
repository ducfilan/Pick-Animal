package com.example.pickanimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ShowToast" })
public class MainActivity extends Activity {
	public final static String	EXTRA_RESTART			= "com.example.bubblegame.MainActivity.RESTART";
	public final static String	EXTRA_TYPE				= "com.example.bubblegame.MainActivity.TYPE";
	public final static String	EXTRA_DIFFICULT			= "com.example.bubblegame.MainActivity.DIFFICULT";
	public final static String	EXTRA_RESULT			= "com.example.bubblegame.MainActivity.RESULT";
	public final static String	EXTRA_SOUND				= "com.example.bubblegame.MainActivity.SOUND";
	public final static String	EXTRA_TIME				= "com.example.bubblegame.MainActivity.TIME";
	public final static String	EXTRA_BONUS				= "com.example.bubblegame.MainActivity.BONUS";
	ArrayList<Integer>			animalsImage;
	ArrayList<Integer>			animalsSound;
	ArrayList<Integer>			backgrounds;
	SoundManager				sound;
	int							trueChoice;
	int							totalScore				= 0;
	int							countContinousTrue		= 0;
	private MediaPlayer			themeSong;
	int							speedTime;
	final Context				context					= this;
	int							countRandomLimit		= 0;
	int							countSameAnimalContinue	= 0;
	int							oldAnimal				= -1;
	boolean						firstTime				= true;
	int							second					= 0;
	boolean						isEnd					= false;
	public final static int		length					= 90;
	public final static int		mustTouch				= 30;
	boolean						soundOn					= true;
	boolean						isRestart				= false;
	boolean						isRotate				= false;
	int							bonus					= 0;
	int[]						result;
	String						gameMode;
	String						difficult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			totalScore = Integer.parseInt(savedInstanceState.getString("score"));
			second = length - Integer.parseInt(savedInstanceState.getString("time"));
			bonus = Integer.parseInt(savedInstanceState.getString("bonus"));
			result = savedInstanceState.getIntArray("result");
			soundOn = savedInstanceState.getBoolean("sound");
			gameMode = savedInstanceState.getString("mode");
			difficult = savedInstanceState.getString("difficult");
			isRestart = false;
			isRotate = true;
		} else {
			/*
			 * Get setting
			 */
			Intent getSetting = getIntent();
			gameMode = getSetting.getStringExtra(MainMenu.EXTRA_TYPE);
			difficult = getSetting.getStringExtra(MainMenu.EXTRA_DIFFICULT);
			soundOn = getSetting.getBooleanExtra(MainMenu.EXTRA_SOUND, true);
			isRestart = false;
			if (gameMode == null || gameMode.isEmpty()) {
				gameMode = getSetting.getStringExtra(ResultActivity.EXTRA_TYPE);
				difficult = getSetting.getStringExtra(ResultActivity.EXTRA_DIFFICULT);
				soundOn = getSetting.getBooleanExtra(ResultActivity.EXTRA_SOUND, true);
			}
			isRotate = false;
		}
		if (difficult.equals(getString(R.string.hard))) {
			speedTime = 3000;
		} else if (difficult.equals(getString(R.string.medium))) {
			speedTime = 5000;
		} else {
			speedTime = 10000;
		}
		result = new int[12];
		Arrays.fill(result, 0);
		/*
		 * Init array of animals image
		 */
		animalsImage = new ArrayList<Integer>();
		animalsImage.add(R.drawable.dog);
		animalsImage.add(R.drawable.bear);
		animalsImage.add(R.drawable.cat);
		animalsImage.add(R.drawable.cow);
		animalsImage.add(R.drawable.dolphin);
		animalsImage.add(R.drawable.eagle);
		animalsImage.add(R.drawable.elephant);
		animalsImage.add(R.drawable.tiger);
		animalsImage.add(R.drawable.frog);
		animalsImage.add(R.drawable.pig);
		animalsImage.add(R.drawable.wolf);
		animalsImage.add(R.drawable.horse);
		final int numOfAnimals = animalsImage.size();

		// Init backgrounds
		backgrounds = new ArrayList<Integer>();
		backgrounds.add(R.drawable.background1);
		backgrounds.add(R.drawable.d2);
		backgrounds.add(R.drawable.d3);
		backgrounds.add(R.drawable.d4);
		backgrounds.add(R.drawable.d5);

		// Init animals sound
		sound = new SoundManager(getApplicationContext());
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		animalsSound = new ArrayList<Integer>();
		animalsSound.add(sound.load(R.raw.dog));
		animalsSound.add(sound.load(R.raw.bear));
		animalsSound.add(sound.load(R.raw.cat));
		animalsSound.add(sound.load(R.raw.cow));
		animalsSound.add(sound.load(R.raw.dolphin));
		animalsSound.add(sound.load(R.raw.eagle));
		animalsSound.add(sound.load(R.raw.elephant));
		animalsSound.add(sound.load(R.raw.tiger));
		animalsSound.add(sound.load(R.raw.frog));
		animalsSound.add(sound.load(R.raw.pig));
		animalsSound.add(sound.load(R.raw.wolf));
		animalsSound.add(sound.load(R.raw.horse));
		trueChoice = sound.load(R.raw.true_choice);

		/*
		 * Screen size
		 */
		Point size = new Point();
		WindowManager w = getWindowManager();
		w.getDefaultDisplay().getSize(size);
		final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.bubble_layout);
		relativeLayout.setBackgroundResource(backgrounds.get(new Random().nextInt(backgrounds.size())));

		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		final int screenHeight = displayMetrics.heightPixels;
		final int screenWidth = displayMetrics.widthPixels;

		/*
		 * Init first animal
		 */
		MyImageView xAnimal = (MyImageView) findViewById(R.id.X);
		xAnimal.getLayoutParams().height = screenWidth / 6;
		xAnimal.getLayoutParams().width = screenWidth / 6;

		int firstAnimal = new Random().nextInt(numOfAnimals);
		xAnimal.setImageResource(animalsImage.get(firstAnimal));
		xAnimal.setAnimal(firstAnimal);

		/*
		 * Theme sound
		 */
		if (!isRotate || isRestart) {
			themeSong = MediaPlayer.create(getApplicationContext(), R.raw.theme);
			themeSong.setLooping(true);
			themeSong.start();
		}
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		final ImageView soundOnOff = (ImageView) findViewById(R.id.soundOnOff);
		soundOnOff.getLayoutParams().height = screenWidth / 6;
		soundOnOff.getLayoutParams().width = screenWidth / 6;

		if (soundOn) {
			soundOnOff.setImageResource(R.drawable.sound);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
		} else {
			soundOnOff.setImageResource(R.drawable.soundoff);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
		}

		/*
		 * Bat/tat tieng
		 */
		soundOnOff.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if (soundOn) {
							soundOnOff.setImageResource(R.drawable.soundoff);
							audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
							soundOn = false;
						} else {
							soundOnOff.setImageResource(R.drawable.sound);
							audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
							soundOn = true;
						}
						break;
					default:
						break;
				}
				return true;
			}
		});

		final TextView tvScore = (TextView) findViewById(R.id.tvScore);
		final TextView tvTime = (TextView) findViewById(R.id.tvTime);

		/*
		 * Restart
		 */
		final Button restart = (Button) findViewById(R.id.restart);
		restart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						Intent restart = new Intent(context, MainMenu.class);
						restart.putExtra(EXTRA_RESTART, true);
						restart.putExtra(EXTRA_TYPE, gameMode);
						restart.putExtra(EXTRA_DIFFICULT, difficult);
						restart.putExtra(EXTRA_SOUND, soundOn);
						isEnd = true;
						isRestart = true;
						if (themeSong != null) {
							themeSong.stop();
						}
						finish();
						startActivity(restart);
						break;
					default:
						break;
				}
				return true;
			}
		});

		/*
		 * Main thread
		 */
		Thread mainThread = new Thread() {
			public void run() {
				while (!isEnd) {
					try {
						sleep(1000);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (gameMode.equals("count")) {
									tvTime.setText("" + second);
									tvScore.setText("" + totalScore + " / " + mustTouch);
									if (totalScore >= mustTouch) {
										isEnd = true;
									}
								} else {
									tvTime.setText("" + (length - second));
									tvScore.setText("" + totalScore);
									if (second == length) {
										isEnd = true;
									}
								}
								final ImageView bubble = new ImageView(MainActivity.this);
								bubble.setImageResource(R.drawable.bubble3);

								final ImageView animal = new ImageView(MainActivity.this);
								final ImageView animal2 = new ImageView(MainActivity.this);
								RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
								/*
								 * Random con vat
								 */
								int randomAnimal = new Random().nextInt(numOfAnimals);
								/*
								 * Khong cho qua 2 con giong nhau lien tuc
								 */
								if (oldAnimal == -1) {
									oldAnimal = randomAnimal;
								} else {
									if (oldAnimal == randomAnimal) {
										countSameAnimalContinue++;
									}
									if (countSameAnimalContinue >= 2) {
										if (randomAnimal == numOfAnimals - 1) {
											randomAnimal--;
										} else {
											randomAnimal++;
										}
										countSameAnimalContinue = 0;
									}
									oldAnimal = randomAnimal;
								}
								int targetAnimal = ((MyImageView) findViewById(R.id.X)).getAnimal();
								if (targetAnimal != randomAnimal) {
									countRandomLimit++;
									if (countRandomLimit >= 10) {
										countRandomLimit = 0;
										randomAnimal = targetAnimal;
									}
								} else {
									countRandomLimit = 0;
								}
								animal.setImageResource(animalsImage.get(randomAnimal));
								animal2.setImageResource(animalsImage.get(randomAnimal));
								/*
								 * Random vi tri
								 */
								final int randomPostion = new Random().nextInt(70);
								lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
								lp.setMargins(screenWidth * randomPostion / 100, 0, 0, 0);

								/*
								 * Add con vat va qua bong vao layout
								 */
								final MyFrameLayout bubbleWithAnimalFrame = new MyFrameLayout(context);
								bubbleWithAnimalFrame.setLayoutParams(lp);
								// random size
								final int animalSize;

								animalSize = new Random().nextInt(11);
								bubbleWithAnimalFrame.getLayoutParams().height = (animalSize + 30) * screenWidth / 120;
								bubbleWithAnimalFrame.getLayoutParams().width = (animalSize + 30) * screenWidth / 120;

								bubbleWithAnimalFrame.addView(animal);
								bubbleWithAnimalFrame.addView(bubble);
								bubbleWithAnimalFrame.setAnimal(randomAnimal);
								relativeLayout.addView(bubbleWithAnimalFrame, lp);
								/*
								 * Cho qua bong va con vat bay len
								 */
								final ObjectAnimator bubbleAndAnimalAnimator;

								bubbleAndAnimalAnimator = ObjectAnimator.ofFloat(bubbleWithAnimalFrame, "y", screenHeight, -(animalSize + 30) * screenWidth / 120);

								bubbleAndAnimalAnimator.setInterpolator(new TimeInterpolator() {

									@Override
									public float getInterpolation(float input) {
										return input;
									}
								});
								bubbleAndAnimalAnimator.setDuration(speedTime);
								bubbleAndAnimalAnimator.addListener(new Animator.AnimatorListener() {
									boolean	isCanceled;

									@Override
									public void onAnimationStart(Animator animation) {
										isCanceled = false;
									}

									@Override
									public void onAnimationRepeat(Animator animation) {
									}

									@Override
									public void onAnimationEnd(Animator animation) {
										relativeLayout.removeView(bubbleWithAnimalFrame);
										/*
										 * Tru diem khi de con can chon di qua man hinh
										 */
										if (!isCanceled) {
											MyImageView mustTouch = (MyImageView) findViewById(R.id.X);
											if (bubbleWithAnimalFrame.getAnimal() == mustTouch.getAnimal()) {
												Toast.makeText(context, getString(R.string.miss), 500).show();
												countContinousTrue = 0;
											}
										}
									}

									@Override
									public void onAnimationCancel(Animator animation) {
										isCanceled = true;
									}
								});
								bubbleAndAnimalAnimator.start();
								/*
								 * Cham vao qua bong thi no bien mat
								 */
								bubbleWithAnimalFrame.setOnTouchListener(new OnTouchListener() {

									@Override
									public boolean onTouch(View v, MotionEvent event) {

										switch (event.getAction()) {
											case MotionEvent.ACTION_DOWN:

												MyImageView x = (MyImageView) findViewById(R.id.X);
												int animalNum = bubbleWithAnimalFrame.getAnimal();
												if (animalNum != x.getAnimal()) {
													Toast.makeText(context, getString(R.string.wrong), 500).show();
													/*
													 * Truong hop cham sai
													 */
													sound.play(animalsSound.get(animalNum));

													if (totalScore >= 1) {
														totalScore -= 1;
														bonus -= 1;
													}
													countContinousTrue = 0;
													return true;
												}
												Toast.makeText(context, getString(R.string.right), 500).show();
												/*
												 * Cham dung
												 */
												bubbleAndAnimalAnimator.cancel();
												sound.play(trueChoice);
												totalScore += 1;
												result[animalNum]++;
												countContinousTrue++;
												if (countContinousTrue == 5) {
													Toast.makeText(context, getString(R.string.miniBonus), 500).show();
													bonus += 3;
													/*
													 * +3 diem khi 5 lan lien tiep dung
													 */
													totalScore += 3;
												} else if (countContinousTrue == 10) {
													Toast.makeText(context, getString(R.string.extraBonus), 500).show();
													/*
													 * +10 diem khi 10 lan lien tiep dung
													 */
													bonus += 10;
													totalScore += 10;
													countContinousTrue = 0;
												}
												final MyFrameLayout animalFall = new MyFrameLayout(context);
												RelativeLayout.LayoutParams lpFall = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
												lpFall.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
												lpFall.setMargins(screenWidth * randomPostion / 100, 0, 0, 0);
												animalFall.setLayoutParams(lpFall);

												animalFall.getLayoutParams().height = (animalSize + 30) * screenWidth / 120;
												animalFall.getLayoutParams().width = (animalSize + 30) * screenWidth / 120;

												animalFall.addView(animal2);
												relativeLayout.addView(animalFall);
												ObjectAnimator animalAnimatorFall = ObjectAnimator.ofFloat(animalFall, "y", v.getY(), screenHeight);
												animalAnimatorFall.setDuration(2000);
												animalAnimatorFall.addListener(new Animator.AnimatorListener() {

													@Override
													public void onAnimationStart(Animator animation) {

													}

													@Override
													public void onAnimationRepeat(Animator animation) {

													}

													@Override
													public void onAnimationEnd(Animator animation) {
														relativeLayout.removeView(animalFall);

													}

													@Override
													public void onAnimationCancel(Animator animation) {
													}
												});
												animalAnimatorFall.start();
												int newAnimal = new Random().nextInt(numOfAnimals);
												x.setImageResource(animalsImage.get(newAnimal));
												x.setAnimal(newAnimal);
												break;
											default:
												break;
										}
										return true;
									}
								});
								second++;
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (isEnd && !isRestart) {
					Intent sendResult = new Intent(context, ResultActivity.class);
					sendResult.putExtra(EXTRA_RESULT, result);
					sendResult.putExtra(EXTRA_TYPE, gameMode);
					sendResult.putExtra(EXTRA_DIFFICULT, difficult);
					sendResult.putExtra(EXTRA_SOUND, soundOn);
					sendResult.putExtra(EXTRA_TIME, second + "");
					sendResult.putExtra(EXTRA_BONUS, bonus + "");
					if (themeSong != null) {
						themeSong.stop();
					}

					finish();
					startActivity(sendResult);
				}
			}
		};
		mainThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		TextView tvTime = (TextView) findViewById(R.id.tvTime);
		outState.putString("score", totalScore + "");
		outState.putString("bonus", bonus + "");
		outState.putIntArray("result", result);
		outState.putString("time", tvTime.getText().toString());
		outState.putString("mode", gameMode);
		outState.putString("difficult", difficult);
		outState.putBoolean("sound", soundOn);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Your code here
			Intent restart = new Intent(context, MainMenu.class);
			restart.putExtra(EXTRA_RESTART, false);
			if (themeSong != null) {
				themeSong.stop();
			}
			isEnd = true;
			isRestart = true;

			finish();
			startActivity(restart);
		}
		return true;
	}

}
