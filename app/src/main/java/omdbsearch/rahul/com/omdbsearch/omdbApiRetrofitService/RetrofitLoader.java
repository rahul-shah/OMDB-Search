package omdbsearch.rahul.com.omdbsearch.omdbApiRetrofitService;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;

import omdbsearch.rahul.com.omdbsearch.model.Movie;
import omdbsearch.rahul.com.omdbsearch.model.Result;

/**
 * Created by shahr on 05/01/17.
 */
public class RetrofitLoader extends AsyncTaskLoader<RetrofitHelper.ResultWithDetail> {

    private static final String LOG_TAG = "RetrofitLoader";

    private final String mTitle;

    private RetrofitHelper.ResultWithDetail mData;

    public RetrofitLoader(Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    public RetrofitHelper.ResultWithDetail loadInBackground()
    {
        // get results from calling API
        try
        {
            Result result =  RetrofitHelper.performSearch(mTitle);
            RetrofitHelper.ResultWithDetail resultWithDetail = new RetrofitHelper.ResultWithDetail(result);
            if(result.Search != null)
            {
                for(Movie movie: result.Search)
                {
                    resultWithDetail.addToList(RetrofitHelper.getDetail(movie.imdbID));
                }
            }
            return  resultWithDetail;
        } catch(final IOException e) {
            Log.e(LOG_TAG, "Error from api access", e);
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData); // makes loaderManager call onLoadFinished
        } else {
            forceLoad(); // calls the loadInBackground
        }
    }


    @Override
    protected void onReset() {
        Log.d(LOG_TAG, "onReset");
        super.onReset();
        mData = null;
    }

    @Override
    public void deliverResult(RetrofitHelper.ResultWithDetail data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        RetrofitHelper.ResultWithDetail oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

    }
}
