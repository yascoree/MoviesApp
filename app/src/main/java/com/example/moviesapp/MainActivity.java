package com.example.moviesapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "e21c9bd08ef733416fa4adc42dad2a14";
    private static final String URL = "https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY;
    private RecyclerView recyclerView;
    private MyMovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchMovies();
    }

    private void fetchMovies() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        MyMovieData[] movies = new MyMovieData[results.length()];
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject obj = results.getJSONObject(i);
                            movies[i] = new MyMovieData(
                                    obj.getInt("id"),
                                    obj.getString("title"),
                                    obj.getString("release_date"),
                                    obj.getString("poster_path")
                            );
                        }
                        adapter = new MyMovieAdapter(movies, this);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        Log.e("MainActivity", "JSON Parsing error", e);
                    }
                }, error -> Log.e("Volley", error.toString()));
        queue.add(request);
    }
}
