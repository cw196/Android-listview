package com.example.wang.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wang on 4/18/2015.
 */
public interface ResponseListener {
    public void onListResponse(JSONArray json_array);

    public void onDetailsResponse(JSONObject json_object);
}
