package com.a605.cse.audiosampler;

/**
 * Created by might on 10/23/17.
 * Updated
 */

public class AudioDataObject {
    private String timestamp = "";
    private String intensity = "";
    private boolean FLAG = false;

    public AudioDataObject(String _timestamp, String _intensity, boolean _flag){
        this.timestamp = _timestamp;
        this.intensity = _intensity;
        this.FLAG = _flag;
    }

    public AudioDataObject(String _timestamp, String _intensity){
        this.timestamp = _timestamp;
        this.intensity = _intensity;
    }

    public AudioDataObject(String _timestamp){
        this.timestamp = _timestamp;
    }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setIntensity(String intensity) { this.intensity = intensity; }
    public void setFlag(Boolean flag) { this.FLAG = flag; }

    public String getTimestamp(){ return timestamp; }
    public String getIntensity(){ return intensity; }
    public Boolean getFlag(){ return FLAG; }

}
