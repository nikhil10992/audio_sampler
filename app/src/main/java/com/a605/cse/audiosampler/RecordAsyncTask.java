package com.a605.cse.audiosampler;

import android.app.Activity;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

class RecordAsyncTask extends AsyncTask<Void, Integer, Void> {

    private MainActivity parentActivity;
    private AudioConfiguration audioConfiguration;

    public RecordAsyncTask(MainActivity _parentActivity, AudioConfiguration _audioConfiguration) {
        parentActivity = _parentActivity;
        audioConfiguration = _audioConfiguration;
    }

    @Override
    protected Void doInBackground(Void... params) {
        parentActivity.isRecording = true;
        try {
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(
                            parentActivity.recordingFile)));
            int bufferSize = AudioRecord.getMinBufferSize(audioConfiguration.getFrequency(),
                    audioConfiguration.getChannelConfiguration(), audioConfiguration.getAudioEncoding());
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, audioConfiguration.getFrequency(),
                    audioConfiguration.getChannelConfiguration(), audioConfiguration.getAudioEncoding(), bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            int r = 0;
            while (parentActivity.isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0,
                        bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    dos.writeShort(buffer[i]);
                }
                publishProgress(new Integer(r));
                r++;
            }
            audioRecord.stop();
            dos.close();
        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
        return null;
    }
    protected void onProgressUpdate(Integer... progress) {
        parentActivity.statusText.setText(progress[0].toString());
    }
    protected void onPostExecute(Void result) {
        parentActivity.startRecordingButton.setEnabled(true);
        parentActivity.stopRecordingButton.setEnabled(false);
        parentActivity.startPlaybackButton.setEnabled(true);
    }
}