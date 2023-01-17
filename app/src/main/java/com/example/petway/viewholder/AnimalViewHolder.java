package com.example.petway.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petway.Interface.AnimalClickListener;
import com.example.petway.R;

public class AnimalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView txt_animal_name;
    public TextView txt_animal_info;
    public TextView date_posted;
    public ImageView img_view;
    public AnimalClickListener listener;


    public AnimalViewHolder(@NonNull View itemView) {
        super(itemView);

        img_view = (ImageView) itemView.findViewById(R.id.places_img);
        txt_animal_name = (TextView) itemView.findViewById(R.id.animal_name);
        txt_animal_info = (TextView) itemView.findViewById(R.id.breed_detail);
        date_posted = (TextView) itemView.findViewById(R.id.date_added);

    }


    public void setAnimalClickListener(AnimalClickListener listener) {

        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
