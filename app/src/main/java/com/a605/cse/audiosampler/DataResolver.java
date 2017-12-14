package com.a605.cse.audiosampler;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import com.a605.cse.audiosampler.calculators.AudioCalculator;
import com.a605.cse.audiosampler.dataobjects.AudioDataObject;
import com.a605.cse.audiosampler.dataobjects.NetworkDataObject;
import com.a605.cse.audiosampler.dataobjects.SyncDataObject;

public class DataResolver {

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
                networkDataObject = new SyncDataObject(deviceID, timestamp);
                isSynchronized = true;
            } else { // making an assumption. if (frequency >= LISTENING_FREQUENCY)
                networkDataObject = new AudioDataObject(deviceID, timestamp);
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
