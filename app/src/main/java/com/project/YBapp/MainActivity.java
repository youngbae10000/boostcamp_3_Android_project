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

    private String clientId = "SozSTvGuCAU2mIBAOqo2"; //네이버에서 발급받은 애플리케이션 클라이언트 아이디값";
    private String clientSecret = "h4QZ1Ceunx"; //네이버에서 발급받은애플리케이션 클라이언트 시크릿값";
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

        ////RequestQueue 초기화 코드

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
        //리사이클러 뷰 클릭 이벤트
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
        ////RequestQueue 초기화 코드

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext()); //Context 객체 전달
        }
    }

    //코드출력
    public void println(String data) {

        textView.append(data + "\n"); //한 줄씩 추가 append?
    }


    //Request Queue;
    //Request 객체를 만들고
    public void sendRequest(String title) {

        String NAVERURL = "https://openapi.naver.com/v1/search/movie.json?dispaly=100&query=" + title;

        StringRequest request = new StringRequest(

                Request.Method.GET,
                NAVERURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response =>", response);
                        //println("응답 -> " + response);

                        //응답처리 Json -> java 객체로
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("ERROR_RESPONSE =>", error.toString());
                        //println("에러 ->" + error.getMessage());
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

        //volley는 catshing을 하기 때문에
        request.setShouldCache(false); //받은 결과를 그대로 보여주세요
        AppHelper.requestQueue.add(request);//request 객체를 넣어 준댜
        //println("요첨 보냄."); //console 에서
    }



    //응답처리 Json -> java 객체로
    public void processResponse(String response) {

        Gson gson = new Gson();

        adapter.items.clear();
        MovieList moveList = gson.fromJson(response, MovieList.class);//fromJson 라이브러리

        if (moveList != null) {

            for (int i = 0; i < moveList.items.size(); i++) {

                String t_title = moveList.items.get(i).title;
                String t_link = moveList.items.get(i).link;
                String t_image = moveList.items.get(i).image;
                String t_subtitle = moveList.items.get(i).subtitle;
                String t_pubDate = moveList.items.get(i).pubDate;
                String t_director = moveList.items.get(i).director;
                String t_actor = moveList.items.get(i).actor;
                String t_userRating = moveList.items.get(i).userRating;

                //MovieAdapter adapter;
                adapter.addItem(new Movie(t_title,t_link,t_image,t_subtitle,t_pubDate,t_director,t_actor,t_userRating));
            }

            recyclerView.setAdapter(adapter);
        }
    }
}