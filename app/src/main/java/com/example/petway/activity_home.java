package com.example.petway;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petway.Interface.OnNavigationItemSelected;
import com.example.petway.Model.Animals;
import com.example.petway.Model.Users;
import com.example.petway.Model.Vet;
import com.example.petway.viewholder.AnimalListHolder;
import com.example.petway.viewholder.AnimalViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petway.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_home extends AppCompatActivity implements OnNavigationItemSelected {


    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference animal_ref, user_ref;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    RecyclerView.LayoutManager layoutManager;
    FirebaseAuth mFirebaseAuth;
    private  List<Animals> animalsList;

    AlertDialog.Builder builder;

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawer = findViewById(R.id.drawer_layout);
        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_gallery);
        mFirebaseAuth = FirebaseAuth.getInstance();
        animal_ref = FirebaseDatabase.getInstance().getReference().child("Animals");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            Intent intent = new Intent(activity_home.this, AddPetAdoption.class);
            startActivity(intent);
        });

        navigationView = findViewById(R.id.nav_view);

        TextView user_name_text_view = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_name);
        CircleImageView profile_img = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.user_profile_image);

        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        String id = user.getUid();
        user_ref = FirebaseDatabase.getInstance().getReference("Users").child(id);
        //Use  DB reference object and add this method to access realtime data
        user_ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.child("Users").child(id).getValue(Users.class);

                if (user != null) {
                    //Fetch values from you database child and set it to the specific view object.
                    user_name_text_view.setText(user.getUsername());
                    String link = user.getImage();
                    Picasso.get().load(link).into(profile_img);
                }
                else
                {
                    user_name_text_view.setText("username");
                    profile_img.setImageResource(R.drawable.img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //toggle button
        toggle = new ActionBarDrawerToggle(this , drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //when an item is selected from menu
        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId())
            {
                case R.id.nav_cart:

                    Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                    //close drawer
                    drawer.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_search:
                    Intent NearVet = new Intent(activity_home.this, NearVetActivity.class);
                    startActivity(NearVet);
                    Toast.makeText(getApplicationContext(),"Finding nearby vet..",Toast.LENGTH_SHORT).show();
                    //close drawer
                    drawer.closeDrawer(GravityCompat.START);
                    break;

                case R.id.nav_categories:
                    Intent intent = new Intent(activity_home.this, SettingsActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                    //close drawer
                    drawer.closeDrawer(GravityCompat.START);

                    break;

                case R.id.nav_logout:

                    mFirebaseAuth.signOut();

                    startActivity(new Intent(activity_home.this, MainActivity.class));

                    drawer.closeDrawer(GravityCompat.START);
                    break;
            }

            return true;
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Animals> options = new FirebaseRecyclerOptions.Builder<Animals>()
                .setQuery(animal_ref, Animals.class).build();

        FirebaseRecyclerAdapter<Animals, AnimalViewHolder> adapter =
                new FirebaseRecyclerAdapter<Animals, AnimalViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AnimalViewHolder animalViewHolder, int i, @NonNull final Animals animals) {
                        animalViewHolder.txt_animal_name.setText(animals.getAnimal_name());
                        animalViewHolder.txt_animal_info.setText(animals.getBreed());
                        animalViewHolder.date_posted.setText(animals.getDate());

                        Picasso.get().load(animals.getImageUrl()).into(animalViewHolder.img_view);
                        //for the addition of animal to our cart
                        animalViewHolder.itemView.setOnClickListener(v -> {

                            String id = animals.getAnimal_id();
                            Intent intent = new Intent(activity_home.this, AdoptPetDetailActivity.class);
                            intent.putExtra("key", id);
                            startActivity(intent);





                        });
                    }

                    @NonNull
                    @Override
                    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item_layout, parent, false);
                        AnimalViewHolder hol = new AnimalViewHolder(view);
                        return hol;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
}