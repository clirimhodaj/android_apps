package com.example.top10downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView listApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApp = (ListView) findViewById(R.id.xmlListView);
        Log.d(TAG,  "onCreate: starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");
    }
    // <Url, void, xml content>  --> me poshte
    private class DownloadData extends AsyncTask<String, Void, String>{
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParseApplications parseApplications = new ParseApplications();
            parseApplications.parse(s);

            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
                    MainActivity.this, R.layout.list_item, parseApplications.getApplications()
            );
            listApp.setAdapter(arrayAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        private String downloadXML(String urlPath){
            StringBuilder xmlResult = new StringBuilder();
            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "download: The response code was " + response);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){
                        break;
                    }
                    if (charsRead > 0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

            }
            catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            }
            catch (IOException e){
                Log.e(TAG,  "downloadXML: IO Exception reading data: " + e.getMessage());
            }
            return xmlResult.toString();
        }
    }
}