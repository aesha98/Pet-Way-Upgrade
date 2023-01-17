package com.example.petway.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petway.Model.Vet;
import com.example.petway.R;
import com.example.petway.VetDetailActivity;

import java.util.List;

public class VetAdapter extends RecyclerView.Adapter<VetAdapter.VetViewHolder> {
    private final List<Vet> vetList;
    private final Context context;

    public VetAdapter(List<Vet> vetList, Context context) {
        this.vetList = vetList;
        this.context = context;
    }

    @NonNull
    @Override
    public VetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adoption_list, parent, false);
        return new VetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VetViewHolder holder, int position) {
        Vet vet = vetList.get(position);
        holder.name.setText(vet.getName());
        holder.address.setText(vet.getAddress());
        holder.placeid.setText(vet.getPhoneNumber());
        //holder.phoneNumber.setText(vet.getPhoneNumber());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, VetDetailActivity.class);
            intent.putExtra("vet", (CharSequence) vet);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vetList.size();
    }

    public static class VetViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, placeid;

        public VetViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.vet_place);
            address = itemView.findViewById(R.id.place_address);
            placeid = itemView.findViewById(R.id.place_distance);
        }

    }
}