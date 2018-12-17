package com.project.YBapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String clientId = "SozSTvGuCAU2mIBAOqo2";
    private String clientSecret = "h4QZ1Ceunx";
    String keyword;

    TextView textView;
    EditText editText;
    Button button;

    RecyclerView recyclerView;
    MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView1);
        editText = (EditText) findViewById(R.id.editText);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                keyword = editText.getText().toString();
                sendRequest(keyword);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MovieAdapter(getApplicationContext());

        adapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieAdapter.ViewHolder holder, View view, int position) {
                Movie item = adapter.getItem(position);

               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
               startActivity(intent);

            }
        });

    }

    public void init() {

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    public void println(String data) {

        textView.append(data + "\n");
    }

    public void sendRequest(String title) {

        String NAVERURL = "https://openapi.naver.com/v1/search/movie.json?dispaly=100&query=" + title;

        StringRequest request = new StringRequest(

                Request.Method.GET,
                NAVERURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Response =>", response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("ERROR_RESPONSE =>", error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                Log.d("getHedaer =>", "" + params);
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {

        Gson gson = new Gson();

        adapter.items.clear();
        MovieList moveList = gson.fromJson(response, MovieList.class);

        if (moveList != null) {

            for (int i = 0; i < moveList.items.size(); i++) {

                String tmp = moveList.items.get(i).title;
                String t_title = null;
                t_title = tmp.replace("<b>", "");
                t_title = t_title.replace("</b>", "");
                t_title = t_title.replace("&amp;", ":");
                String t_link = moveList.items.get(i).link;
                String t_image = moveList.items.get(i).image;
                String t_subtitle = moveList.items.get(i).subtitle;
                String t_pubDate = moveList.items.get(i).pubDate;
                String t_director = moveList.items.get(i).director;
                String t_actor = moveList.items.get(i).actor;
                String t_userRating = moveList.items.get(i).userRating;

                adapter.addItem(new Movie(t_title,t_link,t_image,t_subtitle,t_pubDate,t_director,t_actor,t_userRating));
            }

            recyclerView.setAdapter(adapter);
        }
    }
}