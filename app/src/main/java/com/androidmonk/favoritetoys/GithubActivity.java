package com.androidmonk.favoritetoys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class GithubActivity extends AppCompatActivity {

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
    }

    private void makeGithubSearchQuery(){
        String githubQuery = mSearchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
        String githubSearchResult = null;

        //Create a new GithubQueryTask and call its execute method, passing in the url to query
        new GithubQueryTask().execute(githubSearchUrl);

    }

    private void showJsonDataView(){
        mLoadingJson.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(){
        mErrorMessage.setVisibility(View.VISIBLE);
        mLoadingJson.setVisibility(View.INVISIBLE);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            mLoadingJson.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0]; //This in string array
            String githubSearchResult = null;
            try {
                githubSearchResult = NetworkUtils.getResponseFromHttpUrl(searchURL);
            }catch (IOException e){
                e.printStackTrace();
            }
            return githubSearchResult;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null && !s.equals("")){
                showJsonDataView();
                mSearchResultsTextView.setText(s);
            }
            showErrorMessage();
        }
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
}
