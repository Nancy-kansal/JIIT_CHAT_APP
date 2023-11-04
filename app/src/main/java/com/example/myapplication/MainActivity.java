package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button login_bt,signin_bt;
    EditText email_et,password_et;
    String email_value,enroll_value, password_value,status,imageuri;
    private static Context context;
    private FirebaseDatabase database;

    TextView attepts_txt;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_bt = (Button)findViewById(R.id.button);
        email_et = (EditText)findViewById(R.id.editText);
        password_et = (EditText)findViewById(R.id.editText2);
        MainActivity.context = getApplicationContext();
        signin_bt = (Button)findViewById(R.id.button2);
        attepts_txt = (TextView)findViewById(R.id.textView3);
        attepts_txt.setVisibility(View.GONE);

        imageuri = "https://i.imgur.com/tGbaZCY.jpg";
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            // if user is already signed in
            Intent intent = new Intent(MainActivity.this, mainscreen.class);
            startActivity(intent);
        }
        signin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enroll_value = email_et.getText().toString();
                enroll_value = enroll_value.replaceAll("\\s", "");
                email_value = enroll_value + "@mail.jiit.ac.in";
                password_value = password_et.getText().toString();
                String yearString = enroll_value.substring(0, 2);

                // Parse the year as an integer
                int year = Integer.parseInt(yearString);

                // Print the extracted year
                status= "Batch of : 20" + year;

                mAuth.createUserWithEmailAndPassword(email_value, password_value)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("FirebaseACT", "createUserWithEmail:success");
                                    String id = task.getResult().getUser().getUid();
                                    Users users = new Users(id,enroll_value,email_value,password_value,imageuri,status);
                                    DatabaseReference reference = database.getReference().child("user").child(id);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(MainActivity.this, "USER CREATED",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(MainActivity.this, mainscreen.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(MainActivity.this, "ERROR USER CANT BE CREATED",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("FirebaseACT", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }}});   }});
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_value = email_et.getText().toString();
                email_value = email_value.replaceAll("\\s", "");
                email_value = email_value + "@mail.jiit.ac.in";
                password_value = password_et.getText().toString();
                mAuth.signInWithEmailAndPassword(email_value, password_value)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("FirebaseACT", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(MainActivity.this, "Login Succesfull",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, mainscreen.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("FirebaseACT", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });




    }

}