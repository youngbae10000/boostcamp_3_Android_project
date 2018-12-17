package com.project.YBapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    ArrayList<Movie> items = new ArrayList<Movie>();

    OnItemClickListener listener;

    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder,View view, int position);
    }

    public MovieAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.movie_item,parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

            Movie item = items.get(position);

            viewHolder.setItem(item);

            viewHolder.setOnItemClickListener(listener);
    }

    public Movie getItem(int position){
        return items.get(position);
    }

    public void addItem(Movie item){
        items.add(item);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView1,textView2,textView3,textView4;
        ImageView imageView;
        RatingBar ratingBar;

        OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                textView1 = (TextView) itemView.findViewById(R.id.textView1);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
                textView2 = (TextView) itemView.findViewById(R.id.textView2);
                textView3 = (TextView) itemView.findViewById(R.id.textView3);
                textView4 = (TextView) itemView.findViewById(R.id.textView4);
                ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }


        public void setItem(Movie item){
            textView1.setText(item.getTitle());

            ImageLoadTask task = new ImageLoadTask(item.getImage(), imageView);
            task.execute();

            ratingBar.setRating(Float.parseFloat(item.getUserRating()));
            textView2.setText(item.getPubDate());
            textView3.setText(item.getDirector());
            textView4.setText(item.getActor());
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

    }
}
