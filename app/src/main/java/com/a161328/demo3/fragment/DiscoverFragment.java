package com.a161328.demo3.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.activity.DiscoverPageActivity;
import com.a161328.demo3.R;
import com.a161328.demo3.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment implements View.OnClickListener {

    CardView card1, card2, card3, card4, card5;
    ImageButton info1, info2, info3, info4, info5;
    TextView title1, title2, title3, title4, title5;

    AlertDialog dialog;
    FirebaseUser user;


    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discover, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {

        }

        card1 = v.findViewById(R.id.card_chapter_1);
        card2 = v.findViewById(R.id.card_chapter_2);
        card3 = v.findViewById(R.id.card_chapter_3);
        card4 = v.findViewById(R.id.card_chapter_4);
        card5 = v.findViewById(R.id.card_chapter_5);

        title1 = v.findViewById(R.id.txt_card_title1);
        title2 = v.findViewById(R.id.txt_card_title2);
        title3 = v.findViewById(R.id.txt_card_title3);
        title4 = v.findViewById(R.id.txt_card_title4);
        title5 = v.findViewById(R.id.txt_card_title5);

        info1 = v.findViewById(R.id.ic_info_chapter1);
        info2 = v.findViewById(R.id.ic_info_chapter2);
        info3 = v.findViewById(R.id.ic_info_chapter3);
        info4 = v.findViewById(R.id.ic_info_chapter4);
        info5 = v.findViewById(R.id.ic_info_chapter5);

        info1.setOnClickListener(this);
        info2.setOnClickListener(this);
        info3.setOnClickListener(this);
        info4.setOnClickListener(this);
        info5.setOnClickListener(this);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {

        if (user == null) {
            Dialog dialog = new Dialog(getActivity());
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
        else {
            Intent discoverPage = new Intent(getActivity(), DiscoverPageActivity.class);
            switch (v.getId()) {
                case R.id.card_chapter_1:
                    discoverPage.putExtra("id", R.id.card_chapter_1);
                    startActivity(discoverPage);
                    break;
                case R.id.card_chapter_2:
                    discoverPage.putExtra("id", R.id.card_chapter_2);
                    startActivity(discoverPage);
                    break;
                case R.id.ic_info_chapter1:
                case R.id.ic_info_chapter2:
                    //
                    AlertDialog.Builder mBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.popup_discover_badge, null);
                    Button btn_okay = mView.findViewById(R.id.btn_popup_okay);
                    btn_okay.setOnClickListener(this);
                    mBuilder.setView(mView);
                    dialog = mBuilder.create();
                    dialog.show();
                    break;
                case R.id.btn_popup_okay:
                    dialog.dismiss();
                    break;

            }
        }



    }
}
