package com.a161328.demo3.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a161328.demo3.fragment.DiscoverContent1;
import com.a161328.demo3.fragment.DiscoverContent2;
import com.a161328.demo3.R;

public class DiscoverPageActivity extends AppCompatActivity implements View.OnClickListener {

    TextView text_1, text_2;
    ImageView img_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_page);
        text_1 = findViewById(R.id.text_chapter1);
        text_2 = findViewById(R.id.text_chapter2);

        Intent i = getIntent();
        Fragment selectedDiscoverFragment = null;
        switch (i.getIntExtra("id", 0)) {
            case R.id.card_chapter_1:
                selectedDiscoverFragment = new DiscoverContent1();
                break;
            case R.id.card_chapter_2:
                selectedDiscoverFragment = new DiscoverContent2();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_discover, selectedDiscoverFragment).commit();

    }

    @Override
    public void onClick(View v) {
        Fragment selectedFragment = null;
        switch (v.getId()) {
            case R.id.card_chapter_1:
                selectedFragment = new DiscoverContent1();
                break;
            case R.id.card_chapter_2:
                selectedFragment = new DiscoverContent2();
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_discover, selectedFragment).commit();
    }
}
