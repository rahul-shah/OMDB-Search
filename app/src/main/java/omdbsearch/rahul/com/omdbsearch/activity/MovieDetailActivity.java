package omdbsearch.rahul.com.omdbsearch.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import omdbsearch.rahul.com.omdbsearch.R;
import omdbsearch.rahul.com.omdbsearch.model.Detail;

/**
 * Created by shahr on 05/01/17.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_title) TextView mMovieTitle;
    @BindView(R.id.movie_writers) TextView mMovieWriters;
    @BindView(R.id.movie_actors) TextView mMovieActors;
    @BindView(R.id.movie_director) TextView mMovieDirector;
    @BindView(R.id.movie_genre) TextView mMovieGenre;
    @BindView(R.id.movie_released) TextView mMovieReleased;
    @BindView(R.id.movie_plot) TextView mMoviePlot;
    @BindView(R.id.movie_runtime) TextView mMovieRuntime;

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymoviedetail);

        ButterKnife.bind(this);

        final Detail detail = getIntent().getParcelableExtra(MOVIE_DETAIL);
        final String imageUrl =  getIntent().getStringExtra(IMAGE_URL);
        Glide.with(this).load(imageUrl).into( (ImageView) findViewById(R.id.main_backdrop));

        // set title for the appbar
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(detail.Title);

        mMovieTitle.setText(detail.Title);
        mMovieWriters.setText(detail.Writer);
        mMovieActors.setText(detail.Actors);
        mMovieDirector.setText(detail.Director);
        mMovieGenre.setText(detail.Genre);
        mMovieReleased.setText(detail.Released);
        mMoviePlot.setText(detail.Plot);
        mMovieRuntime.setText(detail.Runtime);
    }
}
