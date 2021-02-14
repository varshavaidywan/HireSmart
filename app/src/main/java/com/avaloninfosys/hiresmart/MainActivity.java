package com.avaloninfosys.hiresmart;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button button;
    TextView textView;
    Toolbar toolbar;
    EditText editText;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar(toolbar);
        getSupportActionBar().setTitle("Search GitHub Profiles");

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatetextView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.item1){
            Intent intent = new Intent(MainActivity.this, firebaseDb.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatetextView(){
        //make network call

        editText = findViewById(R.id.editText);

            name = editText.getText().toString();

        NetworkTask networkTask = new NetworkTask();
        networkTask.execute("https://api.github.com/search/users?q=" + name);

    }

    void makeNetworkCall(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: "+ result);
                ArrayList<GithubUser> users = parseJson(result);
                final GithubUserAdapter githubUserAdapter = new GithubUserAdapter(users);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = findViewById(R.id.rvUsers);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        recyclerView.setAdapter(githubUserAdapter);
                    }
                });

            }
        });
    }

    class NetworkTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");
                if(scanner.hasNext()){
                    String s = scanner.next();
                    return s;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Failed to load";
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);

            ArrayList<GithubUser> users = parseJson(s);
            //Log.e(TAG, "onPostExecute: " + users.size() );

            GithubUserAdapter githubUserAdapter = new GithubUserAdapter(users);
            RecyclerView recyclerView = findViewById(R.id.rvUsers);
            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

            recyclerView.setAdapter(githubUserAdapter);

        }
    }

    ArrayList<GithubUser> parseJson(String s){
        ArrayList<GithubUser> GithubUsers = new ArrayList<>();

        //parse the json

        try {
            JSONObject root = new JSONObject(s);
            JSONArray items = root.getJSONArray("items");
            for(int i = 0; i < items.length(); i ++){
                JSONObject object = items.getJSONObject(i);
                String login = object.getString("login");
                Integer id = object.getInt("id");
                String html_url = object.getString("html_url");
                String avatar_url = object.getString("avatar_url");
                Double score = object.getDouble("score");

                GithubUser githubUser = new GithubUser(login, id, html_url, score, avatar_url);
                GithubUsers.add(githubUser);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return GithubUsers;
    }
}
