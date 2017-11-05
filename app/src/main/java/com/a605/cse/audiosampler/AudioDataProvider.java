package com.a605.cse.audiosampler;
import android.os.Handler;
import android.os.Looper;

import com.a605.cse.audiosampler.calculators.AudioCalculator;

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

        final String amp = String.valueOf(amplitude + " Amp");
        final String db = String.valueOf(decibel + " db");
        final String hz = String.valueOf(frequency + " Hz");

        handler.post(new Runnable() {
            @Override
            public void run() {
                mainActivity.textAmplitude.setText(amp);
                mainActivity.textDecibel.setText(db);
                mainActivity.textFrequency.setText(hz);
            }
        });
    }
}
