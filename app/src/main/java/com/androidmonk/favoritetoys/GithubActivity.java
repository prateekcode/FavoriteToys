package com.androidmonk.favoritetoys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmonk.favoritetoys.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class GithubActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static final String SEARCH_QUERY_URL = "query";
    //public static final String SEARCH_RESULT_RAW_JSON = "results";

    public static final int GITHUB_SEARCH_LOADER = 22;

    private EditText mSearchBoxEditText;
    private TextView mUrlDisplayTextView, mSearchResultsTextView, mErrorMessage;
    private ProgressBar mLoadingJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);

        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.search_url_text_box);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingJson = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null){
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL);
            //String rawJsonResult = savedInstanceState.getString(SEARCH_RESULT_RAW_JSON);

            mUrlDisplayTextView.setText(queryUrl);
            //mSearchResultsTextView.setText(rawJsonResult);
        }
        getSupportLoaderManager().initLoader(GITHUB_SEARCH_LOADER, null, this);
    }

    private void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();

        if (TextUtils.isEmpty(githubQuery)){
            mUrlDisplayTextView.setText("No query entered, nothing to search for");
            return;
        }

        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        String githubSearchResult = null;

        //Create a new GithubQueryTask and call its execute method, passing in the url to query
        //new GithubQueryTask().execute(githubSearchUrl);
        //Removing the call that execute AsyncTask

        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL, githubSearchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(GITHUB_SEARCH_LOADER);
        if (githubSearchLoader == null){
            loaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }else {
            loaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this);
        }

    }

    private void showJsonDataView(){
        mLoadingJson.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessage.setVisibility(View.VISIBLE);
        mLoadingJson.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mGithubJson;

            @Override
            protected void onStartLoading() {
                if (args == null){
                    return;
                }
                if (mGithubJson!=null){
                    deliverResult(mGithubJson);
                }else {
                    mLoadingJson.setVisibility(View.VISIBLE);
                    forceLoad();
                }

            }

            @Nullable
            @Override
            public String loadInBackground() {
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL);
                if (TextUtils.isEmpty(searchQueryUrlString)){
                    return null;
                }

                try {
                    URL searchURL = new URL(searchQueryUrlString); //This in string array
                    String githubSearchResult = null;
                    githubSearchResult = NetworkUtils.getResponseFromHttpUrl(searchURL);
                    return githubSearchResult;
                }catch (IOException e){
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(@Nullable String githubJson) {
                mGithubJson = githubJson;
                super.deliverResult(githubJson);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mLoadingJson.setVisibility(View.INVISIBLE);
        if (data == null && !data.equals("")){
            showJsonDataView();
        }else {
            mSearchResultsTextView.setText(data);
            showErrorMessage();
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        //Do nothing in this method
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemThatWasSelected = item.getItemId();
        if (itemThatWasSelected == R.id.search_query){
            Context context = GithubActivity.this;
            String textToShow = "Search Clicked";
            Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL, queryUrl);

        //String rawJsonResult = mSearchResultsTextView.getText().toString();
        //outState.putString(SEARCH_RESULT_RAW_JSON, rawJsonResult);
    }
}
