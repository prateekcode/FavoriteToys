package com.androidmonk.favoritetoys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mToysListTextView;
    private EditText mEntryText;
    private Button mOpenGithubProject, mOpenRecyclerView, mDoExplicit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToysListTextView = (TextView) findViewById(R.id.tv_toy_name);
        mEntryText = findViewById(R.id.et_entry_text);
        mDoExplicit = findViewById(R.id.open_explicit_intent);
        mDoExplicit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEntered = mEntryText.getText().toString();
                Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, textEntered);
                Toast.makeText(MainActivity.this, "You entered " + textEntered, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
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
