package com.a605.cse.audiosampler.dataobjects;

public class SyncDataObject {
    int messageId;
    String senderTimestamp;
    String receiverTimestamp;

    public SyncDataObject(int messageId, String senderTimestamp)
    {
        this.messageId = messageId;
        this.senderTimestamp = senderTimestamp;
    }
}
