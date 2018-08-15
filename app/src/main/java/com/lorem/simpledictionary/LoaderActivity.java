package com.lorem.simpledictionary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.lorem.simpledictionary.db.DatabaseContract;
import com.lorem.simpledictionary.db.OperationHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoaderActivity extends AppCompatActivity {


    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        progressBar = (ProgressBar) findViewById(R.id.pb_load);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Void> {
        final String TAG = LoadData.class.getSimpleName();
        OperationHelper operationHelper;
        OperationHelper operationID;
        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute() {
            operationHelper = new OperationHelper(LoaderActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {

            SharedPreferences preferences = getSharedPreferences("LOADER", Context.MODE_PRIVATE);
            Boolean firstRun = preferences.getBoolean("LOADED", true);

            if (firstRun) {

                ArrayList<Word> enWords = preLoadRaw(R.raw.english_indonesia);
                ArrayList<Word> idWords = preLoadRaw(R.raw.indonesia_english);

                progress = 10;
                publishProgress((int) progress);
                Double progressMaxInsert = 100.0;
                Double progressDiff = (progressMaxInsert - progress) / (enWords.size() + idWords.size());

                try {
                    operationHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                operationHelper.beginTransaction();
                try {
                    for (Word model : enWords) {
                        Log.d("INSRETION EN", "Inserting word " + model.getWord());
                        operationHelper.insertTransaction(DatabaseContract.TABLE_EN,model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }

                    for (Word model : idWords) {
                        Log.d("INSRETION ID", "Inserting word " + model.getWord());
                        operationHelper.insertTransaction(DatabaseContract.TABLE_ID,model);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }

                    // Jika semua proses telah di set success maka akan di commit ke database
                    operationHelper.setTransactionSuccess();
                } catch (Exception e) {
                    // Jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }
                operationHelper.endTransaction();
                operationHelper.close();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("LOADED", false);
                editor.apply();


                publishProgress((int) maxprogress);

            } else {
                try {
                    synchronized (this) {
                        this.wait(1000);

                        publishProgress(50);

                        this.wait(1000);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(LoaderActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<Word> preLoadRaw(int fileId) {
        ArrayList<Word> mahasiswaModels = new ArrayList<>();
        String line = null;
        BufferedReader reader;
        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(fileId);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                Word mahasiswaModel;

                mahasiswaModel = new Word(splitstr[0], splitstr[1]);
                mahasiswaModels.add(mahasiswaModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mahasiswaModels;
    }
}