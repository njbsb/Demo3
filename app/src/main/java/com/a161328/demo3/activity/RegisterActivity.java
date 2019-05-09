package com.a161328.demo3.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.a161328.demo3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText et_email, et_name, et_password, et_confirmPassword;
    Button btn_register, btn_have_account;
    ProgressBar progress_register;

    private FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    FirebaseUser firebaseUser;

    public static final String TAG = "YOUR-TAG-NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = findViewById(R.id.et_register_email);
        et_name = findViewById(R.id.et_register_name);
        et_password = findViewById(R.id.et_register_password);
        et_confirmPassword = findViewById(R.id.et_register_confirm_password);
        btn_register = findViewById(R.id.btn_register_register);
        btn_have_account = findViewById(R.id.btn_register_login);
        progress_register = findViewById(R.id.progressBar_register);

        progress_register.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_register.setVisibility(View.INVISIBLE);
                btn_have_account.setVisibility(View.INVISIBLE);
                progress_register.setVisibility(View.VISIBLE);

                final String email = et_email.getText().toString();
                final String name = et_name.getText().toString();
                final String password = et_password.getText().toString();
                final String confirmPassword = et_confirmPassword.getText().toString();

                HashMap<String, Object> userMap = new HashMap<>();

                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
                    longToast("please fill all fields");
                    btn_register.setVisibility(View.VISIBLE);
                    btn_have_account.setVisibility(View.VISIBLE);
                    progress_register.setVisibility(View.INVISIBLE);
                }
                else {
                    try {
                        userMap.put("name", name);
                        userMap.put("email", email);
                        userMap.put("password", password);
                        userMap.put("userPoints", 10);
                        userMap.put("level", "Level 1: Starter");
                        // userMap to save data to db
                        CreateUserAccount(name, email, password, userMap);
                    }
                    catch (Exception e) {
                        longToast(e.getMessage());
                    }
                }
            }
        });

        btn_have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });
    }

//    private void saveUserToDatabase(Map<String, Object> userMap) {
////        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            mFirestore.collection("users").document(user.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    shortToast("User saved in db");
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    longToast(e.getMessage());
//                }
//            });
//            updateUI();
//        }
//        else {
//
//        }
//    }

    private void CreateUserAccount(final String name, String email, String password, final HashMap userMap) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    shortToast("Account registered");
                    updateUserInfo(name, mAuth.getCurrentUser());
                    mFirestore.collection("users").document(mAuth.getCurrentUser().getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            shortToast("user saved in db");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            shortToast(e.getMessage());
                        }
                    });
                }
                else {
                    longToast("registration failed" + task.getException().getMessage());
                    btn_register.setVisibility(View.VISIBLE);
                    progress_register.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(String name, FirebaseUser currentUser) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        ///////// get uid here

        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    shortToast("User profile updated");
                    updateUI();
                }

            }
        });
    }

    private void updateUI() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    public void shortToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
    }
}
