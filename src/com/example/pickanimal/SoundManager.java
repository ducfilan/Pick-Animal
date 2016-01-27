package com.example.pickanimal;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager 
{
	private Context pContext;
	private SoundPool soundPool;
	private float rate = 1.0f;
	private float leftVolume = 1.0f;
	private float rightVolume = 1.0f;
    // Constructor, setup the audio manager and store the app context
	public SoundManager(Context appContext)
	{
	  soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
 	  pContext = appContext;
	}
	
	// Load up a sound and return the id
	public int load(int sound_id)
	{
		return soundPool.load(pContext, sound_id, 1);
	}
	
	// Play a sound
	public void play(int sound_id)
	{
		soundPool.play(sound_id, leftVolume, rightVolume, 0, 0, rate); 	
	}
	
	// Free ALL the things!
	public void unloadAll()
	{
		soundPool.release();		
	}

}
