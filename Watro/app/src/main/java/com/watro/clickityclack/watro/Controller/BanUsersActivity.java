package com.watro.clickityclack.watro.Controller;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.SuperUser;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;
import java.util.List;

public class BanUsersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private ArrayAdapter<String> usersAdapter;
    private DatabaseReference db;
    private DatabaseReference usersReference;
    private List<String> usersList;
    private ListView usersListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_users);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        usersList = new ArrayList<>();
        usersListView = (ListView) findViewById(R.id.UsersListView);

        db = FirebaseDatabase.getInstance().getReference();
        usersReference = db.child("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BasicUser user = snapshot.getValue(BasicUser.class);
                    usersList.add(user.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        usersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usersList);
        usersListView.setAdapter(usersAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Toast.makeText(getApplicationContext(),
                        "Banned " + usersList.get(position), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            startActivity(new Intent(this, ReportsActivity.class));
        }
    }
}
