package com.a605.cse.audiosampler.dataobjects;

public class SyncDataObject extends NetworkDataObject {
    private final String TYPE = "Sync";

    public String deviceID;
    public String timestamp;

    public SyncDataObject(String deviceID, String timestamp) {
        this.deviceID = deviceID;
        this.timestamp = timestamp;
    }
}
