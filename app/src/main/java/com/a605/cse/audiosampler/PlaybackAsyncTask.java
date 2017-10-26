package com.a605.cse.audiosampler;

import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * Created by might on 10/26/17.
 */

public class PlaybackAsyncTask extends AsyncTask<Void, Integer, Void> {
    private MainActivity parentActivity;
    private AudioConfiguration audioConfiguration;

    public PlaybackAsyncTask(MainActivity _mainActivity, AudioConfiguration _audioConfiguration) {
        parentActivity = _mainActivity;
        audioConfiguration = _audioConfiguration;
    }

    @Override
    protected Void doInBackground(Void... params) {
        parentActivity.isPlaying = true;

        int bufferSize = AudioTrack.getMinBufferSize(audioConfiguration.getFrequency(),audioConfiguration.getChannelConfiguration(), audioConfiguration.getAudioEncoding());
        short[] audiodata = new short[bufferSize / 4];

        try {
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(parentActivity.recordingFile)));
            AudioTrack audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC, audioConfiguration.getFrequency(),
                    audioConfiguration.getChannelConfiguration(), audioConfiguration.getAudioEncoding(), bufferSize,
                    AudioTrack.MODE_STREAM);

            audioTrack.play();
            while (parentActivity.isPlaying && dis.available() > 0) {
                int i = 0;
                while (dis.available() > 0 && i < audiodata.length) {
                    audiodata[i] = dis.readShort();
                    i++;
                }
                audioTrack.write(audiodata, 0, audiodata.length);
            }
            dis.close();
        } catch (Throwable t) {
            Log.e("AudioTrack", "Playback Failed");
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        parentActivity.startPlaybackButton.setEnabled(false);
        parentActivity.stopPlaybackButton.setEnabled(true);
    }
}