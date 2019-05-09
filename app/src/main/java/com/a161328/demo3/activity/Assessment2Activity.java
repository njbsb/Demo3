package com.a161328.demo3.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.Model.Record;
import com.a161328.demo3.R;
import com.a161328.demo3.activity.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Assessment2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_count, txt_questionSet, txt_monitor;
    private ImageView img_question;
    private Button btn1, btn2, btn3, btnNext;
    private ProgressBar progressBar_load;

    private Date currentDate;
    private ArrayList<Integer> recordList;
    private ArrayList<Button> buttonList;
    private int questionSet;
    private int score1, score2, score3;

    private Record record;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    int limitCount = 0;
    int added = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment2);
        txt_count = findViewById(R.id.txt_count);
        txt_questionSet = findViewById(R.id.txt_questionSet);
        txt_monitor = findViewById(R.id.monitor);
        img_question = findViewById(R.id.img_question);
        btn1 = findViewById(R.id.btn_q1);
        btn2 = findViewById(R.id.btn_q2);
        btn3 = findViewById(R.id.btn_q3);
        btnNext = findViewById(R.id.btnNext);
        progressBar_load = findViewById(R.id.progress_assessmentLoad);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        record = Record.getInstance();
        recordList = new ArrayList<>();
        if (record.getRecord() == null) {
            record.setRecord(recordList);
        }



        setQuestionSet();
        setQuestion(questionSet);
        if (questionSet == 1) {
            currentDate = Calendar.getInstance().getTime();
            record.setDate(currentDate);
        }
//        for (int i = 0; i<11; i++) {
//            recordList.add(i, 0);
//        }
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        buttonList = new ArrayList<>();
        buttonList.add(btn1);
        buttonList.add(btn2);
        buttonList.add(btn3);
    }

    public void setQuestion(int question) {

        if (question<=11) {
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
            progressBar_load.setVisibility(View.VISIBLE);


            DocumentReference documentReference = db.collection("assessment").document("item" + question);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
//                    shortToast("doc exists");
                        String itemID = documentSnapshot.getString("id");
                        String category = documentSnapshot.getString("category");
//                    String q1 = documentSnapshot.getString("q1");
//                    String q2 = documentSnapshot.getString("q2");
//                    String q3 = documentSnapshot.getString("q3");
                        ArrayList<String> question = new ArrayList<>();
                        question.add(documentSnapshot.getString("q1"));
                        question.add(documentSnapshot.getString("q2"));
                        question.add(documentSnapshot.getString("q3"));
                        score1 = documentSnapshot.getLong("s1").intValue();
                        score2 = documentSnapshot.getLong("s2").intValue();
                        score3 = documentSnapshot.getLong("s3").intValue();

                        progressBar_load.setVisibility(View.INVISIBLE);
                        for (int i = 0; i<buttonList.size(); i++) {
                            buttonList.get(i).setVisibility(View.VISIBLE);
                            btnNext.setVisibility(View.VISIBLE);
                            buttonList.get(i).setText(question.get(i));
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    shortToast(e.getMessage());
                }
            });
        }
        else {
            shortToast("assessment finished");
            String userId = mAuth.getCurrentUser().getUid();
            record.setUser_id(userId);

            /*
            get the number of doc in a collection
             */

            Map<String, Object> recordMap = new HashMap<>();
            recordMap.put("date", record.getDate());
            recordMap.put("userid", mAuth.getCurrentUser().getUid());
            recordMap.put("record", record.getRecord());
            recordMap.put("noteid", record.getNote_id());

//            db.collection("users").document(userId).collection("userRecord").document("myrecord").set(recordMap)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            shortToast("written to db");
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    shortToast(e.getMessage());
//                }
//            });

            db.collection("users").document(userId).collection("userRecord").document("myRecord").set(recordMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            shortToast("written to db");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    shortToast(e.getMessage());
                }
            });
            Intent mainActivity = new Intent(this, MainActivity.class);
            this.startActivity(mainActivity);
            finish();
        }
        /*
        set loading interface
         */
//        if (buttonList != null) {
//            for (int i = 0; i<buttonList.size(); i++) {
//                buttonList.get(i).setVisibility(View.INVISIBLE);
//                shortToast("loop read");
//            }
//        }



//        if (limitCount < 2) {
//
//        } else {
//
//            // finish the activity
//        }

    }

    public void setQuestionSet() {
        Intent intent = getIntent();
        questionSet = intent.getIntExtra("btnNo", 0);
        txt_questionSet.setText(String.valueOf(questionSet));
    }

    public void shortToast(String m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int i = 0;
        int score = 8;
        /*
        8 indicates error
         */
        switch (v.getId()) {
            case R.id.btn_q1:
                //
                score = score1;
//                record.getRecord().add(score1);
                break;
            case R.id.btn_q2:
                //
                score = score2;
//                record.getRecord().add(score2);
                break;
            case R.id.btn_q3:
                //
                score = score3;
//                record.getRecord().add(score3);
                break;
            case R.id.btnNext:
                //
                break;
        }
        if (limitCount<2) {
            record.getRecord().add(score);
            added++;
            txt_monitor.setText(record.getRecord().toString());
            questionSet++;
            if (added<2) {
                setQuestion(questionSet);
                limitCount++;
            } else {
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                finish();
            }

        }
        else {

        }

    }
}
