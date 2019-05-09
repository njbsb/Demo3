package com.a161328.demo3.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    ProgressBar progress_login;
    TextView forgotPassword, noAccount;

    private FirebaseAuth mAuth;
    private Intent mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = findViewById(R.id.et_login_email);
        et_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login_login);
        progress_login = findViewById(R.id.progressBar_login);
        forgotPassword = findViewById(R.id.tv_forgotPassword);
        noAccount = findViewById(R.id.tv_register);

        mAuth = FirebaseAuth.getInstance();
        mainActivity = new Intent(this, MainActivity.class);

        progress_login.setVisibility(View.INVISIBLE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_login.setVisibility(View.INVISIBLE);
                progress_login.setVisibility(View.VISIBLE);

                final String email = et_email.getText().toString();
                final String password = et_password.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {
                    longToast("some fields are empty");
                    btn_login.setVisibility(View.VISIBLE);
                    progress_login.setVisibility(View.INVISIBLE);
                }
                else {
                    signIn(email, password);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longToast("hahahahaha");
            }
        });

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progress_login.setVisibility(View.INVISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                    updateUI();
                    shortToast("Welcome to MoodSwinger");
                }
                else {
                    longToast(task.getException().getMessage());
                    progress_login.setVisibility(View.INVISIBLE);
                    btn_login.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            updateUI();
        }
    }

    private void updateUI() {
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
