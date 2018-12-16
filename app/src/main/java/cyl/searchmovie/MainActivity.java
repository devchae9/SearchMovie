package cyl.searchmovie;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import cyl.searchmovie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding amb;
    InputMethodManager keyboard;
    Boolean doublepress = false;
    int backpressed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amb = DataBindingUtil.setContentView(this, R.layout.activity_main);
        amb.frameNofile.setVisibility(View.INVISIBLE);
        amb.framePb.setVisibility(View.INVISIBLE);

        keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        amb.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amb.frameNofile.setVisibility(View.GONE);
                keyboard.hideSoftInputFromWindow(amb.txtSearch.getWindowToken(),0);
                new getMovie().execute();
            }
        });


    }

    @Override
    public void onBackPressed() {
        backpressed += 1;
        if (backpressed == 1) {
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (backpressed == 2) {
            finish();
            super.onBackPressed();
        }

        new Handler().postDelayed(new Runnable() { //1초내에 back버튼을 한번 더 누르지 않을시 doublepress 는 false로 변경
            @Override
            public void run() {backpressed = 0;}
        }, 1000);

    }

    private class getMovie extends AsyncTask<Void, Void, Void> {
        List<MovieInfo> mi = new ArrayList();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            amb.framePb.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String movie_title=  amb.txtSearch.getText().toString();
            final String clientId = "FP8EGtgi3aQey16rSxYk";
            final String clientSecret = "sjOBFtYYEY";
            try {
                String text = URLEncoder.encode(movie_title, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/movie.json?query="   + text + "&display=100"; // 최대 100개가능하여 최대치로
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                int responseCode = con.getResponseCode();

                BufferedReader br;
                if(responseCode==200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                JSONObject obj = new JSONObject(response.toString());
                JSONArray arr = obj.getJSONArray("items");

                for (int i = 0; i < arr.length(); i++) { //json 데이터를 mi 리스트로 추가
                    String title = arr.getJSONObject(i).getString("title");
                    String director = arr.getJSONObject(i).getString("director");
                    String imagelink = arr.getJSONObject(i).getString("image");
                    String date = arr.getJSONObject(i).getString("pubDate");
                    String actor = arr.getJSONObject(i).getString("actor");
                    String rating = arr.getJSONObject(i).getString("userRating");
                    String link = arr.getJSONObject(i).getString("link");
                    mi.add(new MovieInfo(title,director,imagelink,date,actor,rating, link));
                }



            } catch (Exception e) {
                Log.v("movie_error", e.toString()); //에러시 발생
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            amb.recyclerview.setHasFixedSize(true);
            amb.recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            Adapter adt = new Adapter(getApplicationContext(), mi);
            amb.recyclerview.setAdapter(adt);
            try {
                 Boolean a = mi.isEmpty(); //리스트에 데이터가 비어있는지 확인합니다
                 if (a) {//비어있을시 결과물이 없습니다 표기
                     amb.frameNofile.setVisibility(View.VISIBLE);
                 } else if (!a) {//리스트에 데이터가 있을시
                     amb.frameNofile.setVisibility(View.GONE);
                 }

                amb.framePb.setVisibility(View.INVISIBLE);
            } catch (Exception e){
                Log.e("Error", e.toString());
            }
        }


    }

}
