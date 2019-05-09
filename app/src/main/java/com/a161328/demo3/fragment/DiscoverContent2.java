package com.a161328.demo3.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a161328.demo3.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverContent2 extends Fragment {


    public DiscoverContent2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_discover_content_2, container, false);
        return v;
    }

}
