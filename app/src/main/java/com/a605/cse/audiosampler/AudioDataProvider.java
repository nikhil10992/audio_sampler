package com.a605.cse.audiosampler;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.a605.cse.audiosampler.calculators.AudioCalculator;
import com.a605.cse.audiosampler.dataobjects.AudioDataObject;
import com.google.gson.Gson;

public class AudioDataProvider implements CallbackInterface {

    private static final String LOG_TAG = "AudioDataProvider: ";
    private AudioCalculator audioCalculator;
    private Handler handler;
    private MainActivity mainActivity;

    public AudioDataProvider(MainActivity _mainActivity) {
        mainActivity = _mainActivity;
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onBufferAvailable(byte[] buffer) {
        Log.d(LOG_TAG,"onBufferAvailable method called");
        final String timestamp = String.valueOf(System.currentTimeMillis());

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
        int targetFrequency = Integer.parseInt(mainActivity.inputFrequency.getText().toString());

        if (frequency > targetFrequency) {

            String deviceId = Settings.Secure.getString(mainActivity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            AudioDataObject audioDataObject = new AudioDataObject(amp, hz, db, deviceId, timestamp);
            Gson gson = new Gson();
            String jsonAudioDataObject = gson.toJson(audioDataObject);
            Communicator communicator = new Communicator(mainActivity); // here we will send audiodataobject later.
            Log.d(LOG_TAG,"Communicator class initialized");
            Log.d(LOG_TAG,"Sending data through communicator");
            communicator.sendData(jsonAudioDataObject);

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
