package omdbsearch.rahul.com.omdbsearch.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import omdbsearch.rahul.com.omdbsearch.R;
import omdbsearch.rahul.com.omdbsearch.Utils.CommonUtils;
import omdbsearch.rahul.com.omdbsearch.adapter.MovieRecyclerViewAdapter;
import omdbsearch.rahul.com.omdbsearch.omdbApiRetrofitService.RetrofitHelper;
import omdbsearch.rahul.com.omdbsearch.omdbApiRetrofitService.RetrofitLoader;

/**
 * Created by shahr on 05/01/17.
 */

public class MainSearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<RetrofitHelper.ResultWithDetail>
{
    @BindView(R.id.search_button) Button mSearchButton;
    @BindView(R.id.search_edittext) EditText mSearchEditText;
    @BindView(R.id.recycler_view) RecyclerView mMovieListRecyclerView;
    @BindView(R.id.progress_spinner) ProgressBar mProgressBar;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private String mMovieTitle;
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymainsearch);

        ButterKnife.bind(this);

        setUpViewControllers();

        mMovieAdapter = new MovieRecyclerViewAdapter(null,this);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(null);
        // Attach the layout manager to the recycler view
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        getSupportLoaderManager().enableDebugLogging(true);
    }

    private void setUpViewControllers()
    {
        // set action for pressing search button on keyboard
        mSearchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))) {
                    startSearch();
                    handled = true;
                }
                return handled;
            }
        });

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                startSearch();
            }
        });
    }

    @OnClick(R.id.search_button)
    public void searchButtonClicked(View view)
    {
        startSearch();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mMovieTitle", mMovieTitle);
        outState.putInt("progress_visibility",mProgressBar.getVisibility());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        int progress_visibility= savedInstanceState.getInt("progress_visibility");
        // if the progressBar was visible before orientation-change
        if(progress_visibility == View.VISIBLE)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        // init the loader, so that the onLoadFinished is called
        mMovieTitle = savedInstanceState.getString("mMovieTitle");
        if (mMovieTitle != null)
        {
            Bundle args = new Bundle();
            args.putString("movieTitle", mMovieTitle);
            getSupportLoaderManager().initLoader(LOADER_ID, args, this);
        }
    }

    @Override
    public Loader<RetrofitHelper.ResultWithDetail> onCreateLoader(int id, Bundle args) {
        return new RetrofitLoader(MainSearchActivity.this, args.getString("movieTitle"));
    }

    @Override
    public void onLoadFinished(Loader<RetrofitHelper.ResultWithDetail> loader, RetrofitHelper.ResultWithDetail resultWithDetail) {
        mProgressBar.setVisibility(View.GONE);
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        if(resultWithDetail.getResponse().equals("True")) {
            mMovieAdapter.swapData(resultWithDetail.getMovieDetailList());
        } else {
            //Snackbar.make(mMovieListRecyclerView, getResources().getString(R.string.snackbar_title_not_found), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<RetrofitHelper.ResultWithDetail> loader) {
        mMovieAdapter.swapData(null);
    }

    private void startSearch() {
        //Check if network is available
        if(CommonUtils.isNetworkAvailable(getApplicationContext()))
        {
            //Hide keyboard
            //CommonUtils.hideSoftKeyboard(MainSearchActivity.this);
            String movieTitle = mSearchEditText.getText().toString().trim();
            if (!movieTitle.isEmpty())
            {
                Bundle args = new Bundle();
                args.putString("movieTitle", movieTitle);
                getSupportLoaderManager().restartLoader(LOADER_ID, args, this);
                mMovieTitle = movieTitle;
                mProgressBar.setVisibility(View.VISIBLE);
                mMovieListRecyclerView.setVisibility(View.GONE);
            }
            else
            {
                Snackbar.make(mMovieListRecyclerView, getResources().getString(R.string.snackbar_title_empty), Snackbar.LENGTH_LONG).show();
            }
        }
        else
        {
            Snackbar.make(mMovieListRecyclerView, getResources().getString(R.string.network_not_available), Snackbar.LENGTH_LONG).show();
        }
    }
}