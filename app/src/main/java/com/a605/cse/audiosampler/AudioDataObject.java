package com.a605.cse.audiosampler;

/**
 * Created by might on 10/23/17.
 * Updated
 */

class AudioDataObject {
    private static int counter;

    private String timestamp;
    private String amplitude;
    private String frequency;
    private String intensity;
    private String sequenceNumber;
    private String deviceId;

    private AudioDataObject() {
        this.timestamp = String.valueOf(System.currentTimeMillis());
        this.sequenceNumber = String.valueOf(++counter);
    }

    AudioDataObject(String _amplitude, String _frequency, String _intensity, String _deviceId) {
        this();
        this.amplitude = _amplitude;
        this.frequency = _frequency;
        this.intensity = _intensity;
        this.deviceId = _deviceId;
    }
}
