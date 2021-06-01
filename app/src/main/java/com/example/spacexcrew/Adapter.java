package com.example.spacexcrew;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.spacexcrew.CrewDB.Crew;

import java.io.Serializable;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Crew> CrewList;
    private Context context;

    public Adapter(Context context2, List<Crew> list) {
        this.context = context2;
        this.CrewList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.items,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(CrewList.get(position).getImage()).into(holder.imageView);
        Glide.with(context).load(CrewList.get(position).getImage()).override(15, 15).into(holder.imageView2);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(context, Profile.class);
                intent.putExtra("detail", CrewList.get(position));
                context.startActivity(intent);
            }
        });
        holder.edName.setText(CrewList.get(position).getName());
        holder.edAgency.setText(CrewList.get(position).getAgency());
    }

    @Override
    public int getItemCount() {
        return CrewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public TextView edAgency;
        public TextView edName;
        public ImageView imageView;
        public ImageView imageView2;

        public ViewHolder(View itemview) {
            super(itemview);
            imageView =  itemview.findViewById(R.id.backImage);
            imageView2 = itemview.findViewById(R.id.imgBlur);
            edName = itemview.findViewById(R.id.edName);
            edAgency = itemview.findViewById(R.id.edAgency);
            cardView = itemview.findViewById(R.id.personalCard);
        }
    }
}
