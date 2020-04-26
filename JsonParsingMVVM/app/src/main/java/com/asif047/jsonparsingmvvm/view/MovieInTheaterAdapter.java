package com.asif047.jsonparsingmvvm.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.asif047.jsonparsingmvvm.R;
import com.asif047.jsonparsingmvvm.apiUtils.Image;
import com.asif047.jsonparsingmvvm.apiUtils.NetworkState;
import com.asif047.jsonparsingmvvm.apiUtils.ServiceGenerator;
import com.asif047.jsonparsingmvvm.dataSource.MoviesInTheaterDataSource;
import com.asif047.jsonparsingmvvm.model.Movie;
import com.bumptech.glide.Glide;

public class MovieInTheaterAdapter extends PagedListAdapter<Movie, RecyclerView.ViewHolder> {

    private static final String TAG = "MoviesInTheaterAdapter";
    public static final int MOVIE_ITEM_VIEW_TYPE = 1;
    public static final int LOAD_ITEM_VIEW_TYPE = 0;

    private Context mContext;
    private NetworkState mNetworkState;

    public MovieInTheaterAdapter(Context context) {
        super(Movie.DIFF_CALL);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

            view = inflater.inflate(R.layout.row_item_movies, parent, false);
            return new MovieViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Movie movie = getItem(position);
            movieViewHolder.bind(movie, mContext);
        }

    }


    public void setNetworkState(NetworkState networkState) {
        NetworkState prevState = networkState;
        boolean wasLoading = isLoading();

        mNetworkState = networkState;
        boolean willLoad = isLoading();

        if(wasLoading != willLoad) {
            if(wasLoading) notifyItemRemoved(getItemCount());
            else {
                notifyItemInserted(getItemCount());
            }
        }

    }

    public boolean isLoading() {
        return (mNetworkState != null && mNetworkState != NetworkState.LOADED);
    }


    private static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvOverview;
        ImageView ivMovie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivMovie = itemView.findViewById(R.id.ivMovie);

        }

        public void bind(Movie movie, Context context) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            Image image = new Image(ServiceGenerator.IMAGE_BASE_URL);


            Glide.with(context).load( image.getLowQualityImagePath()+ movie.getPosterPath()).into(ivMovie);
        }

        private static class ProgressViewHolder extends RecyclerView.ViewHolder {

            public ProgressViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }


}
