package com.lorem.simpledictionary;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lorem.simpledictionary.db.OperationHelper;
import com.lorem.simpledictionary.db.DatabaseContract;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvWords;
    EditText searchQuery;
    ImageButton searchButton;

    private ArrayList<Word> list;
    private WordAdapter adapter;
    private OperationHelper operationHelper;

    private String langMode = DatabaseContract.TABLE_EN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvWords = (RecyclerView) findViewById(R.id.rv_words);
        searchQuery = (EditText) findViewById(R.id.edt_search);
        searchButton = (ImageButton) findViewById(R.id.btn_search);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataChanged(searchQuery.getText().toString());
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvWords.setLayoutManager(llm);

        DataChanged("");

    }

    private void DataChanged(String filter){
        adapter = new WordAdapter(this);
        operationHelper = new OperationHelper(this);

        rvWords.setAdapter(adapter);

        try {
            operationHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        list = operationHelper.query(langMode,filter);
        operationHelper.close();
        adapter.addItem(list);

        ItemClickSupport.addTo(rvWords).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent moveWithObjectIntent = new Intent(MainActivity.this, DetailActivity.class);
                moveWithObjectIntent.putExtra("WORD", list.get(position));
                startActivity(moveWithObjectIntent);
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (operationHelper != null){
            operationHelper.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.translation_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.en_to_id:
                langMode = DatabaseContract.TABLE_EN;
                DataChanged("");
                return true;
            case R.id.id_to_en:
                langMode = DatabaseContract.TABLE_ID;
                DataChanged("");
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
