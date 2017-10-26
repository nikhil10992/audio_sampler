package com.a605.cse.audiosampler;

import android.media.AudioFormat;

/**
 * Created by might on 10/23/17.
 */

public class AudioConfiguration {
    private int frequency;
    private int channelConfiguration;
    private int audioEncoding;

    public AudioConfiguration() {
        frequency = 11025;
        channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getChannelConfiguration() {
        return channelConfiguration;
    }

    public int getAudioEncoding() {
        return audioEncoding;
    }
}
