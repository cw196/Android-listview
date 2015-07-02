package com.example.wang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.wang.assignment2.R;
import com.example.wang.movies.Movie;

/**
 * Created by wang on 4/17/2015.
 */
public class MovieDetailsFragment extends Fragment{

    private static final String	TAG	= "MovieDetailsFragment";

    public MovieDetailsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View details_view=inflater.inflate(R.layout.movie_details_fragment,container,false);

        return details_view;
    }

    //update UI with new property details
    public void update(Movie movie){

        TextView title_view = (TextView)getView().findViewById((R.id.movie_details_title));
        RatingBar ratingBar_view=(RatingBar)getView().findViewById(R.id.movie_details_rating);
        TextView MPPA_view=(TextView)getView().findViewById(R.id.movie_details_MPPA);
        TextView passage_view=(TextView)getView().findViewById(R.id.movie_details_passage);

        title_view.setText(movie.title);
        ratingBar_view.setVisibility(View.VISIBLE);
        ratingBar_view.setRating(movie.rating/2);
        MPPA_view.setText(movie.mpaa_rating);
        passage_view.setText(movie.synopsis);
    }
    public void clear(){
        TextView title_view = (TextView)getView().findViewById((R.id.movie_details_title));
        RatingBar ratingBar_view=(RatingBar)getView().findViewById(R.id.movie_details_rating);
        TextView MPPA_view=(TextView)getView().findViewById(R.id.movie_details_MPPA);
        TextView passage_view=(TextView)getView().findViewById(R.id.movie_details_passage);

        title_view.setText(" ");
        ratingBar_view.setRating(0);
        MPPA_view.setText(" ");
        passage_view.setText(" ");
    }
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
    }
}
