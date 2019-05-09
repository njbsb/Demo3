package com.a161328.demo3.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Record {
    private @ServerTimestamp Date date;
    private ArrayList<Integer> recordList;
    private String note_id;
    private String user_id;

    private static Record instance;

    public static Record getInstance() {
        if (instance == null) {
            instance = new Record();
        }
        return instance;
    }

    private Record() {
    }

    public Record(ArrayList<Integer> record, Date date) {
        this.recordList = record;
        this.date = date;
    }

    public int calculateSubScale(String scale) {
        int subScaleScore = 0;
        if (this.recordList != null) {
            if (scale.equals("NM")) {
                subScaleScore = subScaleScore + this.recordList.get(0) + this.recordList.get(7) + this.recordList.get(10);
            }
            else if (scale.equals("NS")) {
                subScaleScore = subScaleScore + this.recordList.get(1) + this.recordList.get(5) + this.recordList.get(6);
            }
            else if (scale.equals("IE")) {
                subScaleScore = subScaleScore + this.recordList.get(2) + this.recordList.get(3) + this.recordList.get(8) + this.recordList.get(10);
            }
            else if (scale.equals("IP")) {
                subScaleScore += this.recordList.get(4);
            }
        }
        return subScaleScore;
    }

    public int calculateTotalScore() {
        int totalScore = 0;
        if (this.recordList != null) {
            for (int i = 0; i<this.recordList.size(); i++) {
                totalScore += this.recordList.get(i);
            }
        }

        return totalScore;
    }

    public String getSevereness() {
        String s;
        int total = calculateTotalScore();
        if (total == 0) {
            s = "Not present";
        } else if (total <7) {
            s = "Mild Depression";
        } else if (total<13) {
            s = "Moderate Depression";
        } else if (total<19) {
            s = "Severe Depression";
        } else {
            s = "Very Severe Depression";
        }
        return s;
    }

    public ArrayList<Integer> getRecord() {
        return recordList;
    }

    public void setRecord(ArrayList<Integer> record) {
        this.recordList = record;
    }

    public void addScore(int index, int score) {

        recordList.set(index, score);

//        if(recordList.get(index) == null|| recordList.isEmpty()) {
//            recordList.add(index, score);
//        }
//        else {
//            recordList.set(index, score);
//        }
    }

//    public void addScore(int score) {
//        recordList.add(score);
//    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
