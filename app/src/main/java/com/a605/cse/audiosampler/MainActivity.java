package com.a605.cse.audiosampler;

import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = "MainActivity: ";

    public static String ipAddress;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioConfiguration audioConfiguration;
    private AudioDataProvider audioDataProvider;
    private RecorderThread recorderThread;

    // Need to handle these better.
    Button startRecordingButton, stopRecordingButton;
    TextView textAmplitude, textDecibel, textFrequency, textDeviceId;
    EditText ipAddressEditText, inputFrequency;
    File recordingFile;


    public MainActivity() {
        audioConfiguration = new AudioConfiguration();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);

        initializeViews();

        audioDataProvider = new AudioDataProvider(this);
    }

    public void initializeViews() {
        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);
        textDeviceId = findViewById(R.id.deviceId);
        ipAddressEditText = (EditText) findViewById(R.id.ipAddress);
        inputFrequency = (EditText) findViewById(R.id.inputFrequency);
        // Set Device Id
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        textDeviceId.setText(deviceId);
        startRecordingButton = (Button) this
                .findViewById(R.id.StartRecordingButton);
        stopRecordingButton = (Button) this
                .findViewById(R.id.StopRecordingButton);

        startRecordingButton.setOnClickListener(this);
        stopRecordingButton.setOnClickListener(this);

        stopRecordingButton.setEnabled(false);
    }

    public void onClick(View v) {
        if (v == startRecordingButton) {
            record();
        } else if (v == stopRecordingButton) {
            stopRecording();
        }
    }

    public void record() {
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(true);
        ipAddress = ipAddressEditText.getText().toString();
        recorderThread = new RecorderThread(audioConfiguration, audioDataProvider);
        Log.d(LOG_TAG,"Recorder Thread Initialized");
        recorderThread.start();
    }

    public void stopRecording() {
        recorderThread.stop();
        startRecordingButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
    }
}