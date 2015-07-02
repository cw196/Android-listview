package com.example.wang.JSON;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.example.wang.com.example.wang.utils.BitmapCache;
import com.example.wang.interfaces.ResponseListener;

import org.json.JSONArray;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientFragment";

    private ResponseListener response_listener;
    private RequestQueue request_queue=null;
    private ImageLoader image_loader = null;
    private BitmapCache bitmap_cache = null;

    public ClientFragment(){

    }

    @Override
    public void onAttach(Activity activity){
        Log.d(TAG, "onAttach()");

        super.onAttach(activity);
        try {
            response_listener = (ResponseListener)activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+ " must implement ResponseListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.i(TAG, "onCreate(Bundle)");

        super.onCreate(savedInstanceState);

        request_queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        bitmap_cache = new BitmapCache();
        image_loader = new ImageLoader(request_queue,bitmap_cache);

        setRetainInstance(true);
    }

    public ImageLoader getImageLoader(){
        return image_loader;
    }

    public void cancelAllRequests() {
        request_queue.cancelAll(this);
    }

    @Override
    public void onStop(){
        Log.d(TAG, "onStop");
        super.onStop();
        cancelAllRequests();
    }
    public void getMovieList(String current_movie_information){
        String url=null;
        if(current_movie_information.equals("In Theaters")){
            url="http://www.myapifilms.com/imdb/inTheaters";}
        if (current_movie_information.equals("Opening")){
            url="http://www.myapifilms.com/imdb/comingSoon";
        }
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET,url,null,
           new Response.Listener<JSONArray>() {
               public void onResponse(JSONArray json_array) {
                   Log.d("JSON",json_array.toString());
                   response_listener.onListResponse(json_array);
               }
           },new Response.ErrorListener(){
                public void onErrorResponse(VolleyError error){
                       error.printStackTrace();
                   }
            }
        );
        request_queue.add(request);
    }
}
