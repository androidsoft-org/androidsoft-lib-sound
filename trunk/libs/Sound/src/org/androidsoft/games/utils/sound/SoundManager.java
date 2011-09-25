/* Copyright (c) 2010-2011 Pierre LEVY androidsoft.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.androidsoft.games.utils.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import java.util.HashMap;

/**
 * Sound Manager
 */
public class SoundManager
{

    private static final int MAX_STREAMS = 4;
    private static final int SOURCE_QUALITY = 0;
    private static final String TAG = "Androidsoft Sound Manager";
    private static SoundManager mInstance;
    private static SoundPool mSoundPool;
    private static HashMap<Integer, Integer> mSoundPoolMap;
    private static AudioManager mAudioManager;
    private static Context mContext;
    private static boolean mInitialized;

    private SoundManager()
    {
    }

    public static SoundManager instance()
    {
        if (mInstance == null)
        {
            mInstance = new SoundManager();
        }
        return mInstance;
    }

    public static void init(Context context)
    {
        Log.d(TAG, "Init SoundManager");
        mContext = context;
        mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, SOURCE_QUALITY);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);     
        mInitialized = true;
    }

    public void addSound(int index, int sound)
    {
        Log.d(TAG, "Add sound : " + index );
        mSoundPoolMap.put(index, mSoundPool.load(mContext, sound, 1));
    }

    public void playSound(int index )
    {
        playSound(index, false );
    }

    public void playSound(int index, boolean loop )
    {
        Log.d(TAG, "Play sound : " + index );
        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int repeat = ( loop ) ? -1 : 0;
        if( mSoundPoolMap != null )
        {    
            mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, repeat, 1f);
        }
        else
        {
            Log.w(TAG, "SoundPoolMap not defined while playing sound ID : " + index );
        }
    }

    public void stopSound(int index)
    {
        Log.d(TAG, "Stop sound : " + index );
        if( mSoundPoolMap != null )
        {    
            mSoundPool.stop(mSoundPoolMap.get(index));
        }
        else
        {
            Log.w(TAG, "SoundPoolMap not defined while stoping sound ID : " + index );
        }
    }

    public static void release()
    {
        Log.d(TAG, "Release SoundManager");
        mSoundPool.release();
        mSoundPool = null;
        mSoundPoolMap.clear();
        mAudioManager.unloadSoundEffects();
        mInstance = null;
        mInitialized = false;

    }

    public static boolean isInitialized()
    {
        return mInitialized;
    }
}