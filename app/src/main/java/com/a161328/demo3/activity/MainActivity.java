package com.a161328.demo3.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.a161328.demo3.Model.Record;
import com.a161328.demo3.Model.User;
import com.a161328.demo3.fragment.DiscoverFragment;
import com.a161328.demo3.fragment.HomeFragment;
import com.a161328.demo3.fragment.ProfileFragment;
import com.a161328.demo3.R;
import com.a161328.demo3.fragment.ResultFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private User user;
    private Record record;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        user = User.getInstance();
        record = Record.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mUser != null) {
            userID = mUser.getUid();
            user = User.getInstance();
            record = Record.getInstance();
            loadUser();
            loadRecord();
        }
    }


    private void loadUser() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    final String name = documentSnapshot.getString("name");
                    final String email = documentSnapshot.getString("email");
                    final String level = documentSnapshot.getString("level");
                    final int userPoints = Objects.requireNonNull(documentSnapshot.getLong("userPoints")).intValue();

                    user.setUserName(name);
                    user.setUserEmail(email);
                    user.setUserLevel(level);
                    user.setUserPoint(userPoints);
//                        shortToast("doc exists");
                }
                else {
                    shortToast("doc does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shortToast(e.getMessage());
            }
        });
    }

    private void loadRecord() {
        CollectionReference recordRef = db.collection("users").document(userID).collection("userRecord");
        DocumentReference myRecordRef = db.collection("users").document(userID).collection("userRecord").document("myRecord");
        myRecordRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Record recordBuffer = documentSnapshot.toObject(Record.class);
                    assert recordBuffer != null;
                    record.setRecord(recordBuffer.getRecord());
                    record.setDate(recordBuffer.getDate());
                    record.setUser_id(recordBuffer.getUser_id());
                    record.setNote_id(recordBuffer.getNote_id());

                }
                else {
                    shortToast("Doc does not exist");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shortToast(e.getMessage());
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//            return false;
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.item_discover:
                    selectedFragment = new DiscoverFragment();
                    break;
                case R.id.item_result:
                    selectedFragment = new ResultFragment();
                    break;
                case R.id.item_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.show();
        //super.onBackPressed();
    }

    public void shortToast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }
}
