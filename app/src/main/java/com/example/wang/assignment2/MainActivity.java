package com.example.wang.assignment2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;


import com.android.volley.toolbox.ImageLoader;
import com.example.wang.fragment.MovieDetailsFragment;
import com.example.wang.fragment.MovieListFragment;
import com.example.wang.interfaces.ResponseListener;
import com.example.wang.interfaces.MovieSelectionListener;
import com.example.wang.movies.Movie;
import com.example.wang.JSON.ClientFragment;
import com.example.wang.JSON.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


//change1

//main activity
public class MainActivity extends Activity implements ResponseListener,MovieSelectionListener, ActionBar.OnNavigationListener{

    private static final String     TAG ="MainActivity";

    private static final String     CLIENT_FRAGMENT_TAG="ClientFragment";
    private static final String     MOVIE_LIST_FRAGMENT_TAG="MovieListFragment";
    private static final String     MOVIE_DETAILS_FRAGMENT_TAG="MovieDetailsFragment";
    private static final String     SELECTED_MOVIE_IDX ="SELECTED_MOVIE_IDX";
    private static final int        NO_SELECTION = -1;
    static final int                 PICK_CONTACT_REQUEST = 1;

    //stores position of currently selected movie information from menu so that we can store/restore it on config change
    private int selected_movie_position=NO_SELECTION;
    private boolean movie_category_changed = false;

    //adapter for movie information menu in action bar
    private ArrayAdapter<String> action_bar_nav_adapter;

    //fragment manager and dynamic fragments
    private FragmentManager fragment_manager;
    private ClientFragment client_fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        SlidingPaneLayout sliding_layout=(SlidingPaneLayout)findViewById(R.id.sliding_layout);
        sliding_layout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        sliding_layout.setPanelSlideListener(new SliderListener());
        sliding_layout.openPane();



        fragment_manager=getFragmentManager();

        addNonUIFragment();
        if(savedInstanceState != null){
            selected_movie_position =savedInstanceState.getInt(SELECTED_MOVIE_IDX);
        }
        setUpActionBar();

    }




    private void addNonUIFragment() {
        Log.d(TAG, "addNonUIFragments()");

        client_fragment= (ClientFragment)fragment_manager.findFragmentByTag(CLIENT_FRAGMENT_TAG);

        FragmentTransaction ft= fragment_manager.beginTransaction();

        if(client_fragment==null){
            client_fragment=new ClientFragment();
            ft.add(client_fragment,CLIENT_FRAGMENT_TAG);
        }
        ft.commit();
        fragment_manager.executePendingTransactions();
    }



    private void setUpActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        String[] movie_information_list = getResources().getStringArray(R.array.movie_information);

        action_bar_nav_adapter = new ArrayAdapter<String>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
                android.R.id.text1);
        action_bar_nav_adapter.addAll(movie_information_list);

        actionBar.setListNavigationCallbacks(action_bar_nav_adapter, this);
    }

    @Override
    public void onListResponse(JSONArray json_array) {
        Log.d(TAG, "onListResponse()");

        MovieListFragment movie_list_fragment=(MovieListFragment)fragment_manager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        if (movie_list_fragment!=null){
            movie_list_fragment.update(JSONParser.parseMovieListJSON(json_array),movie_category_changed);
        }
    }

    @Override
    public void onDetailsResponse(JSONObject json_object) {

    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        Log.d(TAG, "onNavigationItemSelected : " + position);

        movie_category_changed = selected_movie_position != position;


        if (movie_category_changed) {

            String[] movie_information_controller = getResources().getStringArray(R.array.movie_information);
            String current_movie_information = movie_information_controller[position];

            client_fragment.getMovieList(current_movie_information);

            MovieListFragment movie_list_fragment = (MovieListFragment) fragment_manager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
            if (movie_list_fragment != null) {
                movie_list_fragment.setIsLoading(true);
            }

            selected_movie_position = position;

            MovieDetailsFragment movie_details_fragment = (MovieDetailsFragment) fragment_manager
                    .findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
            if (movie_details_fragment != null) {
                movie_details_fragment.clear();
            }
            RatingBar movie_details_rating=(RatingBar)findViewById(R.id.movie_details_rating);
            movie_details_rating.setVisibility(View.INVISIBLE);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MovieListFragment movieListFragment= (MovieListFragment) fragment_manager.findFragmentByTag(MOVIE_LIST_FRAGMENT_TAG);
        switch (item.getItemId()) {
            case android.R.id.home:
                SlidingPaneLayout sliding_layout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
                if (sliding_layout.isOpen() == false) {
                    sliding_layout.openPane();
                }
                return true;
            case R.id.browser:
                movieListFragment.startBrowser();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }


    @Override
    public void onMovieSelected(Movie movie) {

        MovieDetailsFragment movieDetailsFragment=(MovieDetailsFragment)fragment_manager.findFragmentByTag(MOVIE_DETAILS_FRAGMENT_TAG);
        if(movieDetailsFragment!=null){
            movieDetailsFragment.update(movie);
        }
        SlidingPaneLayout slidingPaneLayout=(SlidingPaneLayout)findViewById(R.id.sliding_layout);
        slidingPaneLayout.closePane();
    }

    public ImageLoader getImageLoader(){
        return client_fragment.getImageLoader();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(client_fragment!=null){
            client_fragment.cancelAllRequests();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        int selected_movie_idx = getActionBar().getSelectedNavigationIndex();
        outState.putInt(SELECTED_MOVIE_IDX,selected_movie_idx);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        SlidingPaneLayout sliding_layout = (SlidingPaneLayout) findViewById(R.id.sliding_layout);
        if (sliding_layout.isOpen()) {
            super.onBackPressed();
        } else {
            sliding_layout.openPane();
        }
    }

    public class SliderListener extends  SlidingPaneLayout.SimplePanelSlideListener{
        @Override
        public void onPanelOpened(View panel){
            getActionBar().setHomeButtonEnabled(false);
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }
        @Override
        public void onPanelClosed(View panel){
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
