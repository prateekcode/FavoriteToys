package com.androidmonk.favoritetoys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ChildActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        mTextView = findViewById(R.id.tv_display);

        Intent intentThatStarted = getIntent();
        if (intentThatStarted.hasExtra(Intent.EXTRA_TEXT)){
            String textEntered = intentThatStarted.getStringExtra(Intent.EXTRA_TEXT);
            mTextView.setText(textEntered);
        }

    }

    public void onClickOpenWebAddress(View view) {
        Toast.makeText(this, "Open Web Address", Toast.LENGTH_SHORT).show();
        String urlAsString = "http://www.udacity.com";
        openWebPage(urlAsString);
    }

    public void onClickOpenAddress(View view){
        String addressString = "1600 Amphitheatre Parkway, CA";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .query(addressString);
        Uri addressUri = builder.build();
        showMap(addressUri);
        

        Toast.makeText(this, "Open Location in Map", Toast.LENGTH_SHORT).show();
    }

    public void onClickOpenShare(View view){
        String shareText = "Hello there";
        shareText(shareText);
        Toast.makeText(this, "Share text to App", Toast.LENGTH_SHORT).show();
    }

    public void onCreateOwn(View view){
        Toast.makeText(this, "Create your own", Toast.LENGTH_SHORT).show();
    }

    public void openWebPage(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void showMap(Uri uri){
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void shareText(String text){
        String mimeType = "text/plain";
        String title = "Learn how to share";
        ShareCompat.IntentBuilder
                .from(this)
                .setChooserTitle(title)
                .setType(mimeType);
    }

}
