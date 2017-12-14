package com.a605.cse.audiosampler;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.a605.cse.audiosampler.calculators.AudioCalculator;
import com.a605.cse.audiosampler.dataobjects.AudioDataObject;
import com.a605.cse.audiosampler.dataobjects.NetworkDataObject;
import com.a605.cse.audiosampler.dataobjects.SyncDataObject;

public class DataResolver {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "DataProvider";
    private final String LOG_TAG = NAME + CLAZZ; // Don't want to clutter AudioSampler tag results.

    private boolean isSynchronized;
    private AudioCalculator audioCalculator;

    private final int SYNC_FREQUENCY = 5000;
    private final int LISTENING_FREQUENCY = 10000;

    private MainActivity mainActivity;
    private Handler handler;
    private String deviceID;

    public DataResolver(MainActivity mainActivity) {
        this.isSynchronized = false;
        this.mainActivity = mainActivity;
        this.deviceID = Settings.Secure.getString(this.mainActivity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        handler = new Handler(Looper.getMainLooper());

        audioCalculator = new AudioCalculator();
    }

    public NetworkDataObject getDataObject(byte[] buffer) {
        final String timestamp = String.valueOf(System.currentTimeMillis());
        NetworkDataObject networkDataObject = null;
        double frequency = getFrequency(buffer);

        if (frequency >= SYNC_FREQUENCY) {
            if (!isSynchronized) {
                isSynchronized = true;
                networkDataObject = new SyncDataObject(deviceID, timestamp);
                Log.d(LOG_TAG, "Sending Sync Data Object");
            } else { // making an assumption.
                if (frequency >= LISTENING_FREQUENCY) {
                    networkDataObject = new AudioDataObject(deviceID, timestamp);
                    Log.d(LOG_TAG, "Sending Audio Data Object");
                }
            }
            printToMain(frequency);
        }
        return networkDataObject;
    }

    public double getFrequency(byte[] buffer) {
        audioCalculator.setBytes(buffer);
        return audioCalculator.getFrequency();
    }

    public void printToMain(double frequency) {
        final String hz = String.valueOf(frequency);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mainActivity.textFrequency.setText(hz + " Hz");
            }
        });
    }
}
