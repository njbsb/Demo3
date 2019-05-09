package com.a161328.demo3.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.Model.Record;
import com.a161328.demo3.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AssessmentActivity extends AppCompatActivity implements View.OnClickListener {
    // interface
    public TextView txt_num, txt_score;
    private Button
            btn_opt_1,
            btn_opt_2,
            btn_opt_3,
            btn_next;
    private ImageButton imgBtn_start;
    private ImageView img_ass;
    // variable
    private Date currentDate;
    private int
            btnNumberAss,
            count,
            limitCount,
            score,
            totalScore,
            scoreBuffer1, scoreBuffer2, scoreBuffer3;
    private String
            buffer1,
            buffer2,
            buffer3;
    private ArrayList<Integer> recordList;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // findViewByID
        txt_num = findViewById(R.id.number);
        txt_score = findViewById(R.id.txt_score);
        btn_opt_1 = findViewById(R.id.btn_option_1);
        btn_opt_2 = findViewById(R.id.btn_option_2);
        btn_opt_3 = findViewById(R.id.btn_option_3);
        btn_next = findViewById(R.id.btn_next);
        imgBtn_start = findViewById(R.id.imgBtn_start);
        img_ass = findViewById(R.id.img_ass);

        // set OnClickListener(this)
        imgBtn_start.setOnClickListener(this);
        btn_opt_1.setOnClickListener(this);
        btn_opt_2.setOnClickListener(this);
        btn_opt_3.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        // set option and next buttons invisible before user pressed start
        setBtnOptionInvisible();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // set the set of question based on the button pressed at home
        setQuestionSet();
        recordList = new ArrayList<>();
        recordList.addAll(Arrays.asList(0,0,0,0,0,0,0,0,0,0,0));
        record = Record.getInstance();
        if(record.getRecord() == null) {
            record.setRecord(recordList); // empty list
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        this.startActivity(mainActivity);
        finish();
        super.onBackPressed();
    }

    public void setQuestionSet() {
        Intent intent = getIntent();
        btnNumberAss = intent.getIntExtra("btnNo", 0);
        txt_num.setText(String.valueOf(btnNumberAss));
    }

    public void shortToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_LONG).show();
    }

    public void loadFromJSON(int cc) {
        String json;
        try {
            InputStream is = getAssets().open("demo3_assessment_questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
//            limitCount = 0;

//            if(btnNumberAss == 1) {
//                limitCount = 0;
//                count = 0;
//                if (limitCount<2) {
//                    JSONObject obj = jsonArray.getJSONObject(count);
//                    buffer1 = obj.getString("q1");
//                    buffer2 = obj.getString("q2");
//                    buffer3 = obj.getString("q3");
//
//                    if (count + 1 == 2 || count + 1 == 6 || count + 1 == 7 || count + 1 == 9 || count + 1 == 10 || count + 1 == 11) {
//                        // reversely scored
//                        scoreBuffer1 = 3;
//                        scoreBuffer2 = 2;
//                        scoreBuffer3 = 1;
//                    } else {
//                        // normal score
//                        scoreBuffer1 = 1;
//                        scoreBuffer2 = 2;
//                        scoreBuffer3 = 3;
//                    }
//                    limitCount++;
//                }
//                else {
//                    longToast("you have reached end of this stage.");
//                }
//
//            }
            JSONObject obj = jsonArray.getJSONObject(cc);
            buffer1 = obj.getString("q1");
            buffer2 = obj.getString("q2");
            buffer3 = obj.getString("q3");

            if (cc + 1 == 2 || cc + 1 == 6 || cc + 1 == 7 || cc + 1 == 9 || cc + 1 == 10 || cc + 1 == 11) {
                // reversely scored
                scoreBuffer1 = 3;
                scoreBuffer2 = 2;
                scoreBuffer3 = 1;
            } else {
                // normal score
                scoreBuffer1 = 1;
                scoreBuffer2 = 2;
                scoreBuffer3 = 3;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateQuestion() {

        // do is only for first time to assign the count for the index of the jsonArray
        if (limitCount == 0) {
            switch (btnNumberAss) {
                case 1:
                    count = 0;
                    break;
                case 2:
                    count = 2;
                    break;
                case 3:
                    count = 4;
                    break;
                case 4:
                    count = 6;
                    break;
                case 5:
                    count = 8;
                    break;
                case 6:
                    count = 10;
                    break;
                case 7:
                    count = 12;
                    break;
            }
        }

        if (limitCount < 2) {
            // initial limitCount is set at 0 when btn start was clicked
            loadFromJSON(count);
            // string of answer and mark of index 'count' will be loaded at 3 questionBuffers and scoreBuffers;

            btn_opt_1.setBackgroundColor(Color.WHITE);
            btn_opt_2.setBackgroundColor(Color.WHITE);
            btn_opt_3.setBackgroundColor(Color.WHITE);

            if (count == 11) {
//                writeToFireStore(record.getRecord());
                shortToast("assessment finished");
                btn_opt_1.setEnabled(false);
                btn_opt_2.setEnabled(false);
                btn_opt_3.setEnabled(false);
                btn_next.setEnabled(false);
//                shortToast(record.getRecord().toString());

                String userId = mAuth.getCurrentUser().getUid();
                record.setUser_id(userId);

                Map<String, Object> recordMap = new HashMap<>();
                recordMap.put("date", record.getDate());
                recordMap.put("userid", mAuth.getCurrentUser().getUid());
                recordMap.put("record", record.getRecord());
                recordMap.put("noteid", record.getNote_id());

                db.collection("users").document(userId).collection("userRecord").add(recordMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
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

                // add function to write to database


            } else {
                //set the question text in button
                btn_opt_1.setText(buffer1);
                btn_opt_2.setText(buffer2);
                btn_opt_3.setText(buffer3);
            }
            count++;
            limitCount++;
        } else {
            // add dialog
            btn_opt_1.setEnabled(false);
            btn_opt_2.setEnabled(false);
            btn_opt_3.setEnabled(false);
            btn_next.setEnabled(false);
//            longToast("You have already answered 2 questions from this stage!!!");
//            shortToast(record.getRecord().toString());
//            finish();
            Intent mainActivity = new Intent(this, MainActivity.class);
            this.startActivity(mainActivity);
            finish();

            // bring to home activity
        }
    }

    private void writeToFireStore(ArrayList<Integer> recordLi) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String userId = mAuth.getCurrentUser().getUid();
        record.setUser_id(userId);
        record.setRecord(recordLi);

        firestore.collection("record").document("new").set(record).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                shortToast("yeah");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                shortToast("boo");
            }
        });

//        firestore.collection("record").add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                shortToast("written to firestore");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                shortToast(e.getMessage() + " \nwrite fail");
//            }
//        });


//        DocumentReference newRecordRef = firestore.collection("record").document();
////        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        String userId = mAuth.getCurrentUser().getUid();
//        record.setUser_id(userId);
//        record.setRecord(recordLi);
//        record.setNote_id(newRecordRef.getId());
//
//        newRecordRef.set(record).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                longToast("Uploaded");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                longToast("Failed");
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_start:
                count = 0; // index for json object array
                limitCount = 0; // limit the number of question to only TWO
                totalScore = 0;
                if (record.getRecord().get(0) == 0) {
                    currentDate = Calendar.getInstance().getTime();
                    record.setDate(currentDate);
                    shortToast("Assessment started at " +(record.getDate()));
                }
                record.setNote_id("id1");
//                recordList = new ArrayList<>();
                imgBtn_start.setVisibility(View.INVISIBLE);

                setBtnOptionVisible();
                btn_next.setEnabled(false);
                imgBtn_start.setEnabled(false);
                updateQuestion();
                break;
            case R.id.btn_next:
//                recordList.add(1, score);
                try {
                    record.addScore(count-1, score);
                } catch (Exception e) {
                    shortToast(e.getMessage());
                }

                totalScore += score;
                btn_next.setEnabled(false);
                updateQuestion();
                txt_score.setText("Score: " + String.valueOf(totalScore));
                break;
            case R.id.btn_option_1:
                btn_opt_1.setBackgroundColor(Color.GREEN);
                btn_opt_2.setBackgroundColor(Color.WHITE);
                btn_opt_3.setBackgroundColor(Color.WHITE);
                score = scoreBuffer1;
//                shortToast(String.valueOf(score));
                btn_next.setEnabled(true);
                break;
            case R.id.btn_option_2:
                btn_opt_1.setBackgroundColor(Color.WHITE);
                btn_opt_2.setBackgroundColor(Color.GREEN);
                btn_opt_3.setBackgroundColor(Color.WHITE);
                score = scoreBuffer2;
//                shortToast(String.valueOf(score));
                btn_next.setEnabled(true);
                break;
            case R.id.btn_option_3:
                btn_opt_1.setBackgroundColor(Color.WHITE);
                btn_opt_2.setBackgroundColor(Color.WHITE);
                btn_opt_3.setBackgroundColor(Color.GREEN);
                score = scoreBuffer3;
//                shortToast(String.valueOf(score));
                btn_next.setEnabled(true);
                break;
        }
    }

    public void setBtnOptionVisible() {
        img_ass.setVisibility(View.VISIBLE);
        btn_opt_1.setVisibility(View.VISIBLE);
        btn_opt_2.setVisibility(View.VISIBLE);
        btn_opt_3.setVisibility(View.VISIBLE);
        btn_next.setVisibility(View.VISIBLE);
    }

    public void setBtnOptionInvisible() {
        img_ass.setVisibility(View.INVISIBLE);
        btn_opt_1.setVisibility(View.INVISIBLE);
        btn_opt_2.setVisibility(View.INVISIBLE);
        btn_opt_3.setVisibility(View.INVISIBLE);
        btn_next.setVisibility(View.INVISIBLE);
    }
}
