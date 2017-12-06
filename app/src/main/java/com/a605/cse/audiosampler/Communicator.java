package com.a605.cse.audiosampler;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by might on 10/23/17.
 * Updated
 */

class Communicator {

    private static final String LOG_TAG = "Communicator: ";
    private static final String PORT = "8080";
    private static String SERVER_IP;
    private static String serverURL = "";
    private static String ERROR = "";
    private RequestQueue volleyRequestQueue;
    private MainActivity mainActivity;
    //private String audioDataObject;

    Communicator(MainActivity _mainActivity) {
        this.mainActivity = _mainActivity;
        SERVER_IP = _mainActivity.ipAddress;
        volleyRequestQueue = Volley.newRequestQueue(mainActivity);
        serverURL = "http://" + SERVER_IP + ":" + PORT;
    }

    boolean sendData(final String data) {
        Log.d(LOG_TAG,"Sending data"+data);
        StringRequest sendDataRequest = new StringRequest(Request.Method.POST, serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        ERROR = "";
                        Log.d(LOG_TAG,"Data Sent. Response received = "+response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        ERROR = error.toString();
                        Log.d(LOG_TAG, "Error = "+error.toString()+" and Data = "+data);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> dataParams = new HashMap<>();
                dataParams.put("data", data);
                return dataParams;
            }
        };
        sendDataRequest.setRetryPolicy(new DefaultRetryPolicy(0,0,0));
        volleyRequestQueue.add(sendDataRequest);
        Log.d(LOG_TAG,"sendData function ends");
        return ERROR.equals("");
    }
}
