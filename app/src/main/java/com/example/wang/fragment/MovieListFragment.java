package com.example.wang.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.wang.Adapter.MovieListAdapter;
import com.example.wang.assignment2.MainActivity;
import com.example.wang.assignment2.R;
import com.example.wang.interfaces.MovieSelectionListener;
import com.example.wang.movies.Movie;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends ListFragment {
    private static final String			TAG						= "MovieListFragment";
    private static final int             NO_SELECTION = -1;
    private ArrayList<Movie>               movies=new ArrayList<Movie>();
    MovieSelectionListener                  movieSelectionListener;
    private MovieListAdapter               movieListAdapter;
    static final int                      PICK_CONTACT_REQUEST = 1;
    private int                           selected_item_position = NO_SELECTION;

    String movie_url="http://www.imdb.com/";
    public  MovieListFragment(){

    }

    @Override
    public void onAttach(Activity activity){
        Log.d(TAG,"onAttach");
        super.onAttach(activity);
        try{
            movieSelectionListener=(MovieSelectionListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+ " must implement MovieSelectionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            selected_item_position = savedInstanceState.getInt("selected_item_position");
        }
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        Log.d("List.onActivityCreated","onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        movieListAdapter =new MovieListAdapter(getActivity(), R.layout.movies_list_item, R.id.movie_title,movies,((MainActivity)getActivity()).getImageLoader());

        getListView().setAdapter(movieListAdapter);
    }

    public  void onResume(){
        Log.d(TAG, "onResume()");
        super.onResume();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setIsLoading(true);

        if(movieListAdapter.getCount()>0){
            setIsLoading(false);
        }

        if(movieListAdapter.getCount()>0&&selected_item_position!=NO_SELECTION){
            getListView().setItemChecked(selected_item_position,true);
            getListView().setSelection(selected_item_position);
            getListView().smoothScrollToPositionFromTop(selected_item_position, 200, 0);
            movieSelectionListener.onMovieSelected((Movie) movieListAdapter.getItem(selected_item_position));
        }
    }

    public  void setIsLoading(boolean is_loading){
        setListShown(!is_loading);
    }


    @Override
    public void onListItemClick(ListView l,View v,int position,long id){
        Log.d(TAG, "onListItemClick() : " + position);

        selected_item_position =position;

        getListView().setItemChecked(selected_item_position,true);
        Log.d(TAG+"1",((Movie) movieListAdapter.getItem(selected_item_position)).title);

        movieSelectionListener.onMovieSelected((Movie) movieListAdapter.getItem(selected_item_position));

    }

    public void startBrowser(){

        if(movieListAdapter.getCount()>0&& selected_item_position!=NO_SELECTION)
        {movie_url=((Movie) movieListAdapter.getItem(selected_item_position)).movie_url;}
        Uri webpage = Uri.parse(movie_url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = getActivity().getPackageManager();
        List activities = packageManager.queryIntentActivities(webIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        boolean isIntentSafe = activities.size() > 0;
        if(isIntentSafe) {
            startActivityForResult(webIntent,PICK_CONTACT_REQUEST);
        }

    }




    public void update(ArrayList<Movie> retrieved_movies, boolean movie_category_changed){
        Log.d("ListFragment.Update", "upDate");

        setIsLoading(false);

        movies.clear();
        movies.addAll(retrieved_movies);
        movieListAdapter.notifyDataSetChanged();

        if(movie_category_changed){
            getListView().setItemChecked(selected_item_position,false);
            selected_item_position = NO_SELECTION;
            getListView().setSelection(0);

        }else if(movieListAdapter.getCount()>0 && selected_item_position!=NO_SELECTION){
            getListView().setItemChecked(selected_item_position,true);
            getListView().setSelection(selected_item_position);
            getListView().smoothScrollToPositionFromTop(selected_item_position, 200, 0);
            movieSelectionListener.onMovieSelected((Movie) movieListAdapter.getItem(selected_item_position));
        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt("selected_item_position",selected_item_position);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }
}
