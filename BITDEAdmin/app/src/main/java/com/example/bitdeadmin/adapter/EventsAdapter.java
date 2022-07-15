package com.example.bitdeadmin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bitdeadmin.model.EventModel;
import com.example.bitdeadmin.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EventsAdapter extends FirebaseRecyclerAdapter<EventModel, EventsAdapter.EventViewHolder> {


    private Context context;
    private String cat;
    public EventsAdapter(@NonNull FirebaseRecyclerOptions<EventModel> options, Context c, String cat) {
        super(options);
        context = c;
       this.cat = cat;
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
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);

                deleteBuilder.setTitle("Delete Event");
                deleteBuilder.setMessage("After Deleting, you cant make calls ").setPositiveButton("Delete", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       FirebaseDatabase.getInstance().getReference().child("events").child(cat)
                               .child(getItem(position).getId()).removeValue();
                        StorageReference desertRef = FirebaseStorage.getInstance().getReference().child("events")
                                .child(getItem(position).getId());
                        // Delete the file
                        desertRef.delete();
                    }

                }).setNegativeButton("Cancel",null);

                AlertDialog alert = deleteBuilder.create();
                alert.show();
            }
        });
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
        Button delete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.cv_title);
            description = itemView.findViewById(R.id.cv_desc);
            guest = itemView.findViewById(R.id.cv_guest);
            date_time = itemView.findViewById(R.id.cv_date_time);
            organiser = itemView.findViewById(R.id.cv_organ);
            img =itemView.findViewById(R.id.cv_image);
            delete = itemView.findViewById(R.id.cv_delete);

        }
    }
}