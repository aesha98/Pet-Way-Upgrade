package com.example.petway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.petway.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    private CheckBox check_box_remember_me;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference UserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login_btn = (Button) findViewById(R.id.btnLoginPage);
        input_password = (EditText) findViewById(R.id.id_email);
        input_phone_number = (EditText) findViewById(R.id.etPassword);

        loading_bar = new ProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        check_box_remember_me = (CheckBox) findViewById(R.id.checkBox);
        //Paper.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent_database_name = "Users";
                loginUser();

            }
        });
    }

    private void loginUser() {
        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(intent);
        String pass = input_password.getText().toString();
        String username = input_phone_number.getText().toString();


        if (pass.isEmpty() || username.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
            builder.setMessage("Please enter email and password").setTitle("Warning").setPositiveButton("OK", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            mFirebaseAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    AllowAccessToAccount(username, pass);
                    gotomainpage();
                }
            });
        }
    }

    private void AllowAccessToAccount(final String email, final String password) {


        if (check_box_remember_me.isChecked()) {
            //Paper.book().write(Prevalent.user_phone_key, phone);
            //Paper.book().write(Prevalent.user_password_key, phone);
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
                                Intent intent = new Intent(LogInActivity.this, activity_home.class);
                                // Prevalent.online_user=usersdata;
                                startActivity(intent);
                            }
                        }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void gotomainpage()
    {
        Intent intent = new Intent(LogInActivity.this, activity_home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    };
}