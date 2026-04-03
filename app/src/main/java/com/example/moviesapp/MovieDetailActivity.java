package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MovieDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String trailerKey;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieId = getIntent().getIntExtra("movieId", -1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fetchMovieDetails();

        findViewById(R.id.playButton).setOnClickListener(v -> {
            if (trailerKey != null) {
                Intent intent = new Intent(this, VideoPlayer.class);
                intent.putExtra("videoUrl", "https://www.youtube.com/embed/" + trailerKey);
                startActivity(intent);
            }
        });
    }

    private void fetchMovieDetails() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=e21c9bd08ef733416fa4adc42dad2a14";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ((TextView)findViewById(R.id.textName)).setText(response.getString("title"));
                        ((TextView)findViewById(R.id.Details)).setText(response.getString("overview"));
                        String imgUrl = "https://image.tmdb.org/t/p/w500" + response.getString("poster_path");
                        Glide.with(this).load(imgUrl).into((ImageView)findViewById(R.id.imageview));
                    } catch (Exception e) { e.printStackTrace(); }
                }, null);
        Volley.newRequestQueue(this).add(request);
        fetchTrailerKey();
    }

    private void fetchTrailerKey() {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=e21c9bd08ef733416fa4adc42dad2a14";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        trailerKey = response.getJSONArray("results").getJSONObject(0).getString("key");
                    } catch (Exception e) { e.printStackTrace(); }
                }, null);
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cinema = new LatLng(33.596460, -7.615480); // Exemple Casablanca
        googleMap.addMarker(new MarkerOptions().position(cinema).title("Cinema"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cinema, 15));
    }
}
