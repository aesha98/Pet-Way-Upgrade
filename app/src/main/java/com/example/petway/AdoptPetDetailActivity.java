package com.example.petway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petway.Model.Animals;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdoptPetDetailActivity extends AppCompatActivity {

    private TextView status, breed, gender, age, date, name, desc;
    private Button contactOwner;
    private ImageView pet_image;
    private Animals data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adopt_pet_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        status = findViewById(R.id.status_txt);
        breed = findViewById(R.id.breed_txt);
        gender = findViewById(R.id.gender_txt);
        age = findViewById(R.id.age_txt);
        date = findViewById(R.id.date_txt);
        name = findViewById(R.id.pet_name);
        desc = findViewById(R.id.description);

        contactOwner = findViewById(R.id.btnAdopt);
        pet_image = findViewById(R.id.imageMain);

        String key = getIntent().getStringExtra("key");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Animals").child(key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(Animals.class);
                // in the data object

                name.setText(data.getAnimal_name());
                status.setText("available");
                breed.setText(data.getBreed());
                gender.setText(data.getGender());
                age.setText(data.getBirth());
                date.setText(data.getDate());
                desc.setText(data.getDesc());
                Picasso.get().load(data.getImageUrl()).fit().into(pet_image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });

//
//        Glide.with(this)
//                .load(data.getImageUrl())
//                .into(pet_image);


        contactOwner.setOnClickListener(view -> {
            String phoneNumber = "tel:" + "0186674308";
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse(phoneNumber));
            if (callIntent.resolveActivity(getPackageManager()) !=null)
            {
                startActivity(callIntent);
            }
            else
            {
                Toast.makeText(this, "ERROR : app not found",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    }



