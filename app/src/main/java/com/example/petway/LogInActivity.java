package com.example.petway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petway.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {


    private EditText input_phone_number;
    private EditText input_password;
    private Button login_btn;
    private ProgressDialog loading_bar;
    private String parent_database_name = "Users";
    String SP_EMAIL = "Email";
    String SP_PASSWORD = "Password";
    String SP_CHECKBOX = "rememberme";
    String pass;
    String username;
    private CheckBox check_box_remember_me;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference UserRef;
    private SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login_btn = (Button) findViewById(R.id.btnLoginPage);
        input_password = (EditText) findViewById(R.id.id_email);
        input_phone_number = (EditText) findViewById(R.id.etPassword);

        check_box_remember_me = (CheckBox) findViewById(R.id.checkBox);
        sharedPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        input_password.setText(sharedPref.getString(SP_PASSWORD, pass));
        input_phone_number.setText(sharedPref.getString(SP_EMAIL,username));
        check_box_remember_me.setChecked(sharedPref.getBoolean(SP_CHECKBOX, false));
        mFirebaseAuth = FirebaseAuth.getInstance();

        check_box_remember_me.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean(SP_CHECKBOX,b);
            editor.commit();
        });


        login_btn.setOnClickListener(view -> {
            parent_database_name = "Users";
            loginUser();

        });
    }

    private void loginUser() {
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(intent);
        pass = input_password.getText().toString();
        username = input_phone_number.getText().toString();


        if (pass.isEmpty() || username.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
            builder.setMessage("Please enter email and password").setTitle("Warning").setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        }
            if (check_box_remember_me.isChecked())
            {
                editor.putString(SP_EMAIL, username);
                editor.putString(SP_PASSWORD,pass);
                editor.commit();
            }
            else
            {
                editor.putString(SP_EMAIL,"");
                editor.putString(SP_PASSWORD,"");
                editor.commit();
            }
            mFirebaseAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(task -> {
                //AllowAccessToAccount(username, pass);
                gotomainpage();
            });
        }


    private void AllowAccessToAccount(final String email, final String password) {


        if (check_box_remember_me.isChecked()) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        }

        final DatabaseReference root_ref;
        root_ref = FirebaseDatabase.getInstance().getReference("Users");
        root_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = root_ref.push().getKey();

                if (dataSnapshot.child(parent_database_name).child(id).exists()) {

                    Users usersdata = dataSnapshot.child(parent_database_name).child(id).getValue(Users.class);

                         if(parent_database_name.equals("Users")){
                                Toast.makeText(LogInActivity.this, "Log in success ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void gotomainpage()
    {
        Intent intent = new Intent(LogInActivity.this, activity_home.class);
        startActivity(intent);
    };
}