

package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mainscreen extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RecyclerView mainUserRecyclerView;
    UserAdpter adapter;
    private FirebaseDatabase database;
    String userEmail;
    ArrayList<Users> usersArrayList;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        getSupportActionBar().hide();

        logout = findViewById(R.id.logoutimg);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            userEmail = currentUser.getEmail();
        } else {
            // go back to main screen
            finish();
        }
        database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("user");
        usersArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Log.w("FirebaseACT", "Signed OUT");
                Toast.makeText(mainscreen.this, "SIGN OUT",
                        Toast.LENGTH_LONG).show();
                finish();

            }
        });

        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =  new UserAdpter(mainscreen.this,usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);


    }
}