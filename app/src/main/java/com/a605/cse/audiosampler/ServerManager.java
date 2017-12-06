package com.a605.cse.audiosampler;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerManager {

    private static final String LOG_TAG = "ServerManager: ";
    final int SERVER_PORT = 5000;
    ServerThread serverThread;
    ServerSocket serverSocket;

    private ServerSocket initServerSocket(){
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    public void start_server(){
        serverThread = new ServerThread(initServerSocket());
        serverThread.run();
        Log.d(LOG_TAG,"Server started");
    }

    public void stop_server(){
        serverSocket = null;
        serverThread = null;
        Log.d(LOG_TAG,"Server Stopped");
    }

}
