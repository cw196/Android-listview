package com.example.wang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.wang.assignment2.R;
import com.example.wang.movies.Movie;

import java.util.List;


public class MovieListAdapter extends ArrayAdapter {

    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader=null;
    public MovieListAdapter(Context context, int item_layout_id,int default_text_id, List<Movie> movies, ImageLoader imageLoader) {
        super(context, item_layout_id, default_text_id, movies);
        layoutInflater = LayoutInflater.from(context);
        this.imageLoader=imageLoader;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.movies_list_item,null);

            holder = new ViewHolder();

            holder.thumbnail = (NetworkImageView)convertView.findViewById(R.id.movie_thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.movie_title);
            holder.MPAA = (TextView) convertView.findViewById(R.id.movie_MPPA);
            holder.ratingStar=(RatingBar)convertView.findViewById(R.id.movie_star);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = (Movie) this.getItem(position);

        holder.thumbnail.setImageUrl(movie.thumb_url,imageLoader);
        holder.title.setText(movie.title);
        holder.MPAA.setText(movie.mpaa_rating);
        holder.ratingStar.setRating(movie.rating/2);

        return convertView;
    }

    static class ViewHolder {
        NetworkImageView thumbnail;
        TextView title;
        TextView MPAA;
        RatingBar ratingStar;
    }
}
