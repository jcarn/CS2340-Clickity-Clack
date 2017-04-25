package com.watro.clickityclack.watro.Controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.watro.clickityclack.watro.Model.BasicUser;
import com.watro.clickityclack.watro.Model.SuperUser;
import com.watro.clickityclack.watro.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanUsersActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backButton;
    private ArrayAdapter<String> usersAdapter;
    private DatabaseReference db;
    private DatabaseReference usersReference;
    private List<String> usersList;
    private List<BasicUser> userObjectsList;
    private ListView usersListView;
    private BasicUser selectedUser;
    private int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_users);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        usersList = new ArrayList<>();
        usersListView = (ListView) findViewById(R.id.UsersListView);

        userObjectsList = new ArrayList<>();

        db = FirebaseDatabase.getInstance().getReference();
        usersReference = db.child("Users");
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BasicUser user = snapshot.getValue(BasicUser.class);
                    String isBanned = snapshot.child("banned").getValue(String.class);
                    if (isBanned == null || !isBanned.equals("true")) {
                        usersList.add(user.getEmail());
                        userObjectsList.add(user);
                    }
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
                pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(BanUsersActivity.this, R.style.alertDialog));
                builder.setCancelable(true);
                builder.setTitle("Ban " + userObjectsList.get(position).getFirstName() + " " + userObjectsList.get(position).getLastName() + "?");
                builder.setMessage("Are you sure you want to ban this user? This cannot be undone.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final DatabaseReference userReference = db.child("Users").child(userObjectsList.get(pos).getId());
                                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        selectedUser = dataSnapshot.getValue(BasicUser.class);
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("banned", "true");
                                        userReference.updateChildren(map);
                                        Toast.makeText(getApplicationContext(),
                                                "Banned " + selectedUser.getFirstName(), Toast.LENGTH_LONG)
                                                .show();
                                        startActivity(new Intent(getApplicationContext(), BanUsersActivity.class));
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

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
