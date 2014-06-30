package it.crystalsoft.tolor;

import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundManager
{
	private SoundPool mSoundPool = null;
	private HashMap<Integer, Integer> mSoundPoolMap = null;
	private HashMap<Integer, Integer> mSoundStreams = null;
	private HashMap<Integer, Boolean> looping = null;
	private AudioManager  mAudioManager = null;
	private Context mContext = null;
	private int currentStreamId = 0;
	private int loaded = 0;

	public SoundManager()
	{
		
	}

	public void initSounds(Context theContext)
	{
		mContext = theContext;

		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

		mSoundPoolMap = new HashMap<Integer, Integer>();
		mSoundStreams = new HashMap<Integer, Integer>();
		looping = new HashMap<Integer, Boolean>();

		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);

		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener()
		{
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
			{
				loaded++;
            }
        });
	} 

	public void addSound(int Index, int SoundID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	public void playSound(int Index, float pitch)
	{
		if (mSoundPoolMap.containsKey(Index))
		{
			int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

			mSoundStreams.put(Index, mSoundPool.play(mSoundPoolMap.get(Index), streamVolume, streamVolume, 1, 0, ((pitch < 0.0f) || (pitch > 0.0f)) ? pitch : 1f));

			/*mHandler.postDelayed(new Runnable()
			{
				public void run()
				{
					if(!mSoundStreams.isEmpty())
					{
						mSoundPool.stop(mSoundStreams.get(mSoundStreams.keySet().toArray()[0]));
					}
				}
			}, 100);*/
		}
	}

	public void playLoopedSound(int Index)
	{
		if (mSoundPoolMap.containsKey(Index))
		{
			int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

			mSoundStreams.put(Index, mSoundPool.play(mSoundPoolMap.get(Index), streamVolume, streamVolume, 2, -1, 1f));

			looping.put(Index, true);

			currentStreamId = mSoundStreams.get(Index);
		}
	}

	public void pauseSound(int Index)
	{
		if (mSoundStreams.containsKey(Index))
		{
			mSoundPool.pause(mSoundStreams.get(Index));

			looping.put(Index, false);
		}
	}

	public void stopSound(int Index)
	{
		if (mSoundStreams.containsKey(Index))
		{
			if (currentStreamId != 0)
			{
				mSoundPool.stop(mSoundStreams.get(Index));

				looping.put(Index, false);
			}
		}
	}

	public void stopSounds()
	{
		mSoundPool.autoPause();

		if (currentStreamId != 0)
		{
			mSoundPool.stop(currentStreamId);
		}

		for (Entry<Integer, Boolean> l : looping.entrySet())
		{
			l.setValue(false);
		}
	}

	public void setPitch(float pitch)
	{
		mSoundPool.setRate(currentStreamId, pitch);
	}

	public boolean isPlaying(int Index)
	{
		return (looping.get(Index) != null) ? looping.get(Index) : false;
	}

	public boolean isReady()
	{
		return (loaded == mSoundPoolMap.size()) ? true : false;
	}

	public void clear()
	{
		mSoundPool.release();
	}
}