package cyl.searchmovie;

import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import cyl.searchmovie.databinding.ActivityMainBinding;

public class Adapter extends RecyclerView.Adapter<movieholder> {
    private Context ctx;
    private List<MovieInfo> mi;

    public Adapter(Context ctx, List<MovieInfo> mi) {
        this.ctx = ctx;
        this.mi = mi;
    }


    @Override
    public movieholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View v = inflater.inflate(R.layout.cardview, null);
        movieholder holder = new movieholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(movieholder holder, int position) {
        final MovieInfo movie_info = mi.get(position);

        try {//인터넷 url에서 포스터를 불러오는 작업
            Picasso.get().load(movie_info.getImagelink()).into(holder.imagelink);
        } catch (IllegalArgumentException iae) {//포스터가 없을시 일어나는 에러를 잡아내는 작업
            holder.imagelink.setImageResource(R.color.white); //포스터가 없을시 흰 배경만 보이게 설정
        }
        holder.title.setText(Html.fromHtml(movie_info.getTitle()));
        holder.director.setText(movie_info.getDirector());
        holder.actor.setText(movie_info.getActor());
        holder.date.setText(movie_info.getDate());

        Float rate = Float.valueOf(movie_info.getRating()) * 0.5F; //10점만점 레이팅을 5점에 맞게 작업
        holder.rating.setRating(rate);


        holder.cv.setOnClickListener(new View.OnClickListener() { //카드뷰 터치시 관련 웹사이트로 이동동
            @Override
           public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(); //크롬을 엽니다
                builder.setToolbarColor(ctx.getResources().getColor(R.color.colorPrimary)); //툴바 색설정
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(ctx, Uri.parse(movie_info.getUrllink())); //인터넷 url 여는작업
            }
        });

    }


    @Override
    public int getItemCount() {
        return mi.size();
    }
}


class movieholder extends RecyclerView.ViewHolder{
    TextView title, director, date, actor;
    RatingBar rating;
    ImageView imagelink;
    CardView cv;


    public movieholder(final View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.txt_title);
        director = itemView.findViewById(R.id.txt_director);
        date = itemView.findViewById(R.id.txt_date);
        imagelink = itemView.findViewById(R.id.iv_poster);
        actor = itemView.findViewById(R.id.txt_actor);
        rating = itemView.findViewById(R.id.ratingBar);
        cv = itemView.findViewById(R.id.cardview);
    }


}