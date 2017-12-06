package com.a605.cse.audiosampler;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerManager {

    private static final String LOG_TAG = "ServerManager: ";
    private ServerThread serverThread;

    public void start_server(){
        serverThread = new ServerThread();
        serverThread.start();
        Log.d(LOG_TAG,"Server started");
    }

    public void stop_server(){
        serverThread = null;
        Log.d(LOG_TAG,"Server Stopped");
    }

}
