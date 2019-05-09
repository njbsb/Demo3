package com.a161328.demo3.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.activity.Assessment2Activity;
import com.a161328.demo3.Model.Record;
import com.a161328.demo3.R;
import com.a161328.demo3.activity.AssessmentExtraActivity;
import com.a161328.demo3.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Button> btn;

    private Record record;
    private AlertDialog dialog;
    private FirebaseUser user;
    private int btnNumberHome;
    private TextView tx_monitor;

    private Drawable drawableDone;
    private Drawable drawableUndone;

    public HomeFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Button btn_stage_1, btn_stage_2, btn_stage_3, btn_stage_4, btn_stage_5, btn_stage_6, btn_stage_7;
        Button btn_newAssessment;


        btn_stage_1 = v.findViewById(R.id.btn_stage1);
        btn_stage_2 = v.findViewById(R.id.btn_stage2);
        btn_stage_3 = v.findViewById(R.id.btn_stage3);
        btn_stage_4 = v.findViewById(R.id.btn_stage4);
        btn_stage_5 = v.findViewById(R.id.btn_stage5);
        btn_stage_6 = v.findViewById(R.id.btn_stage6);
        btn_stage_7 = v.findViewById(R.id.btn_stage7);
        btn_newAssessment = v.findViewById(R.id.btn_newAssessment);
        tx_monitor = v.findViewById(R.id.txt_monitor);


        btn_stage_1.setOnClickListener(this);
        btn_stage_2.setOnClickListener(this);
        btn_stage_3.setOnClickListener(this);
        btn_stage_4.setOnClickListener(this);
        btn_stage_5.setOnClickListener(this);
        btn_stage_6.setOnClickListener(this);
        btn_stage_7.setOnClickListener(this);
        btn_newAssessment.setOnClickListener(this);

        btn = new ArrayList<>();
        btn.add(btn_stage_1);
        btn.add(btn_stage_2);
        btn.add(btn_stage_3);
        btn.add(btn_stage_4);
        btn.add(btn_stage_5);
        btn.add(btn_stage_6);
        btn.add(btn_stage_7);

        drawableUndone = getResources().getDrawable(R.drawable.style_button_round_undone);
        drawableDone = getResources().getDrawable(R.drawable.style_button_round);

        record = Record.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (record.getRecord() != null) {
            String mm = record.getRecord().toString();
            tx_monitor.setText(mm);
        } else {
            tx_monitor.setText("arraylist is null");
        }

        refreshButton();


//        if (record.getRecord() == null) {
//            btn_stage_2.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//            btn_stage_3.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//            btn_stage_4.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//            btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//            btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//            btn_stage_2.setEnabled(false);
//            btn_stage_3.setEnabled(false);
//            btn_stage_4.setEnabled(false);
//            btn_stage_5.setEnabled(false);
//            btn_stage_6.setEnabled(false);
//        } else {
//            ArrayList<Integer> rc = record.getRecord();
////            for (int i = 0; i<rc.size(); i++) {
////                switch (rc.get(i)) {
////                    case 0:
////                }
////            }
//
//            if (rc.get(2) == 0) {
////                btn_stage_2.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//                btn_stage_3.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_4.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_3.setEnabled(false);
//                btn_stage_4.setEnabled(false);
//                btn_stage_5.setEnabled(false);
//                btn_stage_6.setEnabled(false);
//
//
//            } else if (rc.get(4) == 0) {
////                btn_stage_3.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//                btn_stage_4.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_4.setEnabled(false);
//                btn_stage_5.setEnabled(false);
//                btn_stage_6.setEnabled(false);
//            } else if (rc.get(6) == 0) {
////                btn_stage_4.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//                btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_5.setEnabled(false);
//                btn_stage_6.setEnabled(false);
//            } else if (rc.get(8) == 0) {
////                btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//                btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//                btn_stage_6.setEnabled(false);
//            } else if (rc.get(10) == 0) {
////                btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//
//            }
//        }

//        if (record.getRecord().get(2) == null) {
//            btn_stage_2.setEnabled(false);
//        }
//        if(record.getRecord().isEmpty()) {
//            btn_stage_1.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//        }
//        else {
//            btn_stage_1.setBackground(getResources().getDrawable(R.drawable.style_button_round));
//        }

//        btn_stage_2.setEnabled(false);
//        btn_stage_3.setEnabled(false);
//        btn_stage_4.setEnabled(false);
//        btn_stage_5.setEnabled(false);
//        btn_stage_6.setEnabled(false);
//        btn_stage_7.setEnabled(false);

//        btn_stage_2.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//        btn_stage_3.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//        btn_stage_4.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//        btn_stage_5.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
//        btn_stage_6.setBackground(getResources().getDrawable(R.drawable.style_button_round_undone));
        return v;
    }

    public void reset() {
        for (int i = 1; i < btn.size(); i++) {
            btn.get(i).setEnabled(false);
        }
        Drawable drawableUndone = getResources().getDrawable(R.drawable.style_button_round_undone);
        for (int i = 1; i < btn.size(); i++) {
            btn.get(i).setBackground(drawableUndone);
        }

        if (record.getRecord() != null) {
            record.getRecord().clear();
            String x = record.getRecord().toString();
            shortToast(x);
            tx_monitor.setText(x);

        }


    }

    public void refreshButton() {


        /*
        initialize the disabled button and the color of the undone
         */
        for (int i = 0; i < btn.size(); i++) {
            btn.get(i).setEnabled(false);
        }

        for (int i = 0; i < btn.size(); i++) {
            btn.get(i).setBackground(drawableUndone);
        }

        /*
        set the enabled and the disabled
         */
        if (record.getRecord() != null) {
            if (record.getRecord().size() < 11) {
                for (int i=0; i<record.getRecord().size(); i++) {
                    if (record.getRecord().size() == 2) {
                        btn.get(0).setEnabled(false);
                        btn.get(1).setEnabled(true);
                    }
                    if (record.getRecord().size() == 4) {
                        btn.get(0).setEnabled(false);
                        btn.get(1).setEnabled(false);
                        btn.get(2).setEnabled(true);
                    }
                    if (record.getRecord().size() == 6) {
                        btn.get(0).setEnabled(false);
                        btn.get(1).setEnabled(false);
                        btn.get(2).setEnabled(false);
                        btn.get(3).setEnabled(true);
                    }
                    if (record.getRecord().size() == 8) {
                        btn.get(0).setEnabled(false);
                        btn.get(1).setEnabled(false);
                        btn.get(2).setEnabled(false);
                        btn.get(3).setEnabled(false);
                        btn.get(4).setEnabled(true);
                    }
                    if (record.getRecord().size() == 10) {
                        btn.get(0).setEnabled(false);
                        btn.get(1).setEnabled(false);
                        btn.get(2).setEnabled(false);
                        btn.get(3).setEnabled(false);
                        btn.get(4).setEnabled(false);
                        btn.get(5).setEnabled(true);
                    }
//                    if (record.getRecord().get(i) != null && record.getRecord().get(i+1) != null) {
//                        btn.get(i).setEnabled(false);
//                    }
//                    else  {
//                        btn.get(i).setEnabled(true);
//                    }
                }
//                for (int i = 0; i < record.getRecord().size(); i++) {
//                    if (record.getRecord().get(i) != null) {
//                        btn.get(i+2).setEnabled(false);
//                    }
//                    else {
//                        btn.get(i+2).setEnabled(true);
//                    }
//                }
            }
            /*
            if user has all questions answered, he cant click on any button
             */
            else
//                if (record.getRecord().size() == 11)
            {
                for (int i = 0; i < btn.size(); i++) {
                    btn.get(i).setEnabled(false);
                }
            }
        }


        /*
        set the color of the done and the undone
         */
        if (record.getRecord() != null) {
            if (record.getRecord().size() >= 2) {
                btn.get(0).setBackground(drawableDone);
                btn.get(1).setBackground(drawableDone);
                if (record.getRecord().size() >= 4) {
                    btn.get(2).setBackground(drawableDone);
                    if (record.getRecord().size() >= 6) {
                        btn.get(3).setBackground(drawableDone);
                        if (record.getRecord().size() >= 8) {
                            btn.get(4).setBackground(drawableDone);
                            if (record.getRecord().size() >= 10) {
                                btn.get(5).setBackground(drawableDone);
                                if (record.getRecord().size() >= 11) {
                                    btn.get(6).setBackground(drawableDone);
                                }
                            }
                        }
                    }
                }
            }
//            if (record.getRecord().size() < 11) {
//                btn.get(5).setBackground(drawable);
//                if (record.getRecord().size() < 10) {
//                    btn.get(4).setBackground(drawable);
//                    if (record.getRecord().size() < 8) {
//                        btn.get(3).setBackground(drawable);
//                        if (record.getRecord().size() < 6) {
//                            btn.get(2).setBackground(drawable);
//                            if (record.getRecord().size() <4) {
//                                btn.get(1).setBackground(drawable);
//                                if (record.getRecord().size()<2) {
//                                    btn.get(0).setBackground(drawable);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }

    public void goToNextActivity(int btnNumberHome) {
        Intent assActivity = new Intent(getActivity(), Assessment2Activity.class);
        if (user != null) {
            assActivity.putExtra("btnNo", btnNumberHome);
            startActivity(assActivity);
            getActivity().finish();
            // signed in
        } else {
            /*
            later change alertdialog to dialog class
             */
            Dialog d = new Dialog(getActivity());

            AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            View mView = getLayoutInflater().inflate(R.layout.popup_profile_login, null);
            Button btn_popup_login = mView.findViewById(R.id.btn_popup_login);
            TextView tv_not_now = mView.findViewById(R.id.tv_popup_notNow);

            mBuilder.setView(mView);
            dialog = mBuilder.create();
            dialog.show();

            btn_popup_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginActivity);
                    getActivity().finish();
                }
            });
            tv_not_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shortToast("takpelah");
                    dialog.dismiss();
                }
            });
        }
    }

    public void goToExtraActivity() {
        Intent extraAssActivity = new Intent(getActivity(), AssessmentExtraActivity.class);
        if (user!= null) {
            startActivity(extraAssActivity);
            getActivity().finish();
        }
        else {

        }
    }

    public void showNewAssessmentPopup() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_new_assessment);
        Button btn_popup_newAss_yes = dialog.findViewById(R.id.btn_popup_new_assessment_yes);
        TextView txt_popup_newAss = dialog.findViewById(R.id.txt_popup_new_assessment);
        ImageView img_popup_newAss = dialog.findViewById(R.id.ic_popup_newAss);
        TextView txt_title_popup_newAss = dialog.findViewById(R.id.txt_title_popup_newAss);

        if (user!=null) {
            /*
            redirect to enable buttons
             */
            btn_popup_newAss_yes.setText("okay");
            txt_title_popup_newAss.setText("New Assessment");
            txt_popup_newAss.setText(R.string.popup_new_assessment);
            img_popup_newAss.setImageResource(R.drawable.ic_popup_new_assessment);
            btn_popup_newAss_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shortToast("yes");
                    dialog.dismiss();
                    reset();
                    btn.get(0).setEnabled(true);
                    btn.get(0).setBackground(drawableDone);
                }
            });
            dialog.show();
        }
        else {
            /*
            if user is not signed in, redirect to login
             */
            btn_popup_newAss_yes.setText("log in");
            txt_title_popup_newAss.setText("Welcome, user?");
            txt_popup_newAss.setText(R.string.popup_login);
            img_popup_newAss.setImageResource(R.drawable.ic_popup_unknown);
            btn_popup_newAss_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    go to login
                     */
                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                    startActivity(loginActivity);
                    getActivity().finish();
                }
            });
            dialog.show();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_stage1:
                btnNumberHome = 1;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage2:
                btnNumberHome = 3;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage3:
                btnNumberHome = 5;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage4:
                btnNumberHome = 7;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage5:
                btnNumberHome = 9;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage6:
                btnNumberHome = 11;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_stage7:
                btnNumberHome = 7;
                goToNextActivity(btnNumberHome);
                break;
            case R.id.btn_newAssessment:
                /*
                enable the first button
                 */
                showNewAssessmentPopup();

                break;
        }

//        if (user != null) {
//            assActivity.putExtra("btnNo", btnNumberHome);
//            startActivity(assActivity);
//            getActivity().finish();
//            // signed in
//        } else {
//            AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
//            View mView = getLayoutInflater().inflate(R.layout.popup_profile_login, null);
//            Button btn_popup_login = mView.findViewById(R.id.btn_popup_login);
//            TextView tv_not_now = mView.findViewById(R.id.tv_popup_notNow);
//
//            mBuilder.setView(mView);
//            dialog = mBuilder.create();
//            dialog.show();
//
//            btn_popup_login.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(loginActivity);
//                    getActivity().finish();
//                }
//            });
//            tv_not_now.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shortToast("takpelah");
//                    dialog.dismiss();
//                }
//            });
//        }
    }

    public void shortToast(String m) {
        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();
    }

    public void longToast(String m) {
        Toast.makeText(getActivity(), m, Toast.LENGTH_LONG).show();
    }
}
