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

import com.example.bitdeadmin.R;
import com.example.bitdeadmin.model.EventModel;
import com.example.bitdeadmin.model.slideImageModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class slideImageAdapter extends FirebaseRecyclerAdapter<slideImageModel, slideImageAdapter.EventViewHolder> {

    public slideImageAdapter(@NonNull FirebaseRecyclerOptions<slideImageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    Context context;


    @NonNull
    @Override
    public slideImageAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view2, parent, false);
        return new slideImageAdapter.EventViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull slideImageModel model) {
        Picasso.get().load(model.getSlideImgLink()).into(holder.img);

        holder.delete.setOnClickListener(v -> {
            AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context);

            deleteBuilder.setTitle("Delete Event");
            deleteBuilder.setMessage("After Deleting, you cant make calls ").setPositiveButton("Delete", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseDatabase.getInstance().getReference().child("events").child("slideImage")
                            .child(getItem(position).getSlideImgKey()).removeValue();
                    StorageReference desertRef = FirebaseStorage.getInstance().getReference().child("events")
                            .child(getItem(position).getSlideImgKey());
                    // Delete the file
                    desertRef.delete();
                }

            }).setNegativeButton("Cancel",null);

            AlertDialog alert = deleteBuilder.create();
            alert.show();
        });
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
            img =itemView.findViewById(R.id.cv2_image);
            delete = itemView.findViewById(R.id.cv2_delete);

        }
    }
}
