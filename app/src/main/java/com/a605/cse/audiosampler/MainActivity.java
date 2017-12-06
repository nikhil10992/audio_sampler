package com.a605.cse.audiosampler;

import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private RecorderThread recorderThread;
    private ServerManager serverManager;

    // Need to handle these better.
    Button startRecordingButton, stopRecordingButton;
    TextView textAmplitude, textDecibel, textFrequency, textDeviceId;
    EditText ipAddressEditText, inputFrequency;
    public String ipAddress;

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

        serverManager = new ServerManager();
        serverManager.start_server();

        initializeViews();
    }

    public void initializeViews() {
        textAmplitude = findViewById(R.id.textAmplitude);
        textDecibel = findViewById(R.id.textDecibel);
        textFrequency = findViewById(R.id.textFrequency);
        ipAddressEditText = findViewById(R.id.ipAddress);
        inputFrequency = findViewById(R.id.inputFrequency);
        textDeviceId = findViewById(R.id.deviceId);
        String deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        textDeviceId.append(deviceId);
        startRecordingButton = this
                .findViewById(R.id.StartRecordingButton);
        stopRecordingButton = this
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
        serverManager.stop_server();
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(true);
        ipAddress = ipAddressEditText.getText().toString();

        AudioDataProvider audioDataProvider = new AudioDataProvider(this);
        AudioConfiguration audioConfiguration = new AudioConfiguration();

        recorderThread = new RecorderThread(audioConfiguration, audioDataProvider);
        recorderThread.start();
    }

    public void stopRecording() {
        recorderThread.stop();
        startRecordingButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
    }
}