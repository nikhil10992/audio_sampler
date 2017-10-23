package com.a605.cse.audiosampler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class Communicator {
    private static final String SERVER_IP = "";
    private static final String PORT = "8080";
    private static String serverURL = "";
    private static String ERROR = "";
    private RequestQueue volleyRequestQueue;
    private Context context;

    public Communicator(Context _context){
        this.context = _context;
        volleyRequestQueue = Volley.newRequestQueue(context);
        serverURL = "http://"+SERVER_IP+":"+PORT;
    }

    public boolean sendData(final String data){
        StringRequest sendDataRequest = new StringRequest(Request.Method.POST, serverURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        ERROR = "";
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        ERROR = error.toString();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  dataParams = new HashMap<>();
                dataParams.put("data", data);
                return dataParams;
            }
        };
        volleyRequestQueue.add(sendDataRequest);
        return ERROR.equals("");
    }



}
