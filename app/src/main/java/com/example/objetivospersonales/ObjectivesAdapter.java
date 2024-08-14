package com.example.objetivospersonales;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ObjectivesAdapter extends RecyclerView.Adapter<ObjectivesAdapter.MyViewHolder> {

    Context context;

    ArrayList<dtObjectives> list;
    public ObjectivesAdapter(Context context, ArrayList<dtObjectives> list, HomeActivity homeActivity) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ObjectivesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.view_objective, null);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ObjectivesAdapter.MyViewHolder holder, int position) {
        dtObjectives objectives = list.get(position);
        holder.item_title.setText(objectives.getName());
        holder.item_date.setText(objectives.getDate());
        holder.item_description.setText(objectives.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OnlyObjectiveActivity.class);
                intent.putExtra("id", objectives.getId());
                context.startActivity(intent);
            }
        });
    };
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_title, item_date, item_description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            item_date = itemView.findViewById(R.id.item_date);
            item_description = itemView.findViewById(R.id.item_description);
        }
    }




}
