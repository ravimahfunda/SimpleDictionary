package com.lorem.simpledictionary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView wordLabel = (TextView)findViewById(R.id.label_word);
        TextView translationLabel = (TextView)findViewById(R.id.label_translation);

        Word word = getIntent().getParcelableExtra("WORD");
        wordLabel.setText(word.getWord());
        translationLabel.setText(word.getTranslation());
    }
}
