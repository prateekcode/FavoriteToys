package com.androidmonk.favoritetoys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mToysListTextView;
    private Button mOpenGithubProject, mOpenRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToysListTextView = (TextView) findViewById(R.id.tv_toy_name);
        mOpenGithubProject = (Button) findViewById(R.id.open_github_project);
        mOpenGithubProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GithubActivity.class);
                startActivity(intent);

            }
        });
        mOpenRecyclerView = findViewById(R.id.open_green_recycler);
        mOpenRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
            }
        });

        String[] toyNames = ToyBox.getToyNames();

        for (String toyName : toyNames){
            mToysListTextView.append(toyName + "\n\n\n");
        }
    }
}
