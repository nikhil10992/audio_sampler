package com.a605.cse.audiosampler;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import com.a605.cse.audiosampler.calculators.AudioCalculator;
import com.google.gson.Gson;

/**
 * Created by might on 11/5/17.
 */

public class AudioDataProvider implements CallbackInterface {

    private AudioCalculator audioCalculator;
    private Handler handler;
    private MainActivity mainActivity;

    public AudioDataProvider(MainActivity _mainActivity) {
        mainActivity = _mainActivity;
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onBufferAvailable(byte[] buffer) {
        audioCalculator.setBytes(buffer);

        // Calculate the calculation time.
        int amplitude = audioCalculator.getAmplitude();
        double decibel = audioCalculator.getDecibel();
        double frequency = audioCalculator.getFrequency();

        final String amp = String.valueOf(amplitude);
        final String db = String.valueOf(decibel);
        final String hz = String.valueOf(frequency);

        // Printing
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mainActivity.textAmplitude.setText(amp);
//                mainActivity.textDecibel.setText(db);
//                mainActivity.textFrequency.setText(hz);
//            }
//        });

        if (frequency > 595) {

            String deviceId = Settings.Secure.getString(mainActivity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            AudioDataObject audioDataObject = new AudioDataObject(amp, hz, db, deviceId);
            Gson gson = new Gson();
            String jsonAudioDataObject = gson.toJson(audioDataObject);
            Communicator communicator = new Communicator(mainActivity); // here we will send audiodataobject later.
            communicator.sendData(jsonAudioDataObject);

            Log.d("UNIQUE", jsonAudioDataObject);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mainActivity.textAmplitude.setText(amp + " Amp");
                    mainActivity.textDecibel.setText(db + " db");
                    mainActivity.textFrequency.setText(hz + " Hz");
                }
            });
        }
    }
}
