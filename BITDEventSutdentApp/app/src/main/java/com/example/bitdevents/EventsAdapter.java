package com.example.bitdevents;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class EventsAdapter extends FirebaseRecyclerAdapter<EventModel, EventsAdapter.EventViewHolder> {


    public EventsAdapter(@NonNull FirebaseRecyclerOptions<EventModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull EventModel model) {
        Picasso.get().load(model.getImg()).into(holder.img);
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        holder.organiser.setText(model.getOrganisation());
        holder.guest.setText(model.getGuest());
        holder.date_time.setText(model.getDate()+","+model.getTime());
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new EventViewHolder(view);
    }


//    @Override
//    protected void onBindViewHolder(@NonNull PastViewHolder holder, int i, @NonNull Post post) {
//        holder.title.setText(post.getTitle());
//        holder.description.setText(post.getDescription());
//        holder.author.setText(post.getAuthor());
//    }
//
//    @NonNull
//    @Override
//    public PastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.post, parent, false);
//        return new PastViewHolder(view);
//    }

    static class EventViewHolder extends RecyclerView.ViewHolder{

        TextView title,description,guest,date_time,organiser;
        ImageView img;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cv_title);
            description = itemView.findViewById(R.id.cv_desc);
            guest = itemView.findViewById(R.id.cv_guest);
            date_time = itemView.findViewById(R.id.cv_date_time);
            organiser = itemView.findViewById(R.id.cv_organ);
            img =itemView.findViewById(R.id.cv_image);

        }
    }
}