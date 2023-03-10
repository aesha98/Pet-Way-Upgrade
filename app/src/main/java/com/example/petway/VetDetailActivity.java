package com.example.petway;

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

public class VetDetailActivity extends AppCompatActivity {

    private TextView status, breed, gender, age, date, vetname, desc;
    private Button contactOwner;
    private ImageView pet_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        status = findViewById(R.id.status_txt);
        breed = findViewById(R.id.city_text);
        gender = findViewById(R.id.contact_txt);
        age = findViewById(R.id.distance_txt);
        vetname = findViewById(R.id.vet_name);

        contactOwner = findViewById(R.id.btnAdopt);
        pet_image = findViewById(R.id.imageMain);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        String imageUrl = intent.getStringExtra("imageUrl");
        String website = intent.getStringExtra("websiteURI");
        String distance = intent.getStringExtra("distance");

        vetname.setText(name);
        status.setText(address);
        breed.setText(website);
        gender.setText(phone);
        age.setText(distance);


        Glide.with(this)
                .load(imageUrl)
                .into(pet_image);

        contactOwner.setOnClickListener(view -> {
            String phoneNumber = "tel:" + phone;
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