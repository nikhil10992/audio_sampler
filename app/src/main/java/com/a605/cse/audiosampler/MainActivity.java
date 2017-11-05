package com.a605.cse.audiosampler;

import java.io.File;
import java.io.IOException;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private static final String LOG_TAG = "UNIQUE";

    private static AudioConfiguration audioConfiguration;

    private RecordThread recordTask;
    private PlaybackAsyncTask playTask;


    // Need to handle these better.
    boolean isRecording = false, isPlaying = false;
    Button startRecordingButton, stopRecordingButton, startPlaybackButton, stopPlaybackButton;
    TextView statusText, textAmplitude, textDecibel, textFrequency;
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

        statusText = (TextView) this.findViewById(R.id.StatusTextView);

        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);

        startRecordingButton = (Button) this
                .findViewById(R.id.StartRecordingButton);
        stopRecordingButton = (Button) this
                .findViewById(R.id.StopRecordingButton);
        startPlaybackButton = (Button) this
                .findViewById(R.id.StartPlaybackButton);
        stopPlaybackButton = (Button) this
                .findViewById(R.id.StopPlaybackButton);

        startRecordingButton.setOnClickListener(this);
        stopRecordingButton.setOnClickListener(this);
        startPlaybackButton.setOnClickListener(this);
        stopPlaybackButton.setOnClickListener(this);

        stopRecordingButton.setEnabled(false);
        startPlaybackButton.setEnabled(false);
        stopPlaybackButton.setEnabled(false);

        try {
            recordingFile = File.createTempFile("recording", ".pcm", this.getCacheDir());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't create file on SD card", e);
        }
    }

    public void onClick(View v) {
        if (v == startRecordingButton) {
            record();
        } else if (v == stopRecordingButton) {
            stopRecording();
        } else if (v == startPlaybackButton) {
            play();
        } else if (v == stopPlaybackButton) {
            stopPlaying();
        }
    }

    public void play() {
        startPlaybackButton.setEnabled(true);

        playTask = new PlaybackAsyncTask(this, audioConfiguration);
        playTask.execute();

        stopPlaybackButton.setEnabled(true);
    }

    public void stopPlaying() {
        isPlaying = false;
        stopPlaybackButton.setEnabled(false);
        startPlaybackButton.setEnabled(true);
    }

    public void record() {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        AudioDataObject audioDataObject = new AudioDataObject(timeStamp, "fake_intensity", true);
        Communicator communicator = new Communicator(this); // here we will send audiodataobject later.
        communicator.sendData(timeStamp);
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(true);
        startPlaybackButton.setEnabled(true);

        recordTask = new RecordThread(this, audioConfiguration);
        recordTask.execute();
    }

    public void stopRecording() {
        isRecording = false;
    }
}