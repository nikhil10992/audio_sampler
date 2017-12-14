package com.a605.cse.audiosampler;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.a605.cse.audiosampler.dataobjects.NetworkDataObject;
import com.google.gson.Gson;

public class DataProvider implements CallbackInterface {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "DataProvider";
    private final String LOG_TAG = CLAZZ; // Don't want to clutter AudioSampler tag results.

    private DataResolver dataResolver;
    private MainActivity mainActivity;

    DataProvider(MainActivity _mainActivity) {
        this.mainActivity = _mainActivity;
        dataResolver = new DataResolver(_mainActivity);  //Integer.parseInt(mainActivity.inputFrequency.getText().toString())
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onBufferAvailable(byte[] buffer) {
        // Calculate the calculation time.
        NetworkDataObject networkDataObject = dataResolver.getDataObject(buffer);

        if (networkDataObject != null){
            Gson gson = new Gson();
            String jsonAudioDataObject = gson.toJson(networkDataObject);

            Communicator communicator = new Communicator(mainActivity, jsonAudioDataObject);
            communicator.start();
        }
    }
}
