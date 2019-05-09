package com.a161328.demo3.fragment;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.Model.User;
import com.a161328.demo3.activity.LoginActivity;
import com.a161328.demo3.activity.SettingsActivity;
import com.a161328.demo3.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private ImageButton imgbtn_settings, imgbtn_logout;
    private ImageView currentBadge,
            collectedBadge1, collectedBadge2, collectedBadge3,
            collectedBadge4, collectedBadge5, collectedBadge6,
            userProfileImage;
    private TextView txt_profile_name, txt_profile_sub, txt_profile_description, txt_profile_place;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User mUser;
    private ArrayList<ImageView> badgeList;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        imgbtn_settings = v.findViewById(R.id.imgbtn_settings);
        imgbtn_logout = v.findViewById(R.id.imgBtn_logOut);
        currentBadge = v.findViewById(R.id.ic_profile_badge);
        collectedBadge1 = v.findViewById(R.id.ic_profile_badge1);
        collectedBadge2 = v.findViewById(R.id.ic_profile_badge2);
        collectedBadge3 = v.findViewById(R.id.ic_profile_badge3);
        collectedBadge4 = v.findViewById(R.id.ic_profile_badge4);
        collectedBadge5 = v.findViewById(R.id.ic_profile_badge5);
        collectedBadge6 = v.findViewById(R.id.ic_profile_badge6);
        userProfileImage = v.findViewById(R.id.img_profile_user);

        badgeList = new ArrayList<>();
        badgeList.add(currentBadge);
        badgeList.add(collectedBadge1);
        badgeList.add(collectedBadge2);
        badgeList.add(collectedBadge3);

        txt_profile_name = v.findViewById(R.id.tv_profile_name);
        txt_profile_sub = v.findViewById(R.id.tv_profile_name_sub);
        txt_profile_description = v.findViewById(R.id.tv_profile_level);
//        txt_profile_place = v.findViewById(R.id.tv_profile_place);

        imgbtn_logout.setOnClickListener(this);
        imgbtn_settings.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mUser = User.getInstance();

        if (currentUser != null) {
            /*
            if user is signed in
             */
            updateUserInfo();

            updateBadges();

            collectedBadge1.setOnLongClickListener(this);
            collectedBadge2.setOnLongClickListener(this);
            collectedBadge3.setOnLongClickListener(this);

        } else {
            /*
            if user is not signed in
             */
            txt_profile_name.setText("@username");
            txt_profile_sub.setText("@email");

            txt_profile_description.setVisibility(View.INVISIBLE);
            imgbtn_logout.setVisibility(View.INVISIBLE);
            userProfileImage.setImageResource(R.drawable.ic_profile_unknown);

            currentBadge.setImageResource(R.drawable.ic_profile_missingbadge);

            collectedBadge1.setImageResource(R.drawable.ic_profile_missingbadge);
            collectedBadge2.setImageResource(R.drawable.ic_profile_missingbadge);
            collectedBadge3.setImageResource(R.drawable.ic_profile_missingbadge);

            showNullUserPopup();

        }
        return v;
    }

    public void updateBadges() {
        switch (mUser.getUserPoint()) {
            case 10:
                currentBadge.setImageResource(R.drawable.badge_level_1);
                break;
            case 20:
                currentBadge.setImageResource(R.drawable.badge_level_2);
                break;
            case 30:
                currentBadge.setImageResource(R.drawable.badge_level_3);
                break;
            default:
                currentBadge.setImageResource(R.drawable.ic_profile_missingbadge);
                break;
        }

        collectedBadge1.setImageResource(R.drawable.badge_level_1);
        collectedBadge2.setImageResource(R.drawable.badge_level_2);
        collectedBadge3.setImageResource(R.drawable.badge_level_3);
        collectedBadge4.setImageResource(R.drawable.badge_level_4);
        collectedBadge5.setImageResource(R.drawable.badge_level_5_undone);
        collectedBadge6.setImageResource(R.drawable.badge_level_6_undone);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showNullUserPopup() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_new_assessment);
        Button btn_popup_newAss_yes = dialog.findViewById(R.id.btn_popup_new_assessment_yes);
        TextView txt_popup_newAss = dialog.findViewById(R.id.txt_popup_new_assessment);
        ImageView img_popup_newAss = dialog.findViewById(R.id.ic_popup_newAss);
        TextView txt_title_popup_newAss = dialog.findViewById(R.id.txt_title_popup_newAss);

        btn_popup_newAss_yes.setText("log in");
        txt_title_popup_newAss.setText("Welcome, user?");
        txt_popup_newAss.setText(R.string.popup_login);
        img_popup_newAss.setImageResource(R.drawable.ic_popup_unknown);
        btn_popup_newAss_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginActivity);
                getActivity().finish();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showBadgePopup(int badgeNum) {
        Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_badge_description);

        TextView popUpTitle = dialog.findViewById(R.id.txt_popupBadge_name);
        TextView popupDescription = dialog.findViewById(R.id.txt_popupBadge_description);
        TextView popupPoint = dialog.findViewById(R.id.txt_popupBadge_point);
        ImageView popupBadgeIcon = dialog.findViewById(R.id.img_popupBadge_icon);

        popUpTitle.setText("Badge Level 1");
        popupDescription.setText("Achieve this badge by completing the first module of the assessment");
        switch (badgeNum) {
            case 1:
                popupBadgeIcon.setImageResource(R.drawable.badge_level_1);
                popupPoint.setText(String.valueOf(10) + " pt");
                break;
            case 2:
                popupBadgeIcon.setImageResource(R.drawable.badge_level_2);
                popupPoint.setText(String.valueOf(20) + " pt");
                break;
            case 3:
                popupBadgeIcon.setImageResource(R.drawable.badge_level_3);
                popupPoint.setText(String.valueOf(30) + " pt");
                break;
        }
        dialog.show();
    }

    private void updateUserInfo() {
        txt_profile_name.setText(mUser.getUserName());
        txt_profile_sub.setText(mUser.getUserEmail());
        txt_profile_description.setText(mUser.getUserLevel());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_settings:
                Intent settingsActivity = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsActivity);
                break;
            case R.id.imgBtn_logOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Sign out of the app?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.ic_profile_badge1:
                showBadgePopup(1);
                break;
            case R.id.ic_profile_badge2:
                showBadgePopup(2);
                break;
            case R.id.ic_profile_badge3:
                showBadgePopup(3);
                break;
        }
        return false;
    }

    public void shortToast(String m) {
        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();
    }

//    public void longToast(String m) {
//        Toast.makeText(getActivity(), m, Toast.LENGTH_LONG).show();
//    }
}
