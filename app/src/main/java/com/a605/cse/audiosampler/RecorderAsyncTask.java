package com.a605.cse.audiosampler;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;
import java.util.Calendar;
import static android.content.ContentValues.TAG;

class RecoderAsyncTask extends AsyncTask<Void, Integer, Void> {

    final static Calendar calendar = Calendar.getInstance();
    static private MainActivity parentActivity;

    int frequency = 11025, channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    RecoderAsyncTask(MainActivity mainActivity) {
        Log.d(TAG, "Async Const");
        parentActivity = mainActivity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d(TAG, "In DINB");
        parentActivity.isRecording = true;
        try {
            int bufferSize = AudioRecord.getMinBufferSize(frequency,
                    channelConfiguration, audioEncoding);
            AudioRecord audioRecord = new AudioRecord(
                    MediaRecorder.AudioSource.MIC, frequency,
                    channelConfiguration, audioEncoding, bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();
            int r = 0;
            Log.d(TAG, "Inside try" + parentActivity.isRecording);
            while (parentActivity.isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0,
                        bufferSize);
                for (int i = 0; i < bufferReadResult; i++) {
                    if (i % 5 == 0) {
                        Log.d("Audio: ", String.valueOf(buffer.length) + " : " + calendar.getTime());
                    }
                }
                publishProgress(new Integer(r));
                r++;
            }
            audioRecord.stop();
        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progress) {
        Log.d(TAG, "On Progress Update");
        parentActivity.statusText.setText(progress[0].toString());
    }

    protected void onPostExecute(Void result) {
        parentActivity.startRecordingButton.setEnabled(true);
        parentActivity.stopRecordingButton.setEnabled(false);
        parentActivity.startPlaybackButton.setEnabled(true);
    }
}
